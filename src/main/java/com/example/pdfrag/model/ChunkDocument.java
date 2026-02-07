package com.example.pdfrag.model;

public record ChunkDocument(
        String documentId,
        int chunkIndex,
        String content,
        double[] embedding
) {
}
