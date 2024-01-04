#include <sys/select.h>
#include <string.h>
#define BUF_SIZE 4096

int main(void){

    fd_set all_fd_set;
    int listen_fd;
    struct sockaddr_in serv_addr;

    listen_fd = socket(AF_INET, SOCK_STREAM, 0);
    printf("listen_fd = %d\n", listen_fd);

    FD_ZERO(&all_fd_set);
    //对输入感兴趣
    FD_SET(STDIN_FILENO, &all_fd_set);
    //对listen_fd感兴趣
    FD_SET(listen_fd,&all_fd_set)

    bzero(&serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = htonl(INADDR_ANY);
    serv_addr.sin_port = htons(9999);

    bind(listen_fd, (struct sockaddr*)&serv_addr, sizeof(serv_addr));
    listen(listen_fd, 128);


    char buffer[BUF_SIZE];

    int rc;
    fd_set rfds;

    while(1){
        rfds = all_fd_set;
        //rc = select(listen_fd+1, &rfds, NULL,  NULL, NULL);
        //不知道哪些事件发生，需要遍历感知
        rc = select(1024, &rfds, NULL, NULL, NULL);

        if(rc <= 0){
            perror("select");
        }

         if (FD_ISSET(listen_fd, &rfds)){
               printf("listen_fd = %d\n", listen_fd);
               struct sockaddr_in client_addr;
               socket_t client_len = sizeof(client_addr);

                int client_fd;

                if((client_fd = accept(listen_fd, NULL, NULL)) > 0){
                    printf("client_fd = %d\n", client_fd);
                    close(client_fd);
                }

        }

        if(FD_ISSET(STDIN_FILENO, &rfds)){
            int n;
            if((n = read(STDIN_FILENO, buffer, BUF_SIZE)) > 0){
                buffer[n] = 0;
                printf("stdin:%s\n", buffer);
            }
        }

    }


}