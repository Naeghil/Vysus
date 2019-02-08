# makefile begins
# define a variable for a parameter, specified as VAR=val when running make

JFLAGS = -d $VSRC/bin -g
JC     = javac
JVM    = java 

# Clear default targets 
.SUFFIXES: .java .class

# Target entry for creating .class from .java files 
# uses the suffix rule syntax:
# 	DSTS:
# 	<tab>	rule
# DSTS (Dependency Suffix     Target Suffix)
# 'TS' target file suffix; 'DS' dependency suffix; 'rule' for building target	
# '$*' built-in macro, gets the basename of the current target 

 .java.class:
         $(JC) $(JFLAGS) $*.java

# CLASSES macro consisting of N words (N=|source files|)
# \<return> split lines but considered a single line
 CLASSES = \
	storage.StorageInterface.java #
	# Experiment.java \
#         Block.java \
#         Spring.java \
#         PhysicsElement.java \
#         Simulator.java

# MAIN is a variable with the name of the file containing the main method
# MAIN = Experiment 

# the default make target entry
default: classes

# Target dependency line, using Suffix Replacement within a macro: 
# $(macroname:string1=string2)
classes: $(CLASSES:.java=.class)

# Target for running the program:
# run: $(MAIN).class
# 	$(JVM) $(MAIN)

#Maybe use to recompile?
#clean:
#	$(RM) *.class
