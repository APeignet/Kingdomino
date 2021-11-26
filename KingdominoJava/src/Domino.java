public class Domino {

    private int mNumber;
    private Landscape mLandscapeRight;
    private Landscape mLandscapeLeft;
    private String mTypeLandscapeLeft;
    private String mTypeLandscapeRight;

    public Domino(int number, Landscape landscapeRight, Landscape landscapeLeft, String typeLandscapeLeft, String typeLandscapeRight)
    {
        this.mNumber = number;
        this.mLandscapeRight = landscapeRight;
        this.mLandscapeLeft = landscapeLeft;
        this.mTypeLandscapeLeft = typeLandscapeLeft;
        this.mTypeLandscapeRight = typeLandscapeRight;

    }

    public int getmNumber(){ return this.mNumber;}

    public Landscape getmLandscapeRight(){ return this.mLandscapeRight;}

    public Landscape getmLandscapeLeft(){ return this.mLandscapeLeft;}

    public String getmTypeLandscapeLeft(){ return this.mTypeLandscapeLeft;}

    public String getmTypeLandscapeRight(){ return this.mTypeLandscapeRight;}
}
