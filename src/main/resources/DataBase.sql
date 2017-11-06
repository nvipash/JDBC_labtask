CREATE SCHEMA IF NOT EXISTS `DB_JDBC` DEFAULT CHARACTER SET utf8;
USE `DB_JDBC`;

CREATE TABLE City 
(
  City VARCHAR(25) NOT NULL,
  PRIMARY KEY (City)
) ENGINE = InnoDB;

CREATE TABLE  Person 
(
  IDPerson INT NOT NULL AUTO_INCREMENT,
  Surname VARCHAR(25) NOT NULL,
  `Name` VARCHAR(25) NOT NULL,
  City VARCHAR(25) NOT NULL,  
  Email VARCHAR(45) NULL,
  PRIMARY KEY (IDPerson),
  CONSTRAINT FOREIGN KEY (City) 
    REFERENCES City (City)
) ENGINE = InnoDB;

CREATE TABLE Book 
(
  IDBook INT NOT NULL AUTO_INCREMENT,
  BookName VARCHAR(45) NOT NULL,
  Author VARCHAR(45) NOT NULL,
  Amount INT NOT NULL,
  PRIMARY KEY (IDBook)
) ENGINE = InnoDB;

CREATE TABLE  PersonBook (
  IDPerson INT NOT NULL,
  IDBook INT NOT NULL,
  PRIMARY KEY (IDPerson, IDBook),
  CONSTRAINT  FOREIGN KEY (IDPerson)
    REFERENCES  Person (IDPerson),
  CONSTRAINT   FOREIGN KEY (IDBook)
    REFERENCES  Book (IDBook)
) ENGINE = InnoDB;

INSERT INTO `book` VALUES 
(1,'Bible','St. mans',5),
(2,'Kobzar','Shevchenko ',4),
(3,'Harry Potter','J. K. Rowling',1),
(4,'Zakhar Berkut','I. Franko',2),
(5,'The Jungle Book','Rudyard Kipling',1);

INSERT INTO `city` VALUES ('Herson'),('Kyiv'),('Lviv'),('Poltava'),('Ternopil');

INSERT INTO `person` VALUES 
(1,'Koldovskyy','Vyacheslav','Lviv','koldovsky@gmail.com'),
(2,'Pavelchak','Andrii','Poltava','apavelchak@gmail.com'),
(3,'Soluk','Andrian','Herson','andriansoluk@gmail.com'),
(4,'Dubyniak','Bohdan','Ternopil','bohdan.dub@gmail.com'),
(5,'Faryna','Igor','Kyiv','farynaihor@gmail.com'),
(6,'Kurylo','Volodymyr','Poltava','kurylo.volodymyr@gmail.com'),
(7,'Matskiv','Marian','Herson','marian3912788@gmail.com'),
(8,'Shyika','Tamara','Kyiv','tamara.shyika@gmail.com'),
(9,'Tkachyk','Volodymyr','Ternopil','vova1234.tkachik@gmail.com');

INSERT INTO `personbook` VALUES (4,1),(5,1),(8,1),(2,2),(6,2),(7,2),(1,3),(1,4),(9,4),(3,5);

DELIMITER //
CREATE PROCEDURE InsertPersonBook
(
IN SurmanePersonIn varchar(25),
IN BookNameIN varchar(45)
)
BEGIN
	DECLARE msg varchar(40);
    
  -- checks for present Surname
  IF NOT EXISTS( SELECT * FROM Person WHERE Surname=SurmanePersonIn)
  THEN SET msg = 'This Surname is absent';
    
  -- checks for present Book
	ELSEIF NOT EXISTS( SELECT * FROM Book WHERE BookName=BookNameIN)
		THEN SET msg = 'This Book is absent';
    
  -- checks if there are this combination already
	ELSEIF EXISTS( SELECT * FROM personbook 
		WHERE IDPerson = (SELECT IDPerson FROM Person WHERE Surname=SurmanePersonIn)
        AND IDBook = (SELECT IDBook FROM Book WHERE BookName=BookNameIN)
        )
        THEN SET msg = 'This Person already has this book';
	
  -- checks whether there is still such a book
	ELSEIF (SELECT Amount FROM Book WHERE BookName=BookNameIN ) 
    <= (SELECT COUNT(*) FROM personbook WHERE IDBook=(SELECT IDBook FROM Book WHERE BookName=BookNameIN) )
    THEN SET msg = 'There are no this Book already';
    
    -- makes insert
    ELSE 
		INSERT personbook (IDPerson, IDBook) 
        Value ( (SELECT IDPerson FROM Person WHERE Surname=SurmanePersonIn),
			     (SELECT IDBook FROM Book WHERE BookName=BookNameIN) );
		SET msg = 'OK';		 

	END IF;

	SELECT msg AS msg;

END //
DELIMITER ;
