package com.csrcb.design.auditlog;

import com.csrcb.design.auditlog.pojo.AuditLog;
import org.springframework.stereotype.Component;

@Component
public class LoginLogProcessor extends AbstractAuditLogProcessor{
    @Override
    protected AuditLog buildDetails(AuditLog auditLog) {
        return auditLog;
    }
}
