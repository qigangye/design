package com.csrcb.design.service;

import com.csrcb.design.esquery.EsQueryProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.stream.Stream;

@Service
public class EsService {
    @Autowired
    private EsQueryProcessor esQueryProcessor;

    public Boolean query(String query, Long fetchSize) {
        Stream<Map<String, Object>> mapStream = esQueryProcessor.scrollStream(query, fetchSize);
        mapStream.forEach(System.out::println);
        return true;
    }
}
