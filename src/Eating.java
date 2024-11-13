import java.lang.reflect.Array;
import java.util.ArrayList;

public class Eating extends Move{
    public Eating(int col, int row, Mark mark, ArrayList<Move> enemies) {
        super(col, row, mark);
        this.enemies = enemies;
    }

    private ArrayList<Move> enemies;

    public ArrayList<Move> getEnemies() {
        return enemies;
    }

}
