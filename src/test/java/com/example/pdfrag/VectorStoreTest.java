package com.example.pdfrag;

import com.example.pdfrag.model.ChunkDocument;
import com.example.pdfrag.service.EmbeddingService;
import com.example.pdfrag.service.InMemoryVectorStore;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VectorStoreTest {

    @Test
    void shouldReturnMostRelevantChunk() {
        EmbeddingService embeddingService = new EmbeddingService();
        InMemoryVectorStore store = new InMemoryVectorStore();

        ChunkDocument javaChunk = new ChunkDocument("doc-1", 0, "Java RAG for PDF", embeddingService.embed("Java RAG for PDF"));
        ChunkDocument cookingChunk = new ChunkDocument("doc-2", 0, "How to cook noodles", embeddingService.embed("How to cook noodles"));

        store.addAll(List.of(javaChunk, cookingChunk));

        List<ChunkDocument> results = store.search(embeddingService.embed("Java PDF question"), 1);

        assertEquals(1, results.size());
        assertEquals("doc-1", results.get(0).documentId());
    }
}
