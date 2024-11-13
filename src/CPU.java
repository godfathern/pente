import java.util.ArrayList;

public class CPU {
    private final Mark cpu;
    private final Mark opponent;
    private final static int MAX_DEPTH = 3;

    public CPU(Mark mark){
        cpu = mark;
        opponent = mark == Mark.Red ? Mark.Black : Mark.Red;
    }

    public Mark getCpu(){
        return cpu;
    }
    public Mark getOpponent(){
        return opponent;
    }

    public Move getNextMove(Board board){
        ArrayList<Move> moves = getNextMovesAB(board);
        return moves.get(0);
    }

    public ArrayList<Move> getNextMovesAB(Board board){
        ArrayList<Move> moves = new ArrayList<>();

        if(cpu == Mark.Red && board.getTurns() == 0){
            moves.add(new Move("H8",Mark.Red));
            return moves;
        }
        ArrayList<Move> possibleMoves;
        if(cpu == Mark.Red && board.getTurns() == 2){
            possibleMoves = board.getPossibleMovesRedSecondTurn();
        }
        else{
            possibleMoves = board.getPossibleMoves(cpu);
        }

        int max = Integer.MIN_VALUE;
        for(Move move : possibleMoves){
            board.play(move);
            depth = MAX_DEPTH;
            int score = minmaxAB(board, opponent, Integer.MIN_VALUE, Integer.MAX_VALUE);
            if(score > max){
                moves.clear();
                moves.add(move);
            }
            if(score == max){
                moves.add(move);
            }
            
            max = score;
            board.undo(move);
        }

        return moves;
    }

    public ArrayList<Move> getNextMovesMinMax(Board board){
        ArrayList<Move> moves = new ArrayList<>();

        if(cpu == Mark.Red && board.getTurns() == 0){
            moves.add(new Move("H8",Mark.Red));
            return moves;
        }
        ArrayList<Move> possibleMoves;
        if(cpu == Mark.Red && board.getTurns() == 2){
            possibleMoves = board.getPossibleMovesRedSecondTurn();
        }
        else{
            possibleMoves = board.getPossibleMoves(cpu);
        }

        int max = cpu == Mark.Red ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for(Move move : possibleMoves){
            board.play(move);
            depth = MAX_DEPTH;
            int score = minmax(board,opponent);
            if(score > max){
                moves.clear();
                moves.add(move);
            }
            if(score == max){
                moves.add(move);
            }
            
            max = score;
            board.undo(move);
        }

        return moves;
    }

    private int depth = MAX_DEPTH;
    private int minmax(Board board, Mark mark){
        int eval = board.evaluate();
        if(depth == 0 || eval == Integer.MAX_VALUE || eval == Integer.MIN_VALUE){
            return eval;
        }
        depth--;
        if(mark == cpu){
            int maxScore = Integer.MIN_VALUE;
            for(Move move : board.getPossibleMoves(cpu)){
                board.play(move);
                int score = minmax(board, opponent);
                maxScore = Math.max(maxScore, score);
                board.undo(move);
            }
            return maxScore;
        }
        else{
            int minScore = Integer.MAX_VALUE;
            for(Move move : board.getPossibleMoves(opponent)){
                board.play(move);
                int score = minmax(board, cpu);
                minScore = Math.min(minScore, score);
                board.undo(move);
            }
            return minScore;
        }
    }

    private int minmaxAB(Board board, Mark mark, int alpha, int beta){
        int eval = board.evaluate();
        if(depth == 0 || eval == Integer.MAX_VALUE || eval == Integer.MIN_VALUE){
            return eval;
        }
        depth--;
        if(mark == cpu){
            int maxScore = Integer.MIN_VALUE;
            for(Move move : board.getPossibleMoves(cpu)){
                board.play(move);
                int score = minmaxAB(board, opponent, Math.max(alpha, maxScore), beta);
                maxScore = Math.max(maxScore, score);
                board.undo(move);
                if(maxScore >= beta){
                    return maxScore;
                }

            }
            return maxScore;
        }
        else{
            int minScore = Integer.MAX_VALUE;
            for(Move move : board.getPossibleMoves(opponent)){
                board.play(move);
                int score = minmaxAB(board, cpu, alpha, Math.min(beta, minScore));
                minScore = Math.min(minScore, score);
                board.undo(move);
                if(minScore <= alpha){
                    return minScore;
                }
            }
            return minScore;
        }
    }

}
