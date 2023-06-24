create sequence public.sanatour_additional_dic_seq start 1 increment 1;

create  table public.san_additional_dic(
    id  int8 not null ,
    name_kz  varchar(255),
    name_ru  varchar(255),
    enable boolean
    );

