--------------------------------------------------------
--  File created - Monday-August-31-2015   
--------------------------------------------------------
--------------------------------------------------------
--  DDL for Sequence MATCH_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "MISTSTU1"."MATCH_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE NOORDER  NOCYCLE
--------------------------------------------------------
--  DDL for Sequence PLAYER_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "MISTSTU1"."PLAYER_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE NOORDER  NOCYCLE
--------------------------------------------------------
--  DDL for Sequence PR_USER_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "MISTSTU1"."PR_USER_SEQ"  MINVALUE 1 MAXVALUE 99999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE  NOORDER  NOCYCLE
--------------------------------------------------------
--  DDL for Sequence SEASON_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "MISTSTU1"."SEASON_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE NOORDER  NOCYCLE
--------------------------------------------------------
--  DDL for Sequence TOURNAMENT_SEQ
--------------------------------------------------------

   CREATE SEQUENCE  "MISTSTU1"."TOURNAMENT_SEQ"  MINVALUE 1 MAXVALUE 9999999999999999999999999999 INCREMENT BY 1 START WITH 1 NOCACHE NOORDER  NOCYCLE
--------------------------------------------------------
--  DDL for Table MATCH
--------------------------------------------------------

  CREATE TABLE "MISTSTU1"."MATCH" ("MATCH_ID" NUMBER(*,0), "PLAYER_1" NUMBER, "PLAYER_2" NUMBER, "WINNER" NUMBER, "TOURNAMENT_ID" NUMBER)
--------------------------------------------------------
--  DDL for Table PLAYER
--------------------------------------------------------

  CREATE TABLE "MISTSTU1"."PLAYER" ("PLAYER_ID" NUMBER(*,0), "NAME" VARCHAR2(64), "ELO_RATING" FLOAT(126), "INITIAL_ELO_SEED" FLOAT(126), "WIN_COUNT" NUMBER(*,0), "LOSS_COUNT" NUMBER(*,0), "SEASON_PARTICIPATING_IN" NUMBER)
--------------------------------------------------------
--  DDL for Table PR_USER
--------------------------------------------------------

  CREATE TABLE "MISTSTU1"."PR_USER" ("USER_ID" NUMBER(*,0), "USER_LOGIN" VARCHAR2(64), "DISPLAY_NAME" VARCHAR2(64))
--------------------------------------------------------
--  DDL for Table PR_USERS_SEASONS
--------------------------------------------------------

  CREATE TABLE "MISTSTU1"."PR_USERS_SEASONS" ("USER_ID" NUMBER, "SEASON_ID" NUMBER, "PERMISSION_LEVEL" VARCHAR2(64), "PERMISSION_KEY" VARCHAR2(64))
--------------------------------------------------------
--  DDL for Table SEASON
--------------------------------------------------------

  CREATE TABLE "MISTSTU1"."SEASON" ("SEASON_ID" NUMBER(*,0), "COMMUNITY_NAME" VARCHAR2(64), "CYCLE_FORMAT" VARCHAR2(64), "CYCLE_COUNT" VARCHAR2(64), "ELO_DEFAULT_SEED" NUMBER, "SYEAR" NUMBER)
--------------------------------------------------------
--  DDL for Table TOURNAMENT
--------------------------------------------------------

  CREATE TABLE "MISTSTU1"."TOURNAMENT" ("TOURNAMENT_ID" NUMBER(*,0), "SERIES_NAME" VARCHAR2(64), "SERIES_NUMBER" NUMBER(*,0), "SEASON_ID" VARCHAR2(20))
