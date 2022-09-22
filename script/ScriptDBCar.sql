create table usuario (
    id_usuario numeric not null,
    login text unique not null,
    senha text not null,
    status boolean not null,
    primary key(id_usuario)
);

create table cargo (
    id_cargo numeric not null,
    nome text unique not null,
    primary key(id_cargo)
);

create table usuario_cargo (
    id_usuario numeric not null,
    id_cargo numeric not null,
    primary key(id_usuario, id_cargo),
    constraint fk_usuario_cargo_cargo foreign key (id_cargo) references cargo (id_cargo),
    constraint fk_usuario_cargo_usuario foreign key (id_usuario) references usuario (id_usuario)
);

create table funcionario (
    id_funcionario numeric not null,
    id_usuario numeric not null,
    nome           text not null,
    matricula      text unique not null,
    salario		text not null,
    comissao       text not null,
    primary key (id_funcionario),
    constraint fk_usuario_funcionario foreign key (id_usuario) references usuario (id_usuario)
);

create table cliente (
    id_cliente numeric not null,
    id_usuario numeric not null,
    nome       text not null,
    data_nascimento date not null,
    cpf        text unique not null,
    telefone   text not null,
    email      text not null,
    primary key (id_cliente),
    constraint fk_usuario_cliente foreign key (id_usuario) references usuario (id_usuario)
);

create table carro (
    id_carro               numeric not null,
    status                 numeric not null,
    modelo                 text not null,
    marca                  text not null,
    classe                 numeric not null,
    placa				  text not null,
    preco_diaria           numeric(8, 2) not null,
    primary key (id_carro)
);

create table aluguel (
    id_aluguel   numeric not null,
    id_carro     numeric not null,
    id_cliente   numeric not null,
    id_funcionario numeric not null,
    data_aluguel date not null,
    data_entrega date not null,
    valor_total  numeric(8, 2) not null,
    status_aluguel numeric not null,
    primary key (id_aluguel),
    constraint fk_carro_aluguel foreign key (id_carro) references carro (id_carro),
    constraint fk_cliente_aluguel foreign key (id_cliente) references cliente (id_cliente),
    constraint fk_funcionario_aluguel foreign key (id_funcionario) references funcionario (id_funcionario)
);

insert into cargo (id_cargo, nome) values (nextval('seq_cargo'), 'ROLE_ADMIN');
insert into cargo (id_cargo, nome) values (nextval('seq_cargo'), 'ROLE_FUNCIONARIO');
insert into cargo (id_cargo, nome) values (nextval('seq_cargo'), 'ROLE_CLIENTE');

create sequence seq_funcionario
increment 1
start 1;

create sequence seq_cliente
increment 1
start 1;

create sequence seq_carro
increment 1
start 1;

create sequence seq_aluguel
increment 1
start 1;

create sequence seq_usuario
increment 1
start 1;

create sequence seq_cargo
    increment 1
start 1;

-- INSERT'S DA LISTA DE CARRO;
---------------------------------------------------------------------------------------------------
insert into carro (id_carro, STATUS, MODELO, MARCA, CLASSE, PLACA, PRECO_DIARIA) values (nextval('SEQ_CARRO'), 0, 'P�lio', 'Fiat', 2, 'GVA-4022', 200);

insert into carro (id_carro, STATUS, MODELO, MARCA, CLASSE, PLACA, PRECO_DIARIA) values (nextval('SEQ_CARRO'), 0, 'Gol', 'Volkswagen', 2, 'HTL-8839', 300);

insert into carro (id_carro, STATUS, MODELO, MARCA, CLASSE, PLACA, PRECO_DIARIA) values (nextval('SEQ_CARRO'), 0, 'Onix', 'Chevrolet', 2, 'IAI-1343', 400);

insert into carro (id_carro, STATUS, MODELO, MARCA, CLASSE, PLACA, PRECO_DIARIA) values (nextval('SEQ_CARRO'), 0, 'Compass', 'Jeep', 1, 'NEZ-5353', 500);

insert into carro (id_carro, STATUS, MODELO, MARCA, CLASSE, PLACA, PRECO_DIARIA) values (nextval('SEQ_CARRO'), 0, 'Civic', 'Honda', 1, 'KME-7658', 500);

insert into carro (id_carro, STATUS, MODELO, MARCA, CLASSE, PLACA, PRECO_DIARIA) values (nextval('SEQ_CARRO'), 0, 'T-Cross', 'Volkswagen', 1, 'KAE-8632', 400);

insert into carro (id_carro, STATUS, MODELO, MARCA, CLASSE, PLACA, PRECO_DIARIA) values (nextval('SEQ_CARRO'), 0, 'Elegance', 'Mercedes-Benz', 0, 'NEX-1053', 500);

insert into carro (id_carro, STATUS, MODELO, MARCA, CLASSE, PLACA, PRECO_DIARIA) values (nextval('SEQ_CARRO'), 0, '911 GTS', 'Porsche', 0, 'JFP-6373', 1000);

insert into carro (id_carro, STATUS, MODELO, MARCA, CLASSE, PLACA, PRECO_DIARIA) values (nextval('SEQ_CARRO'), 0, 'Shelby GT500', 'Mustang', 0, 'MPC-3309', 900);
