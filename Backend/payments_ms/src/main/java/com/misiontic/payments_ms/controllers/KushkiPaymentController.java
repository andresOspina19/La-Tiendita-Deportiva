package com.misiontic.payments_ms.controllers;

import com.misiontic.payments_ms.exceptions.KushkiPaymentNotInitializedException;
import com.misiontic.payments_ms.exceptions.NotAuthorizedException;
import com.misiontic.payments_ms.exceptions.PaymentNotFoundException;
import com.misiontic.payments_ms.exceptions.PaymentNotValidException;
import com.misiontic.payments_ms.models.KushkiMakeTransactionRequest;
import com.misiontic.payments_ms.models.KushkiPayment;
import com.misiontic.payments_ms.models.KushkiPaymentStatus;
import com.misiontic.payments_ms.models.KushkiURLGenerated;
import com.misiontic.payments_ms.repositories.IKushkiOperationsRepository;
import com.misiontic.payments_ms.repositories.KushkiOperationsRepository;
import com.misiontic.payments_ms.repositories.KushkiPaymentRepository;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class KushkiPaymentController {

    private final KushkiPaymentRepository kushkiPaymentRepository;
    private final IKushkiOperationsRepository kushkiOperationsRepository;
    private final MongoTemplate mongoTemplate;

    public KushkiPaymentController(KushkiPaymentRepository kushkiPaymentRepository, KushkiOperationsRepository kushkiOperationsRepository, MongoTemplate mongoTemplate) {
        this.kushkiPaymentRepository = kushkiPaymentRepository;
        this.kushkiOperationsRepository = kushkiOperationsRepository;
        this.mongoTemplate = mongoTemplate;
    }

    @GetMapping("/getPaymentByToken/{token}")
    KushkiPayment getKushkiPaymentByToken(@PathVariable String token, @RequestParam String username){
        KushkiPayment payment = kushkiPaymentRepository.findById(token).orElseThrow(() -> new PaymentNotFoundException("No se encontro el pago con el token: " + token));

        if (!payment.getUsername().equals(username))
            throw new NotAuthorizedException("Usted no está autorizado para realizar esta accion");

        //Si el pago está aprobado no es necesario verificarlo de nuevo en Kushki
        if(payment.getStatus() == null) {
            return kushkiPaymentRepository.save(checkStatusOfPayment(token, payment));
        } else if(payment.getStatus().equals("approvedTransaction") ) {
            return payment;
        } else if(payment.getStatus().equals("declinedTransaction") ) {
            return payment;
        } else {
            return kushkiPaymentRepository.save(checkStatusOfPayment(token, payment));
        }
    }

    @GetMapping("/getAllPaymentsByUsername/{username}")
    List<KushkiPayment> getAllKushkiPaymentsByUsername(@PathVariable String username){
        List<KushkiPayment> payments = kushkiPaymentRepository.findByUsername(username);

        if (payments == null){
            throw new PaymentNotFoundException("No se encontraron pagos con el username " + username );
        }

        checkStatusOfPaymentsNotVerified(payments);

        return payments;
    }

    @GetMapping("/getAllPayments/")
    List<KushkiPayment> getAllKushkiPayments(){
        List<KushkiPayment> payments = kushkiPaymentRepository.findAll();

        if (payments == null){
            throw new PaymentNotFoundException("No se encontraron pagos");
        }

        checkStatusOfPaymentsNotVerified(payments);

        return payments;
    }

    @GetMapping("/getOnlyApprovedPaymentsByUsername/{username}")
    List<KushkiPayment> getKushkiPaymentsByUsername(@PathVariable String username){
        //Hacemos un Query personalizado para verificar que el pago está aprobado y el username corresponde al buscado
        Query query = new Query();
        query.addCriteria(Criteria.where("status").is("approvedTransaction").and("username").is(username));

        List<KushkiPayment> payments = mongoTemplate.find(query, KushkiPayment.class);

        if (payments == null){
            throw new PaymentNotFoundException("No se encontraron pagos aprobados con el username " + username );
        }

        return payments;
    }

    @PostMapping("/initPayment")
    KushkiURLGenerated initKushkiPayment(@RequestBody KushkiPayment kushkiPayment) {

        //Si el pago es menor o igual a 0, no se permite la petición
        if (kushkiPayment.getAmount().getIva() < 0
         || kushkiPayment.getAmount().getSubtotalIva() < 0
         || kushkiPayment.getAmount().getSubtotalIva0() <= 0) {
            throw new PaymentNotValidException("El pago no es valido, el pago debe ser de una cantidad mayor a 0");
        }

        KushkiMakeTransactionRequest kushkiMakeTransactionRequest = new KushkiMakeTransactionRequest(kushkiPayment.getToken(), kushkiPayment.getAmount());

        KushkiURLGenerated kushkiURLGenerated = kushkiOperationsRepository.makeTransaction(kushkiMakeTransactionRequest);

        if (kushkiURLGenerated == null) {
            throw new KushkiPaymentNotInitializedException("Ocurrió un error y no se pudo inicializar el pago");
        }

        //Almacenamos el pago sin status y sin bankurl
        kushkiPayment.setDate(new Date());
        kushkiPaymentRepository.save(kushkiPayment);

        return kushkiURLGenerated;
    }

    private KushkiPayment checkStatusOfPayment(String token, KushkiPayment payment) {
        KushkiPaymentStatus paymentStatus = kushkiOperationsRepository.getPaymentStatus(token).block();

        if (paymentStatus == null ) {
            throw new PaymentNotFoundException("No se pudo verificar el estado del pago en Kushki con el token " + token );
        }

        payment.setStatus( paymentStatus.getStatus() );

        if (paymentStatus.getStatus().equals("approvedTransaction")) {
            payment.setBankurl( paymentStatus.getBankurl().replace("?status=pending", "?status=approved") );
        } else if (paymentStatus.getStatus().equals("declinedTransaction")) {
            payment.setBankurl( paymentStatus.getBankurl().replace("?status=pending", "?status=declined") );
        } else {
            payment.setBankurl( paymentStatus.getBankurl() );
        }

        return payment;
    }

    private List<KushkiPayment> checkStatusOfPaymentsNotVerified(List<KushkiPayment> payments) {
        List<Mono<KushkiPaymentStatus>> kushkiPaymentsStatusAsync = new ArrayList<>();

        //Los pagos que aun no hayan sido verificados, se verifican
        for (KushkiPayment payment:payments) {
            if (payment.getStatus() == null) {
                kushkiPaymentsStatusAsync.add(kushkiOperationsRepository.getPaymentStatus(payment.getToken()));
            } else if (payment.getStatus().equals("approvedTransaction")) {
                kushkiPaymentsStatusAsync.add(null);
            } else if (payment.getStatus().equals("declinedTransaction")) {
                kushkiPaymentsStatusAsync.add(null);
            }
        }

        for (int i = 0; i < kushkiPaymentsStatusAsync.size(); i++) {
            if ( !(kushkiPaymentsStatusAsync.get(i) == null) ) {
                KushkiPaymentStatus paymentStatus = kushkiPaymentsStatusAsync.get(i).block();

                payments.get(i).setStatus( paymentStatus.getStatus() );

                if (paymentStatus.getStatus().equals("approvedTransaction")) {
                    payments.get(i).setBankurl( paymentStatus.getBankurl().replace("?status=pending", "?status=approved") );
                } else if (paymentStatus.getStatus().equals("declinedTransaction")) {
                    payments.get(i).setBankurl( paymentStatus.getBankurl().replace("?status=pending", "?status=declined") );
                } else {
                    payments.get(i).setBankurl( paymentStatus.getBankurl() );
                }

                kushkiPaymentRepository.save(payments.get(i));
            }

        }

        return payments;
    }
}