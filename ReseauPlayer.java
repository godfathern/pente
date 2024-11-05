import java.util.ArrayList;
import java.util.Random;

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
        System.out.println("Number of nodes : " + numExploredNodes);
        return bestMoves;
    }

    private int minmax(Board board, boolean isMaximizing) {
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

    public Move selectBestMove(ArrayList<Move> bestMoves) {
        if (bestMoves.size() == 1) {
            return bestMoves.get(0);
        }
        Random random = new Random();
        int index = random.nextInt(bestMoves.size());
        return bestMoves.get(index);
    }

    char[] letters = new char[15];
    int[] numbers = new int[15];
    boolean firstMove = false;

    public String randomMove() {
        for (char letter = 'A'; letter <= 'O'; letter++) {
            letters[letter - 'A'] = letter;
        }
        for (int i = 0; i < 15; i++) {
            numbers[i] = i + 1;
        }
        if (markReseauPlayer == Mark.R && firstMove == false) {
            firstMove = true;
            return "H8";
        }
        Random random = new Random();
        char randomLetter = letters[random.nextInt(letters.length)];
        int randomNumber = numbers[random.nextInt(numbers.length)];
        return randomLetter + String.valueOf(randomNumber);
    }


}
