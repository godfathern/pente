public class MoveConverter {
    public static String convertMoveToString(Move move) {
        char columnLetter = (char) ('A' + (move.getCol()));
        int row = 15 - move.getRow();
        return "" + columnLetter + row;
    }
    

    public static Move convertStringToMove(String moveString) {
        char columnLetter = moveString.charAt(0);
        int column = columnLetter - 'A';
        int row = 15 - Integer.parseInt(moveString.substring(1));
        return new Move(row, column);
    }

    public static Move convertMovetoMove(Move move){
        return convertStringToMove(convertMoveToString(move));
    }
    
}
