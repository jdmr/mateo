begin;
alter table activos  alter column depreciacion_fecha drop not null;
alter table xactivos alter column depreciacion_fecha drop not null;
-- alter table activos  add constraint activo_codigo_idx unique (codigo);
-- create unique index activo_codigo_idx on activos (codigo);
commit;