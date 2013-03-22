begin;
alter table activos add column last_updated timestamp without time zone;
ALTER TABLE activos ALTER COLUMN version TYPE integer;
ALTER TABLE activos ALTER COLUMN fecha_inactivo TYPE date;
ALTER TABLE activos ALTER COLUMN fecha_compra TYPE date;
ALTER TABLE activos_imagenes RENAME COLUMN activo_imagenes_id TO activo_id; 
ALTER TABLE almacenes ALTER COLUMN version TYPE integer;
CREATE TABLE reportes
(
  id bigserial NOT NULL,
  compilado bytea NOT NULL,
  date_created timestamp without time zone NOT NULL,
  last_updated timestamp without time zone NOT NULL,
  fuente bytea NOT NULL,
  nombre character varying(64) NOT NULL,
  version integer,
  CONSTRAINT reportes_pkey PRIMARY KEY (id )
);
-- CREATE INDEX reporte_nombre_idx
--   ON reportes
--   USING btree
--   (nombre COLLATE pg_catalog."default" );
CREATE TABLE almacenes_reportes
(
  almacen_id bigint NOT NULL,
  reporte_id bigint NOT NULL,
  CONSTRAINT almacenes_fk FOREIGN KEY (almacen_id)
      REFERENCES almacenes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT reportes_fk FOREIGN KEY (reporte_id)
      REFERENCES reportes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE bajas_activo RENAME COLUMN fecha_baja TO fecha; 
ALTER TABLE bajas_activo ALTER COLUMN fecha TYPE date;
ALTER TABLE bajas_activo ALTER COLUMN version TYPE integer;
ALTER TABLE cancelaciones_almacen ALTER COLUMN version TYPE integer;
ALTER TABLE cancelaciones_almacen_entradas RENAME COLUMN cancelacion_almacen_entradas_id TO cancelacion_id; 
ALTER TABLE cancelaciones_almacen_salidas RENAME COLUMN cancelacion_almacen_salidas_id TO cancelacion_id; 
CREATE TABLE cancelaciones_almacen_productos
(
  cancelacion_id bigint NOT NULL,
  producto_id bigint NOT NULL,
  CONSTRAINT cancelaciones_almacen_productos_pkey PRIMARY KEY (cancelacion_id , producto_id ),
  CONSTRAINT cancelaciones_almacen_fk FOREIGN KEY (cancelacion_id)
      REFERENCES cancelaciones_almacen (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT productos_fk FOREIGN KEY (producto_id)
      REFERENCES productos (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE clientes ALTER COLUMN version TYPE integer;
DROP TABLE IF EXISTS auxiliares CASCADE;
CREATE TABLE auxiliares
(
  id bigserial NOT NULL,
  auxiliar boolean NOT NULL,
  aviso boolean NOT NULL,
  clave character varying(50) NOT NULL,
  detalle boolean NOT NULL,
  iva boolean NOT NULL,
  nombre character varying(24) NOT NULL,
  nombre_fiscal character varying(24) NOT NULL,
  porcentaje_iva numeric(19,2) NOT NULL,
  version integer,
  CONSTRAINT auxiliares_pkey PRIMARY KEY (id )
);
CREATE TABLE mayores
(
  id bigserial NOT NULL,
  auxiliar boolean NOT NULL,
  aviso boolean NOT NULL,
  clave character varying(50) NOT NULL,
  detalle boolean NOT NULL,
  iva boolean NOT NULL,
  nombre character varying(24) NOT NULL,
  nombre_fiscal character varying(24) NOT NULL,
  porcentaje_iva numeric(19,2) NOT NULL,
  version integer,
  organizacion_id bigint,
  CONSTRAINT mayores_pkey PRIMARY KEY (id ),
  CONSTRAINT fkffc9c2c013b00844 FOREIGN KEY (organizacion_id)
      REFERENCES organizaciones (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
CREATE TABLE resultados
(
  id bigserial NOT NULL,
  nombre character varying(24) NOT NULL,
  nombre_fiscal character varying(24) NOT NULL,
  version integer,
  CONSTRAINT resultados_pkey PRIMARY KEY (id )
);
ALTER TABLE cuentas ALTER COLUMN version TYPE integer;
CREATE TABLE departamentos
(
  id bigserial NOT NULL,
  nombre character varying(64) NOT NULL,
  version integer,
  cuenta_id bigint NOT NULL,
  empresa_id bigint NOT NULL,
  CONSTRAINT departamentos_pkey PRIMARY KEY (id ),
  CONSTRAINT fkcba854ef7db392f2 FOREIGN KEY (cuenta_id)
      REFERENCES mayores (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fkcba854efae7f89d0 FOREIGN KEY (empresa_id)
      REFERENCES empresas (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT departamentos_empresa_id_nombre_key UNIQUE (empresa_id , nombre )
);
CREATE TABLE empleados
(
  id bigserial NOT NULL,
  apmaterno character varying(24) NOT NULL,
  appaterno character varying(24) NOT NULL,
  clave character varying(10) NOT NULL,
  direccion character varying(24) NOT NULL,
  genero character varying(1) NOT NULL,
  nombre character varying(24) NOT NULL,
  status character varying(2) NOT NULL,
  version integer,
  CONSTRAINT empleados_pkey PRIMARY KEY (id )
);
ALTER TABLE empresas ALTER COLUMN version TYPE integer;
alter table empresas add column cuenta_id bigint;
alter table empresas add CONSTRAINT fk4772d10a7db392f2 FOREIGN KEY (cuenta_id)
      REFERENCES mayores (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;
CREATE TABLE empresas_reportes
(
  empresas_id bigint NOT NULL,
  reportes_id bigint NOT NULL,
  CONSTRAINT fk64f7d0772d29daf FOREIGN KEY (empresas_id)
      REFERENCES empresas (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk64f7d077c27ebff FOREIGN KEY (reportes_id)
      REFERENCES reportes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE entradas ALTER COLUMN fecha_factura TYPE date;
ALTER TABLE entradas ALTER COLUMN version TYPE integer;
ALTER TABLE estatus ALTER COLUMN prioridad TYPE integer;
ALTER TABLE estatus ALTER COLUMN version TYPE integer;
ALTER TABLE facturas_almacen ALTER COLUMN version TYPE integer;
ALTER TABLE facturas_almacen ALTER COLUMN fecha TYPE date;
ALTER TABLE facturas_almacen_entradas RENAME COLUMN factura_almacen_entradas_id TO factura_id; 
ALTER TABLE facturas_almacen_salidas RENAME COLUMN factura_almacen_salidas_id TO factura_id; 
ALTER TABLE folio_inventario ALTER COLUMN version TYPE integer;
ALTER TABLE imagenes ALTER COLUMN version TYPE integer;
ALTER TABLE lotes_entrada ALTER COLUMN version TYPE integer;
ALTER TABLE lotes_salida ALTER COLUMN version TYPE integer;
ALTER TABLE organizaciones ALTER COLUMN version TYPE integer;
CREATE TABLE organizaciones_reportes
(
  organizaciones_id bigint NOT NULL,
  reportes_id bigint NOT NULL,
  CONSTRAINT fk555043ef3cf15dd6 FOREIGN KEY (organizaciones_id)
      REFERENCES organizaciones (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk555043efc27ebff FOREIGN KEY (reportes_id)
      REFERENCES reportes (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE productos ALTER COLUMN version TYPE integer;
ALTER TABLE productos_imagenes RENAME COLUMN producto_imagenes_id TO producto_id; 
ALTER TABLE proveedores ALTER COLUMN version TYPE integer;
ALTER TABLE reubicaciones_activo ALTER COLUMN version TYPE integer;
alter table reubicaciones_activo add column fecha date;
update reubicaciones_activo set fecha = date_created;
ALTER TABLE reubicaciones_activo ALTER COLUMN fecha SET NOT NULL;
ALTER TABLE reubicaciones_activo DROP CONSTRAINT fk3cbbbd756ea08634;
ALTER TABLE reubicaciones_activo DROP CONSTRAINT fk3cbbbd7575a99055;
ALTER TABLE reubicaciones_activo DROP COLUMN centro_costo_id;
ALTER TABLE reubicaciones_activo ADD COLUMN departamento_id bigint NOT NULL;
ALTER TABLE reubicaciones_activo ADD CONSTRAINT reubicaciones_activo_departamentos_fk FOREIGN KEY (departamento_id)
        REFERENCES departamentos(id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE reubicaciones_activo ADD COLUMN empresa_id bigint NOT NULL;
ALTER TABLE reubicaciones_activo ADD CONSTRAINT reubicaciones_activo_empresas_fk FOREIGN KEY (empresa_id)
        REFERENCES empresas(id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION;
ALTER TABLE roles ALTER COLUMN version TYPE integer;
ALTER TABLE salidas ALTER COLUMN version TYPE integer;
ALTER TABLE tipos_activo ALTER COLUMN version TYPE integer;
ALTER TABLE tipos_cliente ALTER COLUMN version TYPE integer;
ALTER TABLE tipos_producto ALTER COLUMN version TYPE integer;
ALTER TABLE usuarios ALTER COLUMN version TYPE integer;
ALTER TABLE usuarios RENAME COLUMN password_expired TO credentials_expired; 
ALTER TABLE usuarios ADD COLUMN open_id character varying(255);
ALTER TABLE usuarios ADD COLUMN empresa_id bigint;
ALTER TABLE usuarios ADD CONSTRAINT usuarios_empresas_fk FOREIGN KEY (empresa_id)
        REFERENCES empresas(id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION;
UPDATE usuarios SET empresa_id = empresas.id FROM almacenes, empresas WHERE almacen_id = almacenes.id and almacenes.empresa_id = empresas.id;
ALTER TABLE usuarios ALTER COLUMN empresa_id SET NOT NULL;
ALTER TABLE usuario_rol RENAME TO usuarios_roles;
ALTER TABLE xactivos ALTER COLUMN fecha_inactivo TYPE date;
ALTER TABLE xactivos ALTER COLUMN version TYPE integer;
ALTER TABLE xentradas ALTER COLUMN version TYPE integer;
ALTER TABLE xentradas ALTER COLUMN fecha_factura TYPE date;
ALTER TABLE xproductos ALTER COLUMN version TYPE integer;
ALTER TABLE xproductos ADD COLUMN cancelacion_id bigint;
ALTER TABLE xsalidas ALTER COLUMN version TYPE integer;
ALTER TABLE xtipos_activo ALTER COLUMN version TYPE integer;
ALTER TABLE organizaciones DROP COLUMN base;
ALTER TABLE organizaciones DROP COLUMN rfc;
ALTER TABLE usuarios DROP COLUMN base;
ALTER TABLE tipos_cliente DROP COLUMN base;
ALTER TABLE tipos_producto DROP COLUMN base;
ALTER TABLE usuarios_roles DROP CONSTRAINT fk3118953eddd84489;
alter table entradas drop column facturada;
alter table entradas drop column pendiente;
alter table xentradas drop column version;
alter table xentradas drop column facturada;
alter table xentradas drop column pendiente;
alter table xentradas drop column last_updated;
alter table lotes_entrada drop column last_updated;
alter table xproductos drop column version;
alter table xproductos drop column last_updated;
alter table salidas drop column cerrada;
alter table salidas drop column facturada;
alter table xsalidas drop column version;
alter table xsalidas drop column cerrada;
alter table xsalidas drop column facturada;
alter table xsalidas drop column last_updated;
alter table lotes_salida drop column last_updated;
drop table eventosasociacion cascade;
drop table eventosclub cascade;
drop table samuel cascade;
drop table asociaciones cascade;
drop table asistentes cascade;
drop table clubes cascade;
drop table uniones_usuarios cascade;
drop table uniones cascade;
drop table familia_activo cascade;
drop table usuarios_usuarios cascade;
drop table rol cascade;
drop table usuario cascade;
update estatus set nombre = 'ABIERTA', prioridad = 100 where nombre='estatus.abierta';
update estatus set nombre = 'PENDIENTE', prioridad = 200 where nombre='estatus.pendiente';
update estatus set nombre = 'CERRADA', prioridad = 300 where nombre='estatus.cerrada';
update estatus set nombre = 'FACTURADA', prioridad = 400 where nombre='estatus.facturada';
update estatus set nombre = 'CANCELADA', prioridad = 500 where nombre='estatus.cancelada';
ALTER TABLE auxiliares ADD COLUMN organizacion_id bigint;
ALTER TABLE auxiliares ADD CONSTRAINT auxiliares_organizaciones_fk FOREIGN KEY (organizacion_id)
        REFERENCES organizaciones(id) MATCH SIMPLE
        ON UPDATE NO ACTION ON DELETE NO ACTION;
drop table if exists resultados;
CREATE TABLE resultados
(
  id bigserial NOT NULL,
  auxiliar boolean NOT NULL,
  aviso boolean NOT NULL,
  clave character varying(50) NOT NULL,
  detalle boolean NOT NULL,
  iva boolean NOT NULL,
  nombre character varying(24) NOT NULL,
  nombre_fiscal character varying(24) NOT NULL,
  porcentaje_iva numeric(19,2) NOT NULL,
  version integer,
  organizacion_id bigint,
  CONSTRAINT resultados_pkey PRIMARY KEY (id ),
  CONSTRAINT resultados_organizaciones_fk FOREIGN KEY (organizacion_id)
      REFERENCES organizaciones (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION
);
ALTER TABLE xfacturas_almacen ALTER COLUMN fecha TYPE date;
ALTER TABLE xfacturas_almacen ADD COLUMN date_created timestamp;
ALTER TABLE xfacturas_almacen ALTER COLUMN version TYPE integer;
ALTER TABLE folio_activos ALTER COLUMN version TYPE integer;

update usuarios set correo = 'bugs@um.edu.mx' where correo = 'jdmendoza@um.edu.mx';
update usuarios set correo = 'jdmendoza@um.edu.mx' where correo = 'david.mendoza@um.edu.mx';

commit;