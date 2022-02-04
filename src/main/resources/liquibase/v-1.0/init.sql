create sequence public.hyper_link_seq start 1 increment 50
create sequence public.hyper_link_type_seq start 1 increment 50
create sequence public.item_pic_seq start 1 increment 50
create sequence public.notification_template_seq start 1 increment 50
create sequence public.review_seq start 1 increment 50
create sequence public.room_seq start 1 increment 50
create sequence public.san_seq start 1 increment 50
create sequence public.sec_role_seq start 1 increment 50
create sequence public.sec_user_seq start 1 increment 50
create sequence public.sys_file_seq start 1 increment 50
create table public.hyper_link (id int8 not null, create_ts timestamp not null, created_by varchar(255) not null, delete_ts timestamp, deleted_by varchar(255), update_ts timestamp, updated_by varchar(255), item_id int8 not null, item_type varchar(255) not null, value varchar(255) not null, type_id int8 not null, primary key (id))
create table public.hyper_link_type (id int8 not null, create_ts timestamp not null, created_by varchar(255) not null, delete_ts timestamp, deleted_by varchar(255), update_ts timestamp, updated_by varchar(255), code varchar(255) not null, description varchar(255), name varchar(255) not null, primary key (id))
create table public.item_pic (id int8 not null, create_ts timestamp not null, created_by varchar(255) not null, delete_ts timestamp, deleted_by varchar(255), update_ts timestamp, updated_by varchar(255), item_id int8 not null, item_type varchar(255) not null, file_id int8 not null, primary key (id))
create table public.notification_template (id int8 not null, create_ts timestamp not null, created_by varchar(255) not null, delete_ts timestamp, deleted_by varchar(255), update_ts timestamp, updated_by varchar(255), code varchar(255) not null, description varchar(255), name varchar(255) not null, message_template text not null, primary key (id))
create table public.review (id int8 not null, create_ts timestamp not null, created_by varchar(255) not null, delete_ts timestamp, deleted_by varchar(255), update_ts timestamp, updated_by varchar(255), text varchar(255) not null, rating float4 not null, user_id int8 not null, parent_id int8, san_id int8 not null, primary key (id))
create table public.room (id int8 not null, create_ts timestamp not null, created_by varchar(255) not null, delete_ts timestamp, deleted_by varchar(255), update_ts timestamp, updated_by varchar(255), description varchar(255), name varchar(255) not null, price numeric(19, 2), primary key (id))
create table public.san (id int8 not null, create_ts timestamp not null, created_by varchar(255) not null, delete_ts timestamp, deleted_by varchar(255), update_ts timestamp, updated_by varchar(255), description varchar(255), name varchar(255) not null, primary key (id))
create table public.san_categories (san_id int8 not null, category_id int8 not null)
create table public.sec_role (id int8 not null, create_ts timestamp not null, created_by varchar(255) not null, delete_ts timestamp, deleted_by varchar(255), update_ts timestamp, updated_by varchar(255), name varchar(255) not null, primary key (id))
create table public.sec_user (id int8 not null, create_ts timestamp not null, created_by varchar(255) not null, delete_ts timestamp, deleted_by varchar(255), update_ts timestamp, updated_by varchar(255), active boolean not null, confirmation_id uuid not null, confirmed_by varchar(255), confirmed_ts timestamp, email varchar(255), first_name varchar(255) not null, last_name varchar(255) not null, logged_out boolean not null, password varchar(255) not null, username varchar(255) not null, primary key (id))
create table public.sec_user_roles (user_id int8 not null, role_id int8 not null)
create table public.sys_file (id int8 not null, extension varchar(255) not null, file_name varchar(255) not null, size varchar(255) not null, primary key (id))
alter table public.hyper_link_type add constraint UK_aarn38mu67l4kc3fox7xuyoqx unique (code)
alter table public.notification_template add constraint UK_78ljxu1rtyj29qj8a3bao9bfx unique (code)
alter table public.sec_role add constraint UK_328v0xhgur113t0ak61ieyp8n unique (name)
alter table public.sec_user add constraint UK_1he91pvs8rpp8lk09us0af9kk unique (confirmation_id)
alter table public.sec_user add constraint UK_numjsbs0wblvnqi7po8eyeo6y unique (email)
alter table public.sec_user add constraint UK_5ctbdrlf3eismye20vsdtk8w8 unique (username)
alter table public.hyper_link add constraint HYPER_LINK_TYPE_FK foreign key (type_id) references public.hyper_link_type
alter table public.item_pic add constraint ITEM_PIC_FILE_FK foreign key (file_id) references public.sys_file
alter table public.review add constraint MESSAGE_USER_FK foreign key (user_id) references public.sec_user
alter table public.review add constraint REVIEW_PARENT_FK foreign key (parent_id) references public.review
alter table public.review add constraint REVIEW_SAN_FK foreign key (san_id) references public.san
alter table public.san_categories add constraint FKi0nbdpnwcioyijcf9fsrjmoiv foreign key (category_id) references public.category
alter table public.san_categories add constraint FKk5s8kjotqiia6hkif69g3nph2 foreign key (san_id) references public.san
alter table public.sec_user_roles add constraint FKabm55itj3htl8yno424528wi8 foreign key (role_id) references public.sec_role
alter table public.sec_user_roles add constraint FKmu39wf2qaxcmq8e5cqtmv8ofv foreign key (user_id) references public.sec_user
select nextval (''public.sec_role_seq'')
select nextval (''public.sec_role_seq'')
insert into public.sec_role (create_ts, created_by, delete_ts, deleted_by, update_ts, updated_by, name, id) values (?, ?, ?, ?, ?, ?, ?, ?)
insert into public.sec_role (create_ts, created_by, delete_ts, deleted_by, update_ts, updated_by, name, id) values (?, ?, ?, ?, ?, ?, ?, ?)
insert into public.sec_role (create_ts, created_by, delete_ts, deleted_by, update_ts, updated_by, name, id) values (?, ?, ?, ?, ?, ?, ?, ?)
select nextval (''public.sec_user_seq'')
select nextval (''public.sec_user_seq'')
insert into public.sec_user (create_ts, created_by, delete_ts, deleted_by, update_ts, updated_by, active, confirmation_id, confirmed_by, confirmed_ts, email, first_name, last_name, logged_out, password, username, id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
insert into public.sec_user (create_ts, created_by, delete_ts, deleted_by, update_ts, updated_by, active, confirmation_id, confirmed_by, confirmed_ts, email, first_name, last_name, logged_out, password, username, id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
insert into public.sec_user (create_ts, created_by, delete_ts, deleted_by, update_ts, updated_by, active, confirmation_id, confirmed_by, confirmed_ts, email, first_name, last_name, logged_out, password, username, id) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
select secuser0_.id as id1_10_, secuser0_.create_ts as create_t2_10_, secuser0_.created_by as created_3_10_, secuser0_.delete_ts as delete_t4_10_, secuser0_.deleted_by as deleted_5_10_, secuser0_.update_ts as update_t6_10_, secuser0_.updated_by as updated_7_10_, secuser0_.active as active8_10_, secuser0_.confirmation_id as confirma9_10_, secuser0_.confirmed_by as confirm10_10_, secuser0_.confirmed_ts as confirm11_10_, secuser0_.email as email12_10_, secuser0_.first_name as first_n13_10_, secuser0_.last_name as last_na14_10_, secuser0_.logged_out as logged_15_10_, secuser0_.password as passwor16_10_, secuser0_.username as usernam17_10_ from public.sec_user secuser0_ where secuser0_.username=?
select roles0_.user_id as user_id1_11_0_, roles0_.role_id as role_id2_11_0_, secrole1_.id as id1_9_1_, secrole1_.create_ts as create_t2_9_1_, secrole1_.created_by as created_3_9_1_, secrole1_.delete_ts as delete_t4_9_1_, secrole1_.deleted_by as deleted_5_9_1_, secrole1_.update_ts as update_t6_9_1_, secrole1_.updated_by as updated_7_9_1_, secrole1_.name as name8_9_1_ from public.sec_user_roles roles0_ inner join public.sec_role secrole1_ on roles0_.role_id=secrole1_.id where roles0_.user_id=?
select secrole0_.id as id1_9_, secrole0_.create_ts as create_t2_9_, secrole0_.created_by as created_3_9_, secrole0_.delete_ts as delete_t4_9_, secrole0_.deleted_by as deleted_5_9_, secrole0_.update_ts as update_t6_9_, secrole0_.updated_by as updated_7_9_, secrole0_.name as name8_9_ from public.sec_role secrole0_ where secrole0_.name=?
insert into public.sec_user_roles (user_id, role_id) values (?, ?)
select secuser0_.id as id1_10_, secuser0_.create_ts as create_t2_10_, secuser0_.created_by as created_3_10_, secuser0_.delete_ts as delete_t4_10_, secuser0_.deleted_by as deleted_5_10_, secuser0_.update_ts as update_t6_10_, secuser0_.updated_by as updated_7_10_, secuser0_.active as active8_10_, secuser0_.confirmation_id as confirma9_10_, secuser0_.confirmed_by as confirm10_10_, secuser0_.confirmed_ts as confirm11_10_, secuser0_.email as email12_10_, secuser0_.first_name as first_n13_10_, secuser0_.last_name as last_na14_10_, secuser0_.logged_out as logged_15_10_, secuser0_.password as passwor16_10_, secuser0_.username as usernam17_10_ from public.sec_user secuser0_ where secuser0_.username=?
select roles0_.user_id as user_id1_11_0_, roles0_.role_id as role_id2_11_0_, secrole1_.id as id1_9_1_, secrole1_.create_ts as create_t2_9_1_, secrole1_.created_by as created_3_9_1_, secrole1_.delete_ts as delete_t4_9_1_, secrole1_.deleted_by as deleted_5_9_1_, secrole1_.update_ts as update_t6_9_1_, secrole1_.updated_by as updated_7_9_1_, secrole1_.name as name8_9_1_ from public.sec_user_roles roles0_ inner join public.sec_role secrole1_ on roles0_.role_id=secrole1_.id where roles0_.user_id=?
select secrole0_.id as id1_9_, secrole0_.create_ts as create_t2_9_, secrole0_.created_by as created_3_9_, secrole0_.delete_ts as delete_t4_9_, secrole0_.deleted_by as deleted_5_9_, secrole0_.update_ts as update_t6_9_, secrole0_.updated_by as updated_7_9_, secrole0_.name as name8_9_ from public.sec_role secrole0_ where secrole0_.name=?
insert into public.sec_user_roles (user_id, role_id) values (?, ?)
select secuser0_.id as id1_10_, secuser0_.create_ts as create_t2_10_, secuser0_.created_by as created_3_10_, secuser0_.delete_ts as delete_t4_10_, secuser0_.deleted_by as deleted_5_10_, secuser0_.update_ts as update_t6_10_, secuser0_.updated_by as updated_7_10_, secuser0_.active as active8_10_, secuser0_.confirmation_id as confirma9_10_, secuser0_.confirmed_by as confirm10_10_, secuser0_.confirmed_ts as confirm11_10_, secuser0_.email as email12_10_, secuser0_.first_name as first_n13_10_, secuser0_.last_name as last_na14_10_, secuser0_.logged_out as logged_15_10_, secuser0_.password as passwor16_10_, secuser0_.username as usernam17_10_ from public.sec_user secuser0_ where secuser0_.username=?
select roles0_.user_id as user_id1_11_0_, roles0_.role_id as role_id2_11_0_, secrole1_.id as id1_9_1_, secrole1_.create_ts as create_t2_9_1_, secrole1_.created_by as created_3_9_1_, secrole1_.delete_ts as delete_t4_9_1_, secrole1_.deleted_by as deleted_5_9_1_, secrole1_.update_ts as update_t6_9_1_, secrole1_.updated_by as updated_7_9_1_, secrole1_.name as name8_9_1_ from public.sec_user_roles roles0_ inner join public.sec_role secrole1_ on roles0_.role_id=secrole1_.id where roles0_.user_id=?
select secrole0_.id as id1_9_, secrole0_.create_ts as create_t2_9_, secrole0_.created_by as created_3_9_, secrole0_.delete_ts as delete_t4_9_, secrole0_.deleted_by as deleted_5_9_, secrole0_.update_ts as update_t6_9_, secrole0_.updated_by as updated_7_9_, secrole0_.name as name8_9_ from public.sec_role secrole0_ where secrole0_.name=?
insert into public.sec_user_roles (user_id, role_id) values (?, ?)
