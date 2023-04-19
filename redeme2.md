# 申请增值税发票（企业+个人）——建造者 + 原型
> 项目需求：用户支付完成后，部分企业用户或个人账户需要开电子增值税发票，实现该功能
> 1. 由于电子增值税发票所需内容较多，而且随国家政策可能有所修改，发票内容的添加删除及后台对象的组装尽量做到灵活
> 2. 发票开具不是高并发访问接口且无法缓存，尽量保证发票创建的性能
## 项目代码实现
pojo类，个人发票以及公司发票
```java
@Data
public class PsnTicket {
    private String type;
    private String footer;

    private String title;
    private String account;

    private String content;//从配置中心或者db。（缓存里获取，只不过如果一放DB或者配置中心有修改，需要同时更新缓存）
    private String product;//db中获取（缓存里有一些热门产品的）
}
```
```java
@Data
public class OrgTicket {
    private String type;
    private String footer;

    private String title;
    private String account;

    private String content;//从配置中心或者db。（缓存里获取，只不过如果一放DB或者配置中心有修改，需要同时更新缓存）
    private String product;//db中获取（缓存里有一些热门产品的）
    private String bankInfo;//校验我们的银行卡信息(通过我们的第三方银行相关接口进行的校验)
}
```
抽象建造者
```java
public abstract class AbstractTicketBuilder<T> {
    public abstract void setCommon(String type, String footer);
    
    public abstract void setParam(String account, String title);
    
    public abstract void setContent(String content);// 配置中心或DB或缓存
    
    public abstract void setProduct(String product);// db or 热数据缓存
    
    public void setBankInfo(String bankInfo){} //需要校验的，校验通过才能设置
    
    public abstract T buildTicket();
}
```
具体的建造者个人的建造者以及公司发票的建造者
```java
public class PsnTicketBuilder extends AbstractTicketBuilder<PsnTicket> {
    private PsnTicket psnTicket = new PsnTicket();
    @Override
    public void setCommon(String type, String footer) {
        psnTicket.setType(type);
        psnTicket.setFooter(footer);
    }

    @Override
    public void setParam(String account, String title) {
        psnTicket.setAccount(account);
        psnTicket.setTitle(title);
    }

    @Override
    public void setContent(String content) {
        psnTicket.setContent(content);
    }

    @Override
    public void setProduct(String product) {
        psnTicket.setProduct(product);
    }

    @Override
    public PsnTicket buildTicket() {
        return psnTicket;
    }
}
```
```java
public class OrgTicketBuilder extends AbstractTicketBuilder<OrgTicket> {
    private OrgTicket orgTicket = new OrgTicket();

    @Override
    public void setBankInfo(String bankInfo) {
        orgTicket.setBankInfo(bankInfo);
    }

    @Override
    public void setCommon(String type, String footer) {
        orgTicket.setType(type);
        orgTicket.setFooter(footer);
    }

    @Override
    public void setParam(String account, String title) {
        orgTicket.setAccount(account);
        orgTicket.setTitle(title);
    }

    @Override
    public void setContent(String content) {
        orgTicket.setContent(content);
    }

    @Override
    public void setProduct(String product) {
        orgTicket.setProduct(product);
    }

    @Override
    public OrgTicket buildTicket() {
        return orgTicket;
    }
}
```
编写指挥者，控制建造过程
> 此处简化就在service直接编写，若需要封装（类似pay的门面模式），也可以编写一个类将逻辑写到类中，然后service调用封装的类
```java
@Service
public class UserService {
    public Object getTicket(TicketParam ticketParam) {
        AbstractTicketBuilder builder = null;
        String bankInfo = null;
        if (null == ticketParam.getBankInfo()) {
            bankInfo = "check information from other channel interface";
            builder = new PsnTicketBuilder();
        } else {
            builder = new OrgTicketBuilder();
        }
        builder.setCommon(ticketParam.getType(), ticketParam.getFooter());
        builder.setParam(ticketParam.getAccount(), ticketParam.getTitle());
        String content = "from config center";
        String product = "from db";
        builder.setContent(content);
        builder.setProduct(product);
        builder.setBankInfo(bankInfo);
        // 详细的逻辑细节控制以及从配置中心或者db获取逻辑步骤就是简单的crud业务处理，自己明白即可
        return builder.buildTicket();
    }
}
```