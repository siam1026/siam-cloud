###构建docker镜像
cd /root/project/siam-cloud/siam-user/user-provider

docker build -t siam-user:v1.0 .

###先杀死原程序进程
docker stop siam-user

docker rm siam-user

###运行镜像
# docker run -d -p 9602:9602 -p 9612:9041 --name siam-user -v /home/dockerdata/oap/volume/siam-user/apache-skywalking-apm-bin/:/opt/apache-skywalking-apm-bin/ siam-user:v1.0
docker run -d -p 9602:9602 --name siam-user siam-user:v1.0

###推送至阿里云容器镜像
spawn docker login --username=siam registry-vpc.cn-hangzhou.aliyuncs.com
expect "password:"
send "123456"

docker tag siam-user:v1.0 registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-user:v1.0

docker push registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-user:v1.0

if [ "$1" = "logs" ];then
  ###打印运行日志
  docker logs -f siam-user
  exit 1
fi