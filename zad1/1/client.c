#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 

#define IN_BUFSIZE 1024
#define REC_BUFSIZE 1024

static int sanitizeInput(char* input, char* sanitized);

static int parameterCountGood(int length);

static int sanitizeInput(char* input, char* sanitized) {
	input[IN_BUFSIZE]=0;
    if (atoi(input) < 0) {
        return -1;
    }
    memcpy(sanitized, input, IN_BUFSIZE-1);
    return 0;
}

static int parameterCountGood(int length) {
	if (length < 2) {
        return -1;
    } else {
        return 0;
    }
}

int main (int argc, char* args[]) {
   	struct sockaddr_in serv_addr;
	int fd, len;
	if (parameterCountGood(argc) < 0) {
		fprintf(stderr, "Usage: client <number>\n");
        return 0;
	}
	char sendline [IN_BUFSIZE];
    if (sanitizeInput(args[1], sendline) < 0) {
		fprintf(stderr, "Invalid number. Please enter a positive integer\n");
        return 0;
    }

	char recvline[REC_BUFSIZE];

	fd = socket(AF_INET, SOCK_STREAM, 0);

	bzero((char*)&serv_addr, sizeof(serv_addr));

	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr= inet_addr("192.168.88.1");
	serv_addr.sin_port = htons(atoi("5000"));

    fprintf(stderr, "Connecting at port:%d\n", serv_addr.sin_port);
	connect(fd, (struct sockaddr*) &serv_addr, sizeof(serv_addr));
    fprintf(stderr, "Connection established\n");

    fprintf(stderr, "Sending %zx byte message: %s\n", strlen(sendline) , sendline);
	len = send(fd, sendline, strlen(sendline), 0);
    fprintf(stderr, "Sent %d bytes\n", len);

    fprintf(stderr, "Waiting for response\n");
	len = recv(fd, recvline, REC_BUFSIZE, 0);
// Unsafe buffer
    fprintf(stderr,"Received %d byte message: %s\n", len, recvline);
	close(fd);
}


