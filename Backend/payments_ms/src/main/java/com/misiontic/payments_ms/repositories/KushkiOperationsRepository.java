package com.misiontic.payments_ms.repositories;

import com.misiontic.payments_ms.exceptions.KushkiPaymentNotInitializedException;
import com.misiontic.payments_ms.models.KushkiMakeTransactionRequest;
import com.misiontic.payments_ms.models.KushkiPaymentStatus;
import com.misiontic.payments_ms.models.KushkiURLGenerated;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class KushkiOperationsRepository implements IKushkiOperationsRepository {
    @Override
    public KushkiURLGenerated makeTransaction(KushkiMakeTransactionRequest kushkiMakeTransactionRequest) {
        WebClient webClient = WebClient.create("https://api-uat.kushkipagos.com/transfer/v1");

        KushkiURLGenerated kushkiURLGenerated = webClient.post()
                .uri("/init")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("private-merchant-id", "017ff0ef392d42fa9ba2e9e28f566a4c")
                .body(Mono.just(kushkiMakeTransactionRequest), KushkiMakeTransactionRequest.class)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    throw new KushkiPaymentNotInitializedException("Ocurrió un error, El token ya fue usado anteriormente o el monto recibido es invalido");
                })
                .bodyToMono(KushkiURLGenerated.class)
                .block();

        return kushkiURLGenerated;
    }

    @Override
    public Mono<KushkiPaymentStatus> getPaymentStatus(String token) {
        WebClient webClient = WebClient.create("https://api-uat.kushkipagos.com/transfer/v1");

        Mono<KushkiPaymentStatus> kushkiPaymentStatus = webClient.get()
                .uri("/status/" + token)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header("private-merchant-id", "017ff0ef392d42fa9ba2e9e28f566a4c")
                .retrieve()
                .bodyToMono(KushkiPaymentStatus.class);

        return kushkiPaymentStatus;
    }
}
