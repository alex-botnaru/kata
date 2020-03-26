# Word Search Program
The project contains the implementation of a program that searches for words in a grid and returns their location.

The first line of the input file contains the list of words, and the following lines contain the grid with the words. The grid should always be square.

Input file example:
<pre>
FOR,JAVA,SUN,TEST
E,N,U,S
F,O,R,H
A,V,A,J
T,E,S,T
</pre>

Output example:
<pre>
FOR: (0,1),(1,1),(2,1)
JAVA: (3,2),(2,2),(1,2),(0,2)
SUN: (3,0),(2,0),(1,0)
TEST: (0,3),(1,3),(2,3),(3,3)
</pre> 

## Build and Run
The program was written in Java and uses Maven to manage dependencies, to build and run it you need to install the following:
* Java 1.8
* Maven 3.6

To build the project and execute the unit tests run:
`mvn clean package`

To execute only the unit tests run:
`mvn test`

To execute the program run:
`java -cp target/kata-word-search-0.0.1-SNAPSHOT.jar com.kata.word.search.Main <path/to/input/file>`
