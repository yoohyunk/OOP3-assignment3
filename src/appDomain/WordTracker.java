package appDomain;

import java.io.*;
import java.util.*;

import implementations.BSTree;
import implementations.BSTreeNode;
import utilities.Iterator;

/**
 * The WordTracker class processes a text file to track word occurrences
 * and the line numbers on which they appear. It stores data in a BST,
 * serializes it to a file, and formats output to console or file.
 * 
 * Usage:
 * java WordTracker <input.txt> -pf|-pl|-po [-f<output.txt>]
 *
 *Flag options: 
 * -pf : show file names only
 * -pl : show file names with line numbers
 * -po : show file names, line numbers, and total occurrences
 * -f<output.txt> : optionally write output to a file
 */
public class WordTracker implements Comparable<WordTracker>, Serializable {
    private static final long serialVersionUID = 1L;
    private final String word;
    private final Map<String, List<Integer>> fileLines;
    private static final String REPO_FILE = "repository.ser";
    
    /**
     * Constructor to initialize a WordTracker object.
     *
     * @param word       the word being tracked
     * @param fileName   the file in which the word appears
     * @param lineNumber the line number where the word appears
     */
    public WordTracker(String word, String fileName, int lineNumber) {
        this.word = word;
        this.fileLines = new HashMap<>();
        addOccurrence(fileName, lineNumber);
    }

    
    /**
     * Adds an occurrence of the word to the tracking map if not already present.
     *
     * @param fileName   the file in which the word was found
     * @param lineNumber the line number of the occurrence
     */

    public void addOccurrence(String fileName, int lineNumber) {
        fileLines.computeIfAbsent(fileName, k -> new ArrayList<>()).add(lineNumber);
    }

    
    /**
     * Returns the word being tracked.
     *
     * @return word
     */
    public String getWord() { return word; }

    
    /**
     * Returns the file-line mapping of word occurrences.
     *
     * @return map of filenames to list of line numbers
     */
    public Map<String, List<Integer>> getFileLines() { return fileLines; }

    
    /**
     * Compares this WordTracker to another based on the word value.
     */
    @Override
    public int compareTo(WordTracker other) { return this.word.compareTo(other.word); }


    /**
     * Main method for executing the WordTracker application.
     * Parses input arguments, processes the input file, and prints or saves output.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
    	
        // Validate command-line arguments
        if (args.length < 2) {
            System.err.println("Usage: java WordTracker <input.txt> -pf|-pl|-po [-f<output.txt>]");
            return;
        }

        String inputFile = args[0];
        String flag = args[1];
        String outputFile = null;

        // Check if output file is specified
        if (args.length == 3 && args[2].startsWith("-f")) {
            outputFile = args[2].substring(2);
        }

        // Load the tree from repository.ser if it exists
        BSTree<WordTracker> wordTree = loadRepository();

        
        // Read file and populate tree
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String line;
            int lineNumber = 1;

            while ((line = reader.readLine()) != null) {
                String[] words = line.toLowerCase().split("[^a-zA-Z']+");

                for (String word : words) {
                	word = word.replace("'", "");
                	
                    if (!word.isEmpty()) {
                        WordTracker temp = new WordTracker(word, inputFile, lineNumber);
                        WordTracker existing = searchWord(wordTree, word);
                        if (existing != null) {
                            existing.addOccurrence(inputFile, lineNumber);
                        } else {
                            wordTree.add(temp);
                        }
                    }
                }
                lineNumber++;
            }
        } catch (IOException e) {
            System.err.println("Error reading input file: " + e.getMessage());
            return;
        }

        // Save to repository file
        saveRepository(wordTree);
        
        
        // Format output
        System.out.println("Displaying " + flag + " format");
        StringBuilder result = new StringBuilder();
        Iterator<WordTracker> it = wordTree.inorderIterator();
        
        while (it.hasNext()) {
        	
            WordTracker wordTracker = it.next();
            String formatted = formatOutput(wordTracker, flag, inputFile);
            if (!formatted.isEmpty()) {
                result.append(formatted);
            }
        }

        // Output to file or console
        if (outputFile != null) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(outputFile))) {
                writer.write(result.toString());
                
            } catch (IOException e) {
                System.err.println("Error writing output file: " + e.getMessage());
            }
        } else {
            System.out.println(result);
            System.out.println("Not exporting file.");
        }
    }

    /**
     * Formats the output for a WordTracker object based on the flag type.
     *
     * @param wordTracker the WordTracker object
     * @param flag        the output mode (-pf, -pl, -po)
     * @return formatted  string output
     */
    private static String formatOutput(WordTracker wordTracker, String flag, String inputFile) {
      
    	StringBuilder output = new StringBuilder();
        
    	Map<String, List<Integer>> occurrences = wordTracker.getFileLines();

    	// Skips word if it doesn't appear in the current input file
        if (!occurrences.containsKey(inputFile)) {
            return "";
        }
        
        // Format based on display flag
        if (flag.equals("-pf")) {
            output.append("Key : ===").append(wordTracker.getWord())
                  .append("===  found in file: ").append(inputFile);

        } else if (flag.equals("-pl")) {
            output.append("Key : ===").append(wordTracker.getWord())
                  .append("===  found in file: ").append(inputFile)
                  .append(" on line(s): ").append(occurrences.get(inputFile));

        } else if (flag.equals("-po")) {
            int freq = occurrences.get(inputFile).size();
            output.append("Key : ===").append(wordTracker.getWord())
                  .append("===  found in file: ").append(inputFile)
                  .append(" on line(s): ").append(occurrences.get(inputFile))
                  .append(" (").append(freq).append(" occurrence")
                  .append(freq > 1 ? "s" : "").append(")");
        } else {
            output.append("Unknown flag.");
        }
        
        output.append("\n");
        return output.toString();
    }

    
    /**
     * Searches the BST for an existing WordTracker by word.
     *
     * @param tree the BSTree to search in
     * @param word the word to look for
     * @return existing WordTracker if found, false if not
     */
    private static WordTracker searchWord(BSTree<WordTracker> tree, String word) {
        WordTracker lookup = new WordTracker(word, "", -1);
        BSTreeNode<WordTracker> resultNode = tree.search(lookup);
        return (resultNode != null) ? resultNode.getElement() : null;
    }


    /**
     * Loads a previously saved BSTree from the repository.ser file.
     *
     * @return loaded BSTree or new tree if not found
     */
    @SuppressWarnings({ "unchecked" })
    private static BSTree<WordTracker> loadRepository() {
    	
        File file = new File(REPO_FILE);
        if (file.exists()) {
        	
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (BSTree<WordTracker>) ois.readObject();
                
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Could not load repository: " + e.getMessage());
            }
        }
        return new BSTree<>();
    }

    /**
     * Saves the BSTree to repository.ser for future reuse.
     *
     * @param tree the BSTree to save
     */
    private static void saveRepository(BSTree<WordTracker> tree) {
    	
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(REPO_FILE))) {
            oos.writeObject(tree);
            System.out.println("repository.ser saved successfully.");
            System.out.println("Absolute path: " + new File(REPO_FILE).getAbsolutePath());
            
        } catch (IOException e) {
            System.err.println("Could not save repository:");
            e.printStackTrace();
        }
    }
}

