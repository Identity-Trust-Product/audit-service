create table if not exists audit_records (
  id varchar(128) primary key,
  origin varchar(128) not null,
  service_name varchar(128) not null,
  actor_user_id varchar(256),
  actor_role varchar(128),
  action varchar(256) not null,
  http_method varchar(32),
  endpoint varchar(512),
  entity_type varchar(128),
  entity_id varchar(256),
  organization_id varchar(128),
  application_id varchar(128),
  schema_id varchar(128),
  schema_version_id varchar(128),
  status varchar(64),
  decision varchar(128),
  request_body jsonb not null default '{}'::jsonb,
  response_body jsonb not null default '{}'::jsonb,
  metadata jsonb not null default '{}'::jsonb,
  ip_address varchar(128),
  user_agent text,
  epoch_time bigint not null,
  occurred_at timestamp not null
);

create index if not exists idx_audit_records_actor_time
  on audit_records(actor_user_id, occurred_at desc);

create index if not exists idx_audit_records_org_app_time
  on audit_records(organization_id, application_id, occurred_at desc);

create index if not exists idx_audit_records_action_time
  on audit_records(action, occurred_at desc);

create table if not exists rsaudit (
  id varchar(128) primary key,
  api varchar(512) not null,
  userid varchar(128) not null,
  epochtime bigint not null,
  resourceid varchar(256) not null,
  isotime varchar(128) not null,
  providerid varchar(128) not null,
  size bigint,
  time timestamp,
  resource_group varchar(128),
  item_type varchar(128),
  delegator_id varchar(128)
);

create index if not exists idx_rsaudit_user_time_provider on rsaudit(userid, epochtime, providerid);

create table if not exists auditingtable (
  id varchar(128) primary key,
  userrole varchar(64) not null,
  userid varchar(128) not null,
  iid varchar(250) not null,
  api varchar(512) not null,
  method varchar(32) not null,
  time bigint not null,
  iudxid varchar(256) not null
);

create index if not exists idx_auditingtable_user_iudx_time on auditingtable(userid, iudxid, time);

create table if not exists auditing_auth (
  id varchar(128) primary key,
  body jsonb not null default '{}'::jsonb,
  endpoint varchar(512) not null,
  method varchar(32) not null,
  time bigint not null,
  userid varchar(128) not null
);

create table if not exists auditing_acl_apd (
  id varchar(128) primary key,
  userid varchar(128) not null,
  endpoint varchar(512) not null,
  method varchar(32) not null,
  body jsonb not null default '{}'::jsonb,
  size bigint not null,
  time timestamp not null
);

create index if not exists idx_auditing_acl_apd_user_endpoint_time on auditing_acl_apd(userid, endpoint, time);

create table if not exists auditing_dmp (
  _id varchar(128) primary key,
  user_id varchar(128) not null,
  api varchar(512) not null,
  method varchar(32) not null,
  info jsonb not null default '{}'::jsonb,
  time timestamp not null
);

create index if not exists idx_auditing_dmp_api_method_user_time on auditing_dmp(api, method, user_id, time);

create table if not exists auditing_consent (
  _id varchar(128) primary key,
  item_id varchar(256) not null,
  item_type varchar(128),
  event varchar(256) not null,
  aiu_id varchar(128) not null,
  aip_id varchar(256) not null,
  dp_id varchar(128) not null,
  artifact varchar(128) not null,
  created_at timestamp not null,
  log text
);

create index if not exists idx_auditing_consent_item_aiu_dp on auditing_consent(item_id, aiu_id, dp_id);

create table if not exists auditing_ogc (
  id varchar(128) primary key,
  userid varchar(128) not null,
  api varchar(512) not null,
  request_json jsonb not null default '{}'::jsonb,
  size bigint not null,
  resourceid varchar(256) not null,
  providerid varchar(128) not null,
  resource_group varchar(128),
  epochtime bigint not null,
  time timestamp not null,
  isotime varchar(128) not null,
  delegator_id varchar(128)
);

create index if not exists idx_auditing_ogc_user_provider_resource_time
  on auditing_ogc(userid, providerid, resourceid, epochtime);

create table if not exists subscriptions (
  subscription_id varchar(128) primary key,
  user_id varchar(128) not null,
  event_type varchar(128) not null,
  subscription_type varchar(128),
  resource_id varchar(256) not null,
  queue_name varchar(128),
  entity varchar(256),
  resource_group varchar(128),
  delegator_id varchar(128),
  item_type varchar(128),
  provider_id varchar(128),
  expiry timestamp
);

create index if not exists idx_subscriptions_entity_expiry on subscriptions(entity, expiry);
