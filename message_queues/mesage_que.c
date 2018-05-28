# include <stdio.h>
# include <sys/types.h>
# include <sys/ipc.h>
# include <sys/msg.h>
# include <stdlib.h>
# include <string.h>

/*
 *
 *System V ipc —— Message_queues
 *
 */

#define SIZE_OF_MSG 2048
#define KEY_VALUE   1235
#define SIZE_OF_MSGTYPE 4
struct _msg
{
	long msgtype;
	char msgtext[SIZE_OF_MSG];

}msg;



int msg_init(void)
{
	int msg_id;
	if (msg_id = msgget((key_t)KEY_VALUE,IPC_CREAT|0666),msg_id<0)
	{
		perror("error of get id ");
	}

	return msg_id;
}


int msg_push(int msg_id ,struct _msg * pmsg)
{


	if (msgsnd(msg_id,pmsg,sizeof(struct _msg )-SIZE_OF_MSGTYPE, IPC_NOWAIT)<0)
	{
		perror("error of message_queues send ");
		return -1;
	}

	return 0;
}


int msg_pop(int msg_id,struct _msg * pmsg)
{

	if ( msgrcv( msg_id, pmsg, sizeof (struct _msg)-SIZE_OF_MSGTYPE,0,0 ) <0)
	{
		perror("error of message_queues send ");
		return -1;
	}
	return 0;
}


int main (int argv ,char **argc)
{

	int msg_id;
	if (argv<2)
	{
		printf("please input send or recv\n");
		return -1;
	}
	if (msg_id =msg_init(),msg_id<0)
		return -1;

	if(!strcmp(argc[1],"send"))
	{
		msg.msgtype=10;
		strcpy(msg.msgtext,"hello world");
		if(msg_push(msg_id,&msg)<0)
		return -1;
		printf("send ok: %s \n",msg.msgtext);
	}
	else if(!strcmp(argc[1],"recv"))
	{

		if(msg_pop(msg_id,&msg)<0)
		return -1;
		printf("recv ok: %s \n",msg.msgtext);

	}
	else
	{

		printf("input error \n");
		return -1;

	}



	return 0;
}
