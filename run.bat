@echo off
title Garbage Reporting System Launcher (DEBUG MODE)

:: =================================================================
:: 1. CONFIGURATION
:: =================================================================
setlocal
echo DEBUG: Starting script and setting variables...

:: --- Project Paths ---
set "PROJECT_DIR=%~dp0"
set "SRC_DIR=%PROJECT_DIR%src"
set "LIB_DIR=%PROJECT_DIR%lib"
set "DB_DIR=%PROJECT_DIR%database"
set "OUT_DIR=%PROJECT_DIR%bin"
set "MAIN_CLASS=ui.Main"
set "JDBC_JAR=%LIB_DIR%\sqlite-jdbc-3.36.0.3.jar"
set "LOCAL_JSON_FILE=%DB_DIR%\data.json"
set "EXPORTED_JSON_FILE=%DB_DIR%\exported_data.json"
set "CLASSPATH=%OUT_DIR%;%JDBC_JAR%"

:: Create database directory if it doesn't exist
if not exist "%DB_DIR%" mkdir "%DB_DIR%"

:: =================================================================
:: 2. SYNC FROM MONGODB (DOWNLOAD - OPTIONAL)
:: =================================================================
echo.
echo [1/5] Attempting to sync data from MongoDB server to local data.json...
mongoexport --uri="mongodb+srv://garbageadmin:Garbage123@garbegeway.jz6axbl.mongodb.net/GarbageSO?retryWrites=true&w=majority&appName=GarbegeWAy" --collection="reports" --out="%LOCAL_JSON_FILE%"

if %errorlevel% neq 0 (
    echo WARNING: MongoDB download failed. Starting in OFFLINE mode.
    echo Using existing local data.json if available.
) else (
    echo Sync from MongoDB successful. Data saved to: %LOCAL_JSON_FILE%
)




if not exist "bin1" mkdir bin1
if not exist "bin2" mkdir bin2

echo Compiling SQLiteToJsonExporter to bin1...
javac -cp ".;lib/json-20231013.jar;lib/sqlite-jdbc-3.36.0.3.jar" -d bin1 SQLiteToJsonExporter.java

echo Compiling JsonToSQLiteImporter to bin2...
javac -cp ".;lib/json-20231013.jar;lib/sqlite-jdbc-3.36.0.3.jar" -d bin2 JsonToSQLiteImporter.java

echo.
echo Starting JSON to SQLite import (using bin2)...
java -cp "bin2;lib/json-20231013.jar;lib/sqlite-jdbc-3.36.0.3.jar" JsonToSQLiteImporter data.json garbage_reporting.db




:: =================================================================
:: 3. COMPILE THE JAVA PROJECT (MANDATORY)
:: =================================================================
echo.
echo [2/5] Compiling Java source files...
if not exist "%OUT_DIR%" mkdir "%OUT_DIR%"

dir /s /b "%SRC_DIR%\*.java" > sources.txt
echo DEBUG: About to run javac...
javac -d "%OUT_DIR%" -cp "%CLASSPATH%" @sources.txt

if %errorlevel% neq 0 (
    echo.
    echo **********************************
    echo * ERROR: Compilation failed!     *
    echo * The program cannot be started. *
    echo **********************************
    del sources.txt
    pause
    exit /b
)
del sources.txt
echo DEBUG: Compilation check passed.
echo Compilation successful.

:: =================================================================
:: 4. RUN THE JAVA APPLICATION
:: =================================================================
echo.
echo [3/5] Preparing to start the Garbage Reporting System...
echo.
echo ----- DEBUG INFO -----
echo Classpath is: %CLASSPATH%
echo Main class is: %MAIN_CLASS%
echo Full command is: java -cp "%CLASSPATH%" %MAIN_CLASS%
echo ----------------------
echo.
echo The script is now paused. If you see this, compilation was successful.
echo Press any key to attempt to launch the Java application...
pause

java -cp "%CLASSPATH%" %MAIN_CLASS%
echo.
echo [4/5] Application closed.






echo.
echo Starting SQLite to JSON export (using bin1)...
java -cp "bin1;lib/json-20231013.jar;lib/sqlite-jdbc-3.36.0.3.jar" SQLiteToJsonExporter garbage_reporting.db exported_data.json

echo.


:: =================================================================
:: 5. SYNC TO MONGODB (UPLOAD - OPTIONAL)
:: =================================================================
echo.
echo [5/5] Attempting to sync local changes back to MongoDB server...
mongoimport --uri="mongodb+srv://garbageadmin:Garbage123@garbegeway.jz6axbl.mongodb.net/GarbageSO?retryWrites=true&w=majority&appName=GarbegeWAy" --collection="reports" --file="%EXPORTED_JSON_FILE%" --drop

if %errorlevel% neq 0 (
    echo WARNING: Could not upload data to MongoDB.
) else (
    echo Sync to MongoDB successful.
)

endlocal
echo.
echo Script finished.
pause