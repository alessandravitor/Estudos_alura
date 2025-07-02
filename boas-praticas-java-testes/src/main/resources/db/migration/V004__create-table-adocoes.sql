create table adocoes(
    id bigserial primary key,
    data timestamp not null,
    tutor_id bigint not null,
    pet_id bigint not null,
    motivo varchar(255) not null,
    status varchar(100) not null,
    justificativa_status varchar(255),
    constraint fk_adocoes_tutor_id foreign key(tutor_id) references tutores(id),
    constraint fk_adocoes_pet_id foreign key(pet_id) references pets(id)
);