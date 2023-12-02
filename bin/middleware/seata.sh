# 启动seata
docker run -d --restart always  --name  seata-server -p 8091:8091  -v /home/dockerdata/seata:/seata-server -e SEATA_IP=127.0.0.1 -e SEATA_PORT=8091 seataio/seata-server:1.3.0