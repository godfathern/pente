class Move
{
    private int row;
    private int col;

    public Move(int i, int j){
        this.row = i;
        this.col = j;
    }

    public int getRow(){
        return row;
    }

    public int getCol(){
        return col;
    }
}