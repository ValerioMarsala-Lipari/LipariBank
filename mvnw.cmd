@REM ----------------------------------------------------------------------------
@REM Licensed to the Apache Software Foundation (ASF) under one
@REM or more contributor license agreements.  See the NOTICE file
@REM distributed with this work for additional information
@REM regarding copyright ownership.  The ASF licenses this file
@REM to you under the Apache License, Version 2.0 (the
@REM "License"); you may not use this file except in compliance
@REM with the License.  You may obtain a copy of the License at
@REM
@REM    https://www.apache.org/licenses/LICENSE-2.0
@REM
@REM Unless required by applicable law or agreed to in writing,
@REM software distributed under the License is distributed on an
@REM "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
@REM KIND, either express or implied.  See the License for the
@REM specific language governing permissions and limitations
@REM under the License.
@REM ----------------------------------------------------------------------------

@IF "%__MVNW_ARG0_NAME__%"=="" (SET "BASE_DIR=%~dp0")

@SET MAVEN_WRAPPER_PROPERTIES=%BASE_DIR%.mvn\wrapper\maven-wrapper.properties

@FOR /F "usebackq tokens=2 delims==" %%A IN (`findstr /i "distributionUrl" "%MAVEN_WRAPPER_PROPERTIES%"`) DO (
  SET DISTRIBUTION_URL=%%A
)

@SET MAVEN_USER_HOME=%USERPROFILE%\.m2
@SET MAVEN_WRAPPER_HOME=%MAVEN_USER_HOME%\wrapper

@FOR %%A IN ("%DISTRIBUTION_URL%") DO (SET DISTRIBUTION_NAME=%%~nxA)
@SET DISTRIBUTION_DIR=%MAVEN_WRAPPER_HOME%\dists\%DISTRIBUTION_NAME:-bin.zip=%

@IF EXIST "%DISTRIBUTION_DIR%\bin\mvn.cmd" GOTO run_maven

@ECHO Downloading Maven...
@IF NOT EXIST "%DISTRIBUTION_DIR%" MKDIR "%DISTRIBUTION_DIR%"
@powershell -Command "Invoke-WebRequest -Uri '%DISTRIBUTION_URL%' -OutFile '%DISTRIBUTION_DIR%\download.zip'"
@powershell -Command "Expand-Archive -Path '%DISTRIBUTION_DIR%\download.zip' -DestinationPath '%DISTRIBUTION_DIR%' -Force"
@DEL "%DISTRIBUTION_DIR%\download.zip"

:run_maven
@FOR /D %%D IN ("%DISTRIBUTION_DIR%\apache-maven-*") DO (SET "MAVEN_HOME=%%D")
@"%MAVEN_HOME%\bin\mvn.cmd" %*
