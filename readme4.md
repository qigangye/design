# ElasticSearch数据查询-Sroll——迭代器模式
> 项目需求：数据从mysql迁移至Es，ES数据查询的默认fetchSize为10000，如果查询超过10000条数据，需要通过scroll形式进行查询
> 要求：
> 1. 处于安全考虑，查询直连ES-IP:9200，不可使用第三方jar包
> 2. 由于目前项目的查询方式是基于mysql的，为了减少改动，暂时使用sql语句查询，ES-IP:9200/_sql
> 3. 我们需要将结果以stream的形式进行返回。（避免我们的内存占用过大以及瞬时的网络带宽问题）
## ElasticSearch本地7版本的搭建
7版本后支持使用_sql的方式查询
准备好es索引
```shell
curl -XPUT http://localhost:9200/estable -H 'content-type:application/json' --data-raw '{
  "mappings": {
    "properties": {
      "username": {"type": "text"},
      "age": {"type": "text"}
    }
  }
}'
```
准备一些数据后续测试使用
```shell
curl -XPOST 'http://localhost:9200/estable/_doc/1' -H 'Content-Type:application/json' -d '{
  "id": 1,
  "username": "zhao",
  "age": "11"
}'
curl -XPOST "http://127.0.0.1:9200/estable/_bulk" -H "Content-Type: application/json" -d '
{ "create": {"_id": 2, "_index": "estable"} }
{ "doc": {"id":2,"username":"qian","age": "12"} }
{ "create": {"_id": 3, "_index": "estable"} }
{ "doc": {"id":3,"username":"sun","age": "11"} }
{ "create": {"_id": 4, "_index": "estable"} }
{ "doc": {"id":4,"username":"li","age": "14"} }
{ "create": {"_id": 5, "_index": "estable"} }
{ "doc": {"id":5,"username":"zhou","age": "11"} }
{ "create": {"_id": 6, "_index": "estable"} }
{ "doc": {"id":6,"username":"wu","age": "9"} }
{ "create": {"_id": 7, "_index": "estable"} }
{ "doc": {"id":7,"username":"zheng","age": "11"} }
{ "create": {"_id": 8, "_index": "estable"} }
{ "doc": {"id":8,"username":"wang","age": "11"} }
'
```
使用es自带的sql的api查询方式
```shell
curl -XPOST http://localhost:9200/_sql?format=json -H 'Content-type:application/json' --data-raw '{
  "query":"select * from estable",
  "fetch_size":5
}'
```
返回的结果
```json
{"columns":[{"name":"age","type":"text"},{"name":"id","type":"long"},{"name":"username","type":"text"}],"rows":[["12",2,"qian"],["11",1,"zhao"],["11",3,"sun"],["14",4,"li"],["11",5,"zhou"]],"cursor": "08fsA0RGTACEkNFKwzAUhnPaiGM3vkrLELzxIoNmo7io7ZJuuRmlSWchpmOprPg0PoTvN9NO5qUfHPj/k3MS/oBE8IYCQANnz92oELoBmNaNNmrn2mOHd6qtzpepAfiGEAWBF+Mq9moE+85wC4Rhudd+olEomHw4fbTlu/fwBdFD7cgpWcoNi7ZF6mQm20yoPvfFTeqeNodmlajiNerzam0smUunLe0LKqiI0zWfzT9Lk9Fnmr5oziz540Rtutxy4fSCRYIe4lUe31dGxmzGOKcJIf+9Rcgjmv5+RAj1EAJwp/vO56p9GMCmtfvBXENdz28R+gEAAP//AwA="}
```
使用上面cursor返回的值继续查询
> 快速使用，防止cursor过期，导致查询报错
```shell
curl -XPOST http://localhost:9200/_sql?format=json -H 'Content-type:application/json' --data-raw '{
  "cursor": "08fsA0RGTACEkNFKwzAUhnPaiGM3vkrLELzxIoNmo7io7ZJuuRmlSWchpmOprPg0PoTvN9NO5qUfHPj/k3MS/oBE8IYCQANnz92oELoBmNaNNmrn2mOHd6qtzpepAfiGEAWBF+Mq9moE+85wC4Rhudd+olEomHw4fbTlu/fwBdFD7cgpWcoNi7ZF6mQm20yoPvfFTeqeNodmlajiNerzam0smUunLe0LKqiI0zWfzT9Lk9Fnmr5oziz540Rtutxy4fSCRYIe4lUe31dGxmzGOKcJIf+9Rcgjmv5+RAj1EAJwp/vO56p9GMCmtfvBXENdz28R+gEAAP//AwA="
}'
```
返回的结果（此时返回剩余的数据，此时cursor消失，因为数据量结束，但是第一次的columns字段没有显示）
```json
{"rows":[["9",6,"wu"],["11",7,"zheng"],["11",8,"wang"]]}
```
## 方案
常规「不可取」
1. 进行第一次访问，然后获取columns，rows和cursor
2. 转化我们第一次结果为map形式的json
3. 拿第一次的cursor id，进行第二次访问
4. 用第一次记录的columns组装第二次的rows结果为map形式的json  以此类推
5. 把所有的转化结果存储到一个对象（数据量太大，占用内存）中，返回给调用者（数据量太大，带宽受不了）
### 升级方案——迭代器模式
1. 将我们访问封装到迭代器中（第一次访问以迭代器的构造函数访问，初始化我们的columns供后续使用；将第一次的结果转化为 map 形式的json 供 迭代器使用）
2. hasnext方法中，进行我们的后续的多次访问
## 代码实现
es的请求入参
```java
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
public class EsSqlQuery {
    private String query;
    private Long fetch_size;
    private String cursor;

    public EsSqlQuery(String cursor) {
        this.cursor = cursor;
    }

    public EsSqlQuery(String query, long fetch_size) {
        this.query = query;
        this.fetch_size = fetch_size;
    }
}
```
封装接收es查询的返回的结果
```java
@Data
public class EsSqlResult {
    private List<Map<String, String>> columns;
    private List<List<Object>> rows;
    private String cursor;
}
```
封装流处理
```java
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
```
编写测试案例
```java
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
```
```java
@RestController
public class EsController {
    @Autowired
    private EsService esService;

    @PostMapping("es")
    public Boolean esQuery (@RequestParam String query, Long fetchSize){
        return esService.query(query, fetchSize);
    }
}
```
使用idea的httpclient测试
```shell
POST http://localhost:8080/es
Content-Type: multipart/form-data; boundary=WebAppBoundary

--WebAppBoundary
Content-Disposition: form-data; name="query"
Content-Type: text/plain

select * from estable
--WebAppBoundary
Content-Disposition: form-data; name="fetchSize"
Content-Type: text/plain

5
--WebAppBoundary--
```
