drop table if exists authorities;
drop table if exists users;

create table users (
    id bigint not null auto_increment
    , username varchar(32) unique not null
    , password varchar(254) not null
    , enabled boolean not null default true
    , primary key (id)
);

create table authorities (
    id bigint not null auto_increment
    , username varchar(32) not null
    , authority varchar(32) not null
    , primary key (id)
    , unique key authorities_key_01 (username, authority)
    , index authorities_idx_01 (username)
    , constraint authorities_fk_01 foreign key(username) references users(username)
);

insert into users(username, password) values('admin',md5('admin'));

insert into authorities(username, authority) values('admin','ROLE_ADMIN');

insert into users(username, password, enabled) values('user',md5('user'),true);

insert into authorities(username, authority) values('user','ROLE_USER');
