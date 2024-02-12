package com.example.elasticbenchmark.repository;

import com.example.elasticbenchmark.entity.ElasticSearchEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ElasticSearchRepository extends ElasticsearchRepository<ElasticSearchEntity, Long> {

    ElasticSearchEntity findByCode(String code);
}
