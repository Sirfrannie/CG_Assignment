@echo off
REM Compile the Java file
javac GraphicsSwing.java
if errorlevel 1 (
    echo Compilation failed.
    pause
    exit /b
)

REM Run the compiled Java program
java GraphicsSwing 

pause

