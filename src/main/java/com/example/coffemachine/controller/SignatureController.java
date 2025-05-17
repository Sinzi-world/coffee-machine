package com.example.coffemachine.controller;

import com.example.coffemachine.generator.SignatureGenerator;
import com.example.coffemachine.model.entity.Signature;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signatures")
@RequiredArgsConstructor
public class SignatureController {

    private final SignatureGenerator signatureGenerator;

    @PostMapping("/generate")
    public ResponseEntity<String> generate(@RequestBody Signature signature) {
        try {
            String digitalSignature = signatureGenerator.generateSignature(signature);
            return ResponseEntity.ok(digitalSignature);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Ошибка генерации подписи: " + e.getMessage());
        }
    }
}
