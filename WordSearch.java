public class WordSearch {
    private char[][] data;

    public WordSearch(int rows, int cols) {
        data = new char[rows][cols];
        for (int m = 0; m < data.length; m++) {
            for (int n = 0; n < data[n].length; n++) {
                data[m][n] = '_';
            }
        }
    }

    private void clear() {
        for (int m = 0; m < data.length; m++) {
            for (int n = 0; n < data[n].length; n++) {
                data[m][n] = '_';
            }
        }
    }
}
