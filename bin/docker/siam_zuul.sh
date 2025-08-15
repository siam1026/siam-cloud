#自动部署画石商城服务端项目-正式环境 9010~9030
#此处是parent父工程集中打包操作执行后，单独的子模块项目部署

#调用parent父工程打包脚本
#. ./siam_parent.sh
# sh ../siam_parent.sh

###从target目录下复制一份jar包到运行目录
# cp /root/project/autoboxing-repository/siam-cloud/siam-zuul/target/siam-zuul-1.0-SNAPSHOT.jar /root/project/siam-cloud-test/jars

###构建docker镜像
cd /root/project/autoboxing-repository/siam-cloud/siam-zuul/

docker build -t siam-zuul-test:v1.0 .

###先杀死原程序进程
docker stop siam-zuul-test

docker rm siam-zuul-test

###运行镜像，带skywalking
#docker run -d -p 9651:9601 -p 9661:9041 -e JAVA_OPTS="-Xms128m -Xmx256m" -e JAVA_PROFILES_ACTIVE="-Dspring.profiles.active=test" --name siam-zuul-test -v /home/dockerdata/oap/volume/siam-zuul/apache-skywalking-apm-bin/:/opt/apache-skywalking-apm-bin/ siam-zuul-test:v1.0
###运行镜像
docker run -d -p 9651:9601 -e JAVA_OPTS="-Xms128m -Xmx256m" -e JAVA_PROFILES_ACTIVE="-Dspring.profiles.active=test" --name siam-zuul-test siam-zuul-test:v1.0

###推送至阿里云容器镜像
#spawn docker login --username=siam registry-vpc.cn-hangzhou.aliyuncs.com
#expect "password:"
#send "123456"
# docker tag siam-eureka-test:v1.0 registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-eureka-test:v1.0
# docker push registry-vpc.cn-hangzhou.aliyuncs.com/siam-cloud/siam-eureka-test:v1.0

if [ "$1" = "logs" ];then
  ###打印运行日志
  docker logs -f siam-zuul-test
  exit 1
fi
