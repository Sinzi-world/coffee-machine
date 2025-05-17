package com.example.coffemachine.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Data
public class Signature {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String threatName;

    @Lob
    private byte[] firstBytes;
    private String remainderHash;
    private int remainderLength;
    private String fileType;
    private int offsetStart;
    private int offsetEnd;
    private String digitalSignature;
    private LocalDateTime updatedAt;
    private String status;
}