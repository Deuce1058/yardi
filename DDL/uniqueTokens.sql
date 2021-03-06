CREATE TABLE "DB2ADMIN"."UNIQUE_TOKENS" (
		"UP1_USER_NAME" VARCHAR(15 OCTETS) NOT NULL, 
		"UP1_TOKEN" CHAR(50 OCTETS) NOT NULL, 
		"UP1_DATE_ADDED" DATE NOT NULL, 
		"UP1_RRN" BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY ( START WITH 1 INCREMENT BY 1 MINVALUE -9223372036854775808 MAXVALUE 9223372036854775807 NO CYCLE CACHE 20 NO ORDER )
	)
	ORGANIZE BY ROW
	DATA CAPTURE NONE 
	IN "USERSPACE1"
	COMPRESS NO;

ALTER TABLE "DB2ADMIN"."UNIQUE_TOKENS" ADD CONSTRAINT "SQL170902145055240" PRIMARY KEY
	("UP1_RRN");

