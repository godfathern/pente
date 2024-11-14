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

    public String toString(){
        return Character.toString((char) (col + 'A')) + (row + 1);
    }

    public boolean isClose(Move move){
        return Math.abs(col - move.col) <= 1 && Math.abs(row - move.row) <= 1;
    }
}

