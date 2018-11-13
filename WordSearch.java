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

    public WordSearch(int rows, int cols, String fileName) {
        wordsToAdd = new ArrayList<String>();
        setData(rows, cols);
        setWords(fileName);
        randgen = new Random();
        addAllWords();
    }

    public WordSearch(int rows, int cols, String fileName, int randSeed) {
        wordsToAdd = new ArrayList<String>();
        setData(rows, cols);
        setWords(fileName);
        randgen = new Random(randSeed);
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

    private void setWords(String fileName) {
        try {
            File f = new File(fileName);
            Scanner in = new Scanner(f);
            while (in.hasNext()) {
                String word = in.next();
                if (word.length() <= data.length || word.length() <= data[0].length) {
                    wordsToAdd.add(word.toUpperCase());
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            System.exit(1);
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
        return grid + words + wordsAdded.get(wordsAdded.size() - 1);
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
}
