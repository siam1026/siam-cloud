###构建docker镜像
cd /root/project/siam-cloud/siam-monitor/

docker build -t siam-monitor:v1.0 .

###先杀死原程序进程
docker stop siam-monitor

docker rm siam-monitor

###运行镜像
docker run -d -p 9605:9605 -e JAVA_OPTS="-Xms128m -Xmx256m" -e JAVA_PROFILES_ACTIVE="-Dspring.profiles.active=test" --name siam-monitor siam-monitor:v1.0

###推送至阿里云容器镜像
spawn docker login --username=siam registry-vpc.cn-hangzhou.aliyuncs.com
expect "password:"
send "123456"

docker tag siam-monitor:v1.0 registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-monitor:v1.0

docker push registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-monitor:v1.0

if [ "$1" = "logs" ];then
  ###打印运行日志
  docker logs -f siam-monitor
  exit 1
fi