create sequence public.room_additional_seq start 1 increment 1;
create  table public.room_additional(
    id  int8 not null ,
    desc_kz varchar(255),
    desc_ru varchar(255));

insert into public.room_additional (id,desc_kz,desc_ru) values (nextval('public.room_additional_seq'),'Кондиционер','Кондиционер');
insert into public.room_additional (id,desc_kz,desc_ru) values (nextval('public.room_additional_seq'),'Тоңазытқыш','Холодильник');
insert into public.room_additional (id,desc_kz,desc_ru) values (nextval('public.room_additional_seq'),'Теледидар','Телевизор');
