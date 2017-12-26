CREATE DATABASE IF NOT EXISTS lab_5;
USE lab_5;

CREATE TABLE IF NOT EXISTS train (
    id_train INT AUTO_INCREMENT PRIMARY KEY,
    number_train VARCHAR(4) NOT NULL,
    CONSTRAINT train_id_uindex UNIQUE (id_train)
);


CREATE TABLE IF NOT EXISTS coach (
    id_coach INT AUTO_INCREMENT PRIMARY KEY,
    number_place INT NOT NULL,
    type_coach VARCHAR(15) NOT NULL,
    number_coach VARCHAR(4) NOT NULL,
    train_id INT NOT NULL,
    CONSTRAINT coach_id_uindex UNIQUE (id_coach),
    CONSTRAINT coach_street_id_fk FOREIGN KEY (train_id)
        REFERENCES train (id_train)
);


CREATE TABLE ticket (
    id_ticket INT AUTO_INCREMENT PRIMARY KEY,
    date_departuere DATE NOT NULL,
    place_departuere VARCHAR(25) NOT NULL,
    place_arrival VARCHAR(25) NOT NULL,
    price_ticket INT NOT NULL,
    CONSTRAINT ticket_id_uindex UNIQUE (id_ticket)
);


CREATE TABLE IF NOT EXISTS ticket_coach (
    ticket_id INT NOT NULL,
    coach_id INT NULL,
    coach_number INT NULL,
    CONSTRAINT ticket_coach_pharmacy_id_fk FOREIGN KEY (ticket_id)
        REFERENCES coach (id_coach),
    CONSTRAINT ticket_coach_medicines_id_fk FOREIGN KEY (coach_id)
        REFERENCES ticket (id_ticket)
);

CREATE INDEX ticket_coach_coach_id_fk
  ON ticket_coach (coach_id);


insert into train (number_train) values
				  ('122П'),
				  ('891Р'),
			      ('98Ж'),
				  ('142В'),
				  ('22М');

insert into coach (number_place, type_coach, number_coach, train_id) values
				  (32,'kupe', 13, 1),
			      (54,'plackart', 21, 2),
				  (12,'lux', 2, 3),
				  (3,'plackart', 23, 4),
				  (22,'lux', 5, 5);

insert into ticket (date_departuere, place_departuere, place_arrival, price_ticket) values
				   ('2017-10-09', 'Lviv', 'Kyiv', 128),
				   ('2018-06-29', 'Dnipro','Chernivtsi', 87),
				   ('2017-02-05', 'Zaporizha', 'Uzhorod', 147),
				   ('2017-04-22', 'Rivne','Kropyvnitskyi', 122),
				   ('2017-04-22', 'Chonhar', 'Kropyvnitskyi', 90);

insert ticket_coach (ticket_id, coach_id, coach_number) values (1, 11, 42), (2, 22, 34), (3, 33, 18), (4, 44, 2)

DELIMITER //
DROP PROCEDURE IF EXISTS InsertCoach;//

CREATE PROCEDURE InsertCoach
(
IN number_place_in   int,
IN type_coach_in        varchar(15),
IN number_coach_in   varchar(4),
in train_in          VARCHAR(15)
)
BEGIN
	DECLARE msg varchar(40);

  IF NOT EXISTS( SELECT * FROM train WHERE number_train = train_in)
    THEN SET msg = 'This coach is absent';
  ELSE
		INSERT coach (number_place, type_coach, number_coach, train_id)
        Value (number_place_in, type_coach_in, number_coach_in,
			     (SELECT id_train FROM train WHERE train.number_train = train_in) );
		SET msg = 'OK';

	END IF;

	SELECT msg AS msg;

END //
DELIMITER ;