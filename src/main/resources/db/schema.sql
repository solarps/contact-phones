drop table if exists users cascade;
drop table if exists contacts cascade;
drop table if exists emails cascade;
drop table if exists phones cascade;


create table if not exists users
(
    id       serial primary key,
    username varchar(255) not null unique,
    password varchar(255) not null,
    role     varchar(10) check ( role in ('USER', 'ADMIN') )
);

create table if not exists contacts
(
    id      serial primary key,
    user_id integer      not null,
    name    varchar(255) not null,
    foreign key (user_id) references users (id),
    unique (user_id, name)
);

create table if not exists emails
(
    id         serial primary key,
    email      varchar(255) not null,
    contact_id integer      not null,
    foreign key (contact_id) references contacts (id)
);

create table if not exists phones
(
    id           serial primary key,
    phone_number varchar(15) not null,
    contact_id   integer     not null,
    foreign key (contact_id) references contacts (id)
);