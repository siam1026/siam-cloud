###构建docker镜像
cd /root/project/siam-cloud/siam-zuul/

docker build -t siam-zuul:v1.0 .

###先杀死原程序进程
docker stop siam-zuul

docker rm siam-zuul

###运行镜像
# docker run -d -p 9601:9601 -p 9611:9041 -e JAVA_OPTS="-Xms128m -Xmx256m" -e JAVA_PROFILES_ACTIVE="-Dspring.profiles.active=test" --name siam-zuul -v /home/dockerdata/oap/volume/siam-zuul/apache-skywalking-apm-bin/:/opt/apache-skywalking-apm-bin/ siam-zuul:v1.0
docker run -d -p 9601:9601 --name siam-zuul siam-zuul:v1.0

###推送至阿里云容器镜像
spawn docker login --username=siam registry-vpc.cn-hangzhou.aliyuncs.com
expect "password:"
send "123456"

docker tag siam-zuul:v1.0 registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-zuul:v1.0

docker push registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-zuul:v1.0

if [ "$1" = "logs" ];then
  ###打印运行日志
  docker logs -f siam-zuul
  exit 1
fi