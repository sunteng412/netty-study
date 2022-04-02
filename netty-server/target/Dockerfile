FROM java:8u111-jdk
MAINTAINER 龙川
USER root
RUN echo '开始....'
ADD netty-server-1.0-SNAPSHOT.jar /

RUN rm /etc/apt/sources.list.d/jessie-backports.list

#apt-get源 使用163的源,忽略掉失效的源
RUN echo "deb http://mirrors.163.com/debian/ jessie main non-free contrib" >/etc/apt/sources.list && \
    echo "deb-src http://mirrors.163.com/debian/ jessie main non-free contrib" >>/etc/apt/sources.list
RUN apt-get clean;
RUN apt-get update;

RUN apt-get install -y strace
RUN apt-get install -y netcat

EXPOSE 9999
CMD ["java", "-jar", "/netty-server-1.0-SNAPSHOT.jar","com.netty.echo.EchoServer"]