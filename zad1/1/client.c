#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 
#include <limits.h>
#include <errno.h>
#include <math.h>

#define IN_BUFSIZE 1024
#define REC_BUFSIZE 1

#define MAX_QUERY_SIZE 8

#define SEPARATOR 10

static char separator = SEPARATOR;

static int querySize;

static int parameterCountGood(int length);

static int makeQuery(char* input, char* query);

static int parameterCountGood(int length) {
	if (length < 2) {
		return -1;
	} else {
		return 0;
	}
}

static int makeQuery(char* in, char* query) {
	long long number;

     
	/* Trying to parse the input as a number */

	number = strtoll(in, &query, 10);

	errno = 0;

	/* To distinguish success/failure after call */

	if (errno != 0 || number <= 0) {
		fprintf(stderr, "Failure to parse input as postive number\n");
		return -1;
	}

	fprintf(stderr, "Number %lld recognized\n", number);

     
	/* Will try all the sizes sequentially */

	unsigned int sizes [] = {8,4,2,1};

	int i = 0;
	int size = -1;
	while (i < 4 ) {
		long long lowerbound = -powl((long long)((2)),((long long)(sizes[i]*8-1))); 
		long long upperbound = -lowerbound-1;
		
		fprintf(stderr, "%lld <? %lld <? %lld\n", lowerbound, number, upperbound);

		if (number >= lowerbound && number <= upperbound) {
			size = sizes[i];
			i++;
		} else {
			break;
		}
	}


	if (size > 0) {
		fprintf(stderr, "Query size of %d bytes has been selected\n", size);
	} else {		
		fprintf(stderr, "Unspecified error in parsing input number. Cannot find a correct size.\n");
		return -1;
	}        

	/* Finally formatting the number to correct size */

	if (size == 4) {
		*query = (int) *query;
	} else if (size == 2) {
		*query = (short) *query;
	} else {
		*query = (char) *query;
	}

	/* Returning the size */

	return size;
}

static void printNumber(FILE* file, char* num, int size) {

	long long number = (long long) num;

	if (size == 8) {
		fprintf(file, "%lld", (long long)number);
	} else if (size == 4) {
		fprintf(file, "%d", (int)number);
	} else if (size == 2) {
		fprintf(file, "%hd", (short)number);
	} else {
		fprintf(file, "%c", (char)number);
	}
}

int main (int argc, char* args[]) {

   	struct sockaddr_in serv_addr;
	int fd, len;
	if (parameterCountGood(argc) < 0) {
		fprintf(stderr, "Usage: client <postive number>\n");
        return 0;

	}
	char query [MAX_QUERY_SIZE];

	if ((querySize = (makeQuery(args[1], query))) < 1) {
		fprintf(stderr, "Invalid number. Please enter a positive integer\n");
		return -1;
	}

	
	char recvline[REC_BUFSIZE];

	fd = socket(AF_INET, SOCK_STREAM, 0);

	bzero((char*)&serv_addr, sizeof(serv_addr));

	serv_addr.sin_family = AF_INET;
	serv_addr.sin_addr.s_addr= inet_addr("192.168.88.1");
	serv_addr.sin_port = htons(atoi("5000"));

    	fprintf(stderr, "Connecting at port %d\n", serv_addr.sin_port);

	connect(fd, (struct sockaddr*) &serv_addr, sizeof(serv_addr));

    	fprintf(stderr, "Socket open\n");

    	fprintf(stderr, "Sending %d byte message: ", querySize);
	printNumber(stderr,query, querySize);
	fprintf(stderr, "\n");

	len = send(fd, query, querySize, 0);

	fprintf(stderr, "Sent %d bytes of message\n", len);

	if (len != querySize) {		
		fprintf(stderr, "Invalid number of sent bytes.\n");
		return -1;
	}

	/* Sending a separator */

	len = send(fd, &separator, 1, 0);

	if (len != 1) {		
		fprintf(stderr, "Error in sending the separator.\n");
		return -1;
	}   

    	fprintf(stderr, "Waiting for response\n");

	len = recv(fd, recvline, REC_BUFSIZE, 0);

	fprintf(stderr, "Recv returned %d\n", len);

	if (len != REC_BUFSIZE) {		
		fprintf(stderr, "Recv malfunction.\n");
		return -1;
	}

// Unsafe buffer

    	fprintf(stderr,"Received %d byte message: %s.1\n", len, recvline);

	close(fd);
}


