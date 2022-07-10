@echo off

set version=0.0.18-SNAPSHOT

set group_id=com.wanshun

set artifact_id=userCenterCtrlApi

set rep_id=maven-snapshots

## set url=https://nexus.wsecar.cn/repository/maven-snapshots/

set jar_file=.\target\%artifact_id%-%version%.jar
set source_file=.\target\%artifact_id%-%version%-sources.jar
set pom_file=.\target\%artifact_id%-%version%.pom

call mvn deploy:deploy-file -DgroupId=%group_id% -DartifactId=%artifact_id% -Dversion=%version% -Dpackaging=jar -Dfile=%jar_file% -Durl=%url% -DrepositoryId=%rep_id%
::call mvn deploy:deploy-file -DgroupId=%group_id% -DartifactId=%artifact_id% -Dversion=%version% -Dpackaging=jar -Dclassifier=sources -Dfile=%source_file% -Durl=%url% -DrepositoryId=%rep_id%
::call mvn deploy:deploy-file -DgroupId=%group_id% -DartifactId=%artifact_id% -Dversion=%version% -Dpackaging=pom -Dfile=%pom_file% -Durl=%url% -DrepositoryId=%rep_id%