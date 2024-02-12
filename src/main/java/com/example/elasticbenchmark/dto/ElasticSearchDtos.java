package com.example.elasticbenchmark.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class ElasticSearchDtos {

    private List<ElasticSearchDto> codes;
}
