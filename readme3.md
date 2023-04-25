# 记录审计日志——模板方法模式
> 项目需求：公司财务、审计、法务部门需要记录用户关键核心日志，防止日后未知的纠纷
> 1. 该日志记录不是普通的日志，不同的日志记录内容可能不一样
> 2. 可以灵活增加日志种类和处理
> 3. 核心日志包括：登录；订单创建；订单支付。其中订单创建需要有相关产品的信息；订单支付需要有相关产品信息以及支付方式和支付金额
> 4. 日志组装完成后，将日志信息发送到queue中，会由数据处理部门进行处理
## 代码实现
日志的pojo类
```java
@Data
public class AuditLog {
    private String account;
    private String action;
    private Date date;
    private String orderId;
    private Object details; //订单创建需要有相关产品的信息；订单支付需要有相关产品信息以及支付方式和支付金额
}
```
抽象的日志打印执行
```java
public abstract class AbstractAuditLogProcessor {
    // 创建我们的auditlog(基础部分)
    public final AuditLog buildAuditLog(String account, String action, String orderId){
        AuditLog auditLog = new AuditLog();
        auditLog.setAccount(account);
        auditLog.setAction(action);
        auditLog.setOrderId(orderId);
        auditLog.setDate(new Date());
        return auditLog;
    }

    protected abstract AuditLog buildDetails(AuditLog auditLog);

    public final void sendToQueue(AuditLog auditLog){
        // send to queue
    }

    public final void processAuditLog(String account, String action, String orderId) {
        this.sendToQueue(buildDetails(buildAuditLog(account, action, orderId)));
    }
}
```
具体的三个不同功能步骤的打印实现
```java
// 登录
@Component
public class LoginLogProcessor extends AbstractAuditLogProcessor{
    @Override
    protected AuditLog buildDetails(AuditLog auditLog) {
        return auditLog;
    }
}
```
```java
// 订单创建
@Component
public class OrderLogProcessor extends AbstractAuditLogProcessor{
    @Override
    protected AuditLog buildDetails(AuditLog auditLog) {
        String orderId = auditLog.getOrderId();
        String productDetails = "通过 orderId 一系列逻辑获取";
        auditLog.setDetails(productDetails);
        System.out.println(auditLog);
        return auditLog;
    }
}
```
```java
// 订单支付
@Component
public class PayLogProcessor extends AbstractAuditLogProcessor{
    @Override
    protected AuditLog buildDetails(AuditLog auditLog) {
        String orderId = auditLog.getOrderId();
        String allDetails = "通过 orderId 或者参数 获取产品信息，金额，支付方式";
        auditLog.setDetails(allDetails);
        System.out.println(auditLog);
        return auditLog;
    }
}
```
服务层调用
```java
@Service
public class OrderService {
    @Autowired
    private PayLogProcessor payLogProcessor;
    @Autowired
    private OrderLogProcessor orderLogProcessor;

    public Order createOrder(Integer oid) {
        Order order = new Order();
        // ... business logic
        orderLogProcessor.processAuditLog("account", "createOrder", oid.toString());
        return order;
    }

    public Order pay(PayBody payBody) {
        // ... business logic
        payLogProcessor.processAuditLog(payBody.getAccount(), "pay", order.getOrderId().toString());
        return null;
    }
}
```