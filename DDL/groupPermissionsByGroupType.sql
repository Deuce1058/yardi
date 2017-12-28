CREATE INDEX "DB2ADMIN"."GROUP_PERMISSIONS_BY_GROUP_TYPE"
	ON "DB2ADMIN"."GROUP_PERMISSIONS"
	("GP_GROUP_TYPE"		ASC)
	MINPCTUSED 0
	ALLOW REVERSE SCANS
	PAGE SPLIT SYMMETRIC
	COLLECT SAMPLED DETAILED STATISTICS
	COMPRESS NO;
