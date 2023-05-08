package com.csrcb.design.esquery;

import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class EsQueryProcessor {
    // 1.要是用stream 返回 为了节省内存
    public Stream<Map<String, Object>> scrollStream(String query, Long fetchSize){
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new ScrollIterator(query, fetchSize), 0), false);
    }

    // 2.要用迭代器
    private class ScrollIterator implements Iterator<Map<String, Object>> {
        private String scrollId;
        private List<String> columns;
        Iterator<Map<String, Object>> iterator;
        RestTemplate restTemplate = new RestTemplate();//真实项目中使用restTemplate的时候，一定是进行过bean配置注入的。此处直接使用new 关键字是为了迭代模式的演示，注重侧重点
        // 构造函数进行第一次查询并且初始化我们后续需要使用的columns 和 iterator 和 scroll
        public ScrollIterator(String query, Long fetchSize) {
            EsSqlResult esSqlResult = restTemplate.postForObject("http://localhost:9200/_sql?format=json",
                    new EsSqlQuery(query, fetchSize), EsSqlResult.class);//第一次访问的结果出来了
            this.scrollId = esSqlResult.getCursor();
            this.columns = esSqlResult.getColumns().stream().map(x -> x.get("name")).collect(Collectors.toList());
            this.iterator = convert(columns, esSqlResult).iterator();
        }

        // hasNext根据 是否scrollId为null进行后续的 第。。。次的访问，直到scrollId为null
        @Override
        public boolean hasNext() {
            return iterator.hasNext() || scrollNext();
        }

        private boolean scrollNext() {
            if (iterator == null || this.scrollId == null){
                return false;
            }
            // 后续访问
            EsSqlResult esSqlResult = restTemplate.postForObject("http://localhost:9200/_sql?format=json",
                    new EsSqlQuery(this.scrollId), EsSqlResult.class);
            this.scrollId = esSqlResult.getCursor();
            this.iterator = convert(columns, esSqlResult).iterator();
            return iterator.hasNext();
        }

        @Override
        public Map<String, Object> next() {
            return iterator.next();
        }
    }

    // 3.返回结果传统一点 List<Map>
    private List<Map<String, Object>> convert(List<String> columnNames, EsSqlResult esSqlResult) {
        List<Map<String, Object>> results = new ArrayList<>();
        for (List<Object> row : esSqlResult.getRows()) {
            Map<String, Object> map = new HashMap<>();
            for (int i = 0; i < columnNames.size(); i++) {
                map.put(columnNames.get(i), row.get(i));
            }
            results.add(map);
        }
        return results;
    }
}
