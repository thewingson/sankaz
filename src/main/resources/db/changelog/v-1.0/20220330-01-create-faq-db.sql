create sequence public.faq_seq start 1 increment 1;

create table public.faq
(
    id             int8           not null,
    question     varchar(255)   not null,
    answer      varchar(255)   not null,
    primary key (id)
);

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
