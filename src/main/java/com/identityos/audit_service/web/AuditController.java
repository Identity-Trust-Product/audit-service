package com.identityos.audit_service.web;

import com.identityos.audit_service.config.AuditProperties;
import com.identityos.audit_service.service.AuditRecordWriter;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/audits")
public class AuditController {
  private final AuditRecordWriter auditRecordWriter;
  private final AuditProperties properties;

  public AuditController(AuditRecordWriter auditRecordWriter, AuditProperties properties) {
    this.auditRecordWriter = auditRecordWriter;
    this.properties = properties;
  }

  @PostMapping
  public ResponseEntity<Map<String, String>> ingest(@RequestBody Map<String, Object> message) {
    auditRecordWriter.write(message);
    return ResponseEntity.accepted().body(Map.of("status", "stored"));
  }

  @GetMapping("/{table}")
  public ResponseEntity<?> recent(
      @PathVariable String table, @RequestParam(defaultValue = "100") int limit) {
    return ResponseEntity.ok(
        auditRecordWriter.findRecent(tableName(table), Math.max(1, Math.min(limit, 500))));
  }

  private String tableName(String alias) {
    return switch (alias) {
      case "records", "onboarding", "identity", "cms" -> properties.getTables().getRecords();
      default -> alias;
    };
  }
}
