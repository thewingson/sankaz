create sequence public.booking_seq start 1 increment 1;
create sequence public.booking_history_seq start 1 increment 1;
create sequence public.city_seq start 1 increment 1;
create sequence public.company_category_seq start 1 increment 1;
create sequence public.gender_seq start 1 increment 1;
create sequence public.jwt_black_list_seq start 1 increment 1;
create sequence public.notification_template_seq start 1 increment 1;
create sequence public.organization_seq start 1 increment 1;
create sequence public.review_seq start 1 increment 1;
create sequence public.room_class_dic_seq start 1 increment 1;
create sequence public.room_seq start 1 increment 1;
create sequence public.san_seq start 1 increment 1;
create sequence public.san_type_seq start 1 increment 1;
create sequence public.sec_role_seq start 1 increment 1;
create sequence public.sec_user_seq start 1 increment 1;
create sequence public.service_category_seq start 1 increment 1;
create sequence public.stock_seq start 1 increment 1;
create sequence public.sys_file_seq start 1 increment 1;
create sequence public.tel_number_seq start 1 increment 1;
create sequence public.user_device_token_seq start 1 increment 1;
create sequence public.user_notification_seq start 1 increment 1;
create sequence public.faq_seq start 1 increment 1;

create table public.booking
(
    id             int8           not null,
    adults_count   int4,
    approved_date  timestamp,
    booking_date   timestamp      not null,
    cancelled_date timestamp,
    children_count int4,
    end_date       timestamp      not null,
    first_name     varchar(255)   not null,
    last_name      varchar(255)   not null,
    paid_date      timestamp,
    start_date     timestamp      not null,
    status         varchar(255),
    sum_price      numeric(19, 2) not null,
    tel_number     varchar(255)   not null,
    room_id        int8           not null,
    user_id        int8,
    woop_order_id  varchar(255),
    payment_url    varchar(255),
    transferred_date timestamp,
    cancel_reason varchar(255),
    primary key (id)
);

create table public.booking_history
(
    id         int8 not null,
    status     varchar(255),
    booking_id int8 not null,
    history_date timestamp not null,
    primary key (id)
);

create table public.city
(
    id             int8         not null,
    code           varchar(255) not null,
    description    varchar(255),
    name           varchar(255) not null,
    description_kz varchar(255),
    name_kz        varchar(255),
    primary key (id)
);

create table public.company_category
(
    id             int8         not null,
    code           varchar(255) not null,
    description    varchar(255),
    name           varchar(255) not null,
    description_kz varchar(255),
    name_kz        varchar(255),
    primary key (id)
);

create table public.gender
(
    id             int8         not null,
    code           varchar(255) not null,
    description    varchar(255),
    name           varchar(255) not null,
    description_kz varchar(255),
    name_kz        varchar(255),
    primary key (id)
);

create table public.notification_template
(
    id               int8         not null,
    code             varchar(255) not null,
    description      varchar(255),
    name             varchar(255) not null,
    message_template text         not null,
    primary key (id)
);

create table public.organization
(
    id                  int8         not null,
    address             varchar(255),
    approved_date       timestamp,
    confirmation_status varchar(255),
    confirmed_by        varchar(255),
    description         varchar(255),
    email               varchar(255),
    iban                varchar(255) not null,
    iin                 varchar(255) not null,
    instagram_link      varchar(255),
    manager_full_name   varchar(255) not null,
    name                varchar(255) not null,
    reject_message      varchar(255),
    rejected_date       timestamp,
    request_date        timestamp,
    site_link           varchar(255),
    tel_number          varchar(255) not null,
    company_category_id int8,
    user_id             int8,
    primary key (id)
);

create table public.organization_pics
(
    organization_id int8 not null,
    file_id         int8 not null
);

create table public.review
(
    id           int8         not null,
    message_date timestamp    not null,
    text         varchar(255) not null,
    rating       float4,
    user_id      int8         not null,
    parent_id    int8,
    san_id       int8         not null,
    primary key (id)
);

