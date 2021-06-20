CREATE TABLE gameserver_utils.discord (
	account_id int4 NOT NULL,
	discord_id varchar NOT NULL,
	CONSTRAINT discord_pkey PRIMARY KEY (account_id),
	CONSTRAINT discord_discord_id_unique UNIQUE (discord_id)
);