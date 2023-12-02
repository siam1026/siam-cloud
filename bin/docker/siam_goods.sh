###构建docker镜像
cd /root/project/siam-cloud/siam-goods/goods-provider

docker build -t siam-goods:v1.0 .

###先杀死原程序进程
docker stop siam-goods

docker rm siam-goods

###运行镜像
# docker run -d -p 9603:9603 -p 9613:9041 --name siam-goods -v /home/dockerdata/oap/volume/siam-goods/apache-skywalking-apm-bin/:/opt/apache-skywalking-apm-bin/ siam-goods:v1.0
docker run -d -p 9603:9603 --name siam-goods siam-goods:v1.0

###推送至阿里云容器镜像
spawn docker login --username=siam registry-vpc.cn-hangzhou.aliyuncs.com
expect "password:"
send "123456"

docker tag siam-goods:v1.0 registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-goods:v1.0

docker push registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-goods:v1.0

if [ "$1" = "logs" ];then
  ###打印运行日志
  docker logs -f siam-goods
  exit 1
fi