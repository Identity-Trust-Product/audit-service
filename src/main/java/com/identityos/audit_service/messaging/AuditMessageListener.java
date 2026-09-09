package com.identityos.audit_service.messaging;

import com.identityos.audit_service.service.AuditRecordWriter;
import java.util.Map;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class AuditMessageListener {
  private final AuditRecordWriter auditRecordWriter;

  public AuditMessageListener(AuditRecordWriter auditRecordWriter) {
    this.auditRecordWriter = auditRecordWriter;
  }

  @RabbitListener(queues = "${audit.audit-queue:auditing-messages}")
  public void consume(Map<String, Object> message) {
    auditRecordWriter.write(message);
  }
}
