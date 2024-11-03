enum Mark{
    R,//rouge
    N,//Noir
    EMPTY;

public Mark getOpponent(){
    if(this == Mark.R){
        return Mark.N;
    }
    return Mark.R;
}
}
