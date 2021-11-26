public class King {

    private Domino mDomino;
    private final String mColor;
    private Player mPlayer;

    public King(String color)
    {
        this.mDomino = null;
        this.mColor  = color;
        this.mPlayer = null;
    }
    public Domino getmDomino(){return this.mDomino;}
    public void setmDomino(Domino domino){this.mDomino = domino;}

    public String getmColor(){return this.mColor;}

    public Player getmPlayer(){return this.mPlayer;}
    public void setmPlayer(Player player){this.mPlayer = player;}

}
