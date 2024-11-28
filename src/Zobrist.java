import java.util.ArrayList;
import java.util.Hashtable;

public class Zobrist {
	private static final long sideToMove = randomLong();
	private static long[][][] zobristTable = new long[15][15][2];
	private static Hashtable<Long, Integer> zobristEntries = new Hashtable<Long, Integer>();
	private static long hash = 0;

	public static void init() {
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				zobristTable[i][j][0] = randomLong();
				zobristTable[i][j][1] = randomLong();
			}
		}
	}

	public static int getSize() {
		return zobristEntries.size();
	}

	private static long randomLong() {
		return (long) (Math.random() * Long.MAX_VALUE);
	}

	private static long getZobristKey(int x, int y, int color) {
		return zobristTable[x][y][color];
	}	

	public static int getScore() {
		if(hashExists()) {
			return zobristEntries.get(hash);
		}

		return -1;
	}

	public static void updateHash(Move move, Boolean isCapture) {
		if(hash == 0) {
			hash = getZobristKey(move.getCol(), move.getRow(), move.getColor().ordinal() - 1);
		} else {
			hash ^= getZobristKey(move.getCol(), move.getRow(), move.getColor().ordinal() - 1);
		}
		
		if(!isCapture) {
			hash ^= sideToMove;
		}
	}

	public static void addEntry(int score) {
		zobristEntries.put(hash, score);
	}

	public static boolean hashExists() {			
		return zobristEntries.containsKey(hash);
	}
}
