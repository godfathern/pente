import java.util.ArrayList;
import java.util.Random;

public class CPU {
    private final Mark _mark;
    private final static int MAX_DEPTH = 3;
    private int numberOfExploredNodes = 0;

    public CPU(Mark mark) {
        _mark = mark;
    }

    public Mark get_mark() {
        return _mark;
    }

    public Move getNextMove(Board board) {
        ArrayList<Move> moves = getNextMoveNegaMax(board);
        //ArrayList<Move> moves = board.getPossibleMoves(_mark);
        Random random = new Random();
        int r = random.nextInt(moves.size());
        return moves.get(r);
    }

    public ArrayList<Move> getNextMoveNegaMax(Board board) {    
        numberOfExploredNodes = 0;
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
            board.play(move);

            if (Zobrist.hashExists()) {
                move.setScore(Zobrist.getScore());
            } else {
                move.setScore(negaMax(board, _mark, -Board.WINNING_SCORE, Board.WINNING_SCORE, MAX_DEPTH));
                Zobrist.addEntry(move.getScore());
            }

            board.undo(move);

            if (move.getScore() > max) {
                max = move.getScore();
                moves.clear();
                moves.add(move);
            } else if (move.getScore() == max && !moves.contains(move)) {
                moves.add(move);
            }

            // Check if the time limit has been exceeded
            if (System.currentTimeMillis() - Client.getStartTime() > Client.getTimeLimit()) {
                System.out.println("Time limit exceeded while getting next move.");
                break;
            }
        }

        System.out.println("[getNextMoveNegaMax()] Number of Explored Nodes: " + numberOfExploredNodes);
        return moves;
    }

    public int negaMax(Board board, Mark mark, int alpha, int beta, int depth) {
        numberOfExploredNodes++;
        if (depth == 0 ||
                board.checkWin(mark.getOpponent()) ||
                board.checkWin(mark) ||
                System.currentTimeMillis() - Client.getStartTime() > Client.getTimeLimit()) {

            return board.evaluate(mark);
        }

        ArrayList<Move> possibleMoves = board.getPossibleMoves(mark.getOpponent());

        int maxScore = Integer.MIN_VALUE;
        for (Move possibleMove : possibleMoves) {            
            board.play(possibleMove);
            possibleMove.setScore(-negaMax(board, mark.getOpponent(), -beta, -alpha, depth - 1));
            board.undo(possibleMove);

            maxScore = Math.max(maxScore, possibleMove.getScore());
            alpha = Math.max(alpha, maxScore);

            if (alpha >= beta) {
                break;
            }
        }

        return maxScore;
    }
}
