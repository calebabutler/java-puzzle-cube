# java-puzzle-cube
An interactive 3x3 Puzzle Cube in LWJGL

![A screenshot of the program](https://github.com/calebabutler/java-puzzle-cube/blob/main/cube-screenshot.png?raw=true)

![A screenshot of the program's help](https://github.com/calebabutler/java-puzzle-cube/blob/main/help-screenshot.png?raw=true)

How to Compile
==============

First, install java 19, maven, and git. On Ubuntu, you do this by executing this command:

    > sudo apt install maven java19-sdk git

Next, clone this git repository:

    > git clone https://github.com/calebabutler/java-puzzle-cube.git
    > cd java-puzzle-cube

Build the project, download the dependencies, and package the project:

    > mvn compile
    > mvn dependency:copy-dependencies
    > mvn package

Lastly, run the jar:

    > java -jar target/puzzlecube-1.0.jar
