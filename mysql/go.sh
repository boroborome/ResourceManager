#!/usr/bin/env bash
cd `dirname $0`
docker run \
    -d \
    -p 3308:3306 \
    --name local-resmgr \
    -e MYSQL_ROOT_PASSWORD=root\
    -e MYSQL_DATABASE=resmgr\
    -e character-set-server=utf8mb4\
    -e collation-server=utf8mb4_unicode_ci\
    --mount type=bind,source=`pwd`/conf/mysqld.cnf,target=/etc/mysql/mysql.conf.d/mysqld.cnf \
    mysql:5.7
