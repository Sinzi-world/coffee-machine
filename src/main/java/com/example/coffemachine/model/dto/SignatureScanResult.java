package com.example.coffemachine.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class SignatureScanResult {
    private UUID signatureId;
    private String threatName;
    private int offsetFromStart;
    private int offsetFromEnd;
    private boolean matched;
}