# Grepy [java]
### Christian Saltarelli
CMPT 440L-200 - Final Project

This project is a version of grep utility, developed in java, and known as grepy. This program searches files for regular expression pattern matches and produces dot graph file output for the automata used in matching computation.

## Functionality
* Learn the alphabet from a given input [file]
* Convert the regular expression [REGEX] to a [NFA]
* Convert the [NFA] to a [DFA]
* Use [DFA] computation to test each line of the file for accept/reject
* Output all accepted lines to the user
* Output the [NFA] and [DFA] to their own files in *DOT Language Format*

## Usage
```bash
java grepy.Grep [-n NFA-FILE] [-d DFA-FILE] REGEX FILE
```
## Project Status
This project is currently in development.

## License
[MIT](https://choosealicense.com/licenses/mit/)
