alter table public.user_notification drop constraint NOTIFICATION_BOOK_FK;
alter table public.user_notification drop column book_id;
alter table public.user_notification add column book_id int8;
alter table public.user_notification add constraint NOTIFICATION_BOOK_FK foreign key (book_id) references public.booking;
