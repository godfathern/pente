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

