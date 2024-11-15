import java.util.ArrayList;

public class CPU {
    private final Mark cpu;
    private final Mark opponent;
    private final static int MAX_DEPTH = 1;
    private int numOfExploredNodes = 0;

    public CPU(Mark mark) {
        cpu = mark;
        opponent = mark == Mark.Red ? Mark.Black : Mark.Red;
    }

    public Mark getCpu() {
        return cpu;
    }

    public Mark getOpponent() {
        return opponent;
    }

    public Move getNextMove(Board board) {
        ArrayList<Move> moves = getNextMovesAB(board);
        return moves.get(0);
    }

    public ArrayList<Move> getNextMovesAB(Board board) {
        ArrayList<Move> moves = new ArrayList<>();

        // First move
        if (cpu == Mark.Red && board.getTurns() == 0) {
            moves.add(new Move("H8", Mark.Red));
            return moves;
        }

        ArrayList<Move> possibleMoves;
        if (cpu == Mark.Red && board.getTurns() == 2) {
            possibleMoves = board.getPossibleMovesRedSecondTurn();
        } else {
            possibleMoves = board.getPossibleMoves(cpu);
        }

        int max = Integer.MIN_VALUE;
        for (Move move : possibleMoves) {
            board.play(move);

            int score = minmaxAB(board, opponent, -100001, 100001, MAX_DEPTH);
            System.out.println("Score minmaxAB: " + score);
            if (score > max) {
                max = score;
                moves.clear();
                moves.add(move);
            }
            if (score == max) {
                moves.add(move);
            }

            board.undo(move);
        }

        return moves;
    }

    public ArrayList<Move> getNextMovesMinMax(Board board) {
        ArrayList<Move> moves = new ArrayList<>();

        // First move
        if (cpu == Mark.Red && board.getTurns() == 0) {
            moves.add(new Move("H8", Mark.Red));
            return moves;
        }

        ArrayList<Move> possibleMoves;
        if (cpu == Mark.Red && board.getTurns() == 2) {
            possibleMoves = board.getPossibleMovesRedSecondTurn();
        } else {
            possibleMoves = board.getPossibleMoves(cpu);
        }

        int max = cpu == Mark.Red ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Move move : possibleMoves) {
            board.play(move);

            int score = minmax(board, opponent, MAX_DEPTH);
            if (score > max) {
                max = score;
                moves.clear();
                moves.add(move);
            }
            if (score == max) {
                moves.add(move);
            }

            board.undo(move);
        }

        return moves;
    }

    private int minmax(Board board, Mark mark, int depth) {
        int eval = board.evaluate(mark);

        if (depth == 0 || eval == Integer.MAX_VALUE || eval == Integer.MIN_VALUE) {
            return eval;
        }

        depth--;

        ArrayList<Move> possibleMoves = board.getPossibleMoves(mark);
        if (mark == cpu) {
            int maxScore = Integer.MIN_VALUE;

            for (Move move : possibleMoves) {
                board.play(move);

                int score = minmax(board, opponent, depth);
                maxScore = Math.max(maxScore, score);

                board.undo(move);
            }

            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;

            for (Move move : possibleMoves) {
                board.play(move);

                int score = minmax(board, cpu, depth);
                minScore = Math.min(minScore, score);

                board.undo(move);
            }
            return minScore;
        }
    }

    private int minmaxAB(Board board, Mark mark, int alpha, int beta, int depth) {
        int eval = board.evaluate(mark);

        if (depth == 0 || eval == -100000 || eval == 100000) {
            return eval;
        }

        depth--;
        
        ArrayList<Move> possibleMoves = board.getPossibleMoves(mark);
        if (mark == cpu) {
            int maxScore = Integer.MIN_VALUE;
            
            for (Move move : possibleMoves) {
                board.play(move);

                int score = minmaxAB(board, opponent, alpha, beta, depth);
                maxScore = Math.max(maxScore, score);
                alpha = Math.max(alpha, maxScore);
                
                board.undo(move);

                if (maxScore >= beta) {
                    return maxScore;
                }
            }

            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;

            for (Move move : possibleMoves) {
                board.play(move);
            
                int score = minmaxAB(board, cpu, alpha, Math.min(beta, minScore), depth);
                minScore = Math.min(minScore, score);
                beta = Math.min(beta, minScore);
            
                board.undo(move);
            
                if (minScore <= alpha) {
                    return minScore;
                }
            }

            return minScore;
        }
    }

}
