alter table public.san_additional_dic add primary key(id);
alter table public.san_additional add constraint san_additional_dic_fk foreign key (additional_dic) references san_additional_dic;
alter table public.san_additional add constraint san_fk foreign key (san) references san;

