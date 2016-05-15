#! /bin/sh -e
# $1 profile, wave3_dev

if [ -n "$1" ]; then
    profile=$1
else
    if [ -f "profile" ]; then
    	profile=$(more profile)
    fi
fi


if [ ! -n "$profile" ]; then
    profile=wave3_uat
fi

java -jar ../build/libs/com.happy3w.ideamgr-1.0-SNAPSHOT.war --spring.profiles.active=$profile 1>> log.log 2>&1&
echo $! > pidFile
