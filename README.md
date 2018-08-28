# socket
This is an implementation of Socket Programming. Project contains Server and Client. Server creates server socket and waits for client request. Server creates thread for each client request. So, each client request is getting processed by each thread. Each thread checks for request from client, prepares for response and sends back this response to client.


1) Compile and Run the code:
(Inside directory smangul1-p1)

Compile

  $ make

Run

  $ java -jar server



2) Implementation:
Project covers area from Socket Programming, Distributed Systems, Multithreading. Project contains two java classes- Server.java and MessageProcessorRunnable.java. Server.java creates server socket and waits for client request. Server creates thread for each client request. So, each client request is getting processed by each thread. Each thread checks for request from client, prepares for response and sends back this response to client.
Build.xml is been generated to generate .jar file of name “server”.

3) Input/output:
After doing steps mentioned in above point 1, server gets started and it prints host name and port number (say 32919). Use this port number to send request from client from one of the other terminal as follow:
wget http://remote03.cs.binghamton.edu:32919/Content.html

example:
input-
server
Host Name: 0.0.0.0
Port number: 39577

client
wget http://remote03.cs.binghamton.edu:32919/Content.html

server
/Content.html | 128.226.180.165 | 46396 | 1
