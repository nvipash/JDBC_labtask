create database if not exists lab_5a;
use lab_5a;

create table if not exists train (
    id_train 			int auto_increment primary key,
    number_train 		int not null,
    type_train 			varchar(25) not null,
    photo_train		    varchar(25) not null
)ENGINE = InnoDB;

insert into train (number_train, type_train, photo_train) values 
				  (122,'high-speed','photo-1'),
				  (891,'low-speed','photo-2'),
			      (98,'electric-regional','photo-3'),
				  (142,'low-speed','photo-4'),
				  (22,'electric-regional','photo-5');

create table if not exists coach
(
	id_coach              int auto_increment primary key,
	number_place		  int not null,
	type_coach 			  varchar(25) not null,

	constraint coach_train_id_fk
		foreign key (id_coach) references train (id_train)
)ENGINE = InnoDB;

alter table coach
	add constraint CK_number_place
	check (number_place > 1 AND number_place < 56);

insert into coach (number_place, type_coach) values 
				  (32,'kupe'),
			      (54,'plackart'),
				  (12,'lux'),
				  (3,'plackart'),
				  (22,'lux');

create table if not exists ticket_coach (
	id_ticket_coach int auto_increment not null,
    coach_id int null,
    number_coach int null,
    
    constraint ticketcoach_coach_id_fk 
		foreign key (coach_id) references coach (id_coach),
	 constraint ticketcoach_ticket_id_fk
		foreign key (id_ticket_coach) references ticket (id_ticket)
)ENGINE = InnoDB;

insert into ticket_coach (number_coach) values 
						 (12), (7), (9), (2);	

create table if not exists ticket
(
	id_ticket             int auto_increment primary key,
	date_departuere       date not null,
	place_departuere	  varchar(25) not null,
	tram_schedule         text not null,
	place_arrival		  varchar(25) not null,
	price_ticket		  int not null,
	status_ticket         varchar(25) not null,
)ENGINE = InnoDB;

alter table ticket
	add constraint CK_price_ticket
	check (price_ticket >= 5),
	add constraint CK_status_ticket
	check (status_ticket in ('free', 'sold', 'booked'));

insert into ticket (date_departuere, place_departuere, tram_schedule, place_arrival, price_ticket, status_ticket) values 
				   ('2017-10-09', 'Lviv','shedule-1', 'Kyiv', 15, 'booked'),
				   ('2018-06-29', 'Dnipro','shedule-2', 'Chernivtsi', 28, 'sold'),
				   ('2017-02-05', 'Zaporizha','shedule-2', 'Uzhorod', 32, 'free'),
				   ('2017-04-22', 'Rivne','shedule-2', 'Kropyvnitskyi', 32, 'free'),
				   ('2017-04-22', 'Chonhar','shedule-1', 'Kropyvnitskyi', 32, 'free');