create table public.room
(
    id          int8         not null,
    bed_count   int4,
    price       numeric(19, 2),
    room_count  int4,
    room_number varchar(255) not null,
    san_id    int8         not null,
    class_id    int8         not null,
    price_child numeric(19, 2),
    primary key (id)
);

create table public.room_class_dic
(
    id             int8         not null,
    deleted        boolean      not null,
    code           varchar(255) not null,
    description    varchar(255),
    description_kz varchar(255),
    name           varchar(255) not null,
    name_kz        varchar(255),
    primary key (id)
);

create table public.room_class_pics
(
    class_id int8 not null,
    file_id  int8 not null
);

create table public.room_pics
(
    room_id int8 not null,
    file_id int8 not null
);

create table public.san
(
    id             int8         not null,
    address        varchar(255),
    description    varchar(255),
    instagram_link varchar(255),
    latitude       float8,
    longitude      float8,
    name           varchar(255) not null,
    site_link      varchar(255),
    city_id        int8         not null,
    org_id         int8         not null,
    san_type_id    int8         not null,
    primary key (id)
);

create table public.san_pics
(
    san_id int8 not null,
    pic_id int8 not null
);

create table public.san_tel_numbers
(
    san_id        int8 not null,
    tel_number_id int8 not null
);

create table public.san_type
(
    id             int8         not null,
    code           varchar(255) not null,
    description    varchar(255),
    name           varchar(255) not null,
    description_kz varchar(255),
    name_kz        varchar(255),
    primary key (id)
);

create table public.sec_role
(
    id   int8         not null,
    name varchar(255) not null,
    primary key (id)
);

create table public.sec_user
(
    id                               int8         not null,
    active                           boolean      not null,
    confirmation_number              int4,
    confirmation_number_created_date timestamp,
    confirmation_status              varchar(255),
    confirmed_by                     varchar(255),
    confirmed_date                   timestamp,
    email                            varchar(255),
    first_name                       varchar(255) not null,
    last_name                        varchar(255) not null,
    logged_out                       boolean      not null,
    password                         varchar(255) not null,
    reset_number                     int4,
    reset_number_created_date        timestamp,
    reset_number_status              varchar(255),
    tel_number                       varchar(255) not null,
    user_type                        varchar(255) not null,
    username                         varchar(255),
    city_id                          int8,
    gender_id                        int8,
    pic_id                           int8,
    primary key (id)
);

create table public.sec_user_roles
(
    user_id int8 not null,
    role_id int8 not null
);

create table public.sec_user_token
(
    id           int8         not null,
    access_token varchar(255) not null,
    block_date   timestamp,
    expire_date  timestamp    not null,
    is_blocked   boolean      not null,
    user_id      int8,
    primary key (id)
);

create table public.service_category
(
    id             int8         not null,
    code           varchar(255) not null,
    description    varchar(255),
    name           varchar(255) not null,
    description_kz varchar(255),
    name_kz        varchar(255),
    primary key (id)
);

create table public.stock
(
    id             int8    not null,
    active         boolean not null,
    description    varchar(255),
    description_kz varchar(255),
    title          varchar(255),
    title_kz       varchar(255),
    view_count     int4    not null,
    san_id         int8,
    primary key (id)
);

create table public.sys_file
(
    id           int8         not null,
    deleted_date date,
    extension    varchar(255) not null,
    file_name    varchar(255) not null,
    size         int8         not null,
    primary key (id)
);

create table public.tel_number
(
    id    int8         not null,
    value varchar(255) not null,
    primary key (id)
);

create table public.user_device_token
(
    id      int8         not null,
    token   varchar(255) not null,
    user_id int8         not null,
    primary key (id)
);

create table public.user_favorites
(
    user_id int8 not null,
    san_id  int8 not null
);

create table public.user_notification
(
    id                int8         not null,
    description       varchar(255),
    description_kz    varchar(255),
    notification_type varchar(255) not null,
    notify_date       timestamp,
    title             varchar(255),
    title_kz          varchar(255),
    viewed            boolean      not null,
    book_id           int8,
    stock_id          int8,
    user_id           int8,
    primary key (id)
);

