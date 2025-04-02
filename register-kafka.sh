#!/bin/bash
BROKER_NAME=$1
BROKER_PORT=$2

BROKER_NAME="kafka-1"
BROKER_PORT=9093

sleep 5
until curl -s http://consul:8500/v1/status/leader | grep -q "."; do
  echo "Waiting for Consul..."
  sleep 2
done

curl -X PUT -H "Content-Type: application/json" -d "{\"ID\": \"${BROKER_NAME}\", \"Name\": \"kafka-broker\", \"Address\": \"kafka\", \"Port\": ${BROKER_PORT}, \"Tags\": [\"kafka\"], \"Check\": {\"TCP\": \"kafka:${BROKER_PORT}\", \"Interval\": \"10s\"}}" http://consul:8500/v1/agent/service/register

echo "Registered ${BROKER_NAME}:${BROKER_PORT} to Consul"