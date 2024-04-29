#!/bin/sh

# Load environment variables from .env file
export $(grep -v '^#' .env | xargs -d '\n')

docker compose up -d --build