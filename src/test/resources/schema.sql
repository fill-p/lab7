create table if not exists worker(
    id SERIAL primary key,
    name varchar(100) not null,
    surname varchar(500) not null
);

create table if not exists presents(
    worker_id int references worker(id) on delete cascade,
    date varchar(100) not null ,
    is_present boolean not null
);