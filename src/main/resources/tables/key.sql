CREATE TABLE gameserver_utils.key (
     id serial NOT NULL,
     serial_number varchar NOT NULL,
     account_id int4 NULL,
     CONSTRAINT key_pkey PRIMARY KEY (id),
     CONSTRAINT key_serial_number_key UNIQUE (serial_number)
);