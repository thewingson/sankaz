update city set code = 'SRG', name = 'Сарыагаш' where code = 'ALM';
update san set city_id = (select id from city where code = 'SRG');
delete from city where code <> 'SRG';