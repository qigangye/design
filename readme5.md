# 多种类第三方账号登录——桥接模式
> 项目需求：简化用户的登录注册流程，允许用户直接使用经过授权的第三方账号登录网站
> 要求：
> 1. 遵循开闭原则：可以新增抽象部分和实现部分，且它们之间不会相互影响
> 2. 遵循单一职责原则：抽象部分专注于处理高层逻辑，实现部分处理平台细节
## 三方账号登录原理 简述
自建账号体系的注册和登录，对用户来讲，过程繁琐，结果很多用户并不想注册你开发的网站或者APP，所以用户量增长缓慢。此时可考虑使用第三方账号登录，比如微信登录和QQ登录<br/>
比如用户点击第三方登录时，会跳转到第三方登录SDK内部；用户输入第三方登录用户名或密码，有些第三方登录平台，可以直接调用已经登录的账号，例如：QQ；完成第三方登录的；登录完成后，第三方平台或者SDK会回调我们的应用，在回调的信息里面，可以拿到用户在第三方平台的OpenId，以及昵称、头像等信息
## 桥接模式
实现implementor
```java
public interface LoginFunc {
    boolean login(String name, String pwd, String type);
}
```
```java
public class WbLoginFunc implements LoginFunc{
    @Override
    public boolean login(String name, String pwd, String type) {
        // 进行第三方账号的校验流程
        System.out.println("微博验证通过，可以登录");
        return true;
    }
}
```
```java
public class ZfbLoginFunc implements LoginFunc{
    @Override
    public boolean login(String name, String pwd, String type) {
        // 进行第三方账号的校验流程
        System.out.println("支付宝验证通过，可以登录");
        return true;
    }
}
```
抽象化角色
```java
public abstract class AbstractLoginProcessor {
    protected LoginFunc loginFunc;

    public AbstractLoginProcessor(LoginFunc loginFunc) {
        this.loginFunc = loginFunc;
    }

    public abstract boolean loginExecute(String name, String pwd, String type);
}
```
```java
public class ThirdPartLogin extends AbstractLoginProcessor{
    public ThirdPartLogin(LoginFunc loginFunc) {
        super(loginFunc);
    }

    @Override
    public boolean loginExecute(String name, String pwd, String type) {
        return super.loginFunc.login(name, pwd, type);
    }
}
```
客户端service调用
```java
public class UserService {
    public Boolean login(String name, String pwd, String type) {
        // 此处不使用策略或者享元模式封装，为了演示桥接模式的客户端调用
        if (type.equals("wb")) {
            LoginFunc lf = new WbLoginFunc();
            AbstractLoginProcessor loginProcessor = new ThirdPartLogin(lf);
            return loginProcessor.loginExecute(name, pwd, type);
        }
        // ...支付宝等后续三方
        return true;
    }
}
```
## 适配器模式（强烈不推荐）——后续不好维护
