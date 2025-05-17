package com.example.coffemachine.generator;

import com.example.coffemachine.model.entity.Signature;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.util.Arrays;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class SignatureGenerator {
    private final PrivateKey privateKey;

    public String generateSignature(Signature signature) throws Exception {
        String dataToSign = String.join("|",
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
        byte[] dataHash = digest.digest(dataToSign.getBytes());

        java.security.Signature sig = java.security.Signature.getInstance("SHA256withRSA");
        sig.initSign(privateKey);
        sig.update(dataHash);
        byte[] signatureBytes = sig.sign();

        return Base64.getEncoder().encodeToString(signatureBytes);
    }
}