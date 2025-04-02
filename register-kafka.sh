#!/bin/sh

# 配置參數
BROKER_NAME="kafka-1"
BROKER_PORT=9093
SERVICE_NAME="kafka-broker"
CHECK_INTERVAL="10s"
CHECK_TIMEOUT="5s"

echo "Starting Kafka registration service..."

# 等待 Consul 完全啟動並運行
echo "Waiting for Consul to be available..."
until curl -s http://consul:8500/v1/status/leader | grep -q "."; do
  echo "Consul not ready yet, waiting..."
  sleep 5
done
echo "Consul is available!"

# 等待 Kafka 可訪問 (使用 nc 檢查端口)
echo "Waiting for Kafka to be accessible..."
apk add --no-cache netcat-openbsd
until nc -z kafka 9093; do
  echo "Kafka not accessible yet, waiting..."
  sleep 5
done
echo "Kafka is accessible!"

# 註冊 Kafka 服務到 Consul
echo "Registering Kafka service to Consul..."
curl -X PUT -H "Content-Type: application/json" -d "{
  \"ID\": \"${BROKER_NAME}\",
  \"Name\": \"${SERVICE_NAME}\",
  \"Address\": \"kafka\",
  \"Port\": ${BROKER_PORT},
  \"Tags\": [\"kafka\"],
  \"Check\": {
    \"Name\": \"Kafka Port Check\",
    \"TCP\": \"kafka:${BROKER_PORT}\",
    \"Interval\": \"${CHECK_INTERVAL}\",
    \"Timeout\": \"${CHECK_TIMEOUT}\",
    \"DeregisterCriticalServiceAfter\": \"1m\"
  }
}" http://consul:8500/v1/agent/service/register

if [ $? -eq 0 ]; then
  echo "Successfully registered Kafka service with Consul!"
else
  echo "Failed to register Kafka service with Consul!"
  exit 1
fi

# 持續監控 Kafka 並保持註冊狀態
echo "Starting Kafka health check monitoring..."
while true; do
  # 檢查 Kafka 連接狀態
  if nc -z kafka 9093; then
    echo "$(date): Kafka is up and running"

    # 確認服務還在 Consul 中註冊
    SERVICE_CHECK=$(curl -s http://consul:8500/v1/agent/service/${BROKER_NAME})
    if [ -z "$SERVICE_CHECK" ] || echo "$SERVICE_CHECK" | grep -q "service not found"; then
      echo "Service registration lost. Re-registering..."
      curl -X PUT -H "Content-Type: application/json" -d "{
        \"ID\": \"${BROKER_NAME}\",
        \"Name\": \"${SERVICE_NAME}\",
        \"Address\": \"kafka\",
        \"Port\": ${BROKER_PORT},
        \"Tags\": [\"kafka\"],
        \"Check\": {
          \"Name\": \"Kafka Port Check\",
          \"TCP\": \"kafka:${BROKER_PORT}\",
          \"Interval\": \"${CHECK_INTERVAL}\",
          \"Timeout\": \"${CHECK_TIMEOUT}\",
          \"DeregisterCriticalServiceAfter\": \"1m\"
        }
      }" http://consul:8500/v1/agent/service/register
    fi
  else
    echo "$(date): Kafka is not responding!"
  fi

  # 每隔30秒檢查一次
  sleep 30
done
