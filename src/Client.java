import java.io.*;
import java.net.*;

class Client {
    public static void main(String[] args) {
        Socket MyClient;
        BufferedInputStream input;
        BufferedOutputStream output;
        int[][] board = new int[15][15];
        Board mBoard = new Board();
        CPU cpu = null;

        try {
            MyClient = new Socket("localhost", 8888);

            input = new BufferedInputStream(MyClient.getInputStream());
            output = new BufferedOutputStream(MyClient.getOutputStream());
            BufferedReader console = new BufferedReader(new InputStreamReader(System.in));

            while (true) {
                char cmd = 0;

                cmd = (char) input.read();
                System.out.println(cmd);

                // Debut de la partie en joueur blanc
                if (cmd == '1') {
                    byte[] aBuffer = new byte[1024];
                    int size = input.available();
                    // System.out.println("size " + size);
                    input.read(aBuffer, 0, size);

                    String s = new String(aBuffer).trim();
                    System.out.println(s);

                    String[] boardValues;
                    boardValues = s.split(" ");

                    int x = 0, y = 0;
                    for (int i = 0; i < boardValues.length; i++) {
                        board[x][y] = Integer.parseInt(boardValues[i]);
                        x++;

                        if (x == 15) {
                            x = 0;
                            y++;
                        }
                    }

                    System.out.println("Nouvelle partie! Vous jouer rouge, entrez votre premier coup : ");
                    cpu = new CPU(Mark.Red);
                    Move move = cpu.getNextMove(mBoard);
                    mBoard.play(move);
                    System.out.println(move);

                    output.write(move.toString().getBytes(), 0, move.toString().getBytes().length);
                    output.flush();
                }
                // Debut de la partie en joueur Noir
                else if (cmd == '2') {
                    System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des blancs");
                    cpu = new CPU(Mark.Black);

                    byte[] aBuffer = new byte[1024];
                    int size = input.available();
                    // System.out.println("size " + size);
                    input.read(aBuffer, 0, size);

                    String s = new String(aBuffer).trim();
                    System.out.println(s);

                    String[] boardValues;
                    boardValues = s.split(" ");

                    int x = 0, y = 0;
                    for (int i = 0; i < boardValues.length; i++) {
                        board[x][y] = Integer.parseInt(boardValues[i]);
                        x++;
                        if (x == 15) {
                            x = 0;
                            y++;
                        }
                    }
                }

                // Le serveur demande le prochain coup
                // Le message contient aussi le dernier coup joue.
                if (cmd == '3') {
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    System.out.println("size :" + size);
                    input.read(aBuffer, 0, size);

                    String s = new String(aBuffer);
                    System.out.println("Dernier coup :" + s);
                    String moveString = s.trim();
                    mBoard.play(new Move(moveString, cpu.getOpponent()));

                    System.out.println("Entrez votre coup : ");
                    Move move = cpu.getNextMove(mBoard);
                    mBoard.play(move);
                    System.out.println(move);

                    output.write(move.toString().getBytes(), 0, move.toString().length());
                    output.flush();
                }
                // Le dernier coup est invalide
                if (cmd == '4') {
                    System.out.println("Coup invalide, entrez un nouveau coup : ");
                    String move = null;

                    move = console.readLine();
                    output.write(move.getBytes(), 0, move.length());
                    output.flush();
                }
                // Le dernier coup est invalide
                if (cmd == '5') {
                    byte[] aBuffer = new byte[16];
                    int size = input.available();
                    input.read(aBuffer, 0, size);

                    String s = new String(aBuffer);
                    System.out.println("Partie Terminé. Le dernier coup joué est: " + s);
                    String move = null;
                    move = console.readLine();

                    output.write(move.getBytes(), 0, move.length());
                    output.flush();
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}