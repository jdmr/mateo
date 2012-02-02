insert into organizaciones(id, version, codigo, nombre, nombre_completo) values(1, 0,'UM','UM','Universidad de Montemorelos A.C.');

insert into empresas(id, version, codigo, nombre, nombre_completo, organizacion_id) values(1, 0,'PF','PF','PLANTA FÍSICA', 1);

insert into almacenes(id, version, nombre, empresa_id) values(1, 0, 'CENTRAL', 1);

insert into roles(id, version, authority) values(1, 0, 'ROLE_ADMIN');
insert into roles(id, version, authority) values(2, 0, 'ROLE_ORG');
insert into roles(id, version, authority) values(3, 0, 'ROLE_EMP');
insert into roles(id, version, authority) values(4, 0, 'ROLE_USER');

insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(1, 0, 'jdmendoza@um.edu.mx', true, false, false, false, 'ADMIN','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(2, 0, 'jdmendozar@gmail.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(3, 0, 'osoto@um.edu.mx', true, false, false, false, 'Omar','Soto', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(4, 0, 'awgarcia@um.edu.mx', true, false, false, false, 'Alejandro','Garcia', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(5, 0, 'lafuente@um.edu.mx', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(6, 0, 'test04@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(7, 0, 'test05@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(8, 0, 'test06@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(9, 0, 'test07@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(10, 0, 'test08@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(11, 0, 'test09@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(12, 0, 'test10@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(13, 0, 'test11@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(14, 0, 'test12@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(15, 0, 'test13@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(16, 0, 'test14@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(17, 0, 'test15@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(18, 0, 'test16@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(19, 0, 'test17@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(20, 0, 'test18@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(21, 0, 'test19@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);
insert into usuarios(id, version, username, enabled, account_expired, account_locked, credentials_expired, nombre, apellido, empresa_id, almacen_id) values(22, 0, 'test20@test.com', true, false, false, false, 'NORMAL','USER', 1, 1);

insert into usuarios_roles(usuario_id, rol_id) values(1,1);
insert into usuarios_roles(usuario_id, rol_id) values(2,4);
insert into usuarios_roles(usuario_id, rol_id) values(3,1);
insert into usuarios_roles(usuario_id, rol_id) values(4,1);
insert into usuarios_roles(usuario_id, rol_id) values(5,1);
insert into usuarios_roles(usuario_id, rol_id) values(6,4);
insert into usuarios_roles(usuario_id, rol_id) values(7,4);
insert into usuarios_roles(usuario_id, rol_id) values(8,4);
insert into usuarios_roles(usuario_id, rol_id) values(9,4);
insert into usuarios_roles(usuario_id, rol_id) values(10,4);
insert into usuarios_roles(usuario_id, rol_id) values(11,4);
insert into usuarios_roles(usuario_id, rol_id) values(12,4);
insert into usuarios_roles(usuario_id, rol_id) values(13,4);
insert into usuarios_roles(usuario_id, rol_id) values(14,4);
insert into usuarios_roles(usuario_id, rol_id) values(15,4);
insert into usuarios_roles(usuario_id, rol_id) values(16,4);
insert into usuarios_roles(usuario_id, rol_id) values(17,4);
insert into usuarios_roles(usuario_id, rol_id) values(18,4);
insert into usuarios_roles(usuario_id, rol_id) values(19,4);
insert into usuarios_roles(usuario_id, rol_id) values(20,4);
insert into usuarios_roles(usuario_id, rol_id) values(21,4);
insert into usuarios_roles(usuario_id, rol_id) values(22,4);

alter sequence organizaciones_id_seq restart with 2;
alter sequence empresas_id_seq restart with 2;
alter sequence almacenes_id_seq restart with 2;
alter sequence roles_id_seq restart with 5;
alter sequence usuarios_id_seq restart with 23;