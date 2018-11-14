import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.lang.Math;
import java.io.File;
import java.io.FileNotFoundException;

public class WordSearch {
    private char[][] data;
    private int seed;
    private Random randgen;
    private ArrayList<String> wordsToAdd;
    private ArrayList<String> wordsAdded;
    private char[] alphabet = {'A','B','C','D','E','F','G','H','I','J','K','L','M',
                               'N','O','P','Q','R','S','T','U','V','W','X','Y','Z'};

    public WordSearch(int rows, int cols, String fileName) throws FileNotFoundException {
        wordsToAdd = new ArrayList<String>();
        setData(rows, cols);
        setWords(fileName);
        Random rng = new Random();
        seed = Math.abs(rng.nextInt()) % 100000;
        randgen = new Random(seed);
        addAllWords();
    }
    public WordSearch(int rows, int cols, String fileName, int randSeed) throws FileNotFoundException {
        wordsToAdd = new ArrayList<String>();
        setData(rows, cols);
        setWords(fileName);
        seed = randSeed;
        randgen = new Random(seed);
        addAllWords();
    }

    private void setData(int rows, int cols) {
        data = new char[rows][cols];
        for (int m = 0; m < data.length; m++) {
            for (int n = 0; n < data[m].length; n++) {
                data[m][n] = '_';
            }
        }
    }

    private void setWords(String fileName) throws FileNotFoundException {
        File f = new File(fileName);
        Scanner in = new Scanner(f);
        while (in.hasNext()) {
            String word = in.next();
            if (word.length() <= data.length || word.length() <= data[0].length) {
                wordsToAdd.add(word.toUpperCase());
            }
        }
    }

    private void clear() {
        for (int m = 0; m < data.length; m++) {
            for (int n = 0; n < data[m].length; n++) {
                data[m][n] = '_';
            }
        }
    }

    public String toString(boolean isKey) {
        String grid = "";
        for (int m = 0; m < data.length; m++) {
            grid += "|";
            for (int n = 0; n < data[m].length - 1; n++) {
                if (data[m][n] == '_') {
                    if (isKey) {
                        grid += "  ";
                    } else {
                        grid += alphabet[Math.abs(randgen.nextInt()) % 26] + " ";
                    }
                } else {
                    grid += data[m][n] + " ";
                }
            }
            if (data[m][data[m].length - 1] == '_') {
                if (isKey) {
                    grid += " |\n";
                } else {
                    grid += alphabet[Math.abs(randgen.nextInt()) % 26] + "|\n";
                }
            } else {
                grid += data[m][data[m].length - 1] + "|\n";
            }
        }
        String words = "Words: ";
        for (int idx = 0; idx < wordsAdded.size() - 1; idx++) {
            words += wordsAdded.get(idx) + ", ";
        }
        return grid + words + wordsAdded.get(wordsAdded.size() - 1) + " (seed: " + seed + ")";
    }

    private boolean addWord(String word, int row, int col, int rowIncrement, int colIncrement) {
        if (rowIncrement == 0 && colIncrement == 0) {
            return false;
        }
        if (row + rowIncrement * word.length() < -1 || row + rowIncrement * word.length() > data.length ||
            col + colIncrement * word.length() < -1 || col + colIncrement * word.length() > data[0].length) {
            return false;
        }
        int m = row;
        int n = col;
        int idx = 0;
        while (idx < word.length()) {
            if (data[m][n] != '_' && data[m][n] != word.charAt(idx)) {
                return false;
            }
            m += rowIncrement;
            n += colIncrement;
            idx++;
        }
        m = row;
        n = col;
        idx = 0;
        while (idx < word.length()) {
            data[m][n] = word.charAt(idx);
            m += rowIncrement;
            n += colIncrement;
            idx++;
        }
        return true;
    }

