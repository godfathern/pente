import java.io.*;
import java.net.*;
import java.util.Scanner;

class Client {
    private static Socket MyClient;
    private static BufferedInputStream input;
    private static BufferedOutputStream output;
    private static BufferedReader console;
    private static int[][] board;
    private static Board mBoard;
    private static CPU cpu;
    private static boolean gameEnded;
    private static long startTime;
    private static long timeLimit = 4000; 
    
    public static long getStartTime() {
        return startTime;
    }    
    
    public static long getTimeLimit() {
        return timeLimit;
    }

    public static void main(String[] args) {        
        try {
            gameInitialization();      
            connect();

            while (true) {                                
                while(gameEnded) {
                    if(input.read() == -1) {
                        if(connect()) {
                            gameInitialization();
                        }
                    }       
                }

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
                    startTime = System.currentTimeMillis();
                    byte[] aBuffer = new byte[16];

                    int size = input.available();
                    System.out.println("size :" + size);
                    input.read(aBuffer, 0, size);

                    String s = new String(aBuffer);
                    System.out.println("Dernier coup :" + s);
                    String moveString = s.trim();
                    mBoard.play(new Move(moveString, cpu.get_mark().getOpponent()));

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
                // Partie terminée
                if (cmd == '5') {
                    byte[] aBuffer = new byte[16];
                    int size = input.available();
                    input.read(aBuffer, 0, size);

                    String s = new String(aBuffer);
                    System.out.println("Partie Terminé. Le dernier coup joué est: " + s);
                    gameEnded = true;
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    private static void gameInitialization() {
        mBoard = new Board();
        board = new int[15][15];
        cpu = new CPU(Mark.Red);
        gameEnded = false;
    }

    private static boolean connect() {
        System.out.println("Tentative de connexion");
        
        try {
            Scanner scIp = new Scanner(System.in);
            System.out.println("Entrez l'adresse IP du serveur avec le port (0.0.0.0:1234) : ");
            // interpolate the IP and port
            String ip = scIp.nextLine();
            String[] ipPort = ip.split(":");
            System.out.println("IP: " + ip);
            System.out.println("Port: " + ipPort[1]);
            MyClient = new Socket(ipPort[0], Integer.parseInt(ipPort[1]));
            scIp.close();

            input = new BufferedInputStream(MyClient.getInputStream());
            output = new BufferedOutputStream(MyClient.getOutputStream());
            console = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Connexion Réussie");

            return true;
        } catch (IOException e) {
            System.out.println("Connexion échouée");
            return false;
        }
    }
}
