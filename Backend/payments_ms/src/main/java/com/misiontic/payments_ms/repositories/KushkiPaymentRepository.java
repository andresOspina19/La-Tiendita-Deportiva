package com.misiontic.payments_ms.repositories;

import com.misiontic.payments_ms.models.KushkiPayment;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface KushkiPaymentRepository extends MongoRepository<KushkiPayment, String> {
    List<KushkiPayment> findByUserId (Long userId);
    KushkiPayment findByPurchaseId (Long purchaseId);
}