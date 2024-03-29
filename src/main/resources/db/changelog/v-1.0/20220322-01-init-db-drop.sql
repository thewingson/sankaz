alter table public.booking
    drop constraint BOOKING_USER_FK;
alter table public.booking_history
    drop constraint BOOKING_HISTORY_BOOKING_FK;
alter table public.booking
    drop constraint BOOKING_ROOM_FK;
alter table public.organization
    drop constraint ORGANIZATION_COMPANY_CATEGORY_FK;
alter table public.organization
    drop constraint ORGANIZATION_USER_FK;
alter table public.organization_pics
    drop constraint ORGANIZATION_PICS_FILE_FK;
alter table public.organization_pics
    drop constraint ORGANIZATION_PICS_ORGANIZATION_FK;
alter table public.review
    drop constraint MESSAGE_USER_FK;
alter table public.review
    drop constraint REVIEW_PARENT_FK;
alter table public.review
    drop constraint REVIEW_SAN_FK;
alter table public.room
    drop constraint ROOM_CLASS_FK;
alter table public.room_class
    drop constraint ROOM_SAN_FK;
alter table public.room_class_dic
    drop constraint ROOM_SAN_FK;
alter table public.room_class_pics
    drop constraint ROOM_CLASS_PICS_PIC_FK;
alter table public.room_class_pics
    drop constraint ROOM_CLASS_PICS_CLASS_FK;
alter table public.room_pics
    drop constraint ROOM_PICS_PIC_FK;
alter table public.room_pics
    drop constraint ROOM_PICS_ROOM_FK;
alter table public.san
    drop constraint SAN_CITY_FK;
alter table public.san
    drop constraint SAN_ORG_FK;
alter table public.san
    drop constraint SAN_TYPE_FK;
alter table public.san_pics
    drop constraint SAN_PICS_PIC_FK;
alter table public.san_pics
    drop constraint SAN_PICS_SAN_FK;
alter table public.san_tel_numbers
    drop constraint SAN_TEL_NUMBERS_NUMBER_FK;
alter table public.san_tel_numbers
    drop constraint SAN_TEL_NUMBERS_SAN_FK;
alter table public.sec_user
    drop constraint SEC_USER_CITY_FK;
alter table public.sec_user
    drop constraint SEC_USER_GENDER_FK;
alter table public.sec_user
    drop constraint SEC_USER_PIC_FK;
alter table public.sec_user_roles
    drop constraint FKabm55itj3htl8yno424528wi8;
alter table public.sec_user_roles
    drop constraint FKmu39wf2qaxcmq8e5cqtmv8ofv;
alter table public.sec_user_token
    drop constraint TOKEN_USER_FK;
alter table public.stock
    drop constraint STOCK_SAN_FK;
alter table public.user_device_token
    drop constraint TOKEN_USER_FK;
alter table public.user_favorites
    drop constraint FAV_SAN_FK;
alter table public.user_favorites
    drop constraint FAV_USER_FK;
alter table public.user_notification
    drop constraint NOTIFICATION_BOOK_FK;
alter table public.user_notification
    drop constraint NOTIFICATION_STOCK_FK;
alter table public.user_notification
    drop constraint NOTIFICATION_USER_FK;
drop table if exists public.booking cascade;
drop table if exists public.booking_history cascade;
drop table if exists public.city cascade;
drop table if exists public.company_category cascade;
drop table if exists public.gender cascade;
drop table if exists public.item_pic cascade;
drop table if exists public.notification_template cascade;
drop table if exists public.organization cascade;
drop table if exists public.organization_pics cascade;
drop table if exists public.review cascade;
drop table if exists public.room cascade;
drop table if exists public.room_class cascade;
drop table if exists public.room_class_dic cascade;
drop table if exists public.room_class_pics cascade;
drop table if exists public.room_pics cascade;
drop table if exists public.san cascade;
drop table if exists public.san_pics cascade;
drop table if exists public.san_tel_numbers cascade;
drop table if exists public.san_type cascade;
drop table if exists public.sec_role cascade;
drop table if exists public.sec_user cascade;
drop table if exists public.sec_user_roles cascade;
drop table if exists public.sec_user_token cascade;
drop table if exists public.service_category cascade;
drop table if exists public.stock cascade;
drop table if exists public.sys_file cascade;
drop table if exists public.tel_number cascade;
drop table if exists public.user_device_token cascade;
drop table if exists public.user_favorites cascade;
drop table if exists public.user_notification cascade;
drop table if exists public.faq cascade;
drop sequence if exists public.booking_history_seq;
drop sequence if exists public.booking_seq;
drop sequence if exists public.city_seq;
drop sequence if exists public.company_category_seq;
drop sequence if exists public.gender_seq;
drop sequence if exists public.item_pic_seq;
drop sequence if exists public.jwt_black_list_seq;
drop sequence if exists public.notification_template_seq;
drop sequence if exists public.organization_seq;
drop sequence if exists public.review_seq;
drop sequence if exists public.room_class_dic_seq;
drop sequence if exists public.room_class_seq;
drop sequence if exists public.room_seq;
drop sequence if exists public.san_seq;
drop sequence if exists public.san_type_seq;
drop sequence if exists public.sec_role_seq;
drop sequence if exists public.sec_user_seq;
drop sequence if exists public.service_category_seq;
drop sequence if exists public.stock_seq;
drop sequence if exists public.sys_file_seq;
drop sequence if exists public.tel_number_seq;
drop sequence if exists public.user_device_token_seq;
drop sequence if exists public.user_notification_seq;
drop sequence if exists public.faq_seq;