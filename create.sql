create table activation
(
    id              bigint not null,
    bank_account_id bigint,
    legal_entity_id bigint,
    primary key (id)
) engine=InnoDB;
create table activation_seq
(
    next_val bigint
) engine=InnoDB;
insert into activation_seq
values (1);
create table bank_account_seq
(
    next_val bigint
) engine=InnoDB;
insert into bank_account_seq
values (1);
create table bank_account
(
    id              bigint not null,
    legal_entity_id bigint,
    primary key (id)
) engine=InnoDB;
create table legal_entity_seq
(
    next_val bigint
) engine=InnoDB;
insert into legal_entity_seq
values (1);
create table legal_entity
(
    id bigint not null,
    primary key (id)
) engine=InnoDB;
create table ownership
(
    id              bigint not null,
    legal_entity_id bigint,
    primary key (id)
) engine=InnoDB;
create table ownership_seq
(
    next_val bigint
) engine=InnoDB;
insert into ownership_seq
values (1);
alter table activation
    add constraint FK2rs1yvttkre6mbb00flsupcvc foreign key (bank_account_id) references bank_account (id);
alter table activation
    add constraint FKcwdndq8ukvr6n61xe0e48w10j foreign key (legal_entity_id) references legal_entity (id);
alter table bank_account
    add constraint FK8nsfsctq31ypojvu2mpj0ixub foreign key (legal_entity_id) references legal_entity (id);
alter table ownership
    add constraint FKkfw2fer55x2jveeot1qvcilog foreign key (legal_entity_id) references legal_entity (id);
