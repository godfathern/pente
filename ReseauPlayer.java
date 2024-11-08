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
        numExploredNodes = 0;
        int bestScore = Integer.MIN_VALUE;
        ArrayList<Move> bestMoves = new ArrayList<>();
        for (Move move : board.generateValidMoves()) {
            board.play(move, markReseauPlayer);
            int score = minmax(board, false);
            board.undo(move);
            if (score > bestScore) {
                bestScore = score;
                bestMoves.clear();
                bestMoves.add(move);
            } else if (score == bestScore) {
                bestMoves.add(move);
            }
        }
        System.out.println("sizeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee" + bestMoves.size());
        System.out.println("Number of nodes : " + numExploredNodes);
        return bestMoves;
    }

    private int minmax(Board board, boolean isMaximizing) {
        numExploredNodes++;

        if (board.checkWin(markReseauPlayer)) {
            System.out.println("winnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn");
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
                int score = minmax(board, false);
                board.undo(move);
                bestScore = Math.max(bestScore, score);
            }
            return bestScore;
        } else {
            int bestScore = Integer.MAX_VALUE;
            for (Move move : board.generateValidMoves()) {
                board.play(move, markReseauPlayer.getOpponent());
                int score = minmax(board, true);
                board.undo(move);
                bestScore = Math.min(bestScore, score);
            }
            return bestScore;
        }
    }

    // Retourne la liste des coups possibles. Cette liste contient
    // plusieurs coups possibles si et seuleument si plusieurs coups
    // ont le même score.
    // public ArrayList<Move> getNextMoveAB(Board board) {
    //     numExploredNodes = 0;
    //     int bestScore = Integer.MIN_VALUE;
    //     ArrayList<Move> bestMoves = new ArrayList<>();

    //     int alpha = Integer.MIN_VALUE;
    //     int beta = Integer.MAX_VALUE;

    //     for (Move move : board.generateValidMoves()) {
    //         board.play(move, markReseauPlayer);
    //         int score = alphaBeta(board, false, alpha, beta);
    //         board.undo(move);

    //         if (score > bestScore) {
    //             bestScore = score;
    //             bestMoves.clear();
    //             bestMoves.add(move);
    //         } else if (score == bestScore) {
    //             bestMoves.add(move);
    //         }
    //     }
    //     System.out.println("Number of nodes : " + numExploredNodes);
    //     return bestMoves;
    // }

    // private int alphaBeta(Board board, boolean isMaximizing, int alpha, int beta) {
    //     numExploredNodes++;

    //     if (board.checkWin(markReseauPlayer)) {
    //         return 100;
    //     } else if (board.checkWin(markReseauPlayer.getOpponent())) {
    //         return -100;
    //     } else if (board.isFull()) {
    //         return 0;
    //     }

    //     if (isMaximizing) {
    //         int bestScore = Integer.MIN_VALUE;
    //         for (Move move : board.generateValidMoves()) {
    //             board.play(move, markReseauPlayer);
    //             int score = alphaBeta(board, false, alpha, beta);
    //             board.undo(move);
    //             bestScore = Math.max(bestScore, score);
    //             alpha = Math.max(alpha, bestScore);
    //             if (beta <= alpha) {
    //                 break;
    //             }
    //         }
    //         return bestScore;
    //     } else {
    //         int bestScore = Integer.MAX_VALUE;
    //         for (Move move : board.generateValidMoves()) {
    //             board.play(move, markReseauPlayer.getOpponent());
    //             int score = alphaBeta(board, true, alpha, beta);
    //             board.undo(move);
    //             bestScore = Math.min(bestScore, score);
    //             beta = Math.min(beta, bestScore);
    //             if (beta <= alpha) {
    //                 break;
    //             }
    //         }
    //         return bestScore;
    //     }
    // }

    public Move selectBestMove(ReseauPlayer reseauPlayer, Board board) {
        ArrayList<Move> bestMoves = reseauPlayer.getNextMoveMinMax(board);
        System.out.println("Best move cooking!");
        if (bestMoves.size() == 1) {
            return bestMoves.get(0);
        }
        Random random = new Random();
        int index = random.nextInt(bestMoves.size());
        return bestMoves.get(index);
    }

    boolean firstMove = false;

    public String randomMove(Board board) {
        ArrayList <Move> validMoves = board.generateValidMoves();
        if (markReseauPlayer == Mark.R && firstMove == false) {
            firstMove = true;
            return "H8";
        }
        Random random = new Random();
        Move randomMove = validMoves.get(random.nextInt(validMoves.size()));
        MoveConverter.convertMoveToString(randomMove);
        return MoveConverter.convertMoveToString(randomMove);


        
    }


}
