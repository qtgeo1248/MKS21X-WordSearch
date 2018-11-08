public class WordSearch {
    private char[][] data;

    public WordSearch(int rows, int cols) {
        data = new char[rows][cols];
        for (int m = 0; m < data.length; m++) {
            for (int n = 0; n < data[m].length; n++) {
                data[m][n] = '_';
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

    public String toString() {
        String grid = "";
        for (int m = 0; m < data.length; m++) {
            for (int n = 0; n < data[m].length - 1; n++) {
                grid += data[m][n] + " ";
            }
            grid += data[m][data[m].length - 1] + "\n";
        }
        return grid;
    }

    public boolean addWordHorizontal(String word, int row, int col) {
        if (data[row].length - col < word.length()) {
            return false;
        }
        for (int n = col; n < data[row].length; n++) {
            if (data[row][n] != '_' && data[row][n] != word.charAt(n - col)) {
                return false;
            }
        }
    }
}
