JFLAGS = -g
JC = javac
JARFILE = agentAnalysis.jar
SRC = $(shell find src -iname '*.java')
all: $(JARFILE)

.SUFFIXES: .java .class
bin/%.class: $(SRC)
	mkdir -p bin/
	$(JC) -cp "lib/*" -sourcepath src -d bin/ $(JFLAGS) src/*.java

$(JARFILE): bin/Main.class
	jar cvfm $(JARFILE) MANIFEST.MF -C bin .


run: $(JARFILE)
	java -javaagent:$(JARFILE)=$(target) $(target)

clean:
	-rm -rf  $(JARFILE) bin/*

# JFLAGS = -g
# JC = javac
# JARFILE = DNSLookupService.jar
# SRC = $(shell find src -iname '*.java')
# all: $(JARFILE)

# .SUFFIXES: .java .class
# bin/%.class: $(SRC)
# 	mkdir -p bin/
# 	$(JC) -sourcepath src -d bin/ $(JFLAGS) src/$*.java

# $(JARFILE): bin/ca/ubc/cs/cs317/dnslookup/DNSLookupService.class
# 	jar cvfe $(JARFILE) ca.ubc.cs.cs317.dnslookup.DNSLookupService -C bin ca/

# run: $(JARFILE)
# 	java -jar $(JARFILE) 199.7.83.42

# clean:
# 	-rm -rf  $(JARFILE) bin/*
