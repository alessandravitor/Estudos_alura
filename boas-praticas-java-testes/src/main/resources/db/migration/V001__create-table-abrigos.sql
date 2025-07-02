create table abrigos(
    id bigserial primary key,
    nome varchar(100) not null unique,
    telefone varchar(14) not null unique,
    email varchar(100) not null unique
);