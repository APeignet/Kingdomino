public class Castle {

    private Position mPosition;
    private Player mPlayer;

    public Castle(Position position)
    {
        this.mPosition = position;
        this.mPlayer = null;
    }

    public Position getmPosition(){return this.mPosition;}

    public void setmPlayer(Player player){this.mPlayer = player;}
}
