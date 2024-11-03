public class MoveConverter {
    public static int[] convertMove(String move) {
        char columnLetter = move.charAt(0);
        String rowNumber = move.substring(1);
        int column = columnLetter - 'A';
        int row = 15 - Integer.parseInt(rowNumber);
        return new int[]{row, column};
    }

    public static String convertBack(Move move) {
        char columnLetter = (char) ('A' + (move.getCol() - 1));
        return columnLetter + String.valueOf(move.getRow());
    }

    public static Move convertStringToMove(String moveString) {
        char columnLetter = moveString.charAt(0);
        int column = columnLetter - 'A';
        int row = 15 - Integer.parseInt(moveString.substring(1));
        return new Move(row, column);
    }
    
}
