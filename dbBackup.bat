@ECHO Starting Backup......
@ECHO OFF
@setlocal enableextensions

SET BIN=C:\Program Files\PostgreSQL\14\bin\
SET HOST=localhost
SET PORT=5432
SET DB=bds-db-design3
SET USER=postgres
SET PATH=C:\Users\spravce\Desktop\
FOR /F "TOKENS=1,2,3 DELIMS=/ " %%i IN ('DATE /T') DO SET d=%%i-%%j-%%k
FOR /F "TOKENS=1,2,3 DELIMS=: " %%i IN ('TIME /T') DO SET t=%%i%%j%%k

SET FILENAME=%DB%_%d%_%t%.sql
@ECHO ON
@ECHO Changing directory.....
cd %BIN%
@ECHO Backuping.....
pg_dump.exe --no-owner -h %HOST% -p %PORT% -U %USER% %DB% > %PATH%%FILENAME%
@ECHO Backup Completed



