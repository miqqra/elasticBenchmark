package com.example.elasticbenchmark.mapper;

import com.example.elasticbenchmark.dto.ElasticSearchDto;
import com.example.elasticbenchmark.entity.ElasticSearchEntity;
import org.mapstruct.Mapper;

@Mapper
public abstract class ElasticSearchMapper {

    public abstract ElasticSearchEntity toEntity(ElasticSearchDto source);

    public abstract ElasticSearchDto toDto(ElasticSearchEntity source);
}
