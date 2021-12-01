package com.misiontic.payments_ms.controllers;

import com.misiontic.payments_ms.exceptions.KushkiPaymentNotInitializedException;
import com.misiontic.payments_ms.exceptions.PaymentNotFoundException;
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
    KushkiPayment getKushkiPaymentByToken(@PathVariable String token){
        KushkiPayment payment = kushkiPaymentRepository.findById(token).orElseThrow(() -> new PaymentNotFoundException("No se encontro el pago con el token: " + token));

        KushkiPaymentStatus paymentStatus = kushkiOperationsRepository.getPaymentStatus(token);

        if (paymentStatus == null ) {
            throw new PaymentNotFoundException("No se pudo verificar el estado del pago en Kushki con el token " + token );
        }

        payment.setStatus( paymentStatus.getStatus() );
        payment.setBankurl( paymentStatus.getBankurl() );

        return kushkiPaymentRepository.save(payment);
    }

    @GetMapping("/getAllPaymentsByUsername/{username}")
    List<KushkiPayment> getAllKushkiPaymentsByUsername(@PathVariable String username){
        List<KushkiPayment> payments = kushkiPaymentRepository.findByUsername(username);

        if (payments == null){
            throw new PaymentNotFoundException("No se encontraron pagos con el username " + username );
        }

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

    @GetMapping("/getPaymentByPurchaseId/{purchaseId}")
    KushkiPayment getKushkiPaymentByPurchaseId(@PathVariable String purchaseId){
        KushkiPayment payment = kushkiPaymentRepository.findByPurchaseId(purchaseId);

        if (payment == null){
            throw new PaymentNotFoundException("No se encontró el pago con el ID de compra " + purchaseId );
        }

        KushkiPaymentStatus paymentStatus = kushkiOperationsRepository.getPaymentStatus(payment.getToken());

        if (paymentStatus == null ) {
            throw new PaymentNotFoundException("No se pudo verificar el estado del pago en Kushki con el token " + payment.getStatus() );
        }

        payment.setStatus( paymentStatus.getStatus() );
        payment.setBankurl( paymentStatus.getBankurl() );

        return kushkiPaymentRepository.save(payment);
    }

    @PostMapping("/initPayment")
    KushkiURLGenerated initKushkiPayment(@RequestBody KushkiPayment kushkiPayment) {
        KushkiMakeTransactionRequest kushkiMakeTransactionRequest = new KushkiMakeTransactionRequest(kushkiPayment.getToken(), kushkiPayment.getAmount());

        KushkiURLGenerated kushkiURLGenerated = kushkiOperationsRepository.makeTransaction(kushkiMakeTransactionRequest);

        if (kushkiURLGenerated == null) {
            throw new KushkiPaymentNotInitializedException("Ocurrió un error y no se pudo inicializar el pago");
        }

        //Almacenamos el pago sin status y sin bankurl
        kushkiPaymentRepository.save(kushkiPayment);

        return kushkiURLGenerated;
    }
}