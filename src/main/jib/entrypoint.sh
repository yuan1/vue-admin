#!/bin/sh

echo "The application will start in ${ADMIN_SLEEP}s..." && sleep ${ADMIN_SLEEP}
exec java ${JAVA_OPTS} -noverify -XX:+AlwaysPreTouch -Djava.security.egd=file:/dev/./urandom -cp /app/resources/:/app/classes/:/app/libs/* "cn.javayuan.admin.AdminApplication"  "$@"
