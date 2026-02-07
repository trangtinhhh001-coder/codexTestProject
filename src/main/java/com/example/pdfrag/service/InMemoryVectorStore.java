package com.example.pdfrag.service;

import com.example.pdfrag.model.ChunkDocument;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class InMemoryVectorStore {

    private final List<ChunkDocument> storage = new CopyOnWriteArrayList<>();

    public void addAll(List<ChunkDocument> chunks) {
        storage.addAll(chunks);
    }

    public int size() {
        return storage.size();
    }

    public List<ChunkDocument> search(double[] queryEmbedding, int topK) {
        List<ScoredChunk> scored = new ArrayList<>();
        for (ChunkDocument chunk : storage) {
            double score = cosine(queryEmbedding, chunk.embedding());
            scored.add(new ScoredChunk(chunk, score));
        }

        return scored.stream()
                .sorted(Comparator.comparingDouble(ScoredChunk::score).reversed())
                .limit(topK)
                .map(ScoredChunk::chunk)
                .toList();
    }

    private double cosine(double[] a, double[] b) {
        double dot = 0.0;
        for (int i = 0; i < a.length; i++) {
            dot += a[i] * b[i];
        }
        return dot;
    }

    private record ScoredChunk(ChunkDocument chunk, double score) {
    }
}
