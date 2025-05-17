package com.example.coffemachine.scanner;

import com.example.coffemachine.model.entity.Signature;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.*;
import org.apache.commons.codec.digest.DigestUtils;

@Component
public class RabinKarpScanner {
    private static final int BASE = 256;
    private static final int MOD = 1000000007;
    private static final int WINDOW_SIZE = 8;


    public List<Signature> scanFile(File file, List<Signature> signatures) throws IOException {
        List<Signature> matchedSignatures = new ArrayList<>();
        Map<Integer, List<Signature>> hashToSignatures = preprocessSignatures(signatures);

        try (RandomAccessFile raf = new RandomAccessFile(file, "r")) {
            long fileLength = raf.length();
            byte[] buffer = new byte[8192];
            int bytesRead;
            long position = 0;

            Queue<Byte> window = new LinkedList<>();
            int currentHash = 0;
            int power = 1;

            for (int i = 0; i < WINDOW_SIZE - 1; i++) {
                power = (power * BASE) % MOD;
            }

            while ((bytesRead = raf.read(buffer)) != -1) {
                for (int i = 0; i < bytesRead; i++) {
                    byte currentByte = buffer[i];
                    if (window.size() == WINDOW_SIZE) {
                        byte oldestByte = window.poll();
                        currentHash = (currentHash - oldestByte * power % MOD + MOD) % MOD;
                    }
                    window.add(currentByte);
                    currentHash = (currentHash * BASE + currentByte) % MOD;

                    if (window.size() == WINDOW_SIZE) {
                        List<Signature> candidates = hashToSignatures.get(currentHash);
                        if (candidates != null) {
                            for (Signature candidate : candidates) {
                                if (bytesMatch(raf, position - WINDOW_SIZE + 1, candidate)) {
                                    matchedSignatures.add(candidate);
                                }
                            }
                        }
                    }
                    position++;
                }
            }
        }
        return matchedSignatures;
    }

    private Map<Integer, List<Signature>> preprocessSignatures(List<Signature> signatures) {
        Map<Integer, List<Signature>> map = new HashMap<>();
        for (Signature sig : signatures) {
            int hash = computeHash(sig.getFirstBytes());
            map.computeIfAbsent(hash, k -> new ArrayList<>()).add(sig);
        }
        return map;
    }

    private int computeHash(byte[] bytes) {
        int hash = 0;
        for (byte b : bytes) {
            hash = (hash * BASE + b) % MOD;
        }
        return hash;
    }

    private boolean bytesMatch(RandomAccessFile raf, long startPos, Signature signature) throws IOException {
        // Проверка первых 8 байт
        byte[] expectedBytes = signature.getFirstBytes();
        raf.seek(startPos);
        byte[] actualBytes = new byte[WINDOW_SIZE];
        raf.read(actualBytes);
        if (!Arrays.equals(expectedBytes, actualBytes)) {
            return false;
        }

        // Проверка "хвоста"
        long tailStart = startPos + WINDOW_SIZE;
        int tailLength = signature.getRemainderLength();
        if (raf.length() < tailStart + tailLength) {
            return false;
        }

        raf.seek(tailStart);
        byte[] tailBytes = new byte[tailLength];
        raf.read(tailBytes);
        String actualHash = DigestUtils.sha256Hex(tailBytes);
        return actualHash.equals(signature.getRemainderHash());
    }
}