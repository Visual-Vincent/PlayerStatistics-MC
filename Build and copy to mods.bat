@echo off
set MCVERSION=1.18.1
set MODVERSION=1.1
gradlew build && copy "build\libs\PlayerStatisticsList-%MCVERSION%-%MODVERSION%.jar" "%AppData%\.minecraft\mods\PlayerStatisticsList-%MCVERSION%-%MODVERSION%.jar"
pause