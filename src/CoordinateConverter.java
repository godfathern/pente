// import java.util.regex.Matcher;
// import java.util.regex.Pattern;

// public class Test {
//     public static void main(String[] args) {
//         EvalBoard evalBoard = new EvalBoard(15);
        
//         // evalBoard.play(0, 1, 1);
//         // evalBoard.play(0, 2, 1);
//         // evalBoard.play(0, 3, 1);
//         // evalBoard.play(0, 4, 1);

//         // evalBoard.play(1, 0, 1);
//         // evalBoard.play(1, 2, 1);
//         // evalBoard.play(1, 3, 1);
//         // evalBoard.play(1, 4, 1);


//         // evalBoard.play(0, 1, 1);
//         // evalBoard.play(0, 2, 1);
//         // evalBoard.play(0, 3, 1);
//         // evalBoard.play(0, 4, 1);
//         // evalBoard.play(0, 0, 1);
//         // evalBoard.play(0, 7, 1);
//         // evalBoard.play(0, 8, 1);
//         // evalBoard.play(0, 9, 1);
//         // //evalBoard.play(0, 10, 1);

//         // evalBoard.play(1, 1, 1);
//         // evalBoard.play(1, 2, 1);
//         // evalBoard.play(1, 3, 1);
//         // evalBoard.play(1, 4, 1);
//         // evalBoard.play(1, 0, 1);
//         // evalBoard.play(1, 7, 1);
//         // evalBoard.play(1, 8, 1);
//         // evalBoard.play(1, 9, 1);
//         // evalBoard.play(1, 10, 1);

//         // evalBoard.play(1, 12, 1);
//         // evalBoard.play(1, 13, 1);

//         // evalBoard.play(3, 9, 1);
//         // evalBoard.play(4, 9, 1);
        
//         evalBoard.play(2, 10, 1);
//         evalBoard.play(3, 9, 1);
//         evalBoard.play(4,8, 1);
//         evalBoard.play(5, 7, 1);
//         //evalBoard.play(3, 3, 1);

//         evalBoard.play(10, 5, 1);
//         evalBoard.play(11, 4, 1);
//         evalBoard.play(12, 3, 1);
//         evalBoard.play(13, 2, 1);

//         evalBoard.play(10, 12, 1);
//         evalBoard.play(11, 11, 1);
//         evalBoard.play(12, 10, 1);
//         evalBoard.play(13, 9, 1);

//         // evalBoard.play(14, 0, 1);
//         // evalBoard.play(13, 0, 1);

        
//         // //Test 3 x 3

//         // evalBoard.play(0, 0, 1);
//         // evalBoard.play(0, 1, 2);
//         // evalBoard.play(0, 2, 0);
//         // evalBoard.play(1, 0, 0);
//         // evalBoard.play(1, 1, 0);
//         // evalBoard.play(1, 2, 0);
//         // evalBoard.play(2, 0, 0);
//         // evalBoard   .play(2, 1, 0);
//         // evalBoard   .play(2, 2, 0); 


        
//         evalBoard.display();

//         evalBoard.evalBoard(1);

//         evalBoard.display();
//     }

// }

public class CoordinateConverter {

    public static int[] stringToCoordinate(String input) {
        char columnChar = input.charAt(0); // Extract column (A, B, C, ...)
        int row = Integer.parseInt(input.substring(1)); // Extract row (15, 14, ...)
        
        int x = 15 - row; // Convert row to x-coordinate
        int y = columnChar - 'A'; // Convert column to y-coordinate
        
        return new int[]{x, y};
    }

    public static String coordinateToString(int x, int y) {
        char columnChar = (char) ('A' + y); // Convert y-coordinate to column (A, B, C, ...)
        int row = 15 - x; // Convert x-coordinate to row
        
        return String.format("%c%d", columnChar, row);
    }

    // public static void main(String[] args) {
    //     // Test examples
    //     String input = "E2";
    //     int[] coord = stringToCoordinate(input);
    //     System.out.printf("String %s -> Coordinate: x = %d, y = %d%n", input, coord[0], coord[1]);

    //     String result = coordinateToString(coord[0], coord[1]);
    //     System.out.printf("Coordinate: x = %d, y = %d -> String %s%n", coord[0], coord[1], result);
    // }
}

