import java.util.ArrayList;

public class Board {
    // 0: Empty, 1: Red, 2: Black
    private final Mark[][] board;
    private int turns;

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

    private int blackEat = 0;
    private int redEat = 0;

    public int getEatings(Mark mark){
        if(mark == Mark.Black){
            return blackEat;
        }
        if(mark == Mark.Red){
            return redEat;
        }
        return 0;
    }

    // negative if black wins, positive if red wins
    public int evaluate(){
        if(redEat == 5){
            return Integer.MAX_VALUE;
        }
        if(blackEat == 5){
            return Integer.MIN_VALUE;
        }
        int maxConnectedRed = getMaxConnected(Mark.Red);
        int maxConnectedBlack = getMaxConnected(Mark.Black);
        if(maxConnectedRed == 5){
            return Integer.MAX_VALUE;
        }
        if(maxConnectedBlack == 5){
            return Integer.MIN_VALUE;
        }

        int redScore = maxConnectedRed * (redEat + 1);
        int blackScore = maxConnectedBlack * (blackEat + 1);

        return redScore - blackScore;
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
                }
                else if(board[i][j].equals(Mark.Red)){
                    sb.append("R");
                }
                else{
                    sb.append("B");
                }
            }
            sb.append("]");
            sb.append("\n");
        }

        return sb.toString();
    }

}
