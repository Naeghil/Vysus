JFLAGS = -sourcepath ./src/ -classpath ./bin/ -d ./bin/ -g
JC     = javac

all: storage bcrypt

default: all

storage:
	$(JC) $(JFLAGS) src/storage/StorageInterface.java
	$(JC) $(JFLAGS) src/storage/Session.java
	$(JC) $(JFLAGS) src/storage/User.java

bcrtypt:
	$(JC) $(JFLAGS) src/org/mindrot/jbcrypt/Bycript.java


clear:
	rm -rf bin/*
