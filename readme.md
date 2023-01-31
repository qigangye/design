# 支付案例——策略模式+工厂模式+门面模式
## 策略模式
### 优化上面的支付的步骤
1. 创建一个支付策略抽象PayStrategy，里面有一个抽象的pay方法
2. 创建三个对应的具体策略支付宝 ZfbPayStrategy、微信 WxPayStrategy、银行卡 BkPayStrategy，三个具体的策略实现类
3. 创建一个上下文封装角色PayContext，里面拥有策略抽象，还有对应的调用执行方法
```java
public class PayContext {
    private PayStrategy payStrategy;

    public PayContext(PayStrategy payStrategy) {
        this.payStrategy = payStrategy;
    }

    public Boolean execute(PayBody payBody){
        return this.payStrategy.pay(payBody);
    }
}
```
4. 在PayService的业务类中替换之前的 payHandler 的 zfbPay、wxPay、bkPay 方法，使用PayContext传入对应的具体策略对象
```java
if (payBody.getType() == 0){
    // 支付宝
//    flag = payHandler.zfbPay(payBody);
    flag = new PayContext(new ZfbPayStrategy()).execute(payBody);
} else if (payBody.getType() == 1){
    // wechat
    flag = new PayContext(new WxPayStrategy()).execute(payBody);
} else if (payBody.getType() == 2){
    // bank
    flag = new PayContext(new BkPayStrategy()).execute(payBody);
} else {
    throw new UnsupportedOperationException("Unsupport type, please choose 0,1,2");
}
```
### 策略模式的优缺点
#### 优点
* 算法可以自由切换
> 这是由策略模式本身定义的，只要实现抽象策略，就成为策略家族的一员，通过封装角色对其进行封装，保证对外提供「可自由切换」的策略。
* 避免使用多重条件判断
> 如果没有策略模式，比如一个策略家族有5个策略算法，一会儿要使用A策略，一会儿要使用B策略，如何设计？使用多重条件语句？多重条件不易维护，而且出错的概率大大增强。使用策略模式后，可以由其他模块决定采用何种策略，策略家族对外提供的访问接口就是封装类，简化了操作，同时避免了条件语句判断。
* 扩展性良好，极好的符合了开闭原则
#### 缺点
* 策略类有点多
> 每个策略都是一个类，复用的可能性很小，类数量增多<br/>
* 所有的策略类都需要对外暴露
> 上层模块必须知道哪些策略，然后才能决定使用那一个策略，这与迪米特法则相违背，我只是想使用了一个策略，凭什么就要了解这个策略？封装类还有什么意义？<br/>

## 策略模式+工厂模式+枚举
枚举类包含策略的全限定路径
```java
public enum StrategyEnum {
    ZfbPayStrategy("com.csrcb.design.pay.strategy.ZfbPayStrategy"),
    WxPayStrategy("com.csrcb.design.pay.strategy.WxPayStrategy"),
    BkPayStrategy("com.csrcb.design.pay.strateg.BkPayStrategy");

    String value = "";
    StrategyEnum(String value){
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
```
工厂类依靠策略枚举返回策略类
```java
public class StrategyFactory {
    public static PayStrategy getPayStrategy(StrategyEnum strategyEnum){
        PayStrategy payStrategy = null;
        try {
            payStrategy = (PayStrategy) Class.forName(strategyEnum.getValue()).newInstance();
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            //异常
            throw new RuntimeException(e);
        }
        return null;
    }
}
```
```java
class PayService{
    //...
    public Boolean pay(PayBody payBody){
        // 书写付款逻辑
        boolean flag = false;
        if (payBody.getType() == 0){
            // 支付宝
//            flag = payHandler.zfbPay(payBody);
            flag = new PayContext(StrategyFactory.getPayStrategy(StrategyEnum.ZfbPayStrategy)).execute(payBody);
        } else if (payBody.getType() == 1){
            // wechat
            flag = new PayContext(StrategyFactory.getPayStrategy(StrategyEnum.WxPayStrategy)).execute(payBody);
        } else if (payBody.getType() == 2){
            // bank
            flag = new PayContext(StrategyFactory.getPayStrategy(StrategyEnum.BkPayStrategy)).execute(payBody);
        } else {
            throw new UnsupportedOperationException("Unsupport type, please choose 0,1,2");
        }
        if (flag) {
            // 如果是true，保存到db
            saveToDb(payBody);
        }
        return flag;
    }
    //...
}
```
## 门面模式
> 一句话概括：门面对象是外界访问子系统内部的**唯一**通道

