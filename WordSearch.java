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
                wordsToAdd.add(word.toUpperCase());
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            System.exit(1);
        }
    }

    public WordSearch(int rows, int cols, String fileName) {
        wordsToAdd = new ArrayList<String>();
        setData(rows, cols);
        setWords(fileName);
        randgen = new Random();
        //addAllWords();
    }

    public WordSearch(int rows, int cols, String fileName, int randSeed) {
        setData(rows, cols);
        setWords(fileName);
        randgen = new Random(randSeed);
        //addAllWords();
    }

    private void clear() {
        for (int m = 0; m < data.length; m++) {
            for (int n = 0; n < data[m].length; n++) {
                data[m][n] = '_';
            }
        }
    }

    public String toString() {
        String grid = "";
        for (int m = 0; m < data.length; m++) {
            grid += "|";
            for (int n = 0; n < data[m].length - 1; n++) {
                grid += data[m][n] + " ";
            }
            grid += data[m][data[m].length - 1] + "|\n";
        }
        String words = "Words: ";
        for (int idx = 0; idx < wordsToAdd.size() - 1; idx++) { //need to change to wordsAdded later
            words += wordsToAdd.get(idx) + ", "; //need to change to wordsAdded later
        }
        return grid + words + wordsToAdd.get(wordsToAdd.size() - 1); //need to change to wordsAdded later
    }

    public boolean addWordHorizontal(String word, int row, int col) {
        if (data[row].length - col < word.length()) {
            return false;
        }
        for (int n = col; n - col < word.length(); n++) {
            if (data[row][n] != '_' && data[row][n] != word.charAt(n - col)) {
                return false;
            }
        }
        for (int n = col; n - col < word.length(); n++) {
            data[row][n] = word.charAt(n - col);
        }
        return true;
    }

    public boolean addWordVertical(String word, int row, int col) {
        if (data.length - row < word.length()) {
            return false;
        }
        for (int m = row; m - row < word.length(); m++) {
            if (data[m][col] != '_' && data[m][col] != word.charAt(m - row)) {
                return false;
            }
        }
        for (int m = row; m - row < word.length(); m++) {
            data[m][col] = word.charAt(m - row);
        }
        return true;
    }

    public boolean addWordDiagonal(String word, int row, int col) {
        if (data.length - row < word.length() || data[row].length - col < word.length()) {
            return false;
        }
        int n = col;
        int m = row;
        while (m - row < word.length() && n - col < word.length()) {
            if (data[m][n] != '_' && data[m][n] != word.charAt(m - row)) {
                return false;
            }
            m++;
            n++;
        }
        n = col;
        m = row;
        while (m - row < word.length() && n - col < word.length()) {
            data[m][n] = word.charAt(m - row);
            m++;
            n++;
        }
        return true;
    }

    public boolean addWord(String word, int row, int col, int rowIncrement, int colIncrement) { //change to private later
        if (rowIncrement == 0 && colIncrement == 0) {
            return false;
        }
        if (row + rowIncrement * word.length() < -1 || row + rowIncrement * word.length() > data.length ||
            col + colIncrement * word.length() < -1 || col + colIncrement * word.length() > data[0].length) {
            return false;
        }
        int m = row;
        int n = col;
        while (Math.abs(row - m) < word.length() && Math.abs(col - n) < word.length()) {
            if (data[m][n] != '_' && data[m][n] != word.charAt(Math.abs(m - row))) {
                return false;
            }
            m += rowIncrement;
            n += colIncrement;
        }
        m = row;
        n = col;
        while (Math.abs(row - m) < word.length() && Math.abs(col - n) < word.length()) {
            data[m][n] = word.charAt(Math.abs(m - row));
            m += rowIncrement;
            n += colIncrement;
        }
        return true;
    }
}
