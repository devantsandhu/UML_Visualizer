# UML Class Diagram Generator

This program uses PlantUML and requires Java and Graphviz to be able to generate the UML class diagrams. 
* Follow the instructions here and install graphviz from the PlantUML site: https://plantuml.com/graphviz-dot
* To install Java: https://www.java.com/en/download/

Once Java and Graphviz are installed, follow the instructions to generate your UML Class Diagram from your project:

## Step One - Compiling
a. Navigate to the project directory and run the following command in your command line:
```
javac src/ui/Main.java
```
b. Once the program has compiled, enter this line to run the program:
```
java src/ui/Main
```
## Step Two - Generating the diagram

Here are instructions on different input options you can use: 
- -i [filepaths] or -input [filepaths]                               Enter multiple filepaths by separating each path with a comma.
- -r or -round_lines                                                         Turn on round lines.
- -c or -coupling                                                             Coupling analysis is visible.
- -m [num] or -max_class_length [num]                      Specify max number of lines to display in each class.
- -m or -max_class_length                                             Max number of lines is 5.
- -s or -silent                                                                   Program will not print to stdout or stderr  

#### Multiple instructions
To input more than one option, simply separate each instruction with a space, for example:
```
-m 10 -r -c
```
Would create a diagram with maximum 10 lines, round lines, and coupling displayed.

#### Multiple filepaths
To pass in multiple filepaths, separate each path with a comma, as follows: 
```
-i src/ui,src/ast,src/libs
```

There are two ways to input your desired parameter. 

1. Before running the program
2. After running, following the instructions

#### Coupling analysis
To get the coupling analysis for each class, use -c or -coupling.
The color for each class diagram will change based on the degree of coupling for each class. The more coupled class has a redder shade and the less coupled class has a greener shade. The degree of coupling for each class is calculated by weighting the number of use classes, extends and inherits. 

### Input parameters before running the program

To input the parameters before running the program, after compiling in Step 1a, input your parameters from the command line. For example, if you wanted to create a diagram from a directory src, with a maximum of 10 lines dispalyed for each class, you would write the following:

```
java src/ui/Main -i src -m 10
```
or

```
java src/ui/Main -input src -max_class_length 10
```

### Input parameters after run

To input the parameters after running the program, follow the instructions printed in the command line after executing the run command:
```
java src/ui/Main
```
**Note: you do not need to add "-i" or "-input" before your filepaths if you run the program first.**

An example of what it would look like:

```
java src/ui/Main
Please enter your file paths (separate paths with commas): 
src
To change features of UML diagram (Press enter to do nothing): 

- -r or -round_lines                        Turn on round lines.
- -c or -coupling                           Coupling analysis is visible.
- -m [num] or -max_class_length [num]       Specify max number of lines to display in each class.
- -m or -max_class_length                   Max number of lines is 5.
- -s or -silent                             (add description)

Enter any features you wish to add to the diagram, and separate commands by spaces: 
-m 10
```

## Step Three - viewing your diagram

Once the output.png diagram is generated, it will be located in the output folder in the project directory. To view, navigate to the output folder and open the png file, or execute the following command on the command line: 

#### MacOS
```
open output/output.png
```

#### Windows
```
\output\output.png
```

#### Linux
```
mimeopen -d output/output.png
```

### Example of the output
![output](https://github.com/devantsandhu/UML_Class_Diagram_Generator/exampleOutput.png)
