package com.example.pdfrag.service;

import com.example.pdfrag.model.ChunkDocument;
import com.example.pdfrag.model.RagAnswer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {

    private final EmbeddingService embeddingService;
    private final InMemoryVectorStore vectorStore;

    public RagService(EmbeddingService embeddingService, InMemoryVectorStore vectorStore) {
        this.embeddingService = embeddingService;
        this.vectorStore = vectorStore;
    }

    public RagAnswer ask(String question) {
        double[] q = embeddingService.embed(question);
        List<ChunkDocument> contexts = vectorStore.search(q, 4);
        List<String> contextTexts = contexts.stream().map(ChunkDocument::content).toList();

        StringBuilder answer = new StringBuilder("基于PDF内容检索到如下信息：\n");
        for (int i = 0; i < contextTexts.size(); i++) {
            answer.append(i + 1)
                    .append(". ")
                    .append(shorten(contextTexts.get(i), 200))
                    .append("\n");
        }

        answer.append("\n请将以上上下文送入你的LLM（如OpenAI、Qwen、DeepSeek）进行最终润色回答。");
        return new RagAnswer(answer.toString(), contextTexts);
    }

    private String shorten(String text, int maxChars) {
        if (text.length() <= maxChars) {
            return text;
        }
        return text.substring(0, maxChars) + "...";
    }
}
