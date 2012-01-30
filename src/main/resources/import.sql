insert into organizaciones(id, version, codigo, nombre, nombre_completo) values(1, 0,'UM','UM','Universidad de Montemorelos A.C.');

insert into empresas(id, version, codigo, nombre, nombre_completo, organizacion_id) values(1, 0,'PF','PF','PLANTA FÍSICA', 1);

insert into almacenes(id, version, nombre, empresa_id) values(1, 0, 'CENTRAL', 1);

insert into roles(id, version, authority) values(1, 0, 'ROLE_ADMIN');
insert into roles(id, version, authority) values(2, 0, 'ROLE_ORG');
insert into roles(id, version, authority) values(3, 0, 'ROLE_EMP');
insert into roles(id, version, authority) values(4, 0, 'ROLE_USER');

insert into usuarios(id, version, username, password, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, correo, empresa_id, almacen_id) values(1, 0, 'admin', 'a40546cc4fd6a12572828bb803380888ad1bfdab', true, false, false, false, 'ADMIN','USER','david.mendoza@um.edu.mx', 1, 1);
insert into usuarios(id, version, username, password, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, correo, empresa_id, almacen_id) values(2, 0, 'user', 'b6b1f4781776979c0775c71ebdd8bdc084aac5fe', true, false, false, false, 'NORMAL','USER','portal@um.edu.mx', 1, 1);

insert into usuarios_roles(usuario_id, rol_id) values(1,1);
insert into usuarios_roles(usuario_id, rol_id) values(2,4);

alter sequence organizaciones_id_seq restart with 2;
alter sequence empresas_id_seq restart with 2;
alter sequence almacenes_id_seq restart with 2;
alter sequence roles_id_seq restart with 5;
alter sequence usuarios_id_seq restart with 3;