--------------------------------------------------------
--  DDL for Index MATCH_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MISTSTU1"."MATCH_PK" ON "MISTSTU1"."MATCH" ("MATCH_ID")
--------------------------------------------------------
--  DDL for Index PLAYER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MISTSTU1"."PLAYER_PK" ON "MISTSTU1"."PLAYER" ("PLAYER_ID")
--------------------------------------------------------
--  DDL for Index PLAYER_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "MISTSTU1"."PLAYER_UK1" ON "MISTSTU1"."PLAYER" ("NAME")
--------------------------------------------------------
--  DDL for Index PR_USER_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MISTSTU1"."PR_USER_PK" ON "MISTSTU1"."PR_USER" ("USER_ID")
--------------------------------------------------------
--  DDL for Index PR_USER_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "MISTSTU1"."PR_USER_UK1" ON "MISTSTU1"."PR_USER" ("USER_LOGIN")
--------------------------------------------------------
--  DDL for Index PR_USER_UK2
--------------------------------------------------------

  CREATE UNIQUE INDEX "MISTSTU1"."PR_USER_UK2" ON "MISTSTU1"."PR_USER" ("DISPLAY_NAME")
--------------------------------------------------------
--  DDL for Index TABLE1_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MISTSTU1"."TABLE1_PK" ON "MISTSTU1"."SEASON" ("SEASON_ID")
--------------------------------------------------------
--  DDL for Index TABLE1_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "MISTSTU1"."TABLE1_UK1" ON "MISTSTU1"."SEASON" ("COMMUNITY_NAME")
--------------------------------------------------------
--  DDL for Index TOURNAMENT_PK
--------------------------------------------------------

  CREATE UNIQUE INDEX "MISTSTU1"."TOURNAMENT_PK" ON "MISTSTU1"."TOURNAMENT" ("TOURNAMENT_ID")
--------------------------------------------------------
--  DDL for Index TOURNAMENT_UK1
--------------------------------------------------------

  CREATE UNIQUE INDEX "MISTSTU1"."TOURNAMENT_UK1" ON "MISTSTU1"."TOURNAMENT" ("SERIES_NAME")
