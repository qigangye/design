package com.csrcb.design.auditlog;

import com.csrcb.design.auditlog.pojo.AuditLog;

import java.util.Date;

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
