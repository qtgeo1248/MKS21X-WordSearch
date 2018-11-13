public class Driver {
    public static void main(String[] args) {
        if (args.length <= 2) {
            System.out.println("You inputted an incorrect number of arguments");
            System.out.println();
            System.out.println("You need to put a desired number of rows, a");
            System.out.println("desired number of columns, and the file name");
            System.out.println("of the file which contains the words you desire");
            System.out.println("immediately after \"java Driver\"");
            System.out.println();
            System.out.println("Ex: java Driver 9 9 words.txt");
        }
    }
}
