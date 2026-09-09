package com.identityos.audit_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "audit")
public class AuditProperties {
  private String exchangeName = "auditing";
  private String routingKey = "#";
  private String auditQueue = "auditing-messages";
  private Tables tables = new Tables();

  public String getExchangeName() {
    return exchangeName;
  }

  public void setExchangeName(String exchangeName) {
    this.exchangeName = exchangeName;
  }

  public String getRoutingKey() {
    return routingKey;
  }

  public void setRoutingKey(String routingKey) {
    this.routingKey = routingKey;
  }

  public String getAuditQueue() {
    return auditQueue;
  }

  public void setAuditQueue(String auditQueue) {
    this.auditQueue = auditQueue;
  }

  public Tables getTables() {
    return tables;
  }

  public void setTables(Tables tables) {
    this.tables = tables;
  }

  public static class Tables {
    private String records = "audit_records";

    public String getRecords() {
      return records;
    }

    public void setRecords(String records) {
      this.records = records;
    }
  }
}
