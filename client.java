// import java.io.*;
// import java.net.*;
// import java.util.ArrayList;


// class Client {
// 	public static void main(String[] args) {
         
// 	Socket MyClient;
// 	BufferedInputStream input;
// 	BufferedOutputStream output;
//     int[][] board = new int[15][15];
// 	try {
// 		MyClient = new Socket("localhost", 8888);

// 	   	input    = new BufferedInputStream(MyClient.getInputStream());
// 		output   = new BufferedOutputStream(MyClient.getOutputStream());
// 		BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
//         Board reseauBoard = new Board();


//         ReseauPlayer reseauPlayer = new ReseauPlayer(Mark.N);

        

// 	   	while(1 == 1){
// 			char cmd = 0;  	
//             cmd = (char)input.read();
//             System.out.println("cmd" + cmd);
//             // Debut de la partie en joueur rouge
//             if(cmd == '1'){
//                 reseauPlayer = new ReseauPlayer(Mark.R);
//                 byte[] aBuffer = new byte[1024];
// 				int size = input.available();
// 				//System.out.println("size " + size);
// 				input.read(aBuffer,0,size);
//                 String s = new String(aBuffer).trim();
//                 System.out.println(s);
//                 String[] boardValues;
//                 boardValues = s.split(" ");
//                 int x=0,y=0;
//                 for(int i=0; i<boardValues.length;i++){
//                     board[x][y] = Integer.parseInt(boardValues[i]);
//                     x++;
//                     if(x == 15){
//                         x = 0;
//                         y++;
//                     }
//                 }

//                 System.out.println("Nouvelle partie! Vous jouer rouge, entrez votre premier coup : ");
//                 reseauBoard.checkFirstMove(reseauPlayer);
//                 String move = reseauPlayer.randomMove(reseauBoard);
// 				output.write(move.getBytes(),0,move.length());
// 				output.flush();
//             }
//             // Debut de la partie en joueur Noir
//             if(cmd == '2'){
//                 System.out.println("Nouvelle partie! Vous jouer noir, attendez le coup des rouges");//ReseauPlayer = noir
//                 byte[] aBuffer = new byte[1024];
				
// 				int size = input.available();
// 				//System.out.println("size " + size);
// 				input.read(aBuffer,0,size);
//                 String s = new String(aBuffer).trim();
//                 //System.out.println(s);
//                 String[] boardValues;
//                 boardValues = s.split(" ");
//                 int x=0,y=0;
//                 for(int i=0; i<boardValues.length;i++){
//                     board[x][y] = Integer.parseInt(boardValues[i]);
                    
//                     x++;
//                     if(x == 15){
//                         x = 0;
//                         y++;
//                     }
//                 }
                
//             }


// 			// Le serveur demande le prochain coup
// 			// Le message contient aussi le dernier coup joue.
// 			if(cmd == '3'){
// 				byte[] aBuffer = new byte[16];
				
// 				int size = input.available();
// 				System.out.println("size :" + size);
// 				input.read(aBuffer,0,size);
			
// 				String s = new String(aBuffer).trim();
//                 System.out.println(s);
//                 Move ordinateurMove = new Move (MoveConverter.convertMove(s)[0],MoveConverter.convertMove(s)[1]);
//                 reseauBoard.play(ordinateurMove, reseauPlayer.getOpponentMark());
//                 reseauBoard.printBoard();
// 				System.out.println("Dernier coup :"+ s);
// 		       	System.out.println("Entrez votre coup : ");
//                 //--------------
//                 // String move = reseauPlayer.randomMove(reseauBoard);
//                 // output.write(move.getBytes(),0,move.length());
// 				// output.flush();
//                 //-----------------------
//                 Move reseauMove = reseauPlayer.selectBestMove(reseauPlayer, reseauBoard);
//                 reseauBoard.play(reseauMove, reseauPlayer.getMark());
//                 String stringReseauMove = MoveConverter.convertBack(reseauMove);
// 				output.write(stringReseauMove.getBytes(),0,stringReseauMove.length());
// 				output.flush();
				
// 			}
// 			// Le dernier coup est invalide
// 			if(cmd == '4'){
// 				System.out.println("Coup invalide, entrez un nouveau coup : ");
// 		       	String move = null;
// 				//move = console.readLine();
//                 move = new RandomGenerator().move();
// 				output.write(move.getBytes(),0,move.length());
// 				output.flush();
				
// 			}
//             // Le dernier coup est invalide
// 			if(cmd == '5'){
//                 byte[] aBuffer = new byte[16];
//                 int size = input.available();
//                 input.read(aBuffer,0,size);
// 				String s = new String(aBuffer).trim();
//                 Move ordinateurMove = new Move (MoveConverter.convertMove(s)[0],MoveConverter.convertMove(s)[1]);
//                 reseauBoard.play(ordinateurMove, reseauPlayer.getOpponentMark());
//                 reseauBoard.printBoard();
//                 System.out.println("Check winnnnnnn : " + reseauBoard.checkWin(reseauPlayer.getOpponentMark())); 
// 				System.out.println("Partie Terminé. Le dernier coup joué est: "+s);

// 		       	String move = null;
// 				move = console.readLine();
// 				output.write(move.getBytes(),0,move.length());
// 				output.flush();
// 			}
//         }
// 	}
// 	catch (IOException e) {
//    		System.out.println(e);
// 	}
	
//     }
// }







//-----------------------------------------------

class Client {
	public static void main(String[] args) {
            Board boardReseau = new Board();
            
            boardReseau.play(MoveConverter.convertStringToMove("H8"), Mark.R);
            for (int i = 1; i < 15; i++){
                for (int j = 1; j < 15; j++ ){
                    boardReseau.play(MoveConverter.convertMovetoMove(new Move(i,j)),Mark.R);
                }
            }

            boardReseau.play(MoveConverter.convertMovetoMove(new Move(1,1)),Mark.R);
            System.err.println(MoveConverter.convertMoveToString(new Move(0,0)));
            boardReseau.generateValidMoves();
            boardReseau.printBoard();
            }
        }