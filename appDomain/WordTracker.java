package appDomain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordTracker implements Comparable<WordTracker>, Serializable {
    private static final long serialVersionUID = 1L;
    private final String word;
    private final Map<String, List<Integer>> fileLines;

    public WordTracker(String word, String fileName, int lineNumber) {
        this.word = word;
        this.fileLines = new HashMap<>();
        addOccurrence(fileName, lineNumber);
    }

    public void addOccurrence(String fileName, int lineNumber) {
        fileLines.computeIfAbsent(fileName, k -> new ArrayList<>()).add(lineNumber);
    }

    public String getWord() { return word; }

    public Map<String, List<Integer>> getFileLines() { return fileLines; }

    @Override
    public int compareTo(WordTracker other) { return this.word.compareTo(other.word); }
}