create table public.faq
(
    id             int8           not null,
    question     varchar(255)   not null,
    answer      varchar(255)   not null,
    primary key (id)
);

alter table public.booking add constraint UK_WOOP_ORDER_ID unique (woop_order_id);
alter table public.city add constraint UK_p738egrkomomgourst3hqfipb unique (code);
alter table public.company_category add constraint UK_irrd8l4f2wtj4pd263qxkcoda unique (code);
alter table public.gender add constraint UK_mxvfcsf1euhi5hsw1uecvke7b unique (code);
alter table public.notification_template add constraint UK_78ljxu1rtyj29qj8a3bao9bfx unique (code);
alter table public.organization add constraint UK_t4bamosb7eleheafmlcalbhjf unique (email);
alter table public.organization add constraint UK_6xmmhtj3oxb3xv5pmwey8q98o unique (iban);
alter table public.organization add constraint UK_nk7pj2mrxhl33pqk8l3cifn5x unique (iin);
alter table public.organization add constraint UK_csspk5w1f93fqqkl9uet91fae unique (tel_number);
alter table public.room add constraint UKnhcy0jecpm5nd7r9xfmngbrd unique (room_number, san_id);
alter table public.room_class_dic add constraint UKl91ax35umclncnq413o8hicqq unique (code);
alter table public.san_type add constraint UK_nvcn26t59vjtpavtcf8dhyq78 unique (code);
alter table public.sec_role add constraint UK_328v0xhgur113t0ak61ieyp8n unique (name);
alter table public.sec_user add constraint UK_8s0loc6vmiu65wedpybnydh0b unique (tel_number);
alter table public.sec_user add constraint UK_5ctbdrlf3eismye20vsdtk8w8 unique (username);
alter table public.service_category add constraint UK_mr019mf8foyex0jdkjtaet7s0 unique (code);
alter table public.user_device_token add constraint UK_dd839ilume1jlv5l6q0wl4ave unique (token);
alter table public.user_favorites add constraint UK_USER_ID_AND_SAN_ID unique (user_id, san_id);
alter table public.booking add constraint BOOKING_ROOM_FK foreign key (room_id) references public.room;
alter table public.booking add constraint BOOKING_USER_FK foreign key (user_id) references public.sec_user;
alter table public.booking_history add constraint BOOKING_HISTORY_BOOKING_FK foreign key (booking_id) references public.booking;
alter table public.organization add constraint ORGANIZATION_COMPANY_CATEGORY_FK foreign key (company_category_id) references public.company_category;
alter table public.organization add constraint ORGANIZATION_USER_FK foreign key (user_id) references public.sec_user;
alter table public.organization_pics add constraint ORGANIZATION_PICS_FILE_FK foreign key (file_id) references public.sys_file;
alter table public.organization_pics add constraint ORGANIZATION_PICS_ORGANIZATION_FK foreign key (organization_id) references public.organization;
alter table public.review add constraint MESSAGE_USER_FK foreign key (user_id) references public.sec_user;
alter table public.review add constraint REVIEW_PARENT_FK foreign key (parent_id) references public.review;
alter table public.review add constraint REVIEW_SAN_FK foreign key (san_id) references public.san;
alter table public.room add constraint ROOM_SAN_FK foreign key (san_id) references public.san;
alter table public.room add constraint ROOM_CLASS_FK foreign key (class_id) references public.room_class_dic;
alter table public.room_pics add constraint ROOM_PICS_PIC_FK foreign key (file_id) references public.sys_file;
alter table public.room_pics add constraint ROOM_PICS_ROOM_FK foreign key (room_id) references public.room;
alter table public.san add constraint SAN_CITY_FK foreign key (city_id) references public.city;
alter table public.san add constraint SAN_ORG_FK foreign key (org_id) references public.organization;
alter table public.san add constraint SAN_TYPE_FK foreign key (san_type_id) references public.san_type;
alter table public.san_pics add constraint SAN_PICS_PIC_FK foreign key (pic_id) references public.sys_file;
alter table public.san_pics add constraint SAN_PICS_SAN_FK foreign key (san_id) references public.san;
alter table public.san_tel_numbers add constraint SAN_TEL_NUMBERS_NUMBER_FK foreign key (tel_number_id) references public.tel_number;
alter table public.san_tel_numbers add constraint SAN_TEL_NUMBERS_SAN_FK foreign key (san_id) references public.san;
alter table public.sec_user add constraint SEC_USER_CITY_FK foreign key (city_id) references public.city;
alter table public.sec_user add constraint SEC_USER_GENDER_FK foreign key (gender_id) references public.gender;
alter table public.sec_user add constraint SEC_USER_PIC_FK foreign key (pic_id) references public.sys_file;
alter table public.sec_user_roles add constraint FKabm55itj3htl8yno424528wi8 foreign key (role_id) references public.sec_role;
alter table public.sec_user_roles add constraint FKmu39wf2qaxcmq8e5cqtmv8ofv foreign key (user_id) references public.sec_user;
alter table public.sec_user_token add constraint TOKEN_USER_FK foreign key (user_id) references public.sec_user;
alter table public.stock add constraint STOCK_SAN_FK foreign key (san_id) references public.san;
alter table public.user_device_token add constraint TOKEN_USER_FK foreign key (user_id) references public.sec_user;
alter table public.user_favorites add constraint FAV_SAN_FK foreign key (san_id) references public.san;
alter table public.user_favorites add constraint FAV_USER_FK foreign key (user_id) references public.sec_user;
alter table public.user_notification add constraint NOTIFICATION_STOCK_FK foreign key (stock_id) references public.stock;
alter table public.user_notification add constraint NOTIFICATION_USER_FK foreign key (user_id) references public.sec_user;



