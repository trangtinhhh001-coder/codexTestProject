package com.example.pdfrag.controller;

import com.example.pdfrag.model.RagAnswer;
import com.example.pdfrag.service.InMemoryVectorStore;
import com.example.pdfrag.service.PdfIngestService;
import com.example.pdfrag.service.RagService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
public class RagController {

    private final PdfIngestService pdfIngestService;
    private final RagService ragService;
    private final InMemoryVectorStore vectorStore;

    public RagController(PdfIngestService pdfIngestService, RagService ragService, InMemoryVectorStore vectorStore) {
        this.pdfIngestService = pdfIngestService;
        this.ragService = ragService;
        this.vectorStore = vectorStore;
    }

    @PostMapping(value = "/api/rag/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> upload(@RequestParam("file") MultipartFile file) throws IOException {
        int chunkCount = pdfIngestService.ingest(file);
        return ResponseEntity.ok(Map.of(
                "message", "PDF已索引",
                "chunks", chunkCount,
                "totalIndexedChunks", vectorStore.size()
        ));
    }

    @GetMapping("/api/rag/ask")
    public RagAnswer ask(@RequestParam("question") String question) {
        return ragService.ask(question);
    }
}
