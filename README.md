# FAREC

**FAREC** (*Finite Automata and Regular Expression Converter*) is an educational
tool designed to demonstrate the algorithms used to convert between **finite
automata** and **regular expressions**.

FAREC allows you to create a finite automaton/regular expression and then
convert it into an equivalent regular expression/finite automaton. Each step of
the conversion process is shown.

To convert finite automata into regular expressions, FAREC uses the **State
Elimination with GNFAs** algorithm. To convert regular expressions into finite
automata, FAREC uses the **McNaughton–Yamada–Thompson algorithm** (also known as
Thompson's construction algorithm).

For more information about finite automata, regular expressions or the
aforementioned algorithms, see the included [report](/report.pdf).

## Installation

The easiest way to run the program is to download [*FAREC.zip*](/FAREC.zip),
extract its contents and then launch the program by double-clicking
*launcher.vbs*.

If you wish to run the program from an IDE, I recommend using
[IntelliJ](https://www.jetbrains.com/idea/). First clone the repo and then open
it in IntelliJ, making sure to set the project SDK. Then start from the
application using the *App* class.

To package the program into a custom runtime image, first run the *compile*
lifecycle using [Maven](https://maven.apache.org/) and then run *javafx:jlink*
using the [javafx-maven-plugin](https://github.com/openjfx/javafx-maven-plugin).
Finally, run *copy_launcher.bat*. The packaged program will be inside the
*target* folder.

## Usage

For information on how to use the program, see the included [user
guide](/User%20Guide/user_guide.pdf). Two example use cases are also
demonstrated in the [project video](https://youtu.be/m4aCRncxnWQ), from 2:40
onwards.

## Future Work

For ideas about potential future work, see [*TODO.txt*](/TODO.txt).

## Further Information

FAREC was created during the third year of my MSci Computer Science degree at
King's College London, for the module 6CCS3PRJ - Individual Project. The title
of my project was *"Implementing algorithms connecting finite automata and
regular expressions"*. My supervisor for the project was Dr. Agi Kurucz.

For more information about the project, see the included [report](/report.pdf)
or [project video](https://youtu.be/m4aCRncxnWQ).

## Gallery

![](https://github.com/Meezeus/farec/blob/98ff259e3ddfcef8f36d3851bfe27d25bb75e07f/User%20Guide/Diagrams/CreateREScreen.png?raw=true)

![](https://github.com/Meezeus/farec/blob/98ff259e3ddfcef8f36d3851bfe27d25bb75e07f/User%20Guide/Diagrams/ConvertREScreen.png?raw=true)

![](https://github.com/Meezeus/farec/blob/98ff259e3ddfcef8f36d3851bfe27d25bb75e07f/User%20Guide/Diagrams/CreateFAScreen.png?raw=true)

![](https://github.com/Meezeus/farec/blob/98ff259e3ddfcef8f36d3851bfe27d25bb75e07f/User%20Guide/Diagrams/ConvertFAScreen.png?raw=true)
