###运行jar包
nohup java -jar -Dspring.profiles.active=test /root/project/siam-cloud/jars/siam-user.jar > /root/project/siam-cloud/logs/user/web_info.log 2>&1 &
