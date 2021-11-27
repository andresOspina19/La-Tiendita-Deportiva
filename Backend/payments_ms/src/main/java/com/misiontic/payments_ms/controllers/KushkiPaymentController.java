package com.misiontic.payments_ms.controllers;

import com.google.gson.Gson;
import com.misiontic.payments_ms.models.KushkiMakeTransactionRequest;
import com.misiontic.payments_ms.models.KushkiPayment;
import com.misiontic.payments_ms.models.KushkiURLGenerated;
import com.misiontic.payments_ms.repositories.KushkiPaymentRepository;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class KushkiPaymentController {
    private final KushkiPaymentRepository kushkiPaymentRepository;

    public KushkiPaymentController(KushkiPaymentRepository kushkiPaymentRepository){
        this.kushkiPaymentRepository = kushkiPaymentRepository;
    }

    @GetMapping("/getPaymentByToken/{token}")
    KushkiPayment getKushkiPaymentByToken(@PathVariable String token){
        return kushkiPaymentRepository.findById(token).orElseThrow();
    }

    @GetMapping("/getPaymentsByUserId/{userId}")
    List<KushkiPayment> getKushkiPaymentsByUserId(@PathVariable Long userId){
        return kushkiPaymentRepository.findByUserId(userId);
    }

    @GetMapping("/getPaymentByPurchaseId/{purchaseId}")
    KushkiPayment getKushkiPaymentByPurchaseId(@PathVariable Long purchaseId){
        return kushkiPaymentRepository.findByPurchaseId(purchaseId);
    }

    @GetMapping("/getPaymentStatus/{token}")
    @ResponseBody
    Map<String, Object> getKushkiPaymentStatus(@PathVariable String token) throws IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        Request request = new Request.Builder()
                .url("https://api-uat.kushkipagos.com/transfer/v1/status/" + token)
                .method("GET", null)
                .addHeader("private-merchant-id", "***REMOVED***")
                .build();

        Response response = client.newCall(request).execute();
        JSONObject json = null;

        if (response.isSuccessful()) {
            json = new JSONObject(response.body().string());

        } else {
            json = new JSONObject();
            json.put("information", "Ocurrió un error inesperado");

        }

        return json.toMap();
    }

    @PostMapping("/initPayment")
    KushkiURLGenerated initKushkiPayment(@RequestBody KushkiMakeTransactionRequest kushkiMakeTransactionRequest) throws IOException {
        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        MediaType mediaType = MediaType.parse("application/json");
        JSONObject json = new JSONObject(gson.toJson(kushkiMakeTransactionRequest));
        okhttp3.RequestBody body = okhttp3.RequestBody.create(json.toString(), mediaType);
        Request request = new Request.Builder()
                .url("https://api-uat.kushkipagos.com/transfer/v1/init")
                .method("POST", body)
                .addHeader("content-type", "application/json")
                .addHeader("private-merchant-id", "***REMOVED***")
                .build();
        Response response = client.newCall(request).execute();

        return gson.fromJson(response.toString(), KushkiURLGenerated.class);
    }



}
