package com.example.coffemachine.service.impl;

import com.example.coffemachine.model.entity.Signature;
import com.example.coffemachine.repository.SignatureRepository;
import lombok.RequiredArgsConstructor;

import java.security.*;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SignatureService {
    private final SignatureRepository signatureRepository;
    private final PublicKey publicKey;


    public List<Signature> getAllActualSignatures() {
        return signatureRepository.findByStatus("ACTUAL");
    }


    public boolean verifySignature(Signature signature) {
        try {
            String dataToVerify = String.join("|",
                    signature.getThreatName(),
                    Arrays.toString(signature.getFirstBytes()),
                    signature.getRemainderHash(),
                    String.valueOf(signature.getRemainderLength()),
                    signature.getFileType(),
                    String.valueOf(signature.getOffsetStart()),
                    String.valueOf(signature.getOffsetEnd()),
                    signature.getUpdatedAt().toString()
            );

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] dataHash = digest.digest(dataToVerify.getBytes());

            java.security.Signature sig = java.security.Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(dataHash);

            byte[] signatureBytes = Base64.getDecoder().decode(signature.getDigitalSignature());

            return sig.verify(signatureBytes);
        } catch (NoSuchAlgorithmException | InvalidKeyException | SignatureException e) {
            throw new RuntimeException("Ошибка проверки подписи", e);
        }
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