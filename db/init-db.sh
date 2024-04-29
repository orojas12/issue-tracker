#!/bin/bash

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" \
    -f /docker-entrypoint-initdb.d/scripts/schema.sql;

if [ $ENVIRONMENT == "dev" ]; then
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" \
        -f /docker-entrypoint-initdb.d/scripts/data.sql;
fi

if [ $ENVIRONMENT == "dev"]; then
    psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" \
        -f /docker-entrypoint-initdb.d/scripts/issue.sql;
fi
