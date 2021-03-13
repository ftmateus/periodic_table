@echo off

call mvn clean package assembly:single -q

robocopy target . periodic_table_server-1.0-jar-with-dependencies.jar /mov > nul

del periodic_table_server.jar

ren periodic_table_server-1.0-jar-with-dependencies.jar periodic_table_server.jar