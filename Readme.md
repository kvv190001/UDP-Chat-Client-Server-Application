# TCP-Chat-Client-Server-Application

Description:  This UDP-based chat application allows client to communicate with each other through a chat server.

Platform/compiler: UNIX, javac

Files:

Client.java - Source code for client application.
Server.java - Source code for server application.

Compiling instructions:

To compile, place all files within a single directory and type "javac *.java".

Running instructions:
- Type "java TCPServer" on the server host.
- Type "java TCPClient netxx" on the client host, where netxx is the name of
  the server host.
- To exit the client or server, type CTRL-C.

Message Format:
+ Client to Server:
    - DATA <seq. no.> <char> \n

+ Server to Client:
    - ACK <seq. no.> \n

Testing instructions:
- To test the client program, a compiled server program will be provided
- The program simulates an unreliable channel by randomly losing DATA messages and ACK messages, as well as corrupting occasional ACK messages.
- Typing the command “chmod +x testserv” and run the program by typing “testserv port_num max_seq loss_prob”
- For testing server program, we can do the same
- Typing the command “chmod +x testclient” and run the program by typing "testclient <server-name> <port#> <MAXSEQ#> <drop_prob>"
