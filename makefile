all: compile

compile:
	slice2java MusicManager.ice
	rm -rf client/src/main/java/com/example/MusicServer
	mv MusicServer/ client/src/main/java/com/example
	cd Server && slice2py ../MusicManager.ice
	cd client && mvn clean install

run_server:
	cd Server && python3 Server.py

install_client:
	cd client && mvn clean install

run_client:
	cd client && java -jar target/client-1.0-SNAPSHOT.jar
