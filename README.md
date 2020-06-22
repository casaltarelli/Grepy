# Grepy [java]
### Christian Saltarelli
Marist College CMPT 440L-200 - Final Project

This project is a version of grep utility, developed in java, and known as grepy. This program searches files for regular expression pattern matches and produces dot graph file output with PNG representation for the automata used in matching computation.

## Functionality
* Learn the alphabet from a given input [FILE]
* Convert the regular expression [REGEX] to a [NFA]
* Convert the [NFA] to a [DFA]
* Use [DFA] computation to test each line of the file for accept/reject
* Output all accepted lines to the user
* Output the [NFA] and [DFA] to their own files in *DOT Language Format*
* *Option* output [NFA] and [DFA] Graphs via graphviz

## Usage
```bash
java Grep [-y] [-n NFA-FILE] [-d DFA-FILE] REGEX FILE
```
## Project Status
Completed.

## License
[GNU General Public License v3.0](https://www.gnu.org/licenses/gpl-3.0.en.html)
