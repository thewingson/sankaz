alter table public.booking drop column if exists woop_order_id  ;
alter table public.booking drop constraint if exists UK_WOOP_ORDER_ID;