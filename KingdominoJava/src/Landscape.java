public class Landscape {

    private int mCrown;
    private String mType;
    private Position mPosition;

    public Landscape(int crown, String type)
    {
        this.mCrown = crown;
        this.mType = type;
        this.mPosition = null;
    }

    public int getmCrown(){ return this.mCrown;}

    public String getmType(){ return this.mType;}

    public Position getmPosition(){ return this.mPosition;}
    public void setmPosition(Position position){ this.mPosition = position;}

}
