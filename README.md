# Java PDF RAG Demo

这是一个可运行的 Java（Spring Boot）PDF RAG 示例系统，支持：

1. 上传 PDF 文档
2. 抽取文本并切分 chunk
3. 为 chunk 建立向量（本地哈希向量）
4. 通过问题检索最相关上下文

> 当前版本使用本地可复现的向量化逻辑，避免外部依赖，便于快速搭建。你可以在 `RagService` 中对接真实大模型 API 做最终回答生成。

## 运行

```bash
mvn spring-boot:run
```

## API

### 1) 上传 PDF 并建立索引

```bash
curl -X POST http://localhost:8080/api/rag/upload \
  -F "file=@/path/to/your.pdf"
```

### 2) 问答检索

```bash
curl "http://localhost:8080/api/rag/ask?question=这份文档的核心观点是什么？"
```

返回结果包含：

- `answer`：基于检索上下文拼接的示例回答
- `contexts`：召回到的原始 chunk

## 目录说明

- `controller/RagController`：上传与问答 API
- `service/PdfIngestService`：PDF 文本抽取 + chunk + 建索引
- `service/InMemoryVectorStore`：内存向量检索
- `service/RagService`：RAG 召回与答案组织
- `service/EmbeddingService`：本地哈希 embedding
