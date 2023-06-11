create sequence public.sanatour_image_seq start 1 increment 1;

create  table public.sanatour_image(
    id  int8 not null ,
    san_id  int8  ,
    room_id  int8  ,
    type char(1) not null,
    base64_original text not null ,
    base64_scaled text not null
    );

