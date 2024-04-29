#!/bin/sh

# Load environment variables from .env file
export $(grep -v '^#' .env | xargs -d '\n')

docker compose -f compose.yml -f compose.test.yml up -d --build
docker compose logs -f backend
docker compose down