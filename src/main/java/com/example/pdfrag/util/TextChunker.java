package com.example.pdfrag.util;

import java.util.ArrayList;
import java.util.List;

public class TextChunker {

    private TextChunker() {
    }

    public static List<String> chunkByLength(String text, int chunkSize, int overlap) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        if (overlap >= chunkSize) {
            throw new IllegalArgumentException("overlap must be smaller than chunkSize");
        }

        String normalized = text.replaceAll("\\s+", " ").trim();
        List<String> chunks = new ArrayList<>();
        int start = 0;

        while (start < normalized.length()) {
            int end = Math.min(start + chunkSize, normalized.length());

            if (end < normalized.length()) {
                int lastSpace = normalized.lastIndexOf(' ', end);
                if (lastSpace > start + chunkSize / 2) {
                    end = lastSpace;
                }
            }

            chunks.add(normalized.substring(start, end).trim());
            int nextStart = end - overlap;
            start = Math.max(nextStart, start + 1);
        }
        return chunks;
    }
}
