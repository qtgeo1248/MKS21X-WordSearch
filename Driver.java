public class Driver {
    public static void printInputError() {
        System.out.println("You need to put a desired number of rows, a");
        System.out.println("desired number of columns, and the file name");
        System.out.println("of the file which contains the words you desire");
        System.out.println("immediately after \"java Driver\"");
        System.out.println("It is optional to put a seed after it, and it is");
        System.out.println("also optional to put a seed and the word \"key\"");
        System.out.println("if you want a certain puzzle");
        System.out.println();
        System.out.println("Ex: java Driver 9 9 words.txt");
    }
    public static void main(String[] args) {
        int rows;
        int cols;

        if (args.length <= 2) {
            System.out.println("You inputted an incorrect number of arguments");
            System.out.println();
            printInputError();
            System.exit(1);
        } else if (args.length == 2) {
            try {
                rows = Integer.parseInt(args[0]);
                cols = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.out.println("First two arguments have to be the numbers");
                printInputError();
                System.exit(1);
            }
        }
    }
}