1. 将所有的strategy包下、strategyContext、strategyEnum、strategyFactory都封装到pay包下
2. 在pay包下创建一个facade的包，在里面创建一个门面对象
```java
public class StrategyFacade {
    // 可以定义一个map，不使用getStrategyEnum的switch方式
    
    public static Boolean pay(PayBody payBody){
        // 获取策略枚举
        StrategyEnum strategyEnum = getStrategyEnum(payBody.getType());
        if (strategyEnum == null) {
            return false;
        }
        // 获取策略对象
        PayStrategy payStrategy = StrategyFactory.getPayStrategy(strategyEnum);
        // 生成策略的上下文
        PayContext payContext = new PayContext(payStrategy);
        // 进行业务逻辑处理
        return payContext.execute(payBody);
    }

    private static StrategyEnum getStrategyEnum(int type) {
        switch (type){
            case 0:
                return StrategyEnum.ZfbPayStrategy;
            case 1:
                return StrategyEnum.WxPayStrategy;
            case 2:
                return StrategyEnum.BkPayStrategy;
            default:
                return null;
        }
    }
}
```
3. 后续的service层直接调用即可
```java
// 后续对于付款模块的删除或增加或修改，无需改动service
// 不会对调用层产生任何代码的改动
// 调用层使用pay模块，无需关系实现的逻辑，只需要将入参传递给pay模块即可
public Boolean pay(PayBody payBody){
    // 书写付款逻辑
    boolean flag = false;
    flag = StrategyFacade.pay(payBody);
    if (flag) {
        // 如果是true，保存到db
        saveToDb(payBody);
    }
    return flag;
}
```
### 门面模式的优缺点
优点：
* 减少系统的相互依赖
* 提高了灵活性
* 提高了安全性

