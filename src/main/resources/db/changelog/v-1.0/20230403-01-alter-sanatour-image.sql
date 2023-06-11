alter table public.sanatour_image add constraint sanatour_image_san_fk foreign key (san_id) references san(id);
alter table public.sanatour_image add constraint sanatour_image_room_fk foreign key (room_id) references room(id);

