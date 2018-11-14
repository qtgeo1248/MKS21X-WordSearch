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

    public WordSearch(int rows, int cols, String fileName) throws FileNotFoundException {
        wordsToAdd = new ArrayList<String>();
        setData(rows, cols);
        setWords(fileName);
        Random rng = new Random();
        seed = rng.nextInt() % 100000;
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

    public String toString() { //REMEMBER SEED
        String grid = "";
        for (int m = 0; m < data.length; m++) {
            grid += "|";
            for (int n = 0; n < data[m].length - 1; n++) {
                grid += data[m][n] + " ";
            }
            grid += data[m][data[m].length - 1] + "|\n";
        }
        String words = "Words: ";
        for (int idx = 0; idx < wordsAdded.size() - 1; idx++) {
            words += wordsAdded.get(idx) + ", ";
        }
        return grid + words + wordsAdded.get(wordsAdded.size() - 1) + "(seed: " + seed + ")";
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
        System.out.println("You need to put a desired number of rows, a");
        System.out.println("desired number of columns, and the file name");
        System.out.println("of the file which contains the words you desire");
        System.out.println("immediately after \"java Driver\"");
        System.out.println("It is optional to put a seed after it, and it is");
        System.out.println("also optional to put a seed and the word \"key\"");
        System.out.println("if you want a certain puzzle solution");
        System.out.println();
        System.out.println("Ex: java Driver 9 9 words.txt");
    }
    public static void main(String[] args) {
        int rows;
        int cols;

        if (args.length <= 2) {
            System.out.println("You inputted an incorrect number of arguments");
            System.out.println();
            printInstructions();
            System.exit(1);
        } else if (args.length == 2) {
            try {
                rows = Integer.parseInt(args[0]);
                cols = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("First two arguments have to be the numbers");
                printInstructions();
                System.exit(1);
            }
        }
    }
}
