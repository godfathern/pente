public enum Mark {
    Empty,
    Red,
    Black;

    public Mark getOpponent(){
        if(this == Mark.Red){
            return Mark.Black;
        }
        return Mark.Red;
    }
}
