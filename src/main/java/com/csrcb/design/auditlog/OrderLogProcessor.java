package com.csrcb.design.auditlog;

import com.csrcb.design.auditlog.pojo.AuditLog;
import org.springframework.stereotype.Component;

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