    private void addAllWords() {
        wordsAdded = new ArrayList<String>();
        for (int wordIdx = 0; wordIdx < wordsToAdd.size(); wordIdx++) {
            boolean isDone = false;
            for (int trial0 = 1; trial0 < 100 && !isDone; trial0++) {
                String word = wordsToAdd.get(Math.abs(randgen.nextInt()) % wordsToAdd.size());
                int caseNum = Math.abs(randgen.nextInt()) % 8;
                int rowIncrement = ((caseNum / 3) + 2) % 3 - 1; //I think this is super cool {0,1,2} -> {2,3,4} -> {2,0,1} -> {1,-1,0}
                int colIncrement = ((caseNum % 3) + 2) % 3 - 1;
                for (int trial1 = 1; trial1 < 100 && !isDone; trial1++) {
                    if (Math.abs(word.length() * rowIncrement) <= data.length && Math.abs(word.length() * colIncrement) <= data[0].length) {
                        int offSetRow = 0;
                        int offSetCol = 0;
                        if (rowIncrement < 0) {
                            offSetRow = word.length() - 1;
                        }
                        if (colIncrement < 0) {
                            offSetCol = word.length() - 1;
                        }
                        int row = Math.abs(randgen.nextInt()) % (data.length - Math.abs(rowIncrement) * (word.length() - 1)) + offSetRow;
                        int col = Math.abs(randgen.nextInt()) % (data[0].length - Math.abs(colIncrement) * (word.length() - 1)) + offSetCol;
                        boolean isAdded = addWord(word, row, col, rowIncrement, colIncrement);
                        if (isAdded) {
                            isDone = true;
                            wordsAdded.add(word);
                            wordsToAdd.remove(word);
                            wordIdx--;
                        }
                    } else {
                        trial1 = 100;
                    }
                }
            }
        }
    }

    public static void printInstructions() {
        System.out.println("Directions:");
        System.out.println("After the \"java WordSearch\", include the number");
        System.out.println("of rows you want in the puzzle, then the number of");
        System.out.println("columns, then the name of the text file that");
        System.out.println("contains the words you want in the puzzle.");
        System.out.println("It is optional to put a seed after it to get a");
        System.out.println("particular puzzle.");
        System.out.println("It is also optional to put a seed and the word \"key\"");
        System.out.println("if you want a certain puzzle's solution.");
        System.out.println();
        System.out.println("Ex: java Driver 10 10 words.txt");
    }
    public static void main(String[] args) {
        int rows;
        int cols;
        int seed;
        WordSearch puzzle;

        if (args.length <= 2) {
            System.out.println("ERROR: You inputted an incorrect number of arguments");
            System.out.println();
            printInstructions();
            System.exit(1);
        } else if (args.length == 3) {
            try {
                rows = Integer.parseInt(args[0]);
                cols = Integer.parseInt(args[1]);
                puzzle = new WordSearch(rows, cols, args[2]);
                System.out.println(puzzle.toString(false));
            } catch (NumberFormatException e) {
                System.out.println("ERROR: First two arguments have to be the integers");
                System.out.println("specifying number of rows and columns");
                System.out.println();
                printInstructions();
                System.exit(1);
            } catch (FileNotFoundException e) {
                System.out.println("ERROR: File not found; could be in a different folder or spelled incorrectly.");
                System.out.println("Make sure the file exists.");
                System.out.println();
                printInstructions();
                System.exit(1);
            }
        } else if (args.length == 4) {
            try {
                rows = Integer.parseInt(args[0]);
                cols = Integer.parseInt(args[1]);
                seed = Integer.parseInt(args[3]);
                puzzle = new WordSearch(rows, cols, args[2], seed);
            } catch (NumberFormatException e) {
                System.out.println("ERROR: First two arguments have to be the integers");
                System.out.println("specifying number of rows and columns.");
                System.out.println("And the last argument has to be an integer");
                System.out.println("specifying the seed of the puzzle.");
                System.out.println();
                printInstructions();
                System.exit(1);
            } catch (FileNotFoundException e) {
                System.out.println("ERROR: File not found; could be in a different folder or spelled incorrectly.");
                System.out.println("Make sure the file exists.");
                System.out.println();
                printInstructions();
                System.exit(1);
            }
        }
    }
}
