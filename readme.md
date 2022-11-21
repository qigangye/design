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
