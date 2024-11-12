import java.util.ArrayList;

// IMPORTANT: Il ne faut pas changer la signature des méthodes
// de cette classe, ni le nom de la classe.
// Vous pouvez par contre ajouter d'autres méthodes (ça devrait 
// être le cas)
class Board
{
    private Mark[][] board;
    private static final int SIZE = 15;
    private static final int winLength = 5;
    private static final int firstMoveRougeLimit = 5;
    private boolean isReseauPlayerRouge = false;
    private boolean firstMoveReseauPlayerRouge = false;

    // Ne pas changer la signature de cette méthode
    public Board() {
        board = new Mark[SIZE][SIZE];

    }

    public Mark[][] getBoard(){
        return this.board;
    }
    public void checkFirstMove (ReseauPlayer reseauPlayer){
        if (reseauPlayer.getMark() == Mark.R){
            System.out.println("[checkFirstMove] Reseau player is rouge");
            isReseauPlayerRouge = true;
        }
    }
    //Generateur de movements
    public ArrayList<Move> generateValidMoves() {
        System.out.println("[generateValidMoves] Generating valid moves");
        ArrayList<Move> validMoves = new ArrayList<>();
    
        int centerStart = (SIZE - firstMoveRougeLimit) / 2;
        int centerEnd = (SIZE + firstMoveRougeLimit) / 2 - 1;

        // Skip center moves for the first move if the reseau player is rouge and goes first
        boolean skipCenter = isReseauPlayerRouge && firstMoveReseauPlayerRouge;
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null) {
                    if (skipCenter && i >= centerStart && i <= centerEnd && j >= centerStart && j <= centerEnd) {
                        System.out.println("fuck");
                        continue;
                    }
                    validMoves.add(new Move(i, j));
                }
            }
        }
        System.out.println("[generateValidMoves] Amount of valid moves :  " + validMoves.size());
        for (Move m : validMoves){
            System.out.print(m + "-");
        }
        
        if (skipCenter) {
            firstMoveReseauPlayerRouge = false;
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
            System.out.println("\r\n[board.play] Played move: " + m + " with mark: " + mark);
        } else {
            System.out.println("[board.play] La case est déjà occupée !");
        }
    }

    public void undo(Move m){
        int row = m.getRow();
        int col = m.getCol();
        board[row][col] = null;
        System.out.println("[board.undo] Undid move: " + m);
    }

    public boolean checkWin(Mark mark) {
        for (int i = 0; i < SIZE; i++){
            for (int j = 0; j < SIZE; j++){
                // check horizontal condition
                if (j <= SIZE - winLength && board[i][j] == mark && board[i][j + 1] == mark && board[i][j + 2] == mark && board[i][j + 3] == mark && board[i][j + 4] == mark) {
                    return true;
                }
                // check vertical condition
                if (i <= SIZE - winLength && board[i][j] == mark && board[i + 1][j] == mark && board[i + 2][j] == mark && board[i + 3][j] == mark && board[i + 4][j] == mark) {
                    return true;
                }
                // check diagonal (top-left to bottom-right) condition
                if (i <= SIZE - winLength && j <= SIZE - winLength && board[i][j] == mark && board[i + 1][j + 1] == mark && board[i + 2][j + 2] == mark && board[i + 3][j + 3] == mark && board[i + 4][j + 4] == mark) {
                    return true;
                }
                // check diagonal (top-right to bottom-left) condition
                if (i <= SIZE - winLength && j >= winLength - 1 && board[i][j] == mark && board[i + 1][j - 1] == mark && board[i + 2][j - 2] == mark && board[i + 3][j - 3] == mark && board[i + 4][j - 4] == mark) {
                    return true;
            }

            }
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
        System.out.print("[printBoard] Board:\n\r");
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
