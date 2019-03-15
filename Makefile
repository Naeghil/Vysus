JFLAGS = -sourcepath ./src/ -classpath ./bin/ -d ./bin/ -Xlint:unchecked -g
JC     = javac

all: exceptions storage bcrypt request

default: all

storage:
	$(JC) $(JFLAGS) src/storage/StorageAbstract.java
	$(JC) $(JFLAGS) src/storage/User.java

exceptions:
	$(JC) $(JFLAGS) src/storage/StorageException.java
	$(JC) $(JFLAGS) src/storage/DBProblemException.java
	$(JC) $(JFLAGS) src/storage/InvalidDataException.java
	$(JC) $(JFLAGS) src/storage/NoLogException.java	

bcrypt:
	$(JC) $(JFLAGS) src/org/mindrot/jbcrypt/BCrypt.java

request:
	$(JC) $(JFLAGS) src/request/RequestAbstract.java
	$(JC) $(JFLAGS) src/request/SignUp.java

clear:
	rm -rf bin/*
