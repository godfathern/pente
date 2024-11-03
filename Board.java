import java.util.ArrayList;

// IMPORTANT: Il ne faut pas changer la signature des méthodes
// de cette classe, ni le nom de la classe.
// Vous pouvez par contre ajouter d'autres méthodes (ça devrait 
// être le cas)
class Board
{
    private Mark[][] board;
    private static final int SIZE = 15;

    // Ne pas changer la signature de cette méthode
    public Board() {
        board = new Mark[SIZE][SIZE];

    }

    public ArrayList<Move> generateValidMoves(){
        ArrayList<Move> validMoves = new ArrayList<Move>();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null) {
                    validMoves.add(new Move(i, j));
                }
            }
        }
        return validMoves;
    }

    // Place la pièce 'mark' sur le plateau, à la
    // position spécifiée dans Move
    //
    // Ne pas changer la signature de cette méthode
    public void play(Move m, Mark mark){
        int row = m.getRow();
        int col = m.getCol();
        
        // On vérifie si la case est libre avant de jouer
        if (board[row][col] == null) {
            board[row][col] = mark;
        } else {
            System.out.println("La case est déjà occupée !");
        }
    }

    public void undo(Move m){
        int row = m.getRow();
        int col = m.getCol();
        board[row][col] = null;
    }

    // retourne  100 pour une victoire
    //          -100 pour une défaite
    //           0   pour un match nul
    // Ne pas changer la signature de cette méthode


    public int evaluate(Mark mark) {
        // Vérifier si le joueur actuel (mark) a gagné
        if (checkWin(mark)) {
            return 100; // Victoire
        }
        // Vérifier si l'adversaire a gagné
        Mark opponent = (mark == Mark.R) ? Mark.N : Mark.R;
        if (checkWin(opponent)) {
            return -100; // Défaite
        }
        // Si aucune victoire ou défaite, on regarde si c'est un match nul
        if (isFull()) {
            return 0; // Match nul
        }
        // Jeu en cours, aucune évaluation
        return 0;
    }

    public boolean checkWin(Mark mark) {
        // Vérification des lignes
        for (int i = 0; i < SIZE; i++) {
            if (board[i][0] == mark && board[i][1] == mark && board[i][2] == mark) {
                return true;
            }
        }
        // Vérification des colonnes
        for (int i = 0; i < SIZE; i++) {
            if (board[0][i] == mark && board[1][i] == mark && board[2][i] == mark) {
                return true;
            }
        }
        // Vérification des diagonales
        if (board[0][0] == mark && board[1][1] == mark && board[2][2] == mark) {
            return true;
        }
        if (board[0][2] == mark && board[1][1] == mark && board[2][0] == mark) {
            return true;
        }
        return false;
    }

    public boolean isFull() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null) {
                    return false;
                }
            }
        }
        return true;
    }

    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null) {
                    System.out.print("- ");
                } else {
                    System.out.print(board[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public boolean isValidMove(Move move) {
        return true;
    }

}
