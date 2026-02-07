package com.example.pdfrag;

import com.example.pdfrag.util.TextChunker;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TextChunkerTest {

    @Test
    void shouldSplitTextIntoMultipleChunks() {
        String text = "Java RAG ".repeat(400);
        List<String> chunks = TextChunker.chunkByLength(text, 120, 20);

        assertTrue(chunks.size() > 1);
        assertTrue(chunks.stream().allMatch(chunk -> chunk.length() <= 120));
    }

    @Test
    void shouldRejectInvalidOverlap() {
        assertThrows(IllegalArgumentException.class,
                () -> TextChunker.chunkByLength("abc", 100, 100));
    }
}
