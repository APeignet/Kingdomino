import java.util.*;
public class Player {

    private String mName;
    private int mNumberDomino;
    private ArrayList<King> mListKing;
    private ArrayList<Domino> mListDomino;
    private ArrayList<Position> mListPosition;
    private ArrayList<String> mListPositionString;
    private ArrayList<String> mPositionValide;
    private ArrayList<String> mCopiePositionStringPlayer;
    private ArrayList<Position> mCopiePositionPlayer;
    private Castle mCastle;
    private int mTour;
    private int cptDetectionPlateau;
    private int mNbPoint;

    public Player(String name, ArrayList<King> listKing, Castle castle)
    {
        this.cptDetectionPlateau=0;
        this.mTour = 0;
        this.mNumberDomino = 0;
        this.mName = name;
        this.mListKing = listKing;
        this.mListDomino = new ArrayList<>();
        this.mListPosition = new ArrayList<>();
        this.mListPositionString = new ArrayList<>();
        this.mCopiePositionStringPlayer = new ArrayList<>();
        this.mCopiePositionPlayer = new ArrayList<>();
        this.mCastle = castle;
        this.mPositionValide = new ArrayList<>();
        this.mNbPoint = 0;
    }

    public String getmName(){return this.mName;}

    public int getcptDetectionPlateau(){return this.cptDetectionPlateau;}
    public void setcptDetectionPlateau(int cptDetectionPlateau){this.cptDetectionPlateau = cptDetectionPlateau;}

    public void setmNumberDomino(int numberDomino){this.mNumberDomino = numberDomino;}

    public int getmTour(){return this.mTour;}
    public void setmTour(int tour){this.mTour = tour;}

    public ArrayList<King> getmListKing(){return this.mListKing;}

    public ArrayList<Domino> getmListDomino(){return this.mListDomino;}

    public ArrayList<Position> getmListPosition(){return this.mListPosition;}

    public ArrayList<String> getmListPositionString(){return this.mListPositionString;}

    public ArrayList<String> getmPositionValide(){return this.mPositionValide;}

    public ArrayList<String> getmCopiePositionStringPlayer(){return this.mCopiePositionStringPlayer;}

    public ArrayList<Position> getmCopiePositionPlayer(){return this.mCopiePositionPlayer;}

    public Castle getmCastle(){return this.mCastle;}

    public int getmNbPoint(){return this.mNbPoint;}
    public void setmNbPoint(int nbPoint){this.mNbPoint = nbPoint;}

    public void creationPositionCastle(){
        Position positionCastle = new Position("e5");
        positionCastle.setmTypeLandscape("All");
        mListPosition.add(positionCastle);
        mListPositionString.add("e5");
    }
}
