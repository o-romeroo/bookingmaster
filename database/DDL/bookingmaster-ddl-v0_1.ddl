CREATE TABLE bmdb.users (
	user_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	email varchar(254) NOT NULL,
	password varchar(64),
	first_name varchar(32) NOT NULL,
	last_name varchar(32) NOT NULL,
	document char(11) NOT NULL
)
ENGINE=InnoDB;

CREATE TABLE bmdb.companions (
	companion_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id int NOT NULL REFERENCES users(user_id),
	first_name varchar(32) NOT NULL,
	last_name varchar(32) NOT NULL,
	document char(11) NOT NULL
)
ENGINE=InnoDB;

CREATE TABLE bmdb.hotels (
	hotel_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	hotel_name varchar(64) NOT NULL,
	hotel_description tinytext,
	hotel_address varchar(128),
	night_price_per_guest double NOT NULL
)
ENGINE=InnoDB;

CREATE TABLE bmdb.bookings (
	booking_id int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	user_id int NOT NULL REFERENCES users(user_id),
	hotel_id int NOT NULL REFERENCES hotels(hotel_id),
	order_date date NOT NULL,
	order_code varchar(12) NOT NULL,
	check_in_date date NOT NULL,
	check_out_date date,
	price double NOT NULL
)
ENGINE=InnoDB;

CREATE TABLE bmdb.booking_companions (
	booking_id int NOT NULL REFERENCES bookings(booking_id),
	companion_id int NOT NULL REFERENCES companions(companion_id),
	PRIMARY KEY (booking_id, companion_id)
)
ENGINE=InnoDB;