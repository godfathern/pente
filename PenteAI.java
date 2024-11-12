// import java.util.ArrayList;
// import java.util.List;

// public class PenteAI {
//     private static final int BOARD_SIZE = 15;
//     private static final int MAX_DEPTH = 4;
//     private static final int WIN_SCORE = 10000;
//     private static final int LOSS_SCORE = -10000;

//     public int[] findBestMove(int[][] board, int player) {
//         int bestScore = Integer.MIN_VALUE;
//         int[] bestMove = null;

//         for (int[] move : generateMoves(board)) {
//             board[move[0]][move[1]] = player;
//             int score = minimax(board, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false, player);
//             board[move[0]][move[1]] = 0;

//             if (score > bestScore) {
//                 bestScore = score;
//                 bestMove = move;
//             }
//         }
//         return bestMove;
//     }

//     private int minimax(int[][] board, int depth, int alpha, int beta, boolean maximizing, int player) {
//         int opponent = player == 1 ? 2 : 1;

//         if (depth == 0 || isGameOver(board)) return evaluateBoard(board, player);
//         if (maximizing) {
//             int maxEval = Integer.MIN_VALUE;
//             for (int[] move : generateMoves(board)) {
//                 board[move[0]][move[1]] = player;
//                 int eval = minimax(board, depth - 1, alpha, beta, false, player);
//                 board[move[0]][move[1]] = 0;
//                 maxEval = Math.max(maxEval, eval);
//                 alpha = Math.max(alpha, eval);
//                 if (beta <= alpha) break;
//             }
//             return maxEval;
//         } else {
//             int minEval = Integer.MAX_VALUE;
//             for (int[] move : generateMoves(board)) {
//                 board[move[0]][move[1]] = opponent;
//                 int eval = minimax(board, depth - 1, alpha, beta, true, player);
//                 board[move[0]][move[1]] = 0;
//                 minEval = Math.min(minEval, eval);
//                 beta = Math.min(beta, eval);
//                 if (beta <= alpha) break;
//             }
//             return minEval;
//         }
//     }

//     private List<int[]> generateMoves(int[][] board) {
//         List<int[]> moves = new ArrayList<>();
//         for (int i = 0; i < BOARD_SIZE; i++) {
//             for (int j = 0; j < BOARD_SIZE; j++) {
//                 if (board[i][j] == 0) moves.add(new int[]{i, j});
//             }
//         }
//         return moves;
//     }

//     private boolean isGameOver(int[][] board) {
//         return checkWin(board, 1) || checkWin(board, 2);
//     }

//     private boolean checkWin(int[][] board, int player) {
//         for (int i = 0; i < BOARD_SIZE; i++) {
//             for (int j = 0; j < BOARD_SIZE; j++) {
//                 if (checkLine(board, i, j, 1, 0, player) ||
//                     checkLine(board, i, j, 0, 1, player) ||
//                     checkLine(board, i, j, 1, 1, player) ||
//                     checkLine(board, i, j, 1, -1, player)) return true;
//             }
//         }
//         return false;
//     }

//     private boolean checkLine(int[][] board, int row, int col, int dRow, int dCol, int player) {
//         int count = 0;
//         for (int i = 0; i < 5; i++) {
//             int r = row + i * dRow;
//             int c = col + i * dCol;
//             if (r < 0 || r >= BOARD_SIZE || c < 0 || c >= BOARD_SIZE || board[r][c] != player) break;
//             count++;
//         }
//         return count == 5;
//     }

//     private int evaluateBoard(int[][] board, int player) {
//         int opponent = player == 1 ? 2 : 1;
//         int score = 0;
//         score += countLines(board, player, 5) * WIN_SCORE;
//         score -= countLines(board, opponent, 5) * WIN_SCORE;
//         score += countLines(board, player, 4) * 1000;
//         score -= countLines(board, opponent, 4) * 1000;
//         score += countLines(board, player, 3) * 100;
//         score -= countLines(board, opponent, 3) * 100;
//         return score;
//     }

//     private int countLines(int[][] board, int player, int length) {
//         int count = 0;
//         for (int i = 0; i < BOARD_SIZE; i++) {
//             for (int j = 0; j < BOARD_SIZE; j++) {
//                 if (checkLine(board, i, j, 1, 0, player, length) ||
//                     checkLine(board, i, j, 0, 1, player, length) ||
//                     checkLine(board, i, j, 1, 1, player, length) ||
//                     checkLine(board, i, j, 1, -1, player, length)) count++;
//             }
//         }
//         return count;
//     }

//     private boolean checkLine(int[][] board, int row, int col, int dRow, int dCol, int player, int length) {
//         int count = 0;
//         for (int i = 0; i < length; i++) {
//             int r = row + i * dRow;
//             int c = col + i * dCol;
//             if (r < 0 || r >= BOARD_SIZE || c < 0 || c >= BOARD_SIZE || board[r][c] != player) break;
//             count++;
//         }
//         return count == length;
//     }
// }
