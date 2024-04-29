#!/bin/sh

# Load environment variables from .env file
export $(grep -v '^#' .env | xargs -d '\n')

docker compose -f compose.yml -f compose.dev.yml up -d --build