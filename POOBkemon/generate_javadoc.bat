@echo off
echo Generating Javadoc documentation...
echo.

REM Set the path to the JDK's javadoc executable
set JAVADOC_PATH=C:\Users\jksan\.jdks\openjdk-24.0.1\bin\javadoc.exe

REM Set the source directory and output directory
set SOURCE_DIR=src
set DOC_DIR=doc

REM Create the doc directory if it doesn't exist
if not exist %DOC_DIR% mkdir %DOC_DIR%

REM Run the javadoc command with appropriate options
"%JAVADOC_PATH%" -d %DOC_DIR% -sourcepath %SOURCE_DIR% -subpackages domain:presentation -encoding UTF-8 -charset UTF-8 -docencoding UTF-8 -protected -author -version

echo.
if %ERRORLEVEL% EQU 0 (
    echo Javadoc generation completed successfully.
    echo Documentation is available in the %DOC_DIR% directory.
) else (
    echo Javadoc generation failed with error code %ERRORLEVEL%.
)

pause