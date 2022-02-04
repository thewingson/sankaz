create sequence public.category_seq start 1 increment 50
>>

create table public.category
(
    id          int8         not null,
    create_ts   timestamp    not null,
    created_by  varchar(255) not null,
    delete_ts   timestamp,
    deleted_by  varchar(255),
    update_ts   timestamp,
    updated_by  varchar(255),
    code        varchar(255) not null,
    description varchar(255),
    name        varchar(255) not null,
    primary key (id)
)
>>

alter table public.category
    add constraint UK_CATEGORY_CODE unique (code)
>>