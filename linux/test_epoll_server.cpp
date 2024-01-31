#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/socket.h>
#include <sys/epoll.h>
#include <arpa/inet.h>
#include <string.h>

#define MAX_EVENTS 1024

/***
 * 堵塞版本
 * */
int main() {
    // 创建一个socket
    int listen_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (listen_fd < 0) {
        perror("socket");
        exit(1);
    }

    // 设置端口复用
    int opt = 1;
    setsockopt(listen_fd, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));

    // 绑定端口
    struct sockaddr_in addr;
    addr.sin_family = AF_INET;
    addr.sin_port = htons(8888);
    addr.sin_addr.s_addr = INADDR_ANY;
    int ret = bind(listen_fd, (struct sockaddr *)&addr, sizeof(addr));
    if (ret < 0) {
        perror("bind");
        exit(1);
    }

    // 监听端口
    ret = listen(listen_fd, 5);
    if (ret < 0) {
        perror("listen");
        exit(1);
    }

    // 创建epoll事件表
    int epoll_fd = epoll_create(MAX_EVENTS);
    if (epoll_fd < 0) {
        perror("epoll_create");
        exit(1);
    }

    // 将监听socket加入epoll事件表
    struct epoll_event event;
    event.events = EPOLLIN;
    event.data.fd = listen_fd;
    ret = epoll_ctl(epoll_fd, EPOLL_CTL_ADD, listen_fd, &event);
    if (ret < 0) {
        perror("epoll_ctl");
        exit(1);
    }

    // 循环监听事件
    while (1) {
        // 等待事件发生
        struct epoll_event events[MAX_EVENTS];
        //该fd是否有事件到来，没有则将自己继续加到eventpoll的wq中注册自己，继续等待执通知
        int n = epoll_wait(epoll_fd, events, MAX_EVENTS, -1);
        printf("wait成功...\n");
        if (n < 0) {
            perror("epoll_wait");
            exit(1);
        }

        // 处理事件
        for (int i = 0; i < n; i++) {
            int fd = events[i].data.fd;
            if (fd == listen_fd) {
                // 有新的连接到来
                struct sockaddr_in client_addr;
                socklen_t client_len = sizeof(client_addr);
                int conn_fd = accept(listen_fd, (struct sockaddr *)&client_addr, &client_len);
                if (conn_fd < 0) {
                    perror("accept");
                    exit(1);
                }

                printf("accect成功...\n");

                // 将新连接的socket加入epoll事件表
                event.events = EPOLLIN | EPOLLET;
                event.data.fd = conn_fd;
                ret = epoll_ctl(epoll_fd, EPOLL_CTL_ADD, conn_fd, &event);
                if (ret < 0) {
                    perror("epoll_ctl");
                    exit(1);
                }

                printf("accect成功...\n");
            } else {
                // 有数据到来
                char buf[1024];
                int len = recv(fd, buf, sizeof(buf), 0);
                if (len < 0) {
                    perror("recv");
                    exit(1);
                } else if (len == 0) {
                    // 连接关闭
                    epoll_ctl(epoll_fd, EPOLL_CTL_DEL, fd, NULL);
                    printf("关闭...\n");
                    close(fd);
                } else {
                    // 发送数据
                    printf("接收到:%s\n", buf);
                    send(fd, buf, len, 0);
                }
            }
        }
    }

    return 0;
}