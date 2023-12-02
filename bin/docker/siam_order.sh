###构建docker镜像
cd /root/project/siam-cloud/siam-order/order-provider

docker build -t siam-order:v1.0 .

###先杀死原程序进程
docker stop siam-order

docker rm siam-order

###运行镜像
# docker run -d -p 9604:9604 -p 9614:9041 --name siam-order -v /home/dockerdata/oap/volume/siam-order/apache-skywalking-apm-bin/:/opt/apache-skywalking-apm-bin/ siam-order:v1.0
docker run -d -p 9604:9604 --name siam-order siam-order:v1.0

###推送至阿里云容器镜像
spawn docker login --username=siam registry-vpc.cn-hangzhou.aliyuncs.com
expect "password:"
send "123456"

docker tag siam-order:v1.0 registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-order:v1.0

docker push registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-order:v1.0

if [ "$1" = "logs" ];then
  ###打印运行日志
  docker logs -f siam-order
  exit 1
fi