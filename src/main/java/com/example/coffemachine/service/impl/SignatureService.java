package com.example.coffemachine.service.impl;

import com.example.coffemachine.model.entity.Signature;
import com.example.coffemachine.repository.SignatureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SignatureService {
    private final SignatureRepository signatureRepository;


    public List<Signature> getAllActualSignatures() {
        return signatureRepository.findByStatus("ACTUAL");
    }


    public boolean verifySignature(Signature signature) {
        // Реализация проверки подписи (например, с использованием публичного ключа)
        return true; // Заглушка
    }


    @Scheduled(cron = "${schedule.signature-check}")
    public void scheduledSignatureCheck() {
        List<Signature> signatures = signatureRepository.findAll();
        for (Signature signature : signatures) {
            if (!verifySignature(signature)) {
                signature.setStatus("CORRUPTED");
                signatureRepository.save(signature);
            }
        }
    }
}