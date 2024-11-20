import java.util.ArrayList;
import java.util.Random;

public class CPU {    
    private final Mark cpu;
    private final Mark opponent;
    private final static int MAX_DEPTH = 3;
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

        Random R = new Random();
        int randomIndex = R.nextInt(moves.size());
        return moves.get(randomIndex);
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
            int score = -minmaxAB(board, opponent, -Board.WINNING_SCORE - 1, Board.WINNING_SCORE + 1, MAX_DEPTH);
            System.out.println("Score minmaxAB: " + score + " Move: " + move);

            boolean moveFound = false;            
            for (Move m : moves) {
                if(m.getCol() == move.getCol() && m.getRow() == move.getRow()) {
                    moveFound = true;
                }
            }

            if(score > max) {
                max = score;
                moves.clear();
                moves.add(move);
            } else if(score == max && !moveFound) {
                moves.add(move);
            }
        }

        return moves;
    }

    private int minmaxAB(Board board, Mark mark, int alpha, int beta, int depth) {
        int eval = board.evaluate(mark);
        if (depth == 0 || eval <= -Board.WINNING_SCORE || eval >= Board.WINNING_SCORE) {
            return eval;
        }

        ArrayList<Move> possibleMoves = board.getPossibleMoves(mark);
        if (mark == cpu) {
            int maxScore = Integer.MIN_VALUE;

            for (Move possibleMove : possibleMoves) {                
                board.play(possibleMove);
                maxScore = Math.max(maxScore, minmaxAB(board, opponent, alpha, beta, depth - 1));
                board.undo(possibleMove);

                if (maxScore >= beta) {
                    break;
                }
                
                alpha = Math.max(alpha, maxScore);
            }

            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;
            for (Move possibleMove : possibleMoves) {
                board.play(possibleMove);
                minScore = Math.min(minScore, minmaxAB(board, cpu, alpha, beta, depth - 1));                
                board.undo(possibleMove);

                if (minScore <= alpha) {
                    break;
                }
                
                beta = Math.min(beta, minScore);
            }

            return minScore;
        }
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
}