缺点
* 最大的缺点就是不符合开闭原则，对修改关闭，对扩展开放。一旦需求调整，修改门面角色的代码逻辑，有风险
## 工厂模式
> **工厂模式变种模式比较多**，虽然支付案例中的工厂类使用的是static的静态方法的工厂类，但是与平常的Utils类区分，utils是功能（生成uuid、日期转化）为导向的。
> 而案例中的factory类，是以产出（一类行为类：案例中是策略类）为导向的
## 单例模式
### 优缺点
* 只有一个实例，减少了内存开支，减少了系统的性能开销，避免资源的多重占用；可以在系统设置全局的访问点，优化和共享资源访问
* 一般没有接口，扩展困难（特性如此）
饿汉式、懒汉式、双重检查模式(变量要用`volatile`修饰)
# 责任链模式
> 项目需求：付款完成后的投放业务，在资源位中展示符合当前用户的资源。在支付成功页筛选活动banner和推荐商品。<br/>
> 要求：
> 1. 允许运营人员
> 2. 资源的过滤规则相对灵活多变：
>   * 过滤规则大部分可重用，但也会有扩展和变更
>   * 不同资源位的过滤规则和过滤顺序是不同的
>   * 同一个资源位由于业务所处的不同阶段、过滤规则可能不同
> 3. 允许规则实时增减和顺序调整<br/>
> 过滤规则:
> 1. 用户个人资质是否满足投放业务
> 2. 用户所在的城市是否在业务投放城市
> 3. 用户近期所购买的产品是否符合业务投放人群
> 4. 新用户首次购买投放指定业务
## 定义
使多个对象都有机会处理请求，从而避免了请求的发送者和接受者之间的耦合关系。将这些对象连成一条链，并沿着这条链传递该请求，直到有对象处理它为止
> 责任链模式的核心在「链」上，「链」是由多个处理者ConcreteHandler组成的
```java
// 定义一个抽象的handler
public abstract class Handler{
    private Handler nextHandler;//指向下一个处理者
    private int level;//处理者能够处理的级别。为我们提供了一些拓展的可能性
    public Handler(int level){
        this.level = level;
    }
    public void setNextHandler(Handler handler){
        this.nextHandler = handler;
    }
    //抽象方法，子类实现
    public abstract void execute(Request request);
}
```
## 方案
1. 创建一个handler的抽象类
```java
public abstract class AbstractSuggestBusinessHandler {
    /**
     * @param userInfo 用户信息
     * @param suggestLists 当前用户需要被投放的业务
     */
    abstract void processHandler(UserInfo userInfo, List<String> suggestLists);
}
```
2. 创建对应的具体的实现
```java
public class PersonalCheckHandler extends AbstractSuggestBusinessHandler{
    @Override
    public void processHandler(UserInfo userInfo, List<String> suggestLists) {
        // 模拟：通过个人资质的check，我们找到了4个可以投放的业务，放到 suggestLists 中。
        suggestLists.add("1");
        suggestLists.add("2");
        suggestLists.add("3");
        suggestLists.add("4");
    }
}
```
```java
public class CityCheckHandler extends AbstractSuggestBusinessHandler{
    @Override
    public void processHandler(UserInfo userInfo, List<String> suggestLists) {
        // 通过userInfo 获取city属性
        String city = userInfo.getCity();
        // 通过 city 和之前保留的 4 个业务信息进行对比，然后筛选出剩余的 3 个业务投放
        suggestLists.remove("1");
    }
}
```
```java
public class RecentCheckHandler extends AbstractSuggestBusinessHandler{
    @Override
    public void processHandler(UserInfo userInfo, List<String> suggestLists) {
        // 通过 userInfo 获取 近期购买的产品信息buyProducts 属性
        List<String> buyProducts = userInfo.getBuyProducts();
        // 通过 buyProducts 和之前保留的 3 个业务信息进行对比，然后筛选出剩余的 2 个业务投放
        suggestLists.remove("2");
    }
}
```
```java
public class NewUserCheckHandler extends AbstractSuggestBusinessHandler{
    @Override
    public void processHandler(UserInfo userInfo, List<String> suggestLists) {
        // 通过 userInfo 获取 新用户标志 isNewUser 属性
        boolean newUser = userInfo.isNewUser();
        if (newUser) {
            suggestLists = new ArrayList<>(); // 特定的新用户奖励
        }
    }
}
```
3. 编写调用执行handler类
```java
@Component
public class SuggestBusinessHandlerProcess {
    @Value("#{'${suggest.business.handler}'.split(',')}")
    private List<String> handlers;

    public void process(UserInfo userInfo, List<String> suggestLists){
        // 如果想要实时的进行顺序的调整或者是增减，那必须要使用配置中心进行配置
        // 比如springcloud里面自带的git这种方式配置； applo 配置中心。
        for (String handler : handlers){
            try {
                AbstractSuggestBusinessHandler handle = (AbstractSuggestBusinessHandler) Class.forName(handler).newInstance();
                handle.processHandler(userInfo, suggestLists);
            } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
```
在配置中心编写，对应的顺序，业务自行在配置中心配置顺序或者增减业务handler
```properties
## 比喻成配置中心
suggest.business.handler=com.csrcb.design.handler.PersonalCheckHandler,com.csrcb.design.handler.CityCheckHandler,com.csrcb.design.handler.RecentCheckHandler,com.csrcb.design.handler.NewUserCheckHandler
```
## 优缺点
* 责任链模式非常显著的优点是将请求和处理分开。请求者可以不知道是谁出里的，处理者可以不用知道请求的全貌，两者解耦，提高系统的灵活性
* 责任链的缺点：一是性能问题，每个请求都是从链头遍历到链尾，特别是链比较长的时候，性能是一个非常大的问题；二是调试不方便，特别是链条比较长、环节比较多的时候。
# 状态模式 + 观察者模式
> 项目需求：用户从开始下订单，到支付完成，再到物流发货，最终用户确认收货；整个流程涉及到很多订单状态，需要通过代码对订单状态进行管理。
> 此外，用户或物流部门每一次触发的不同操作都有可能改变订单状态（如：用户创建订单操作导致订单状态为待支付状态；用户支付操作导致订单状态为待发货状态；物流部门发货操作导致订单状态为待收货状态；哟怒胡确认收货导致订单状态为订单完成状态）。
> * 用户创建订单——>支付订单——>发货——>收货——>订单完成
> * 要求：
> * 1. 创建订单后，订单状态初始化为待支付；
> * 2. 订单状态包括：待支付、待发货、待收货、订单完成；（`状态模式`）
> * 3. 触发订单状态的操作：支付订单、发货、确认收货（`观察者模式`）
## 项目工程实现
引入spring的状态机的相关pom依赖
```xml
<dependency>
    <groupId>org.springframework.statemachine</groupId>
    <artifactId>spring-statemachine-core</artifactId>
    <version>2.2.3.RELEASE</version>
</dependency>
```
订单、订单状态的枚举、订单状态转化的事件的枚举
```java
// 状态转化的一个控制机。状态机：初始化状态；配置我们所有状态之间的转化关系；一些持久化的操作（如：redis）
public class Order {
    private Integer orderId;
    // 订单状态
    private OrderState orderState;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }
}
```
```java
public enum OrderState {
    ORDER_WAIT_PAY,//待支付
    ORDER_WAIT_SEND,// 待发货
    ORDER_WAIT_RECEIVE,// 待收货
    ORDER_FINISH; // 订单完成
}
```
```java
public enum OrderStateChangeAction {
    PAY_ORDER,// 支付操作
    SEND_ORDER,// 发货操作
    RECEIVE_ORDER;// 收货操作
}
```
使用Spring状态机的配置
```java
@Configuration
@EnableStateMachine(name = "orderStateMachine")
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderStateChangeAction> {
    // 初始化状态；
    public void configure(StateMachineStateConfigurer<OrderState, OrderStateChangeAction> states) throws Exception {
        states.withStates().initial(OrderState.ORDER_WAIT_PAY)
                .states(EnumSet.allOf(OrderState.class));
    }

    // 配置我们所有状态之间的转化关系；
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderStateChangeAction> transitions) throws Exception {
        transitions.withExternal().source(OrderState.ORDER_WAIT_PAY)
                .target(OrderState.ORDER_WAIT_SEND)
                .event(OrderStateChangeAction.PAY_ORDER)
                .and()
                .withExternal().source(OrderState.ORDER_WAIT_SEND)
                .target(OrderState.ORDER_WAIT_RECEIVE)
                .event(OrderStateChangeAction.SEND_ORDER)
                .and()
                .withExternal().source(OrderState.ORDER_WAIT_RECEIVE)
                .target(OrderState.ORDER_FINISH)
                .event(OrderStateChangeAction.RECEIVE_ORDER);
    }

    // 配置状态机持久化
    @Bean
    public DefaultStateMachinePersister machinePersister(){
        return new DefaultStateMachinePersister(new StateMachinePersist<Object, Object, Order>() {
            @Override
            public void write(StateMachineContext<Object, Object> stateMachineContext, Order order) throws Exception {
                // 持久化操作，可以通过任何形式持久化。redis、mongodb、mysql、ecache
            }

            @Override
            public StateMachineContext<Object, Object> read(Order order) throws Exception {
                // 本来应该从持久化组件里进行读取的，但是没有做持久化
                return new DefaultStateMachineContext(order.getOrderState(), null, null, null);
            }
        });
    }
}
```
使用监听
```java
// 监听器是监听到action后进行状态的变更
@Component("orderStateListener")
@WithStateMachine(name = "orderStateMachine")
public class OrderStateListener {
    @OnTransition(source = "ORDER_WAIT_PAY", target = "ORDER_WAIT_SEND")
    public boolean payToSend(Message<OrderStateChangeAction> message){
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderState(OrderState.ORDER_WAIT_SEND);
        return true;
    }

    @OnTransition(source = "ORDER_WAIT_SEND", target = "ORDER_WAIT_RECEIVE")
    public boolean sendToReceive(Message<OrderStateChangeAction> message){
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderState(OrderState.ORDER_WAIT_RECEIVE);
        return true;
    }

    @OnTransition(source = "ORDER_WAIT_RECEIVE", target = "ORDER_FINISH")
    public boolean receiveToFinish(Message<OrderStateChangeAction> message){
        Order order = (Order) message.getHeaders().get("order");
        order.setOrderState(OrderState.ORDER_FINISH);
        return true;
    }
}
```
## 客户端测试
### 订单服务层实现
```java
@Service
public class OrderService {
    @Autowired
    private StateMachine<OrderState, OrderStateChangeAction> orderStateMachine;

    @Autowired
    private StateMachinePersister<OrderState, OrderStateChangeAction, Order> stateMachinePersister;

    // 模拟一个存储（示例，不做连接数据库展示）
    private List<Order> orders = new ArrayList<>();
    public Order createOrder(Integer oid) {
        Order order = new Order();
        order.setOrderState(OrderState.ORDER_WAIT_PAY);
        order.setOrderId(oid);
        // 创建的order持久化至数据库中，防止下次访问的时候查询不到
        orders.add(order);//模拟存储到DB
        return order;
    }

    // 后续对于付款模块的删除或增加或修改，无需改动service
    // 不会对调用层产生任何代码的改动
    // 调用层使用pay模块，无需关系实现的逻辑，只需要将入参传递给pay模块即可
    public Order pay(PayBody payBody){
        // 书写付款逻辑
        boolean flag = false;
        flag = StrategyFacade.pay(payBody);
        if (flag) {
            Order order = orders.get(0);// 模拟从DB获取的数据
            Message message = MessageBuilder.withPayload(OrderStateChangeAction.PAY_ORDER).setHeader("order", order).build();
            // 发送消息
            if (changeStateAction(message, order)){
                return order;
            }
            // 如果是true，保存到db
            saveToDb(payBody);
        }
        return null;
    }

    private void saveToDb(PayBody payBody) {
    }

    public Order send(Integer oid) {
        Order order = orders.get(0);// 模拟从DB获取的数据
        Message message = MessageBuilder.withPayload(OrderStateChangeAction.SEND_ORDER).setHeader("order", order).build();
        if (changeStateAction(message, order)){
            return order;
        }
        return null;
    }

    public Order receive(Integer oid) {
        Order order = orders.get(0);// 模拟从DB获取的数据
        Message message = MessageBuilder.withPayload(OrderStateChangeAction.RECEIVE_ORDER).setHeader("order", order).build();
        if (changeStateAction(message, order)){
            return order;
        }
        return null;
    }

    /**
     * 发送订单状态转换事件
     * @param message
     * @param order
     * @return
     */
    private boolean changeStateAction(Message<OrderStateChangeAction> message, Order order){
        try {
            orderStateMachine.start();
            stateMachinePersister.restore(orderStateMachine, order);// 状态机状态的恢复，restore是取
            boolean res = orderStateMachine.sendEvent(message);
            stateMachinePersister.persist(orderStateMachine, order);// 状态机状态的存储，persist是存
            return res;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            orderStateMachine.stop();
        }
        return false;
    }
}
```
### 服务接口提供
```java
@RestController
public class OrderController {
    @Autowired
    private OrderService orderService;

    /**
     * 创建订单
     * @param oid
     * @return
     */
    @GetMapping("/mkOrder")
    public Order createOrder(@RequestParam("oid") Integer oid){
        return orderService.createOrder(oid);
    }

    /**
     * 支付订单
     * @param payBody
     * @return
     */
    @PostMapping("/pay")
    public Order payOrder(@RequestBody PayBody payBody){
        return orderService.pay(payBody);
    }

    /**
     * 发送订单
     * @param oid
     * @return
     */
    @GetMapping("/send")
    public Order send(@RequestParam("oid") Integer oid){
        return orderService.send(oid);
    }

    /**
     * 确认订单收货
     * @param oid
     * @return
     */
    @GetMapping("/receive")
    public Order receive(@RequestParam("oid") Integer oid){
        return orderService.receive(oid);
    }
}
```
### 客户端http发起请求
#### 创建订单
请求调用
```
GET http://localhost:8080/mkOrder?oid=12
```
响应报文
```json
{
  "orderId": 12,
  "orderState": "ORDER_WAIT_PAY"
}
```
#### 支付订单
请求调用
```
POST http://localhost:8080/pay
Content-Type: application/json

{
  "account": "xiaoming",
  "type": 0,
  "product": "1234",
  "amount": 123
}
```
响应返回
```json
{
  "orderId": 12,
  "orderState": "ORDER_WAIT_SEND"
}
```
#### 发送订单
请求调用
```
GET http://localhost:8080/send?oid=12
```
响应报文
```json
{
  "orderId": 12,
  "orderState": "ORDER_WAIT_RECEIVE"
}
```
#### 用户确认订单收货
请求调用
```
GET http://localhost:8080/receive?oid=12
```
响应报文
```json
{
  "orderId": 12,
  "orderState": "ORDER_FINISH"
}
```
