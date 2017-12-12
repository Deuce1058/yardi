CREATE INDEX "DB2ADMIN"."USER_GROUPS_BY_USER_ID"
	ON "DB2ADMIN"."USER_GROUPS"
	("UG_USER_ID"		ASC)
	MINPCTUSED 0
	ALLOW REVERSE SCANS
	PAGE SPLIT SYMMETRIC
	COLLECT SAMPLED DETAILED STATISTICS
	COMPRESS NO;

