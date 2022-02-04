alter table public.category
    drop constraint UK_CATEGORY_CODE unique (code)
>>

drop sequence public.category_seq
>>

drop table public.category
>>