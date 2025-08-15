###构建docker镜像
cd /root/project/autoboxing-repository/siam-cloud/siam-eureka/

docker build -t siam-eureka-test:v1.0 .

###先杀死原程序进程
docker stop siam-eureka-test

docker rm siam-eureka-test

###运行镜像，带skywalking
#docker run -d -p 9650:9600 -p 9660:9041 -e JAVA_OPTS="-Xms128m -Xmx256m" -e JAVA_PROFILES_ACTIVE="-Dspring.profiles.active=test" --name siam-eureka-test -v /home/dockerdata/oap/volume/siam-eureka/apache-skywalking-apm-bin/:/opt/apache-skywalking-apm-bin/ siam-eureka-test:v1.0
###运行镜像
docker run -d -p 9650:9600 -e JAVA_OPTS="-Xms128m -Xmx256m" -e JAVA_PROFILES_ACTIVE="-Dspring.profiles.active=test" --name siam-eureka-test siam-eureka-test:v1.0

###推送至阿里云容器镜像
#spawn docker login --username=siam registry-vpc.cn-hangzhou.aliyuncs.com
#expect "password:"
#send "123456"
# docker tag siam-eureka-test:v1.0 registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-eureka-test:v1.0
# docker push registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-eureka-test:v1.0

if [ "$1" = "logs" ];then
  ###打印运行日志
  docker logs -f siam-eureka-test
  exit 1
fi
