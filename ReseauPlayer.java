import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.text.StyledEditorKit;

// IMPORTANT: Il ne faut pas changer la signature des méthodes
// de cette classe, ni le nom de la classe.
// Vous pouvez par contre ajouter d'autres méthodes (ça devrait 
// être le cas)
class ReseauPlayer {

    // Contient le nombre de noeuds visités (le nombre
    // d'appel à la fonction MinMax ou Alpha Beta)
    // Normalement, la variable devrait être incrémentée
    // au début de votre MinMax ou Alpha Beta.
    private int numExploredNodes;
    private Mark markReseauPlayer;

    // Le constructeur reçoit en paramètre le
    // joueur MAX (X ou O)
    public ReseauPlayer(Mark markReseauPlayer) {
        this.markReseauPlayer = markReseauPlayer;
    }

    public Mark getOpponentMark() {
        return this.markReseauPlayer.getOpponent();
    }

    public Mark getMark() {
        return this.markReseauPlayer;
    }

    // Ne pas changer cette méthode
    public int getNumOfExploredNodes() {
        return numExploredNodes;
    }

    // Retourne la liste des coups possibles. Cette liste contient
    // plusieurs coups possibles si et seuleument si plusieurs coups
    // ont le même score.
    public ArrayList<Move> getNextMoveMinMax(Board board) {
        System.out.println("[getNextMoveMinMax] Getting next move with MinMax...");
        numExploredNodes = 0;
        int bestScore = Integer.MIN_VALUE;
        ArrayList<Move> bestMoves = new ArrayList<>();
        ArrayList<Move> validMoves = board.generateValidMoves();
        for (Move move : validMoves) {
            board.play(move, markReseauPlayer);
            System.out.println("[getNextMoveMinMax] Played move: " + move);
        
            int score = minmax(board, false);
        
            System.out.println("[getNextMoveMinMax] Score for move: " + move + " is " + score);
        
            board.undo(move);
            System.out.println("[getNextMoveMinMax] Undid move: " + move);

            if (score > bestScore) {
                bestScore = score;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (score == bestScore) {
                bestMoves.add(move);
            }
        }
        System.out.println("[getNextMoveMinMax] bestMoves size: " + bestMoves.size());
        System.out.println("[getNumberMoveMinMax] Number of nodes: " + numExploredNodes);
        return bestMoves;
    }

    private int minmax(Board board, boolean isMaximizing) {
        System.out.println("[minmax] Minmaxing...");
        numExploredNodes++;

        if (board.checkWin(markReseauPlayer)) {
            System.out.println("[minmax] win");
            return 100;
        } else if (board.checkWin(markReseauPlayer.getOpponent())) {
            System.out.println("[minmax] lose");
            return -100;
        } else if (board.isFull()) {
            System.out.println("[minmax] full");
            return 0;
        }

        System.out.println("[minmax] no win, no lose, no full");

        if (isMaximizing) {
            System.out.println("[minmax] Maximizing...");
            int bestScore = Integer.MIN_VALUE;
            for (Move move : board.generateValidMoves()) {
                System.out.println("\r\n[minmax - isMaximizing == true] Minimizing move: " + move);
                board.play(move, markReseauPlayer.getOpponent());
                System.out.println("[minmax - isMaximizing == true] Played move: " + move + " with mark: " + markReseauPlayer.getOpponent());
                int score = minmax(board, false);
                System.out.println("[minmax - isMaximizing == true] Score for move: " + move + " is: " + score);
                board.undo(move);
                System.out.println("[minmax - isMaximizing == true] Undid move: " + move);
            }
            return bestScore;
        } else {
            System.out.println("[minmax] Minimizing...");
            int bestScore = Integer.MAX_VALUE;
            for (Move move : board.generateValidMoves()) {
                System.out.println("\r\n[minmax - isMaximizing == false] Minimizing move: " + move);
                board.play(move, markReseauPlayer.getOpponent());
                System.out.println("[minmax - isMaximizing == false] Played move: " + move + " with mark: " + markReseauPlayer.getOpponent());
                int score = minmax(board, true);
                System.out.println("[minmax - isMaximizing == false] Score for move: " + move + " is: " + score);
                board.undo(move);
                System.out.println("[minmax - isMaximizing == false] Undid move: " + move);
                bestScore = Math.min(bestScore, score);
            }
            return bestScore;
        }
    }

    // Retourne la liste des coups possibles. Cette liste contient
    // plusieurs coups possibles si et seuleument si plusieurs coups
    // ont le même score.
    public ArrayList<Move> getNextMoveAB(Board board) {
        numExploredNodes = 0;
        int bestScore = Integer.MIN_VALUE;
        ArrayList<Move> bestMoves = new ArrayList<>();

        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (Move move : board.generateValidMoves()) {
            board.play(move, markReseauPlayer);
            int score = alphaBeta(board, false, alpha, beta);
            board.undo(move);

            if (score > bestScore) {
                bestScore = score;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (score == bestScore) {
                bestMoves.add(move);
            }
        }
        System.out.println("Number of nodes : " + numExploredNodes);
        return bestMoves;
    }

    private int alphaBeta(Board board, boolean isMaximizing, int alpha, int beta) {
        numExploredNodes++;

        if (board.checkWin(markReseauPlayer)) {
            return 100;
        } else if (board.checkWin(markReseauPlayer.getOpponent())) {
            return -100;
        } else if (board.isFull()) {
            return 0;
        }

        if (isMaximizing) {
            int bestScore = Integer.MIN_VALUE;
            for (Move move : board.generateValidMoves()) {
                board.play(move, markReseauPlayer);
                int score = alphaBeta(board, false, alpha, beta);
                board.undo(move);
                bestScore = Math.max(bestScore, score);
                alpha = Math.max(alpha, bestScore);
                if (beta <= alpha) {
                    break;
                }
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (Move move : board.generateValidMoves()) {
                board.play(move, markReseauPlayer.getOpponent());
                int score = alphaBeta(board, true, alpha, beta);
                board.undo(move);
                bestScore = Math.min(bestScore, score);
                beta = Math.min(beta, bestScore);
                if (beta <= alpha) {
                    break;
                }
            }
            return bestScore;
        }
    }

    public Move selectBestMove(ReseauPlayer reseauPlayer, Board board) {
        System.out.println("[selectBestMove] Best move cooking!");
        ArrayList<Move> bestMoves = reseauPlayer.getNextMoveMinMax(board);
        if (bestMoves.size() == 1) {
            return bestMoves.get(0);
        }
        Random random = new Random();
        int index = random.nextInt(bestMoves.size());
        return bestMoves.get(index);
    }

    boolean firstMove = false;

    public String randomMove(Board board) {
        System.out.println("[randomMove] Generating random move...");
        ArrayList <Move> validMoves = board.generateValidMoves();
        if (markReseauPlayer == Mark.R && firstMove == false) {
            System.out.println("\n\r[randomMove] First move for rouge: " + validMoves.get(0)); // ignore the first 
            firstMove = true;
            return "H8";
        }
        Random random = new Random();
        Move randomMove = validMoves.get(random.nextInt(validMoves.size()));
        return MoveConverter.convertMoveToString(randomMove);        
    }
}
