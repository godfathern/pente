import java.util.ArrayList;
import java.util.Random;

public class CPU {    
    private final Mark _mark;
    private final static int MAX_DEPTH = 3;
    private int numOfExploredNodes = 0;

    public CPU(Mark mark) {
        _mark = mark;
    }

    public Mark get_mark() {
        return _mark;
    }

    public Move getNextMove(Board board) {
        // System.out.println("[getNextMove()] Getting Next Move for " + _mark);
        ArrayList<Move> moves = getNextMoveNegaMax(board);
        Random random = new Random();
        int r = random.nextInt(moves.size());
        return moves.get(r);
    }

    public ArrayList<Move> getNextMoveNegaMax(Board board) {
        // System.out.println("[getNextMoveNegaMax()] Getting Next Move with NegaMax for " + _mark); 
        ArrayList<Move> moves = new ArrayList<>();

        // First move
        if (_mark == Mark.Red && board.getTurns() == 0) {        
            moves.add(new Move("H8", Mark.Red));
            return moves;
        }

        ArrayList<Move> possibleMoves;
        possibleMoves = board.getPossibleMoves(_mark);        

        int max = Integer.MIN_VALUE;
         for (Move move : possibleMoves) {
            // System.out.println("[getNextMoveNegaMax()] Evaluating Node: " + move + ", Depth: " + MAX_DEPTH);
            board.play(move);
            move.setScore(negaMax(board, _mark, -Board.WINNING_SCORE, Board.WINNING_SCORE, MAX_DEPTH));
            board.undo(move);
            // System.out.println("Score negaMax: " + score + " Move: " + move);

            if(move.getScore() > max) {
                max = move.getScore();
                moves.clear();
                moves.add(move);
            } else if(move.getScore() == max && !moves.contains(move)) {
                moves.add(move);
            }
        }        

        return moves;
    }


    public int negaMax(Board board, Mark mark, int alpha, int beta, int depth) {
        // System.out.println("[negaMax()] Evaluating Board for: " + mark + ", Depth: " + depth);
        // System.out.println("[negaMax()] Alpha: " + alpha + ", Beta: " + beta);
        if(depth == 0) {
            return board.evaluate(mark);
        } else if (board.checkWin(mark) || board.checkWin(mark.getOpponent())) {
            return Board.WINNING_SCORE;
        }

        ArrayList<Move> possibleMoves = board.getPossibleMoves(mark.getOpponent());
        int maxScore = Integer.MIN_VALUE;
        for (Move possibleMove : possibleMoves) {
            // System.out.println("[()] Evaluating Node: " + possibleMove + ", Depth: " + depth);
            board.play(possibleMove);
            possibleMove.setScore(-negaMax(board, mark.getOpponent(), -beta, -alpha, depth - 1));
            // System.out.println("[getNextMoveNegaMax()] Score for " + possibleMove + ": " + possibleMove.getScore() + ", Depth: " + depth);
            board.undo(possibleMove);

            
            maxScore = Math.max(maxScore, possibleMove.getScore());
            alpha = Math.max(alpha, maxScore);
            // System.out.println("[getNextMoveNegaMax()] Alpha: " + alpha + ", MaxScore: " + maxScore);

            // Pruning
            if (alpha >= beta) {
                break;
            }
        }

        // System.out.println("[getNextMoveNegaMax()] Returning: " + maxScore);
        return maxScore;
    }

    public ArrayList<Move> getNextMovesAB(Board board) {
        ArrayList<Move> moves = new ArrayList<>();

        // First move
        if (_mark == Mark.Red && board.getTurns() == 0) {
            moves.add(new Move("H8", Mark.Red));
            return moves;
        }

        ArrayList<Move> possibleMoves;
        if (_mark == Mark.Red && board.getTurns() == 2) {
            possibleMoves = board.getPossibleMovesRedSecondTurn();
        } else {
            possibleMoves = board.getPossibleMoves(_mark);
        }

        int max = Integer.MIN_VALUE;
        for (Move move : possibleMoves) {
            board.play(move);
            int score = -minmaxAB(board, _mark.getOpponent(), -Board.WINNING_SCORE - 1, Board.WINNING_SCORE + 1, MAX_DEPTH);
            // System.out.println("Score minmaxAB: " + score + " Move: " + move);

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
        if (mark == _mark) {
            int maxScore = Integer.MIN_VALUE;

            for (Move possibleMove : possibleMoves) {                
                board.play(possibleMove);
                maxScore = Math.max(maxScore, minmaxAB(board, _mark.getOpponent(), alpha, beta, depth - 1));
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
                minScore = Math.min(minScore, minmaxAB(board, _mark, alpha, beta, depth - 1));                
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
        if (_mark == Mark.Red && board.getTurns() == 0) {
            moves.add(new Move("H8", Mark.Red));
            return moves;
        }

        ArrayList<Move> possibleMoves;
        if (_mark == Mark.Red && board.getTurns() == 2) {
            possibleMoves = board.getPossibleMovesRedSecondTurn();
        } else {
            possibleMoves = board.getPossibleMoves(_mark);
        }

        int max = _mark == Mark.Red ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        for (Move move : possibleMoves) {
            board.play(move);

            int score = minmax(board, _mark.getOpponent(), MAX_DEPTH);
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
        if (mark == _mark) {
            int maxScore = Integer.MIN_VALUE;

            for (Move move : possibleMoves) {
                board.play(move);

                int score = minmax(board, _mark.getOpponent(), depth);
                maxScore = Math.max(maxScore, score);

                board.undo(move);
            }

            return maxScore;
        } else {
            int minScore = Integer.MAX_VALUE;

            for (Move move : possibleMoves) {
                board.play(move);

                int score = minmax(board, _mark, depth);
                minScore = Math.min(minScore, score);

                board.undo(move);
            }
            return minScore;
        }
    }
}
