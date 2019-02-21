JFLAGS = -sourcepath ./src/ -classpath ./bin/ -d ./bin/ -Xlint:unchecked -g
JC     = javac

all: storage bcrypt

default: all

storage: storageExceptions
	$(JC) $(JFLAGS) src/storage/StorageAbstract.java
	$(JC) $(JFLAGS) src/storage/Session.java
	$(JC) $(JFLAGS) src/storage/User.java

storageExceptions:
	$(JC) $(JFLAGS) src/storage/StorageException.java
	$(JC) $(JFLAGS) src/storage/DBProblemException.java
	$(JC) $(JFLAGS) src/storage/InvalidDataException.java
	$(JC) $(JFLAGS) src/storage/InvalidSessionException.java
	$(JC) $(JFLAGS) src/storage/NoLogException.java	

bcrypt:
	$(JC) $(JFLAGS) src/org/mindrot/jbcrypt/BCrypt.java


clear:
	rm -rf bin/*
