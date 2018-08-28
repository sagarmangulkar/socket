all: Server.class MessageProcessorRunnable.class

Server.class:
	ant

	cp out/artifacts/Programming_Assignment_1_DS_jar/Programming_Assignment_1_DS.jar .

	mv Programming_Assignment_1_DS.jar server

clean:
	ant clean
	rm -f server
	rm -r out