begin;
-- alter table xlotes_entrada drop column last_updated;
-- alter table xlotes_salida  drop column last_updated;
-- alter table xlotes_entrada alter column version type integer;
-- alter table xlotes_salida  alter column version type integer;
alter table cancelaciones_almacen drop column last_updated;
commit;
