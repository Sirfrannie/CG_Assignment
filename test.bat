@echo off
REM Compile the Java file
javac Test.java
if errorlevel 1 (
    echo Compilation failed.
    pause
    exit /b
)

REM Run the compiled Java program
java Test 

pause