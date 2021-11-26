public class Position {

    private String mNameCase;
    private String mStatus;
    private Landscape mLandscape;
    private String mTypeLandscape;

    public Position(String nameCase){
        this.mNameCase = nameCase;
        this.mLandscape = null;
        this.mStatus = "vide";
        this.mTypeLandscape = null;
    }

    public String getmNameCase(){return this.mNameCase;}
    public void setmNameCase(String nameCase){this.mNameCase = nameCase;}

    public void setmStatus(String status){this.mStatus = status;}

    public String getmTypeLandscape(){return this.mTypeLandscape;}
    public void setmTypeLandscape(String typeLandscape){this.mTypeLandscape = typeLandscape;}

    public Landscape getmLandscape(){return this.mLandscape;}
    public void setmLandscape(Landscape landscape){this.mLandscape = landscape;}
}
