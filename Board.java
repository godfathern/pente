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
            isReseauPlayerRouge = true;
        }
    }
    //Generateur de movements
    public ArrayList<Move> generateValidMoves() {
        ArrayList<Move> validMoves = new ArrayList<>();
    
        int centerStart = (SIZE - firstMoveRougeLimit) / 2;
        int centerEnd = (SIZE + firstMoveRougeLimit) / 2 - 1;
        
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if (board[i][j] == null) {
                    if (isReseauPlayerRouge && firstMoveReseauPlayerRouge) {
                        if (i >= centerStart && i <= centerEnd && j >= centerStart && j <= centerEnd) {
                            System.out.println("fuck");
                            continue;
                            
                        }
                    }
                    validMoves.add(new Move(i, j));
                }
            }
        }
        System.out.println("Amount of valid moves :  " + validMoves.size());
        for (Move m : validMoves){
            System.out.print(m + "-");
        }
        System.out.println("------------------------");
        
        if (isReseauPlayerRouge && firstMoveReseauPlayerRouge) {
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
        } else {
            System.out.println("La case est déjà occupée !");
        }
    }

    public void undo(Move m){
        int row = m.getRow();
        int col = m.getCol();
        board[row][col] = null;
    }

    public boolean checkWin(Mark mark) {
        for (int i = 0; i <= (SIZE - winLength); i++){
            for (int j = 0; j <= (SIZE - winLength); j++){
                if (board[i][j+0] == mark && board[i][j+1] == mark && board[i][j+2] == mark && board[i][j+3] == mark && 
                board[i][j+4] == mark) {
                    return true;
                }
                if (board[i+0][j] == mark && board[i+1][j] == mark && board[i+2][j] == mark && board[i+3][j] == mark && 
                board[i+4][j] == mark) {
                    return true;
                }
                if (board[i+0][j+0] == mark && board[i+1][j+1] == mark && board[i+2][j+2] == mark && board[i+3][j+3] == mark && 
                board[i+4][j+4] == mark) {
                    return true;
                }
                if (j >= winLength - 1 && board[i][j] == mark && board[i + 1][j - 1] == mark &&
                board[i + 2][j - 2] == mark && board[i + 3][j - 3] == mark && board[i + 4][j - 4] == mark) {
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