INSERT INTO public.sec_role
(id, "name")
VALUES(nextval('public.sec_role_seq'), 'ROLE_ADMIN'),
      (nextval('public.sec_role_seq'), 'ROLE_MODERATOR'),
      (nextval('public.sec_role_seq'), 'ROLE_USER');

INSERT INTO public.sec_user
(id, active, confirmation_number, confirmation_number_created_date, confirmation_status, confirmed_by, confirmed_date, email, first_name, last_name, logged_out, "password", reset_number, reset_number_created_date, reset_number_status, tel_number, user_type, username, city_id, gender_id, pic_id)
VALUES(nextval('public.sec_user_seq'), true, 0, NULL, 'FINISHED', 'admin', '2022-03-22 20:10:13.871', 'admin@admin.kz', 'Admin', 'Admin', false, '$2a$10$BHCaUQgcCugzPr77l3QVFe2lHN7DhMgzWzEynu6dkPekMXFbovS1q', 0, NULL, 'EMPTY', '+77770000000', 'USER', '+77770000000', NULL, NULL, NULL),
      (nextval('public.sec_user_seq'), true, 0, NULL, 'FINISHED', 'admin', '2022-03-22 20:10:13.983', 'user@user.kz', 'User', 'User', false, '$2a$10$PE5ndBUu/6ME9P50oF87Le7p/80tct2hunzmJkeHMGt6KoHjiHQPe', 0, NULL, 'EMPTY', '+77770000002', 'USER', '+77770000002', NULL, NULL, NULL),
      (nextval('public.sec_user_seq'), true, 0, NULL, 'FINISHED', 'admin', '2022-03-22 20:10:14.075', 'user4@user.kz', 'User2', 'User2', false, '$2a$10$anzS661TGmVzIPo3X6JPbOUGQoErP6o1J53K6trC/JSu9wtUGvlMK', 0, NULL, 'EMPTY', '+77770000004', 'USER', '+77770000004', NULL, NULL, NULL),
      (nextval('public.sec_user_seq'), true, 0, NULL, 'FINISHED', 'admin', '2022-03-22 20:10:14.190', 'organ@organ.kz', 'Organ', 'Organ', false, '$2a$10$kDh7g8.VPveHT/4UDHgwn.CJipgBh0gvk60siDTSfZIUSjtf4HM9O', 0, NULL, 'EMPTY', '+77770000070', 'ORG', '+77770000070', NULL, NULL, NULL),
      (nextval('public.sec_user_seq'), true, 0, NULL, 'FINISHED', 'admin', '2022-03-22 20:10:14.420', 'organ1@organ.kz', 'Organ 1', 'Organ 1', false, '$2a$10$sEJaxDNPxeIb2uVjwwgVc.ocBu5mXaz6CFlY7Mj.aE/3UDz6QcVB.', 0, NULL, 'EMPTY', '+77770000071', 'ORG', '+77770000071', NULL, NULL, NULL),
      (nextval('public.sec_user_seq'), true, 0, NULL, 'FINISHED', 'admin', '2022-03-22 20:10:14.576', 'organ2@organ.kz', 'Organ 2', 'Organ 2', false, '$2a$10$3kWTOoVN2oFsnwI3nn1GB..f596cPpl8S4tbYM0UAqPyGUnDlky3W', 0, NULL, 'EMPTY', '+77770000072', 'ORG', '+77770000072', NULL, NULL, NULL),
      (nextval('public.sec_user_seq'), true, 0, NULL, 'FINISHED', 'admin', '2022-03-22 20:10:14.693', 'organ3@organ.kz', 'Organ 3', 'Organ 3', false, '$2a$10$JdwvW8Fi11Gak/Dcm9hGN.oxNZXt00pLQYVhIyeRAaR1fBNO6uLKe', 0, NULL, 'EMPTY', '+77770000073', 'ORG', '+77770000073', NULL, NULL, NULL);

