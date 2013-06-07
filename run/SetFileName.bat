echo off
rem french format
rem set mydate=%date:~6,4%%date:~3,2%%date:~0,2%
rem set mytime=%time:~0,2%%time:~3,2%

rem all format
set mydate=%date:~%
set mytime=%time:~%

set filename=%mydate%-%mytime%
set filename=%filename:/=-%
set filename=%filename::=-%

echo log-%filename%.txt

