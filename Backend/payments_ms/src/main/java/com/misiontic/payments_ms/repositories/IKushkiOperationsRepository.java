package com.misiontic.payments_ms.repositories;

import com.misiontic.payments_ms.models.KushkiMakeTransactionRequest;
import com.misiontic.payments_ms.models.KushkiPaymentStatus;
import com.misiontic.payments_ms.models.KushkiURLGenerated;
import reactor.core.publisher.Mono;

public interface IKushkiOperationsRepository {

    KushkiURLGenerated makeTransaction(KushkiMakeTransactionRequest kushkiMakeTransactionRequest);
    KushkiPaymentStatus getPaymentStatus(String token);

}
