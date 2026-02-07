package com.example.pdfrag.service;

import org.springframework.stereotype.Service;

@Service
public class EmbeddingService {

    private static final int VECTOR_SIZE = 384;

    public double[] embed(String text) {
        double[] vector = new double[VECTOR_SIZE];
        if (text == null || text.isBlank()) {
            return vector;
        }

        String[] tokens = text.toLowerCase().split("[^\\p{L}\\p{N}]+");
        for (String token : tokens) {
            if (token.isBlank()) {
                continue;
            }
            int index = Math.floorMod(token.hashCode(), VECTOR_SIZE);
            vector[index] += 1.0;
        }
        normalize(vector);
        return vector;
    }

    private void normalize(double[] vector) {
        double norm = 0.0;
        for (double v : vector) {
            norm += v * v;
        }
        norm = Math.sqrt(norm);
        if (norm == 0.0) {
            return;
        }
        for (int i = 0; i < vector.length; i++) {
            vector[i] = vector[i] / norm;
        }
    }
}
