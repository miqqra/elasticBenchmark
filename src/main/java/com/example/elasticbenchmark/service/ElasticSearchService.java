package com.example.elasticbenchmark.service;

import com.example.elasticbenchmark.dto.ElasticSearchDto;
import com.example.elasticbenchmark.dto.ElasticSearchDtos;
import com.example.elasticbenchmark.entity.ElasticSearchEntity;
import com.example.elasticbenchmark.mapper.ElasticSearchMapper;
import com.example.elasticbenchmark.repository.ElasticSearchRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.common.unit.Fuzziness;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ElasticSearchService {

    private final ElasticSearchRepository repository;

    private final ElasticSearchMapper mapper;

    private static final String INDEX_NAME = "index";
    private final ElasticsearchOperations elasticsearchOperations;

    public List<ElasticSearchDto> findByName(String code) {
        QueryBuilder queryBuilder = QueryBuilders
                .matchQuery("code", code)
                .minimumShouldMatch("1")
                //todo add analyzer https://stackoverflow.com/questions/63810021/create-custom-analyzer-with-asciifolding-filter-in-spring-data-elasticsearch
                .fuzziness(Fuzziness.AUTO);

        NativeSearchQuery query = new NativeSearchQueryBuilder()
                .withQuery(queryBuilder)
                .withPageable(Pageable.ofSize(5))
                .build();

        query.setTimeout(Duration.ofSeconds(1L));

        SearchHits<ElasticSearchEntity> hits = elasticsearchOperations
                .search(query, ElasticSearchEntity.class, IndexCoordinates.of(INDEX_NAME));

        return hits
                .stream()
                .map(SearchHit::getContent)
                .map(mapper::toDto)
                .toList();
    }

    public ElasticSearchDto save(ElasticSearchDto dto) {
        return Optional.of(dto)
                .map(mapper::toEntity)
                .filter(v -> !existsByCode(v))
                .map(repository::save)
                .map(mapper::toDto)
                .orElse(null);
    }

    public ElasticSearchDtos bulkSave(ElasticSearchDtos dtos) {
        return new ElasticSearchDtos()
                .setCodes(Optional.of(dtos)
                        .map(ElasticSearchDtos::getCodes)
                        .stream()
                        .flatMap(Collection::stream)
                        .map(this::save)
                        .filter(Objects::nonNull)
                        .toList());
    }

    public boolean existsByCode(ElasticSearchEntity entity) {
        return Optional.of(entity)
                .map(ElasticSearchEntity::getCode)
                .map(repository::findByCode)
                .isPresent();
    }
}
