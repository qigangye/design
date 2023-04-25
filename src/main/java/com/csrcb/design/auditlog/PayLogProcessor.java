package com.csrcb.design.auditlog;

import com.csrcb.design.auditlog.pojo.AuditLog;
import org.springframework.stereotype.Component;

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