--------------------------------------------------------
--  Constraints for Table MATCH
--------------------------------------------------------

  ALTER TABLE "MISTSTU1"."MATCH" MODIFY ("TOURNAMENT_ID" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."MATCH" ADD CONSTRAINT "MATCH_PK" PRIMARY KEY ("MATCH_ID") USING INDEX  ENABLE
  ALTER TABLE "MISTSTU1"."MATCH" MODIFY ("MATCH_ID" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."MATCH" MODIFY ("WINNER" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."MATCH" MODIFY ("PLAYER_2" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."MATCH" MODIFY ("PLAYER_1" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table PLAYER
--------------------------------------------------------

  ALTER TABLE "MISTSTU1"."PLAYER" ADD CONSTRAINT "PLAYER_UK1" UNIQUE ("NAME") USING INDEX  ENABLE
  ALTER TABLE "MISTSTU1"."PLAYER" ADD CONSTRAINT "PLAYER_PK" PRIMARY KEY ("PLAYER_ID") USING INDEX  ENABLE
  ALTER TABLE "MISTSTU1"."PLAYER" MODIFY ("NAME" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."PLAYER" MODIFY ("PLAYER_ID" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."PLAYER" MODIFY ("SEASON_PARTICIPATING_IN" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table PR_USER
--------------------------------------------------------

  ALTER TABLE "MISTSTU1"."PR_USER" ADD CONSTRAINT "PR_USER_U_USER_LOGIN" UNIQUE ("USER_LOGIN") USING INDEX  ENABLE
  ALTER TABLE "MISTSTU1"."PR_USER" ADD CONSTRAINT "PR_USER_U_DISPLAY_NAME" UNIQUE ("DISPLAY_NAME") USING INDEX  ENABLE
  ALTER TABLE "MISTSTU1"."PR_USER" ADD CONSTRAINT "PR_USER_PK" PRIMARY KEY ("USER_ID") USING INDEX  ENABLE
  ALTER TABLE "MISTSTU1"."PR_USER" MODIFY ("DISPLAY_NAME" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."PR_USER" MODIFY ("USER_LOGIN" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."PR_USER" MODIFY ("USER_ID" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table PR_USERS_SEASONS
--------------------------------------------------------

  ALTER TABLE "MISTSTU1"."PR_USERS_SEASONS" MODIFY ("SEASON_ID" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."PR_USERS_SEASONS" MODIFY ("USER_ID" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table SEASON
--------------------------------------------------------

  ALTER TABLE "MISTSTU1"."SEASON" MODIFY ("SYEAR" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."SEASON" ADD CONSTRAINT "SEASON_U_COMMUNITY_NAME" UNIQUE ("COMMUNITY_NAME") USING INDEX  ENABLE
  ALTER TABLE "MISTSTU1"."SEASON" ADD CONSTRAINT "SEASON_PK" PRIMARY KEY ("SEASON_ID") USING INDEX  ENABLE
  ALTER TABLE "MISTSTU1"."SEASON" MODIFY ("CYCLE_COUNT" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."SEASON" MODIFY ("CYCLE_FORMAT" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."SEASON" MODIFY ("COMMUNITY_NAME" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."SEASON" MODIFY ("SEASON_ID" NOT NULL ENABLE)
--------------------------------------------------------
--  Constraints for Table TOURNAMENT
--------------------------------------------------------

  ALTER TABLE "MISTSTU1"."TOURNAMENT" MODIFY ("SEASON_ID" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."TOURNAMENT" ADD CONSTRAINT "TOURNAMENT_UK1" UNIQUE ("SERIES_NAME") USING INDEX  ENABLE
  ALTER TABLE "MISTSTU1"."TOURNAMENT" ADD CONSTRAINT "TOURNAMENT_PK" PRIMARY KEY ("TOURNAMENT_ID") USING INDEX  ENABLE
  ALTER TABLE "MISTSTU1"."TOURNAMENT" MODIFY ("SERIES_NAME" NOT NULL ENABLE)
  ALTER TABLE "MISTSTU1"."TOURNAMENT" MODIFY ("TOURNAMENT_ID" NOT NULL ENABLE)
--------------------------------------------------------
--  Ref Constraints for Table MATCH
--------------------------------------------------------

  ALTER TABLE "MISTSTU1"."MATCH" ADD CONSTRAINT "MATCH_FK1" FOREIGN KEY ("PLAYER_1") REFERENCES "MISTSTU1"."PLAYER" ("PLAYER_ID") ENABLE
  ALTER TABLE "MISTSTU1"."MATCH" ADD CONSTRAINT "MATCH_FK2" FOREIGN KEY ("PLAYER_2") REFERENCES "MISTSTU1"."PLAYER" ("PLAYER_ID") ENABLE
  ALTER TABLE "MISTSTU1"."MATCH" ADD CONSTRAINT "MATCH_FK3" FOREIGN KEY ("TOURNAMENT_ID") REFERENCES "MISTSTU1"."TOURNAMENT" ("TOURNAMENT_ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table PLAYER
--------------------------------------------------------

  ALTER TABLE "MISTSTU1"."PLAYER" ADD CONSTRAINT "PLAYER_FK1" FOREIGN KEY ("PLAYER_ID") REFERENCES "MISTSTU1"."SEASON" ("SEASON_ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table PR_USERS_SEASONS
--------------------------------------------------------

  ALTER TABLE "MISTSTU1"."PR_USERS_SEASONS" ADD CONSTRAINT "PR_USERS_SEASONS_FK1" FOREIGN KEY ("USER_ID") REFERENCES "MISTSTU1"."PR_USER" ("USER_ID") ENABLE
  ALTER TABLE "MISTSTU1"."PR_USERS_SEASONS" ADD CONSTRAINT "PR_USERS_SEASONS_FK2" FOREIGN KEY ("SEASON_ID") REFERENCES "MISTSTU1"."SEASON" ("SEASON_ID") ENABLE
--------------------------------------------------------
--  Ref Constraints for Table TOURNAMENT
--------------------------------------------------------

  ALTER TABLE "MISTSTU1"."TOURNAMENT" ADD CONSTRAINT "TOURNAMENT_FK1" FOREIGN KEY ("TOURNAMENT_ID") REFERENCES "MISTSTU1"."SEASON" ("SEASON_ID") ENABLE
