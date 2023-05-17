# 商品多级分类目录——组合模式 + 访问者模式
> 项目需求：目前商城中有很多商品目录，且层级很多。为了对层级目录进行管理，需要满足对层级目录的增加和删除
> 要求：
> 1. 层级目录是保存在DB中的，项目一旦初始化，需要将层级目录设置为超热点缓存
> 2. 支持在线对层级目录的增删
> 3. 前端获取一定层级目录后，每个24小时对层级目录进行后端重新获取
> 4. 层级目录更新需要先更新redis缓存，再更新DB。后台层级目录缓存应该为永不过期缓存

1. DB存储数据
|id      |parentId        | name    |
|--------|----------------|---------|
|1       |0               | 书籍      |
|2       |1               | 技术书籍    |
|3       |1               | 历史书籍    |
|4       |2               | 并发编程    | 
|5       |2               | JVM     |

2. 当我们的项目进行初始化的时候，就会将我们的DB里的这些目录进行如下的查询转化为如下对象存储
```json
{
  "id": "1",
  "itemName": "书籍",
  "pid": "0",
  "childItems": [{
    "id": "2",
    "itemName": "技术书籍",
    "pid": "1",
    "childItems": [{
      "id": "4",
      "itemName": "并发编程",
      "pid": "2",
      "childItems": null
    },{
      "id": "5",
      "itemName": "JVM",
      "pid": "2",
      "childItems": null
    }]
  },{
    "id": "3",
    "itemName": "历史书籍",
    "pid": "1",
    "childItems": null
  }]
}
```
3. 将这个对象存储到我们的 Nginx 本地缓存，并设置为永不过期
4. APP、页面进行调用的时候，从 Nginx 获取这份目录，并且APP端进行缓存，每隔24小时进行一次标记位的访问（如果业务对我们的目录结构有增删，标记位（序号）就会有变化，前端会再次对数据进行重新的同步）
5. 如果有修改，后端是先修改缓存，只针对缓存中需要修改的部分进行修改，然后更新缓存（目录层级结构+标记位）。缓存更新成功后再更新DB

## 编码实战
### 组合部分
1. 抽象元素
```java
public abstract class AbstractProductItem {
    public abstract void removeChild(AbstractProductItem abstractProductItem);
    public abstract void addChild(AbstractProductItem abstractProductItem);
}
```
2. 具体元素 or 结构体
```java
@Data
public class ProductItem extends AbstractProductItem {
    private String id;
    private String pid;
    private String name;
    private List<ProductItem> childs = new ArrayList<>();

    public ProductItem() {
    }

    public ProductItem(String id, String pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    @Override
    public void removeChild(AbstractProductItem item) {
        ProductItem removeItem = (ProductItem) item;
        this.childs = childs.stream().filter(x -> !x.getId().equals(removeItem.getId())).collect(Collectors.toList());
    }

    @Override
    public void addChild(AbstractProductItem item) {
        this.childs.add((ProductItem) item);
    }
}
```
3. 数据mock
```java
// 模拟数据库的预热数据mock
public class MockDb {
    public static ProductItem PRODUCT_ITEM = new ProductItem();
    static {
        PRODUCT_ITEM.setId("1");
        PRODUCT_ITEM.setPid("0");
        PRODUCT_ITEM.setName("书籍");
        List<ProductItem> childs = new ArrayList<>();
        ProductItem c1 = new ProductItem("2","1", "技术书籍");
        ProductItem c2 = new ProductItem("3","1", "历史书籍");
        List<ProductItem> childs1 = new ArrayList<>();
        ProductItem c3 = new ProductItem("4","2", "并发编程");
        ProductItem c4 = new ProductItem("5","2", "JVM");
        childs1.add(c3);
        childs1.add(c4);
        c1.setChilds(childs1);
        childs.add(c1);
        childs.add(c2);
        PRODUCT_ITEM.setChilds(childs);
    }
}
```
### 访问者模式部分
1. 抽象访问者
```java
public interface ItemVistor<T> {
    T vistor(ProductItem productItem);
}
```
2. 具体访问者
```java
@Component
public class AddItemVistor implements ItemVistor<ProductItem> {
    // 入参是 id=2，pid=1
    @Override
    public ProductItem vistor(ProductItem productItem) {
        ProductItem currentItem = MockDb.PRODUCT_ITEM;// 从缓存来的
        if (productItem.getId().equals(currentItem.getId())) {
            throw new UnsupportedOperationException("根节点是唯一的");
        }
        if (productItem.getPid().equals(currentItem.getId())) {
            currentItem.addChild(productItem);
            return currentItem;
        }
        addChild (productItem, currentItem);
        return currentItem;
    }

    private void addChild(ProductItem productItem, ProductItem currentItem) {
        for (ProductItem item : currentItem.getChilds()) {
            if (item.getId().equals(productItem.getPid())) {
                item.addChild(productItem);
                break;
            }else {
                addChild(productItem, item);
            }
        }
    }
}
```
```java
@Component
public class DelItemVistor implements ItemVistor<ProductItem> {
    // 入参是 id=2，pid=1
    @Override
    public ProductItem vistor(ProductItem productItem) {
        ProductItem currentItem = MockDb.PRODUCT_ITEM;// 从缓存来的
        if (productItem.getId().equals(currentItem.getId())) {
            throw new UnsupportedOperationException("根节点不能删除");
        }
        if (productItem.getPid().equals(currentItem.getId())) {
            currentItem.removeChild(productItem);
            return currentItem;
        }
        delChild (productItem, currentItem);
        return currentItem;
    }

    private void delChild(ProductItem productItem, ProductItem currentItem) {
        for (ProductItem item : currentItem.getChilds()) {
            if (item.getId().equals(productItem.getPid())) {
                item.removeChild(productItem);
                break;
            }else {
                delChild(productItem, item);
            }
        }
    }
}
```

### 测试
```java
@Service
public class ItemService {
    @Autowired
    private DelItemVistor delItemVistor;
    @Autowired
    private AddItemVistor addItemVistor;

    // 这部分只有初始化的时候获取一次 或者 直接预热到缓存中
    public ProductItem getItem (){
        System.out.println("从DB中获取所有的目录");
        System.out.println("将数据组装为 ProductItem");
        System.out.println("将组装好的 ProductItem 放入缓存中，永不过期");
        return MockDb.PRODUCT_ITEM;
    }

    public ProductItem delItem (ProductItem productItem){
        ProductItem item = delItemVistor.vistor(productItem);
        MockDb.PRODUCT_ITEM = item; //更新缓存
        System.out.println("更新DB");
        return item;
    }

    public ProductItem addItem (ProductItem productItem){
        ProductItem item = addItemVistor.vistor(productItem);
        MockDb.PRODUCT_ITEM = item; //更新缓存
        System.out.println("更新DB");
        return item;
    }
}
```

```java
@RestController
public class ItemController {
    @Autowired
    private ItemService itemService;

    @PostMapping("get")
    public ProductItem getItem(){
        return itemService.getItem();
    }

    @PostMapping("del")
    public ProductItem delItem(@RequestBody ProductItem productItem){
        return itemService.delItem(productItem);
    }

    @PostMapping("add")
    public ProductItem addItem(@RequestBody ProductItem productItem){
        return itemService.addItem(productItem);
    }
}
```