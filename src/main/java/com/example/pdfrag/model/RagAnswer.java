package com.example.pdfrag.model;

import java.util.List;

public record RagAnswer(
        String answer,
        List<String> contexts
) {
}
