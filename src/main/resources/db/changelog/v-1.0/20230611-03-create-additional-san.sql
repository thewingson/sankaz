create sequence public.sanatour_additional_seq start 1 increment 1;
create  table public.san_additional(
    id  int8 not null ,
    san  int8 not null,
    additional_dic  int8 not null,
    cost int8,
    enable boolean
    );
-- alter table public.san_additional add constraint san_additional_dic_fk foreign key (additional_dic) references san_additional_dic(id);
-- alter table public.san_additional add constraint san_fk foreign key (san) references san(id);

