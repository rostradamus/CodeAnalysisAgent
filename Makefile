JFLAGS = -g
JC = javac
JARFILE = agentAnalysis.jar
SRC = $(shell find src -iname '*.java')
all: $(JARFILE)

.SUFFIXES: .java .class
bin/%.class: $(SRC)
	mkdir -p bin/
	$(JC) -cp "lib/*" -d bin/ $(JFLAGS) src/*.java

$(JARFILE): bin/*.class
	jar cvfm $(JARFILE) MANIFEST.MF -C bin .


run: $(JARFILE)
	java -javaagent:$(JARFILE) $(target)

clean:
	-rm -rf  $(JARFILE) bin/*
