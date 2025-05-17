package com.example.coffemachine.repository;

import com.example.coffemachine.model.entity.Signature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface SignatureRepository extends JpaRepository<Signature, UUID> {

    List<Signature> findByUpdatedAtAfter(LocalDateTime since);

    List<Signature> findByIdIn(List<UUID> ids);

    List<Signature> findByStatus(String status);
}