INSERT INTO public.sec_user_roles
(user_id, role_id)
VALUES(1, 1),
      (2, 3),
      (3, 3),
      (4, 2),
      (5, 2),
      (6, 2),
      (6, 2);

INSERT INTO public.san_type
(id, code, description, "name", description_kz, name_kz)
VALUES(nextval('public.san_type_seq'), 'ENTERTAINMENT', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'Развлекательный', NULL, NULL),
      (nextval('public.san_type_seq'), 'MEDICAL', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'Лечебный', NULL, NULL);

INSERT INTO public.city
(id, code, description, "name", description_kz, name_kz)
VALUES(nextval('public.city_seq'), 'SRG', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'Сарыагаш', NULL, NULL);


INSERT INTO public.gender
(id, code, description, "name", description_kz, name_kz)
VALUES(nextval('public.gender_seq'), 'MALE', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'Муж.', NULL, NULL),
      (nextval('public.gender_seq'), 'FEMALE', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'Жен.', NULL, NULL);

INSERT INTO public.company_category
(id, code, description, "name", description_kz, name_kz)
VALUES(nextval('public.company_category_seq'), 'TOUR', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'Туристическая', NULL, NULL),
      (nextval('public.company_category_seq'), 'PROF', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'Прфилактическая', NULL, NULL);

INSERT INTO public.organization
(id, address, approved_date, confirmation_status, confirmed_by, description, email, iban, iin, instagram_link, manager_full_name, "name", reject_message, rejected_date, request_date, site_link, tel_number, company_category_id, user_id)
VALUES(nextval('public.organization_seq'), 'г.Сатпаев, ул.Абая, д.175', '2022-03-22 20:10:14.194', 'SERVICE_CREATED', '+77770000000', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'org@mail.kz', '70707070707070', '707070707070', 'instagram.com/rkalmat', 'Organ Organ', 'ТОО Отдых', NULL, NULL, NULL, 'smsc.kz', '+77770000070', 1, 4),
      (nextval('public.organization_seq'), 'г.Сатпаев, ул.Төле би, д.100', '2022-03-22 20:10:14.424', 'SERVICE_CREATED', '+77770000000', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'org1@mail.kz', '71717171717171', '717171717171', 'instagram.com/fckairat', 'Organ 1 Organ 1', 'ИП Общак', NULL, NULL, NULL, 'github.com', '+77770000071', 1, 5),
      (nextval('public.organization_seq'), 'г.Жезказган, ул.Сейдімбек, д.10', '2022-03-22 20:10:14.579', 'CONFIRMED', '+77770000000', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'org2@mail.kz', '72727272727272', '727272727272', 'instagram.com/dauletten', 'Organ 2 Organ 2', 'ИП Улытау', NULL, NULL, NULL, 'jira.com', '+77770000072', 2, 6),
      (nextval('public.organization_seq'), 'г.Кокшетау, ул.Тау, д.14а', '2022-03-22 20:10:14.702', 'SERVICE_CREATED', '+77770000000', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'org3@mail.kz', '73737373737373', '737373737373', 'instagram.com/apple', 'Organ 3 Organ 3', 'Kokshe Group', NULL, NULL, NULL, 'bitbucket.com', '+77770000073', 2, 7);

