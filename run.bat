@echo off

cd /d %~dp0
csc /out:Network\Network.exe Network\Network.cs
start Network\Network.exe

cd /d %~dp0
cd Middleware1
echo compiling:
javac -encoding UTF-8 Middleware1.java
echo run:
start java Middleware1

cd /d %~dp0
cd Middleware2
echo compiling:
javac -encoding UTF-8 Middleware2.java
echo run:
start java Middleware2

cd /d %~dp0
cd Middleware3
echo compiling:
javac -encoding UTF-8 Middleware3.java
echo run:
start java Middleware3

cd /d %~dp0
cd Middleware4
echo compiling:
javac -encoding UTF-8 Middleware4.java
echo run:
start java Middleware4

cd /d %~dp0
cd Middleware5
echo compiling:
javac -encoding UTF-8 Middleware5.java
echo run:
start java Middleware5

pause