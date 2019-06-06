#!/bin/sh

echo "The application will start in ${ADMIN_SLEEP}s..." && sleep ${ADMIN_SLEEP}
exec java ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar "${HOME}/app.war" "$@"
