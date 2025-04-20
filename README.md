WordTracker – Assignment 3

Prerequisites

You’ll need Java 8 (or higher) installed and on your PATH.

Usage

Put your text file in the working directory (or give its full path).

Run one of these commands:

```
java -jar WordTracker.jar <input.txt> -pf
java -jar WordTracker.jar <input.txt> -pl
java -jar WordTracker.jar <input.txt> -po
```

To capture output in a file instead of the console, add -f<filename>:

```
java -jar WordTracker.jar <input.txt> -pl -fresults.txt
```

Options

```
-pf → list each word and the files where it appears  
-pl → list each word with files and line numbers  
-po → list each word with files, line numbers, and total frequency  
-f<file> → redirect report to <file> instead of stdout
```

Persistent Repository

On startup, WordTracker checks for repository.ser in the working folder:

- If it’s there, previous data is loaded and merged.  
- If not, it begins with an empty repository.  

After processing, it overwrites (or creates) repository.ser so your data accumulates over runs.


