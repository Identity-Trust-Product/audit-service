package com.identityos.audit_service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.Map;

public class AuditPayload {
  private final Map<String, Object> values;
  private final ObjectMapper objectMapper;

  public AuditPayload(Map<String, Object> values, ObjectMapper objectMapper) {
    this.values = values;
    this.objectMapper = objectMapper;
  }

  public String string(String key) {
    Object value = values.get(key);
    return value == null ? null : String.valueOf(value);
  }

  public String requiredString(String key) {
    String value = string(key);
    if (value == null || value.isBlank()) {
      throw new IllegalArgumentException("Missing required audit field: " + key);
    }
    return value;
  }

  public long longValue(String key) {
    Object value = values.get(key);
    if (value instanceof Number number) {
      return number.longValue();
    }
    if (value == null) {
      return 0L;
    }
    return Long.parseLong(String.valueOf(value));
  }

  public Object object(String key) {
    return values.get(key);
  }

  public String json(String key) {
    Object value = values.get(key);
    if (value == null) {
      return "{}";
    }
    if (value instanceof String text) {
      return text;
    }
    try {
      return objectMapper.writeValueAsString(value);
    } catch (JsonProcessingException e) {
      throw new IllegalArgumentException("Could not serialize audit field: " + key, e);
    }
  }

  public LocalDateTime utcTimeFromIso(String key) {
    String isoTime = requiredString(key);
    return OffsetDateTime.parse(isoTime).atZoneSameInstant(ZoneOffset.UTC).toLocalDateTime();
  }

  public LocalDateTime utcTimeFromEpoch(String key) {
    return LocalDateTime.ofInstant(Instant.ofEpochSecond(longValue(key)), ZoneOffset.UTC);
  }

  public Map<String, Object> values() {
    return values;
  }
}
