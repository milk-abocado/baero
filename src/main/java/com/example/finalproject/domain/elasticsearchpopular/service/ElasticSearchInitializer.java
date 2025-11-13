package com.example.finalproject.domain.elasticsearchpopular.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.ElasticsearchException;
import co.elastic.clients.elasticsearch.indices.ExistsRequest;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.ResponseException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ElasticSearchInitializer {

    private static final String INDEX = "popular_searches_index";
    private final ElasticsearchClient esClient;

    public ElasticSearchInitializer(ElasticsearchClient esClient) {
        this.esClient = esClient;
    }

    @PostConstruct
    public void createIndexIfNotExists() {
        try {
            // 인덱스 존재 여부 확인
            BooleanResponse existsResponse = esClient.indices()
                    .exists(ExistsRequest.of(e -> e.index(INDEX)));
            boolean exists = existsResponse.value();

            if (!exists) {
                createIndex();
                log.info("ElasticSearch index '{}' created with search_as_you_type mapping.", INDEX);
            } else {
                log.info("ElasticSearch index '{}' already exists.", INDEX);
            }

        } catch (ResponseException e) {
            // 410 Gone 등 응답 예외 발생 시 재생성 시도
            log.warn("Index '{}' check failed (HTTP {}). Trying to recreate it.",
                    INDEX, e.getResponse().getStatusLine());
            try {
                createIndex();
                log.info("Recreated missing index '{}'.", INDEX);
            } catch (Exception ce) {
                log.error("Failed to recreate index '{}'.", INDEX, ce);
            }

        } catch (IOException | ElasticsearchException e) {
            log.error("Failed to check or create ElasticSearch index '{}'.", INDEX, e);
        }
    }

    private void createIndex() throws IOException {
        esClient.indices().create(c -> c
                .index(INDEX)
                .mappings(m -> m
                        .properties("keyword", p -> p.searchAsYouType(s -> s))
                        .properties("region", p -> p.keyword(k -> k))
                        .properties("searchCount", p -> p.integer(i -> i))
                        .properties("ranking", p -> p.integer(i -> i))
                        .properties("created_at", p -> p.date(d -> d))
                        .properties("type", p -> p.keyword(k -> k)) // type 필드
                )
        );
    }
}
