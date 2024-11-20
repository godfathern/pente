public class Move implements Comparable<Move> {
    private int row;
    private int col;
    private Mark color;
    private int score;

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

    public int getScore() {
        return score;
    }

    public void setScore(int evaluate) {
        score = evaluate;
    }

    public boolean isWithinNSquares(Move move, int n) {
        return Math.abs(col  - move.getCol()) <= n && Math.abs(row - move.getRow()) <= n;
    }

    @Override
    public String toString(){
        return Character.toString((char) (col + 'A')) + (row + 1);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof Move)) {
            return false;
        }

        Move move = (Move) obj;
        return move.getCol() == col && move.getRow() == row;
    }

    @Override
    public int compareTo(Move o) {
        return o.getScore() - score;
    }
}

