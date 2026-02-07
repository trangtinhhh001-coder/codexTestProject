package com.example.pdfrag.service;

import com.example.pdfrag.model.ChunkDocument;
import com.example.pdfrag.util.TextChunker;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class PdfIngestService {

    private final EmbeddingService embeddingService;
    private final InMemoryVectorStore vectorStore;

    public PdfIngestService(EmbeddingService embeddingService, InMemoryVectorStore vectorStore) {
        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
    }

    public int ingest(MultipartFile file) throws IOException {
        String text = extractText(file);
        List<String> chunks = TextChunker.chunkByLength(text, 900, 120);

        String documentId = UUID.randomUUID().toString();
        List<ChunkDocument> indexedChunks = new ArrayList<>();

        for (int i = 0; i < chunks.size(); i++) {
            String content = chunks.get(i);
            indexedChunks.add(new ChunkDocument(
                    documentId,
                    i,
                    content,
                    embeddingService.embed(content)
            ));
        }

        vectorStore.addAll(indexedChunks);
        return indexedChunks.size();
    }

    private String extractText(MultipartFile file) throws IOException {
        try (PDDocument document = Loader.loadPDF(file.getBytes())) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
