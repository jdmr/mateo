drop table if exists authorities cascade;
drop sequence if exists authorities_seq;
drop table if exists users cascade;
drop sequence if exists users_seq;

create sequence users_seq;
create table users (
    id bigint not null default nextval('users_seq')
    , username varchar(32) unique not null
    , password varchar(254) not null
    , enabled boolean not null default true
    , primary key (id)
);

create sequence authorities_seq;
create table authorities (
    id bigint not null default nextval('authorities_seq')
    , username varchar(32) not null
    , authority varchar(32) not null
    , primary key (id)
    , unique (username, authority)
    , foreign key(username) references users(username)
);

insert into users(username, password) values('david',md5('david'));

insert into authorities(username, authority) values('david','ROLE_ADMIN');

insert into users(username, password, enabled) values('daniel',md5('daniel'),true);

insert into authorities(username, authority) values('daniel','ROLE_USER');
