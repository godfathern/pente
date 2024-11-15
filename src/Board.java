import java.util.ArrayList;

public class Board {
    // 0: Empty, 1: Red, 2: Black
    private final Mark[][] board;
    private int turns;
    private int blackCaptures;
    private int redCaptures;

    public Board() {
        turns = 0;
        board = new Mark[15][15];

        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                board[i][j] = Mark.Empty;
            }
        }
    }

    public int getTurns(){
        return turns;
    }

    public void play(Move move){
        if(board[move.getCol()][move.getRow()] == Mark.Empty){
            board[move.getCol()][move.getRow()] = move.getColor();
            turns++;
        }
    }

    public void play(Eating move){
        if(board[move.getCol()][move.getRow()] == Mark.Empty){
            board[move.getCol()][move.getRow()] = move.getColor();
        }

        for(Move enemy : move.getEnemies()){
            if(board[enemy.getCol()][enemy.getRow()] == enemy.getColor()){
                board[enemy.getCol()][enemy.getRow()] = Mark.Empty;
            }
        }
    }

    public void undo(Move move){
        if(board[move.getCol()][move.getRow()] == move.getColor()){
            board[move.getCol()][move.getRow()] = Mark.Empty;
            turns--;
        }
    }

    public void undo(Eating move){
        if(board[move.getCol()][move.getRow()] == move.getColor()){
            board[move.getCol()][move.getRow()] = Mark.Empty;
        }
        for(Move enemy : move.getEnemies()){
            if(board[enemy.getCol()][enemy.getRow()] == Mark.Empty){
                board[enemy.getCol()][enemy.getRow()] = enemy.getColor();
            }
        }
    }

    public int getCaptures(Mark mark){
        if(mark == Mark.Black){
            return blackCaptures;
        } else {
            return redCaptures;
        }
    }

    public boolean isCapture(Move move){
        // TODO: Implement this
        return false;
    }

    // negative if black wins, positive if red wins
    public int evaluate(Mark mark){
        Mark oppMark;
        if(mark == Mark.Black) {
            oppMark = Mark.Red;
        } else {
            oppMark = Mark.Black;
        }

        int markCaptures = getCaptures(mark);
        int oppCaptures = getCaptures(oppMark);
        int maxConnectedMark = getMaxConnected(mark);
        int maxConnectedOpp = getMaxConnected(oppMark);

        if(markCaptures == 5 || maxConnectedMark == 5){
            return 100000;
        }

        if(oppCaptures == 5 || maxConnectedOpp == 5){
            return -100000;
        }

        int markScore = maxConnectedMark * (markCaptures + 1);
        int oppScore = maxConnectedOpp * (oppCaptures + 1);

        return markScore - oppScore;
    }

    public ArrayList<Move> getAllMarks(Mark mark){
        ArrayList<Move> moves = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(board[i][j] == mark){
                    moves.add(new Move(i,j,mark));
                }
            }
        }

        return moves;
    }

    public ArrayList<Move> getPossibleMoves(Mark mark){
        ArrayList<Move> moves = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(board[i][j] == Mark.Empty){
                    moves.add(new Move(i,j,mark));
                }
            }
        }

        return moves;
    }

    public ArrayList<Move> getPossibleMovesRedSecondTurn(){
        ArrayList<Move> moves = new ArrayList<>();

        for(int i = 0; i < 15; i++){
            for(int j = 0; j < 5; j++){
                if(board[i][j].equals(Mark.Empty)){
                    moves.add(new Move(i,j,Mark.Red));
                }
            }
        }
        
        for(int i = 0; i < 5; i++){
            for(int j = 5; j < 15; j++){
                if(board[i][j].equals(Mark.Empty)){
                    moves.add(new Move(i,j,Mark.Red));
                }
            }
        }

        for(int i = 5; i < 10; i++){
            for(int j = 10; j < 15; j++){
                if(board[i][j].equals(Mark.Empty)){
                    moves.add(new Move(i,j,Mark.Red));
                }
            }
        }

        for(int i = 10; i < 15; i++){
            for(int j = 5; j < 15; j++){
                if(board[i][j].equals(Mark.Empty)){
                    moves.add(new Move(i,j,Mark.Red));
                }
            }
        }

        return moves;
    }



    public static boolean isInbound(int col, int row){
        if(col < 0 || col >= 15 || row < 0 || row >= 15){
            return false;
        }

        return true;
    }

    public int getMaxConnected(Mark mark){
        ArrayList<Move> moves = getAllMarks(mark);
        int max = 0;

        for(Move move : moves){
            int current = 0;
            for(Move m : moves){
                if(move == m){
                    continue;
                }

                if(move.isClose(m)){
                    current++;
                }
            }
            
            if(current > max){
                max = current;
            }
        }
        
        return max;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < 15; i++){
            sb.append("[");
            for(int j = 0; j < 15; j++){
                if(board[i][j].equals(Mark.Empty)){
                    sb.append("E");
                } else if(board[i][j].equals(Mark.Red)){
                    sb.append("R");
                } else{
                    sb.append("B");
                }
            }

            sb.append("]");
            sb.append("\n");
        }

        return sb.toString();
    }

}
