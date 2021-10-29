@echo off

csc /out:Network\Network.exe Network\Network.cs
start Network\Network.exe

echo compiling:
javac -encoding UTF-8 Middleware1\Middleware1.java
echo run:
start java Middleware1

echo compiling:
javac -encoding UTF-8 Middleware2\Middleware2.java
echo run:
start java Middleware2

echo compiling:
javac -encoding UTF-8 Middleware3\Middleware3.java
echo run:
start java Middleware3

echo compiling:
javac -encoding UTF-8 Middleware4\Middleware4.java
echo run:
start java Middleware4

echo compiling:
javac -encoding UTF-8 Middleware5\Middleware5.java
echo run:
start java Middleware5