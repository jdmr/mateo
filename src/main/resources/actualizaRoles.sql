begin;
update roles set prioridad = 1 where id = 1;
update roles set prioridad = 2 where id = 2;
update roles set prioridad = 3 where id = 3;
update roles set prioridad = 5 where id = 4;
insert into roles(authority, version, prioridad) values('ROLE_JEFE', 0, 4);
commit;
