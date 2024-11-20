public class Move {
    private int row;
    private int col;
    private Mark color;

    public Move(int col, int row, Mark mark) {
        this.col = col;
        this.row = row;
        color = mark;
    }
    
    public Move(String move, Mark mark) {
        col = move.charAt(0) - 'A';
        row = Integer.parseInt(move.substring(1)) - 1;
        color = mark;
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }

    public Mark getColor() {
        return color;
    }

    public boolean isWithinNSquares(Move move, int n) {
        return Math.abs(col  - move.getCol()) <= n && Math.abs(row - move.getRow()) <= n;
    }

    public String toString(){
        return Character.toString((char) (col + 'A')) + (row + 1);
    }
}