INSERT INTO public.san
(id, address, description, instagram_link, latitude, longitude, "name", site_link, city_id, org_id, san_type_id)
VALUES(nextval('public.san_seq'), 'Талгар, ул.Бокейханов, д.17', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', NULL, NULL, NULL, 'Ақбұлак', NULL, 1, 1, 1),
      (nextval('public.san_seq'), 'Талгар, ул.Абылай хан, д.20', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', NULL, NULL, NULL, 'Лесная сказка', NULL, 1, 2, 1),
      (nextval('public.san_seq'), 'Талгар, ул.Есенберлин, д.', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', NULL, NULL, NULL, 'Шымбұлак', NULL, 1, 3, 2),
      (nextval('public.san_seq'), 'Талгар, ул.Кунаев, д.90', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', NULL, NULL, NULL, '8 озер', NULL, 1, 4, 2);

INSERT INTO public.room_class_dic
(id, deleted, code, description, description_kz, "name", name_kz)
VALUES(nextval('public.room_class_dic_seq'), false, 'LUX', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'Люкс', 'Люкс'),
      (nextval('public.room_class_dic_seq'), false, 'COM', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'Lorem Ipsum is simply dummy text of the printing and typesetting industry.', 'Комфорт', 'Комфорт');

INSERT INTO public.room
(id, bed_count, price, room_count, room_number, san_id, class_id,price_child)
VALUES(nextval('public.room_seq'), 1, 14500.00, 2, '201E', 1, 1,5000),
      (nextval('public.room_seq'), 1, 14500.00, 2, '13', 1, 2,5000),
      (nextval('public.room_seq'), 2, 34500.00, 3, '101', 2, 1,5000),
      (nextval('public.room_seq'), 2, 34500.00, 3, '7', 2, 2,5000),
      (nextval('public.room_seq'), 1, 14500.00, 2, '31', 3, 1,5000),
      (nextval('public.room_seq'), 1, 14500.00, 2, '13', 3, 2,5000),
      (nextval('public.room_seq'), 2, 34500.00, 3, '9', 4, 1,5000),
      (nextval('public.room_seq'), 2, 34500.00, 3, '7', 4, 2,5000);

INSERT INTO public.booking
(id, adults_count, approved_date, booking_date, cancelled_date, children_count, end_date, first_name, last_name, paid_date, start_date, status, sum_price, tel_number, room_id, user_id, woop_order_id, payment_url)
VALUES(nextval('public.booking_seq'), 1, NULL, '2022-03-22 20:10:14.000', NULL, 0, '2022-03-26 00:00:00.000', 'Алмат', 'Рахметолла', NULL, '2022-03-23 00:00:00.000', 'WAITING', 15000.00, '+77081997727', 1, NULL, 'ecom_1', 'https://www.google.com/'),
      (nextval('public.booking_seq'), 2, NULL, '2022-03-22 20:10:14.000', NULL, 0, '2022-03-27 00:00:00.000', 'Бекзат', 'Серкебай', NULL, '2022-03-23 00:00:00.000', 'WAITING', 15000.00, '+77081997777', 1, NULL, 'ecom_2', 'https://www.google.com/'),
      (nextval('public.booking_seq'), 1, NULL, '2022-03-22 20:10:14.000', '2022-03-22 20:10:14.000', 0, '2022-03-27 00:00:00.000', 'Асан', 'Пердешов', NULL, '2022-03-25 00:00:00.000', 'CANCELLED', 3000.00, '+77028649054', 1, NULL, 'ecom_3', 'https://www.google.com/'),
      (nextval('public.booking_seq'), 2, NULL, '2022-03-22 20:10:14.000', '2022-03-22 20:10:14.000', 0, '2022-03-29 00:00:00.000', 'Болат', 'Назарбаев', NULL, '2022-03-26 00:00:00.000', 'CANCELLED', 40000.00, '+77021000000', 1, NULL, 'ecom_4', 'https://www.google.com/'),
      (nextval('public.booking_seq'), 1, '2022-03-22 20:10:14.000', '2022-03-22 20:10:14.000', NULL, 0, '2022-03-31 00:00:00.000', 'Мейрамбек', 'Бесбаев', NULL, '2022-03-27 00:00:00.000', 'APPROVED', 60000.00, '+77777777778', 3, NULL, 'ecom_5', 'https://www.google.com/'),
      (nextval('public.booking_seq'), 2, NULL, '2022-03-22 20:10:14.000', NULL, 0, '2022-04-02 00:00:00.000', 'Заттыбек', 'Көпболсынұлы', '2022-03-28 00:00:00.000', '2022-03-28 00:00:00.000', 'PAID', 25000.00, '+77081111111', 3, NULL, 'ecom_6', 'https://www.google.com/'),
      (nextval('public.booking_seq'), 1, '2022-03-22 20:10:14.000', '2022-03-22 20:10:14.000', NULL, 0, '2022-04-04 00:00:00.000', 'Дуа', 'Липа', NULL, '2022-03-29 00:00:00.000', 'APPROVED', 89000.00, '+77070000007', 4, NULL, 'ecom_7', 'https://www.google.com/'),
      (nextval('public.booking_seq'), 2, NULL, '2022-03-22 20:10:14.000', NULL, 0, '2022-04-06 00:00:00.000', 'Қасым-Жомарт', 'Тоқаев', '2022-03-30 00:00:00.000', '2022-03-30 00:00:00.000', 'PAID', 65000.00, '+77777777777', 4, NULL, 'ecom_8', 'https://www.google.com/'),
      (nextval('public.booking_seq'), 1, '2022-03-22 20:10:14.000', '2022-03-22 20:10:14.000', NULL, 0, '2022-04-08 00:00:00.000', 'Евгений', 'Поносенков', NULL, '2022-03-31 00:00:00.000', 'APPROVED', 51000.00, '+77055555555', 5, NULL, 'ecom_9', 'https://www.google.com/'),
      (nextval('public.booking_seq'), 2, NULL, '2022-03-22 20:10:14.000', NULL, 0, '2022-04-10 00:00:00.000', 'Владимир', 'Путин', '2022-03-30 00:00:00.000', '2022-03-30 00:00:00.000', 'PAID', 109000.00, '+77012332233', 5, NULL, 'ecom_10', 'https://www.google.com/'),
      (nextval('public.booking_seq'), 3, NULL, '2022-03-22 20:10:14.000', NULL, 0, '2022-04-12 00:00:00.000', 'Владимир', 'Зеленский', '2022-03-31 00:00:00.000', '2022-03-31 00:00:00.000', 'PAID', 10000.00, '+77012332244', 2, NULL, 'ecom_11', 'https://www.google.com/');

INSERT INTO public.faq
(id, question, answer)
VALUES(nextval('public.faq_seq'), 'Вопрос 1', 'Ответ 1'),
      (nextval('public.faq_seq'), 'Вопрос 2', 'Ответ 2'),
      (nextval('public.faq_seq'), 'Вопрос 3', 'Ответ 3'),
      (nextval('public.faq_seq'), 'Вопрос 4', 'Ответ 4'),
      (nextval('public.faq_seq'), 'Вопрос 5', 'Ответ 5'),
      (nextval('public.faq_seq'), 'Вопрос 6', 'Ответ 6'),
      (nextval('public.faq_seq'), 'Вопрос 7', 'Ответ 7'),
      (nextval('public.faq_seq'), 'Вопрос 8', 'Ответ 8'),
      (nextval('public.faq_seq'), 'Вопрос 9', 'Ответ 9'),
      (nextval('public.faq_seq'), 'Вопрос 10', 'Ответ 10');