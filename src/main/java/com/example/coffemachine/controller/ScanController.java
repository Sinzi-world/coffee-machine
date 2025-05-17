package com.example.coffemachine.controller;

import com.example.coffemachine.model.dto.SignatureScanResult;
import com.example.coffemachine.model.entity.Signature;
import com.example.coffemachine.scanner.RabinKarpScanner;
import com.example.coffemachine.service.impl.SignatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/scan")
@RequiredArgsConstructor
public class ScanController {
    private final RabinKarpScanner scanner;
    private final SignatureService signatureService;

    @PostMapping("/upload")
    public ResponseEntity<List<SignatureScanResult>> scanFile(@RequestParam("file") MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("scan-", ".tmp");
        file.transferTo(tempFile);

        List<Signature> signatures = signatureService.getAllActualSignatures();
        List<Signature> matches = scanner.scanFile(tempFile, signatures);

        tempFile.delete();


        List<SignatureScanResult> results = matches.stream()
                .map(sig -> new SignatureScanResult(
                        sig.getId(),
                        sig.getThreatName(),
                        (int) tempFile.length() - sig.getOffsetEnd(),
                        sig.getOffsetEnd(),
                        true
                ))
                .toList();

        return ResponseEntity.ok(results);
    }
}