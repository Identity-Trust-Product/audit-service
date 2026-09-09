create table if not exists audit_records (
  id varchar(128) primary key,
  origin varchar(128) not null,
  service_name varchar(128) not null,
  actor_user_id varchar(128),
  actor_role varchar(64),
  action varchar(128) not null,
  http_method varchar(16),
  endpoint varchar(512),
  entity_type varchar(128),
  entity_id varchar(256),
  organization_id varchar(128),
  application_id varchar(128),
  schema_id varchar(128),
  schema_version_id varchar(128),
  status varchar(64),
  decision varchar(64),
  request_body jsonb not null default '{}'::jsonb,
  response_body jsonb not null default '{}'::jsonb,
  metadata jsonb not null default '{}'::jsonb,
  ip_address varchar(64),
  user_agent varchar(512),
  epoch_time bigint not null,
  occurred_at timestamp not null,
  created_at timestamp not null default current_timestamp
);

create index if not exists idx_audit_records_service_time
  on audit_records(service_name, occurred_at desc);

create index if not exists idx_audit_records_actor_time
  on audit_records(actor_user_id, occurred_at desc);

create index if not exists idx_audit_records_entity_time
  on audit_records(entity_type, entity_id, occurred_at desc);

create index if not exists idx_audit_records_org_app_schema
  on audit_records(organization_id, application_id, schema_version_id);
