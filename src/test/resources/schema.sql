create table if not exists package_info(
    id SERIAL primary key,
    sender varchar(100) not null,
    receiver varchar(100) not null
);

create table if not exists receive_info(
    package_id int references package_info(id) on delete cascade,
    receive_date varchar(100) not null ,
    is_receive boolean not null
);