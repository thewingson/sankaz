ALTER TABLE public.booking_history add column history_date timestamp;
UPDATE public.booking_history set history_date  = '2020-01-01 00:00:00.000';
ALTER TABLE public.booking_history alter column history_date set not null;