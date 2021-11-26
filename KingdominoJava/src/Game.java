import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Game {

    private final ArrayList<Player> mListPlayer;  // liste contenant tous les objets Player (joueurs)
    private ArrayList<Domino> mListDomino; // liste contenant tous les dominos mélangés
    private final ArrayList<King> mListKing; // liste contenant tous les objets King de tous les joueurs
    private final ArrayList<String> mListColor; // liste contenant les 4 couleurs possibles de King
    private ArrayList<Domino> onBoard; // liste contenant les dominos qui sont posés sur la table
    private ArrayList<Domino> onBoardBis;
    private ArrayList<Domino> onBoardBisCopie;
    private int mNbPlayer; // nombre de joueurs
    private ArrayList<String> mListPosition; // liste des positions possibles
    private int mTurn; // numero du tour
    private int mIndexNumber;

    public Game(){
        this.mIndexNumber = 0;
        this.mTurn = 0;
        this.mListPlayer = new ArrayList<>();
        this.mListDomino = new ArrayList<>();
        this.mListKing = new ArrayList<>();
        this.mNbPlayer = 0;
        this.mListColor = new ArrayList<>(Arrays.asList("rose", "jaune", "vert", "bleu"));
        this.onBoard = new ArrayList<>();
        this.onBoardBis = new ArrayList<>();
        this.onBoardBisCopie = new ArrayList<>();
        this.mListPosition = new ArrayList<>(Arrays.asList("a1","a2","a3","a4","a5","a6","a7","a8","a9","b1","b2","b3","b4","b5","b6","b7","b8","b9","c1","c2","c3","c4","c5","c6","c7","c8","c9","d1","d2","d3","d4","d5","d6","d7","d8","d9","e1","e2","e3","e4","e5","e6","e7","e8","e9","f1","f2","f3","f4","f5","f6","f7","f8","f9","g1","g2","g3","g4","g5","g6","g7","g8","g9","h1","h2","h3","h4","h5","h6","h7","h8","h9","i1","i2","i3","i4","i5","i6","i7","i8","i9"));
    }

    // Décrit le déroulement d'une partie
    public void play() throws IOException
    {
        System.out.println("Jouer à KINGDOMINO");
        nbrPlayer();
        creationDominos();
        System.out.println("----------------------- DEBUT DE LA PARTIE -----------------------");
        while(mListDomino.size() != 0) {
            mTurn++;
            System.out.println("TOUR "+mTurn);
            initialisationTour();
            if (mTurn==1) {
                Collections.shuffle(mListKing);
                chooseDominoFirst();
            }
            placeDomino();
        }
        mTurn++;
        System.out.println("DERNIER TOUR");
        initialisationDernierTour();
        placeDomino();
        System.out.println("---------------------- COMPTAGE DES POINTS ----------------------");
        comptagePoint();
        for(Player player : mListPlayer){
            System.out.println(player.getmName() + " a obtenu " + player.getmNbPoint() + " points.");
        }
        gameOver();
    }

    // Définie le nombre de joueurs et crée les instances de Player et de King.
    public void nbrPlayer()
    {
        // demande combien de joueurs
        System.out.println("Combien de joueurs ? (2 à 4 joueurs)");
        Scanner scanNbrPlayer = new Scanner(System.in);
        // récupère la valeur rentrée dans la console
        this.mNbPlayer = scanNbrPlayer.nextInt();
        // si le nombre de joueurs n'est pas 2,3 ou 4 : erreur
        while (this.mNbPlayer < 2 || this.mNbPlayer > 4)
        {
            // on redemande le nombre de joueurs tant qu'il y a une erreur
            System.out.println("ERREUR : vous devez être entre 2 et 4 joueurs");
            System.out.println("Combien de joueurs ? (2 à 4 joueurs)");
            this.mNbPlayer = scanNbrPlayer.nextInt();
        }
        // Pour 3 ou 4 joueurs :
        if(this.mNbPlayer!= 2) {
            for (int i = 1; i <= this.mNbPlayer; i++) {
                // création de la liste qui sera composée du King du joueur i
                ArrayList<King> listKing = new ArrayList<>();
                // demande le pseudo du joueur i
                System.out.println("Pseudo du joueur n°" + i + " :");
                Scanner scanNomPlayer = new Scanner(System.in);
                // récupère le pseudo du joueur i rentré dans la console
                String nomPlayer = scanNomPlayer.nextLine();

                // demande la couleur du joueur i
                System.out.println("Couleur du joueur n°" + i + " ("+ mListColor +") :");
                Scanner scanColorPlayer = new Scanner(System.in);
                String colorPlayer = scanColorPlayer.nextLine();
                // si la couleur choisie n'est pas rose, bleu, jaune ou vert : erreur
                while (!mListColor.contains(colorPlayer))
                {
                    System.out.println("ERREUR : vous devez choisir une couleur entre "+ mListColor +".");
                    System.out.println("Couleur du joueur n°" + i + " ("+ mListColor +") :");
                    colorPlayer = scanColorPlayer.nextLine();
                }
                // on enlève la couleur choisie de la liste pour que le 2ème joueur ne puisse pas le choisir
                mListColor.remove(colorPlayer);
                // on crée le King du joueur i
                King king = new King(colorPlayer);
                // on ajoute le king à la liste de King du joueur i
                listKing.add(king);
                // on ajoute le king à la liste composé de tous les king de tous les joueurs
                mListKing.add(king);
                // on crée le Player i
                Position position = new Position("e5");
                position.setmStatus("rempli");
                Castle castle = new Castle(position);
                Player player = new Player(nomPlayer, listKing, castle);
                player.creationPositionCastle();
                // on ajoute le player à la liste de tous les players
                mListPlayer.add(player);
                king.setmPlayer(player);
                castle.setmPlayer(player);
            }
        }
        // Pour 2 joueurs :
        else {
            for (int i = 1; i <= this.mNbPlayer; i++) {
                ArrayList<King> listKing = new ArrayList<>();
                System.out.println("Pseudo du joueur n°" + i + " :");
                Scanner scanNomPlayer = new Scanner(System.in);
                String nomPlayer = scanNomPlayer.nextLine();

                System.out.println("Couleur du joueur n°" + i + " ("+ mListColor +") :");
                Scanner scanColorPlayer = new Scanner(System.in);
                String colorPlayer = scanColorPlayer.nextLine();
                while (!mListColor.contains(colorPlayer))
                {
                    System.out.println("ERREUR : vous devez choisir une couleur entre "+ mListColor +".");
                    System.out.println("Couleur du joueur n°" + i + " ("+ mListColor +") :");
                    colorPlayer = scanColorPlayer.nextLine();
                }
                mListColor.remove(colorPlayer);

                // on crée les deux king du joueur i
                King king = new King(colorPlayer);
                King kong = new King(colorPlayer);
                // on les ajoute à la liste de King du joueur i
                listKing.add(king);
                listKing.add(kong);
                mListKing.add(king);
                mListKing.add(kong);
                Position position = new Position("e5");
                position.setmStatus("rempli");
                Castle castle = new Castle(position);
                Player player = new Player(nomPlayer, listKing, castle);
                player.creationPositionCastle();
                mListPlayer.add(player);
                king.setmPlayer(player);
                kong.setmPlayer(player);
                castle.setmPlayer(player);
            }
        }
        // Affiche un résumé des joueurs et de leurs couleurs
        System.out.println(" ");
        System.out.println("JOUEURS :");
        for(int i =0; i<mListPlayer.size();i++){
            System.out.println(mListPlayer.get(i).getmName() + " est la couleur " + mListPlayer.get(i).getmListKing().get(0).getmColor()+".");

        }
        System.out.println(" ");
    }

    // Récupère les informations des dominos dans le fichier dominos.csv et crée tous les dominos.
    public void creationDominos() throws IOException
    {

        BufferedReader lectureFichierCsv = null;
        String ligneCsv;

        try
        {
            lectureFichierCsv = new BufferedReader(new FileReader("dominos.csv"));
        }
        catch(FileNotFoundException exc)
        {
            System.out.println("ERREUR !");
        }
        // parcours ligne par ligne le fichier csv
        while ((ligneCsv = lectureFichierCsv.readLine()) != null)
        {
            String separateur = ",";
            // on récupère chaque info d'une ligne en découpant la chaîne de caractère à chaque virgule et on toutes les infos dans un tableau
            String[] ligne =  ligneCsv.split(separateur);

            // on convertit le nombre de couronne en entier
            int crown1 = Integer.parseInt(ligne[0]);
            String type1 = ligne[1];
            int crown2 = Integer.parseInt(ligne[2]);
            String type2 = ligne[3];
            int number = Integer.parseInt(ligne[4]);

            // on crée les deux landscapes d'un domino et on crée le domino
            Landscape landscapeLeft = new Landscape(crown1, type1);
            Landscape landscapeRight = new Landscape(crown2, type2);
            Domino domino = new Domino(number, landscapeRight, landscapeLeft,type1,type2);
            // on ajoute le domino à la liste de tous les dominos
            mListDomino.add(domino);
            //System.out.println("Domino n°"+domino.getmNumber()+" : "+domino.getmLandscapeLeft().getmType()+" : "+domino.getmLandscapeLeft().getmCrown()+", "+domino.getmLandscapeRight().getmType()+" : "+domino.getmLandscapeRight().getmCrown());
        }
        lectureFichierCsv.close();

        // on mélange la liste de tous les dominos
        Collections.shuffle(mListDomino);

        // Pour 2 joueurs : on enlève 24 dominos au hasard (ici les 24 premiers parce que la liste est déjà mélangée)
        if (this.mNbPlayer == 2){
            for (int i=0 ; i<24 ; i++) {
                mListDomino.remove(i);
            }
            //for(int i=0;i<mListDomino.size();i++){
            //    System.out.println(i+" : "+mListDominoShuffle.get(i).getmNumber());
            //}
        }
        // Pour 3 joueurs : on enlève 12 dominos
        if(this.mNbPlayer == 3){
            for (int i=0 ; i<12 ; i++) {
                mListDomino.remove(i);
            }
        }

    }

    // Affiche les dominos piochés.
    public void initialisationTour()
    {

        int nbKing = 0;

        // compte le nombre de king ( 1 king = 1 domino pioché)
        for (int i = 0; i < mListPlayer.size(); i++) {
            nbKing = nbKing + mListPlayer.get(i).getmListKing().size();
        }

        // ajoute les dominos qui vont être pioché à la liste onBoard
        if(mTurn==1){
            for (int i = 0; i < nbKing; i++) {
                onBoard.add(mListDomino.get(i));
            }

            for (int i = 0; i < nbKing; i++) {
                mListDomino.remove(0);
            }


            // trie la liste onBoard en fonction des numéros des dominos
            for (int i = 1; i < onBoard.size(); i++) {
                Domino dominoATrier = onBoard.get(i);
                int elementATrier = onBoard.get(i).getmNumber();
                int j = i;
                while (j > 0 && onBoard.get(j - 1).getmNumber() > elementATrier) {
                    onBoard.set(j, onBoard.get(j - 1));
                    j--;
                }
                onBoard.set(j, dominoATrier);
            }
        }
        else if (mTurn==2){
            onBoard.addAll(onBoardBisCopie);
        }
        else{
            for(int i = 0;i<onBoardBisCopie.size();i++){
                onBoard.set(i,onBoardBisCopie.get(i));
            }
        }

        System.out.println("----------------------");
        // affiche les dominos piochés par ordre croissant
        for (int i = 0; i < nbKing; i++) {
            System.out.println("Domino n°" + onBoard.get(i).getmNumber() + " ->      Côté gauche : " + onBoard.get(i).getmLandscapeLeft().getmType() + " (" + onBoard.get(i).getmLandscapeLeft().getmCrown() + " couronnes)" + "      Côté droit : " + onBoard.get(i).getmLandscapeRight().getmType() + " (" + onBoard.get(i).getmLandscapeRight().getmCrown() + " couronnes)");
        }

        // 2eme ligne de carte

        for (int i = 0; i < nbKing; i++) {
            onBoardBis.add(mListDomino.get(i));
        }


        for (int i =0; i<nbKing;i++) {
            mListDomino.remove(0);
        }

        for (int i = 1; i < onBoardBis.size(); i++) {
            Domino dominoATrier = onBoardBis.get(i);
            int elementATrier = onBoardBis.get(i).getmNumber();
            int j = i;
            while (j > 0 && onBoardBis.get(j-1).getmNumber() > elementATrier) {
                onBoardBis.set(j,onBoardBis.get(j-1));
                j--;
            }
            onBoardBis.set(j,dominoATrier);
        }

        for (int i = 0; i < onBoardBis.size(); i++) {
            if(mTurn!=1){
                onBoardBisCopie.set(i,onBoardBis.get(i));
            }
            else
                onBoardBisCopie.add(onBoardBis.get(i));
        }

        System.out.println("----------------------");
        // affiche les dominos piochés par ordre croissant
        for (int i =0; i<nbKing;i++) {
            System.out.println("Domino n°"+ onBoardBis.get(i).getmNumber()+" ->      Côté gauche : "+onBoardBis.get(i).getmLandscapeLeft().getmType()+" ("+onBoardBis.get(i).getmLandscapeLeft().getmCrown()+" couronnes)"+"      Côté droit : "+onBoardBis.get(i).getmLandscapeRight().getmType()+" ("+onBoardBis.get(i).getmLandscapeRight().getmCrown()+" couronnes)");
        }

    }

    // Affiche le dernier tour de la partie.
    public void initialisationDernierTour()
    {

        int nbKing = 0;

        // compte le nombre de king ( 1 king = 1 domino pioché)
        for (int i = 0; i < mListPlayer.size(); i++) {
            nbKing = nbKing + mListPlayer.get(i).getmListKing().size();
        }

        // ajoute les dominos qui vont être pioché à la liste onBoard

        for(int i = 0;i<onBoardBisCopie.size();i++){
            onBoard.set(i,onBoardBisCopie.get(i));
        }


        System.out.println("----------------------");
        // affiche les dominos piochés par ordre croissant
        for (int i = 0; i < nbKing; i++) {
            System.out.println("Domino n°" + onBoard.get(i).getmNumber() + " ->      Côté gauche : " + onBoard.get(i).getmLandscapeLeft().getmType() + " (" + onBoard.get(i).getmLandscapeLeft().getmCrown() + " couronnes)" + "      Côté droit : " + onBoard.get(i).getmLandscapeRight().getmType() + " (" + onBoard.get(i).getmLandscapeRight().getmCrown() + " couronnes)");
        }
    }

    // Choisi au hasard quel king commence et le joueur choisi le domino.
    public void chooseDominoFirst()
    {
        // on crée une liste contenant les numéros (en String) des dominos piochés
        ArrayList<String> onBoardNumber = new ArrayList<>();

        // on ajoute les numéros à la liste
        for (int i=0;i<onBoard.size();i++){
            onBoardNumber.add(String.valueOf(onBoard.get(i).getmNumber()));
        }

        // on choisi le domino que l'on veut
        for(int i=0;i<mListKing.size();i++){
            System.out.println("----------------------");
            System.out.println("Le roi "+mListKing.get(i).getmColor()+" ("+mListKing.get(i).getmPlayer().getmName()+") choisi son domino.");
            System.out.println("Quel domino ? "+ onBoardNumber);
            Scanner scanChoiceDomino = new Scanner(System.in);
            String choiceDomino = scanChoiceDomino.nextLine();
            // si le numéro du domino n'appartient pas à la liste : erreur
            while (!onBoardNumber.contains(choiceDomino))
            {
                System.out.println("ERREUR : vous devez choisir un domino entre "+ onBoardNumber +".");
                System.out.println("Quel domino ? "+ onBoardNumber);
                choiceDomino = scanChoiceDomino.nextLine();
            }
            // on récupère l'indice du domino sélectionné dans la liste onBoardNumber
            mIndexNumber = onBoardNumber.indexOf(choiceDomino);

            // on modifie l'attribut Domino du King en remplacant sa valeur par défaut par le domino sélectionné
            mListKing.get(i).setmDomino(onBoard.get(mIndexNumber));
            // enregistre le numéro du domino choisi
            mListKing.get(i).getmPlayer().setmNumberDomino(Integer.parseInt(choiceDomino));
            // on ajoute le domino à la liste des dominos du joueurs
            mListKing.get(i).getmPlayer().getmListDomino().add(onBoard.get(mIndexNumber));
            onBoard.remove(onBoard.get(mIndexNumber));
            // on enlève le domino dans les deux listes
            onBoardNumber.remove(choiceDomino);
        }
    }

    // Choisi son prochain domino pour le tour d'après.
    public void chooseDominoSecond(King king)
    {

        ArrayList<String> onBoardNumber = new ArrayList<>();

        for (int i=0;i<onBoardBis.size();i++){
            onBoardNumber.add(String.valueOf(onBoardBis.get(i).getmNumber()));
        }

        System.out.println("----------------------");
        System.out.println("Le roi "+king.getmColor()+" ("+king.getmPlayer().getmName()+") choisi son domino.");
        System.out.println("Quel domino ? "+ onBoardNumber);
        Scanner scanChoiceDomino = new Scanner(System.in);
        String choiceDomino = scanChoiceDomino.nextLine();
        // si le numéro du domino n'appartient pas à la liste : erreur
        while (!onBoardNumber.contains(choiceDomino))
        {
            System.out.println("ERREUR : vous devez choisir un domino entre "+ onBoardNumber +".");
            System.out.println("Quel domino ? "+ onBoardNumber);
            choiceDomino = scanChoiceDomino.nextLine();
        }
        // on récupère l'indice du domino sélectionné dans la liste onBoardNumber
        mIndexNumber = onBoardNumber.indexOf(choiceDomino);

        // on modifie l'attribut Domino du King en remplacant sa valeur par défaut par le domino sélectionné
        king.setmDomino(onBoardBis.get(mIndexNumber));
        // enregistre le numéro du domino choisi
        king.getmPlayer().setmNumberDomino(Integer.parseInt(choiceDomino));
        // on ajoute le domino à la liste des dominos du joueurs
        king.getmPlayer().getmListDomino().add(onBoardBis.get(mIndexNumber));
        onBoardBis.remove(onBoardBis.get(mIndexNumber));
        // on enlève le domino dans les deux listes
        onBoardNumber.remove(choiceDomino);
    }

    // Place le domino sur le plateau.
    public void placeDomino()
    {
        // trie la liste des king
        for (int i = 1; i < mListKing.size(); i++) {
            King kingATrier = mListKing.get(i);
            int elementATrier = mListKing.get(i).getmDomino().getmNumber();
            int j = i;
            while (j > 0 && mListKing.get(j-1).getmDomino().getmNumber() > elementATrier) {
                mListKing.set(j,mListKing.get(j-1));
                j--;
            }
            mListKing.set(j,kingATrier);
        }
        //affiche le plateau du joueur
        for (int i = 0; i < mListKing.size(); i++) {
            mListKing.get(i).getmPlayer().setmTour(mListKing.get(i).getmPlayer().getmTour() + 1);
            System.out.println("----------------------");
            System.out.println(" ");
            System.out.println("Le roi " + mListKing.get(i).getmColor() + " (" + mListKing.get(i).getmPlayer().getmName() + ") place le domino n°" + mListKing.get(i).getmDomino().getmNumber() + " (Côté gauche : " + mListKing.get(i).getmDomino().getmTypeLandscapeLeft() + " (" + mListKing.get(i).getmDomino().getmLandscapeLeft().getmCrown() + " couronnes)" + " et côté droit : " + mListKing.get(i).getmDomino().getmTypeLandscapeRight() + " (" + mListKing.get(i).getmDomino().getmLandscapeRight().getmCrown() + " couronnes)).");
            System.out.println(" ");
            System.out.println("PLATEAU DE " + mListKing.get(i).getmPlayer().getmName() + " :");
            System.out.println("Le chateau est en position " + mListKing.get(i).getmPlayer().getmCastle().getmPosition().getmNameCase() + ".");

            for (int j = 0; j < mListKing.get(i).getmPlayer().getmListDomino().size(); j++) {
                if (mListKing.get(i).getmPlayer().getmListDomino().get(j).getmLandscapeLeft().getmPosition() != null)
                    System.out.println("Le domino n°" + mListKing.get(i).getmPlayer().getmListDomino().get(j).getmNumber() + " est en position " + mListKing.get(i).getmPlayer().getmListDomino().get(j).getmLandscapeLeft().getmPosition().getmNameCase() + " (côté gauche) et " + mListKing.get(i).getmPlayer().getmListDomino().get(j).getmLandscapeRight().getmPosition().getmNameCase() + " (côté droit).");
            }

            // CHOIX DE LA POSITION DU COTE GAUCHE
            System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
            Scanner scanPositionLeftDomino = new Scanner(System.in);
            String positionLeftDomino = scanPositionLeftDomino.nextLine();

            // erreur si pas de la forme a1
            while (!mListPosition.contains(positionLeftDomino)) {
                System.out.println("ERREUR : vous devez choisir une position valide.");
                System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                positionLeftDomino = scanPositionLeftDomino.nextLine();
            }


            // CHOIX DE LA POSITION DU COTE DROIT
            System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
            Scanner scanPositionRightDomino = new Scanner(System.in);
            String positionRightDomino = scanPositionRightDomino.nextLine();

            while (!mListPosition.contains(positionRightDomino)) {
                System.out.println("ERREUR : vous devez choisir une position valide.");
                System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                positionRightDomino = scanPositionRightDomino.nextLine();
            }

            // crée la position du côté gauche
            Position positionLeft = new Position(positionLeftDomino);
            positionLeft.setmStatus("rempli");
            // change l'attribut Landscape de la position et récupère celui du côté gauche du domino
            positionLeft.setmLandscape(mListKing.get(i).getmDomino().getmLandscapeLeft());
            positionLeft.setmTypeLandscape(mListKing.get(i).getmDomino().getmLandscapeLeft().getmType());
            mListKing.get(i).getmPlayer().getmCopiePositionStringPlayer().add(positionLeftDomino);
            mListKing.get(i).getmPlayer().getmCopiePositionPlayer().add(positionLeft);

            Position positionRight = new Position(positionRightDomino);
            positionRight.setmStatus("rempli");
            positionRight.setmLandscape(mListKing.get(i).getmDomino().getmLandscapeRight());
            positionRight.setmTypeLandscape(mListKing.get(i).getmDomino().getmLandscapeRight().getmType());
            mListKing.get(i).getmPlayer().getmCopiePositionStringPlayer().add(positionRightDomino);
            mListKing.get(i).getmPlayer().getmCopiePositionPlayer().add(positionRight);

            // Vérifie si le domino dépasse du plateau 5x5.

            if(mListKing.get(i).getmPlayer().getcptDetectionPlateau() < 1) {
                detectionPlateau(mListKing.get(i).getmPlayer());
            }
            if(mListKing.get(i).getmPlayer().getmPositionValide().size()>0) {
                if (detectionCasesVides(mListKing.get(i).getmPlayer(), mListKing.get(i).getmDomino()) == 1) {

                    while (!positionValide(mListKing.get(i).getmDomino(), positionLeftDomino, positionRightDomino, mListKing.get(i).getmPlayer()) || !mListKing.get(i).getmPlayer().getmPositionValide().contains(positionRightDomino) || !mListKing.get(i).getmPlayer().getmPositionValide().contains(positionLeftDomino) || !mListPosition.contains(positionRightDomino) || mListKing.get(i).getmPlayer().getmListPositionString().contains(positionRightDomino) || !mListPosition.contains(positionLeftDomino) || mListKing.get(i).getmPlayer().getmListPositionString().contains(positionLeftDomino)) {

                        if(!mListKing.get(i).getmPlayer().getmPositionValide().contains(positionRightDomino) || !mListKing.get(i).getmPlayer().getmPositionValide().contains(positionLeftDomino)) {
                            System.out.println("Le domino dépasse du plateau 5x5.");
                            System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionLeftDomino = scanPositionLeftDomino.nextLine();
                            System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionRightDomino = scanPositionRightDomino.nextLine();
                        }

                        if (!mListPosition.contains(positionLeftDomino)) {
                            System.out.println("ERREUR : le domino gauche n'a pas une position valide.");
                            System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionLeftDomino = scanPositionLeftDomino.nextLine();
                        }

                        if (!mListPosition.contains(positionRightDomino)) {
                            System.out.println("ERREUR : le domino droit n'a pas une position valide.");
                            System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionRightDomino = scanPositionRightDomino.nextLine();
                        }

                        // vérifie si il y a déjà un domino à cette position
                        if (mListKing.get(i).getmPlayer().getmListPositionString().contains(positionLeftDomino) || mListKing.get(i).getmPlayer().getmListPositionString().contains(positionRightDomino)) {
                            System.out.println("ERREUR : il y a déjà un domino sur cette position.");
                            System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionLeftDomino = scanPositionLeftDomino.nextLine();
                            while (!mListPosition.contains(positionLeftDomino)) {
                                System.out.println("ERREUR : le domino gauche n'a pas une position valide.");
                                System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                                positionLeftDomino = scanPositionLeftDomino.nextLine();
                            }
                            System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionRightDomino = scanPositionRightDomino.nextLine();
                            while (!mListPosition.contains(positionRightDomino)) {
                                System.out.println("ERREUR : le domino droit n'a pas une position valide.");
                                System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                                positionRightDomino = scanPositionRightDomino.nextLine();
                            }
                        }

                        if(!positionValide(mListKing.get(i).getmDomino(), positionLeftDomino, positionRightDomino, mListKing.get(i).getmPlayer())){
                            System.out.println("Le domino n'a pas une position valide.");
                            System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionLeftDomino = scanPositionLeftDomino.nextLine();
                            while (!mListPosition.contains(positionLeftDomino)) {
                                System.out.println("ERREUR : le domino gauche n'a pas une position valide.");
                                System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                                positionLeftDomino = scanPositionLeftDomino.nextLine();
                            }
                            System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionRightDomino = scanPositionRightDomino.nextLine();
                            while (!mListPosition.contains(positionRightDomino)) {
                                System.out.println("ERREUR : le domino droit n'a pas une position valide.");
                                System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                                positionRightDomino = scanPositionRightDomino.nextLine();
                            }
                        }

                    }

                    mListKing.get(i).getmPlayer().getmListPosition().add(positionLeft);
                    mListKing.get(i).getmPlayer().getmListPositionString().add(positionLeftDomino);
                    // change l'attribut position du côté gauche du domino
                    mListKing.get(i).getmDomino().getmLandscapeLeft().setmPosition(positionLeft);

                    mListKing.get(i).getmPlayer().getmListPosition().add(positionRight);
                    mListKing.get(i).getmPlayer().getmListPositionString().add(positionRightDomino);
                    mListKing.get(i).getmDomino().getmLandscapeRight().setmPosition(positionRight);

                    System.out.println("Le côté gauche est en " + positionLeftDomino + " et le côté droit en " + positionRightDomino + ".");
                    System.out.println(" ");
                    if(mTurn<6)
                        chooseDominoSecond(mListKing.get(i));
                } else {
                    mListKing.get(i).getmPlayer().getmCopiePositionStringPlayer().remove(mListKing.get(i).getmPlayer().getmCopiePositionStringPlayer().size()-2);
                    mListKing.get(i).getmPlayer().getmCopiePositionStringPlayer().remove( mListKing.get(i).getmPlayer().getmCopiePositionStringPlayer().size()-1);

                    mListKing.get(i).getmPlayer().getmCopiePositionPlayer().remove(mListKing.get(i).getmPlayer().getmCopiePositionPlayer().size()-2);
                    mListKing.get(i).getmPlayer().getmCopiePositionPlayer().remove(mListKing.get(i).getmPlayer().getmCopiePositionPlayer().size()-1);

                    System.out.println("Impossible de placer le domino.");
                    System.out.println("Le domino a été défaussé.");
                    mListKing.get(i).getmPlayer().getmListDomino().remove(mListKing.get(i).getmDomino());
                    if(mTurn<6)
                        chooseDominoSecond(mListKing.get(i));
                }

            }
            else{

                while (mListKing.get(i).getmPlayer().getmListPositionString().contains(positionLeftDomino) || mListKing.get(i).getmPlayer().getmListPositionString().contains(positionRightDomino) || detectionLigne(mListKing.get(i).getmPlayer(), positionLeft) == 1 || detectionLigne(mListKing.get(i).getmPlayer(), positionRight) == 1 || !positionValide(mListKing.get(i).getmDomino(), positionLeftDomino, positionRightDomino, mListKing.get(i).getmPlayer()) || !mListPosition.contains(positionLeftDomino)  || !mListPosition.contains(positionRightDomino)) {

                    // vérifie si il y a déjà un domino à cette position
                    if (mListKing.get(i).getmPlayer().getmListPositionString().contains(positionLeftDomino) || mListKing.get(i).getmPlayer().getmListPositionString().contains(positionRightDomino)) {
                        System.out.println("ERREUR : il y a déjà un domino sur cette position.");
                        System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                        positionLeftDomino = scanPositionLeftDomino.nextLine();
                        while (!mListPosition.contains(positionLeftDomino)) {
                            System.out.println("ERREUR : le domino gauche n'a pas une position valide.");
                            System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionLeftDomino = scanPositionLeftDomino.nextLine();
                        }
                        System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                        positionRightDomino = scanPositionRightDomino.nextLine();
                        while (!mListPosition.contains(positionRightDomino)) {
                            System.out.println("ERREUR : le domino droit n'a pas une position valide.");
                            System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionRightDomino = scanPositionRightDomino.nextLine();
                        }
                    }

                    if (!mListPosition.contains(positionLeftDomino)) {
                        System.out.println("ERREUR : le domino gauche n'a pas une position valide.");
                        System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                        positionLeftDomino = scanPositionLeftDomino.nextLine();
                    }

                    if (!mListPosition.contains(positionRightDomino)) {
                        System.out.println("ERREUR : le domino droit n'a pas une position valide.");
                        System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                        positionRightDomino = scanPositionRightDomino.nextLine();
                    }

                    if(!positionValide(mListKing.get(i).getmDomino(), positionLeftDomino, positionRightDomino, mListKing.get(i).getmPlayer())){
                        System.out.println("Le domino n'a pas une position valide.");
                        System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                        positionLeftDomino = scanPositionLeftDomino.nextLine();
                        while (!mListPosition.contains(positionLeftDomino)) {
                            System.out.println("ERREUR : le domino gauche n'a pas une position valide.");
                            System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionLeftDomino = scanPositionLeftDomino.nextLine();
                        }
                        System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                        positionRightDomino = scanPositionRightDomino.nextLine();
                        while (!mListPosition.contains(positionRightDomino)) {
                            System.out.println("ERREUR : le domino droit n'a pas une position valide.");
                            System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionRightDomino = scanPositionRightDomino.nextLine();
                        }
                    }
                    if(detectionLigne(mListKing.get(i).getmPlayer(), positionLeft) == 1 || detectionLigne(mListKing.get(i).getmPlayer(), positionRight) == 1){
                        mListKing.get(i).getmPlayer().getmCopiePositionStringPlayer().remove(mListKing.get(i).getmPlayer().getmCopiePositionStringPlayer().size()-2);
                        mListKing.get(i).getmPlayer().getmCopiePositionStringPlayer().remove( mListKing.get(i).getmPlayer().getmCopiePositionStringPlayer().size()-1);

                        mListKing.get(i).getmPlayer().getmCopiePositionPlayer().remove(mListKing.get(i).getmPlayer().getmCopiePositionPlayer().size()-2);
                        mListKing.get(i).getmPlayer().getmCopiePositionPlayer().remove(mListKing.get(i).getmPlayer().getmCopiePositionPlayer().size()-1);

                        System.out.println("Le domino dépasse du plateau 5x5.");
                        System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                        positionLeftDomino = scanPositionLeftDomino.nextLine();
                        while (!mListPosition.contains(positionLeftDomino)) {
                            System.out.println("ERREUR : le domino gauche n'a pas une position valide.");
                            System.out.println("Choisi la position du côté gauche du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionLeftDomino = scanPositionLeftDomino.nextLine();
                        }
                        System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                        positionRightDomino = scanPositionRightDomino.nextLine();
                        while (!mListPosition.contains(positionRightDomino)) {
                            System.out.println("ERREUR : le domino droit n'a pas une position valide.");
                            System.out.println("Choisi la position du côté droit du domino " + mListKing.get(i).getmDomino().getmNumber() + " (ex:a1,b2,etc..) : ");
                            positionRightDomino = scanPositionRightDomino.nextLine();
                        }
                        mListKing.get(i).getmPlayer().getmCopiePositionStringPlayer().add(positionLeftDomino);
                        mListKing.get(i).getmPlayer().getmCopiePositionStringPlayer().add(positionRightDomino);

                        mListKing.get(i).getmPlayer().getmCopiePositionPlayer().add(positionLeft);
                        mListKing.get(i).getmPlayer().getmCopiePositionPlayer().add(positionRight);
                    }

                }
                positionLeft.setmNameCase(positionLeftDomino);
                mListKing.get(i).getmPlayer().getmListPosition().add(positionLeft);
                mListKing.get(i).getmPlayer().getmListPositionString().add(positionLeftDomino);
                mListKing.get(i).getmDomino().getmLandscapeLeft().setmPosition(positionLeft);

                positionRight.setmNameCase(positionRightDomino);
                mListKing.get(i).getmPlayer().getmListPosition().add(positionRight);
                mListKing.get(i).getmPlayer().getmListPositionString().add(positionRightDomino);
                mListKing.get(i).getmDomino().getmLandscapeRight().setmPosition(positionRight);

                System.out.println("Le côté gauche est en " + mListKing.get(i).getmDomino().getmLandscapeLeft().getmPosition().getmNameCase() + " et le côté droit en " + mListKing.get(i).getmDomino().getmLandscapeRight().getmPosition().getmNameCase() + ".");
                System.out.println(" ");
                if(mTurn<6)
                    chooseDominoSecond(mListKing.get(i));
            }

        }
    }

    // Vérifie si la position du domino est proche d'un autre domino et qu'ils ont un landscape commun.
    public boolean positionValide(Domino domino, String positionLeft, String positionRight, Player player)
    {
        int compteur = 0;
        int index;
        for (int i=0;i<mListPosition.size();i++){
            // compare la position choisi avec chaque élément de la liste de position.
            if(positionLeft.equals(mListPosition.get(i))){
                // 1er test : si le côté droit est en position i-9
                if(i-9 >= 0 ) {
                    if (positionRight.equals(mListPosition.get(i - 9))) {
                        // Vérifie si il y a un domino en i+9
                        if(i+9 <= 80){
                            if (player.getmListPositionString().contains(mListPosition.get(i + 9))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i + 9));
                                // vérifie si paysage de i+9 = paysage du domino i
                                if (domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape()) || player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }

                        // Vérifie si il y a un domino en i-1
                        if (player.getmListPositionString().contains(mListPosition.get(i - 1))) {
                            index = player.getmListPositionString().indexOf(mListPosition.get(i - 1));
                            if (domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                compteur++;
                            }
                        }

                        // Vérifie si il y a un domino en i+1
                        if(i+1 <= 80) {
                            if (player.getmListPositionString().contains(mListPosition.get(i + 1))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i + 1));
                                if (domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }

                        // Vérifie si il y a un domino en i-10
                        if(i-10 >= 0 ) {
                            if (player.getmListPositionString().contains(mListPosition.get(i - 10))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i - 10));
                                if (domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }

                        // Vérifie si il y a un domino en i-18
                        if(i-18 >= 0 ) {
                            if (player.getmListPositionString().contains(mListPosition.get(i - 18))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i - 18));
                                if (domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i-8
                        if (player.getmListPositionString().contains(mListPosition.get(i - 8))) {
                            index = player.getmListPositionString().indexOf(mListPosition.get(i - 8));
                            if (domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                compteur++;
                            }
                        }
                    }
                }
                // 2ème test :si le côté droit est en position i-1
                if(i-1 >= 0 ) {
                    if (positionRight.equals(mListPosition.get(i - 1))) {
                        // Vérifie si il y a un domino en i+9
                        if(i+9 <= 80) {
                            if (player.getmListPositionString().contains(mListPosition.get(i + 9))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i + 9));
                                if (domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i-9
                        if(i-9 >= 0 ) {
                            if (player.getmListPositionString().contains(mListPosition.get(i - 9))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i - 9));
                                if (domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i+1
                        if(i+1 <= 80) {
                            if (player.getmListPositionString().contains(mListPosition.get(i + 1))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i + 1));
                                if (domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i-10
                        if(i-10 >= 0 ) {
                            if (player.getmListPositionString().contains(mListPosition.get(i - 10))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i - 10));
                                if (domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i-2
                        if(i-2 >= 0 ) {
                            if (player.getmListPositionString().contains(mListPosition.get(i - 2))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i - 2));
                                if (domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i+8
                        if(i+8 <= 80) {
                            if (player.getmListPositionString().contains(mListPosition.get(i + 8))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i + 8));
                                if (domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                    }
                }
                // 3ème test :si le côté droit est en position i+9
                if(i+9 <= 80) {
                    if (positionRight.equals(mListPosition.get(i + 9))) {
                        // Vérifie si il y a un domino en i-1
                        if(i-1 >= 0 ) {
                            if (player.getmListPositionString().contains(mListPosition.get(i - 1))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i - 1));
                                if (domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i-9
                        if(i-9 >= 0 ) {
                            if (player.getmListPositionString().contains(mListPosition.get(i - 9))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i - 9));
                                if (domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i+1
                        if (player.getmListPositionString().contains(mListPosition.get(i + 1))) {
                            index = player.getmListPositionString().indexOf(mListPosition.get(i + 1));
                            if (domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                compteur++;
                            }
                        }
                        // Vérifie si il y a un domino en i+8
                        if (player.getmListPositionString().contains(mListPosition.get(i + 8))) {
                            index = player.getmListPositionString().indexOf(mListPosition.get(i + 8));
                            if (domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                compteur++;
                            }
                        }
                        // Vérifie si il y a un domino en i+18
                        if(i+18 <= 80) {
                            if (player.getmListPositionString().contains(mListPosition.get(i + 18))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i + 18));
                                if (domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i+10
                        if(i+10 <= 80) {
                            if (player.getmListPositionString().contains(mListPosition.get(i + 10))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i + 10));
                                if (domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                    }
                }
                // 4ème test : si le côté droit est en position i+1
                if(i+1 <= 80) {
                    if (positionRight.equals(mListPosition.get(i + 1))) {
                        // Vérifie si il y a un domino en i-9
                        if(i-9 >= 0 ) {
                            if (player.getmListPositionString().contains(mListPosition.get(i - 9))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i - 9));
                                if (domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i-1
                        if(i-1 >= 0 ) {
                            if (player.getmListPositionString().contains(mListPosition.get(i - 1))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i - 1));
                                if (domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i+9
                        if(i+9 <= 80) {
                            if (player.getmListPositionString().contains(mListPosition.get(i + 9))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i + 9));
                                if (domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape()) || player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i-8
                        if(i-8 >= 0 ) {
                            if (player.getmListPositionString().contains(mListPosition.get(i - 8))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i - 8));
                                if (domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i+2
                        if(i+2 <= 80) {
                            if (player.getmListPositionString().contains(mListPosition.get(i + 2))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i + 2));
                                if (domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                        // Vérifie si il y a un domino en i+10
                        if(i+10 <= 80) {
                            if (player.getmListPositionString().contains(mListPosition.get(i + 10))) {
                                index = player.getmListPositionString().indexOf(mListPosition.get(i + 10));
                                if (domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| player.getmListPositionString().get(index).equals("e5")) {
                                    compteur++;
                                }
                            }
                        }
                    }
                }
            }
        }
        if(compteur>=1)
            return true;
        else
            return false;
    }

    // Vérifie si la position est bien dans le plateau 5x5 (avant détection du plateau).
    public int detectionLigne(Player player, Position position)
    {

        ArrayList<String> allPositionTableau = new ArrayList<>();
        ArrayList<Integer> allPositionTableauChiffre = new ArrayList<>();
        ArrayList<Integer> allPositionChiffre = new ArrayList<>();

        int a = 0;

        for(String element : player.getmCopiePositionStringPlayer()){
            int index = mListPosition.indexOf(element);
            allPositionTableau.add(mListPosition.get(index));
        }

        for(String element : allPositionTableau){
            String lettre = element.substring(0,1);
            int index = allPositionTableau.indexOf(element);
            if (lettre.equals("a"))
                allPositionTableau.set(index,"1"+element.substring(1));
            if (lettre.equals("b"))
                allPositionTableau.set(index,"2"+element.substring(1));
            if (lettre.equals("c"))
                allPositionTableau.set(index,"3"+element.substring(1));
            if (lettre.equals("d"))
                allPositionTableau.set(index,"4"+element.substring(1));
            if (lettre.equals("e"))
                allPositionTableau.set(index,"5"+element.substring(1));
            if (lettre.equals("f"))
                allPositionTableau.set(index,"6"+element.substring(1));
            if (lettre.equals("g"))
                allPositionTableau.set(index,"7"+element.substring(1));
            if (lettre.equals("h"))
                allPositionTableau.set(index,"8"+element.substring(1));
            if (lettre.equals("i"))
                allPositionTableau.set(index,"9"+element.substring(1));
        }


        for(String element : allPositionTableau) {
            int c = Integer.parseInt(element);
            allPositionTableauChiffre.add(c);
        }

        // trie la liste de chiffre
        for (int i = 1; i < allPositionTableauChiffre.size(); i++) {
            int elementATrier = allPositionTableauChiffre.get(i);
            int j = i;
            while (j > 0 && allPositionTableauChiffre.get(j-1) > elementATrier) {
                allPositionTableauChiffre.set(j, allPositionTableauChiffre.get(j - 1));
                j--;
            }
            allPositionTableauChiffre.set(j, elementATrier);
        }

        String dernierElementString = String.valueOf(allPositionTableauChiffre.get(allPositionTableauChiffre.size()-1));
        String premierElementString = String.valueOf(allPositionTableauChiffre.get(0));

        int dernierElement = Character.getNumericValue(dernierElementString.charAt(0));
        int premierElement = Character.getNumericValue(premierElementString.charAt(0));

        if(dernierElement-premierElement>4){
            a=1;
        }




        for(String element : allPositionTableau){
            char chiffre = element.charAt(1);
            allPositionChiffre.add(Character.getNumericValue(chiffre));
        }
        Set set2 = new HashSet() ;
        set2.addAll(allPositionChiffre) ;
        ArrayList<Integer> allPositionChiffre2 = new ArrayList<Integer>(set2) ;


        // trie la liste de chiffre
        for (int i = 1; i < allPositionChiffre2.size(); i++) {
            int elementATrier = allPositionChiffre2.get(i);
            int j = i;
            while (j > 0 && allPositionChiffre2.get(j-1) > elementATrier) {
                allPositionChiffre2.set(j, allPositionChiffre2.get(j - 1));
                j--;
            }
            allPositionChiffre2.set(j, elementATrier);
        }

        int premierChiffre = allPositionChiffre2.get(0);
        int dernierChiffre = allPositionChiffre2.get(allPositionChiffre2.size()-1);


        if(dernierChiffre-premierChiffre>4){
            a=1;
        }

        ArrayList<String> ligneTableau = new ArrayList<>();
        ArrayList<String> colonneTableau = new ArrayList<>();


        for (Position element : player.getmCopiePositionPlayer()){
            if(element == position){
                int index = player.getmCopiePositionPlayer().indexOf(element);
                String positionString = player.getmCopiePositionStringPlayer().get(index);

                // Test sur les lettres (lignes)
                char premierCaractere = positionString.charAt(0);
                for (String s : mListPosition) {
                    if (s.charAt(0) == premierCaractere) {
                        ligneTableau.add(s);
                    }
                }
                ligneTableau.removeIf(s -> !player.getmCopiePositionStringPlayer().contains(s));

                if(ligneTableau.size()>5){
                    a = 1;
                }

                // Test avec les chiffres (colonnes)
                char deuxiemeCaractere = positionString.charAt(1);
                for (String s : mListPosition) {
                    if (s.charAt(1) == deuxiemeCaractere) {
                        colonneTableau.add(s);
                    }
                }
                colonneTableau.removeIf(s -> !player.getmCopiePositionStringPlayer().contains(s));

                if(colonneTableau.size()>5){
                    a = 1;
                }
            }
        }
        return a;
    }

    // Détecte le plateau 5x5.
    public void detectionPlateau(Player player)
    {
        ArrayList<String> positionCoin = new ArrayList<>(Arrays.asList("a1","a2","a3","a4","a5","b1","b2","b3","b4","b5","c1","c2","c3","c4","c5","d1","d2","d3","d4","d5","e1","e2","e3","e4","e5")) ;

        for (String element : positionCoin){
            int index = mListPosition.indexOf(element);
            for(int k = 0; k<=36;k=k+9) {
                if (player.getmListPositionString().contains(mListPosition.get(index + k)) && (player.getmListPositionString().contains(mListPosition.get(index + 4)) || player.getmListPositionString().contains(mListPosition.get(index + 13)) || player.getmListPositionString().contains(mListPosition.get(index + 22)) || player.getmListPositionString().contains(mListPosition.get(index + 31)) || player.getmListPositionString().contains(mListPosition.get(index + 40)))) {
                    for(int l = 0; l<=4;l++) {
                        if (player.getmListPositionString().contains(mListPosition.get(index + l)) && (player.getmListPositionString().contains(mListPosition.get(index + 36)) || player.getmListPositionString().contains(mListPosition.get(index + 37)) || player.getmListPositionString().contains(mListPosition.get(index + 38)) || player.getmListPositionString().contains(mListPosition.get(index + 39)) || player.getmListPositionString().contains(mListPosition.get(index + 40)))) {
                            for (int j = 0; j < 5; j++)
                                player.getmPositionValide().add(mListPosition.get(index + j));
                            for (int j = 9; j < 14; j++)
                                player.getmPositionValide().add(mListPosition.get(index + j));
                            for (int j = 18; j < 23; j++)
                                player.getmPositionValide().add(mListPosition.get(index + j));
                            for (int j = 27; j < 32; j++)
                                player.getmPositionValide().add(mListPosition.get(index + j));
                            for (int j = 36; j < 41; j++)
                                player.getmPositionValide().add(mListPosition.get(index + j));
                            player.setcptDetectionPlateau(player.getcptDetectionPlateau()+1);
                        }
                    }
                }
            }
            for(int k = 0; k<=4;k++) {
                if (player.getmListPositionString().contains(mListPosition.get(index + k)) && (player.getmListPositionString().contains(mListPosition.get(index + 4)) || player.getmListPositionString().contains(mListPosition.get(index + 13)) || player.getmListPositionString().contains(mListPosition.get(index + 22)) || player.getmListPositionString().contains(mListPosition.get(index + 31)) || player.getmListPositionString().contains(mListPosition.get(index + 40)))) {
                    for(int l = 0; l<=36;l=l+9) {
                        if (player.getmListPositionString().contains(mListPosition.get(index + l)) && (player.getmListPositionString().contains(mListPosition.get(index + 36)) || player.getmListPositionString().contains(mListPosition.get(index + 37)) || player.getmListPositionString().contains(mListPosition.get(index + 38)) || player.getmListPositionString().contains(mListPosition.get(index + 39)) || player.getmListPositionString().contains(mListPosition.get(index + 40)))) {
                            for (int j = 0; j < 5; j++)
                                player.getmPositionValide().add(mListPosition.get(index + j));
                            for (int j = 9; j < 14; j++)
                                player.getmPositionValide().add(mListPosition.get(index + j));
                            for (int j = 18; j < 23; j++)
                                player.getmPositionValide().add(mListPosition.get(index + j));
                            for (int j = 27; j < 32; j++)
                                player.getmPositionValide().add(mListPosition.get(index + j));
                            for (int j = 36; j < 41; j++)
                                player.getmPositionValide().add(mListPosition.get(index + j));
                            player.setcptDetectionPlateau(player.getcptDetectionPlateau()+1);
                        }
                    }
                }
            }
        }
    }

    // Détecte si il y a un endroit où le domino peut être placé.
    public int detectionCasesVides(Player player, Domino domino)
    {
        int chiffre = 0;
        int index;
        for (int i=0;i<player.getmPositionValide().size();i++) {
            if (!player.getmListPositionString().contains(player.getmPositionValide().get(i))){
                if(i+1 <= 24) {
                    if (!player.getmListPositionString().contains(player.getmPositionValide().get(i + 1))) {
                        if(i-5 >= 0 && player.getmPositionValide().get(i - 5).substring(1).equals(player.getmPositionValide().get(i).substring(1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i-5))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i-5));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape()) || domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape()) ){
                                chiffre=1;
                            }
                        }
                        if(i+5 <= 24 && player.getmPositionValide().get(i + 5).substring(1).equals(player.getmPositionValide().get(i).substring(1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i+5))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i+5));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape()) || domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i-1 >= 0 && player.getmPositionValide().get(i - 1).substring(0,1).equals(player.getmPositionValide().get(i).substring(0,1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i-1))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i-1));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape()) || domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i+2 <= 24 && player.getmPositionValide().get(i +2 ).substring(0,1).equals(player.getmPositionValide().get(i).substring(0,1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i+2))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i+2));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape()) || domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i+6 <= 24 && player.getmPositionValide().get(i +6 ).substring(1).equals(player.getmPositionValide().get(i+1).substring(1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i+6))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i+6));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape()) || domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i-4 >= 0 && player.getmPositionValide().get(i - 4).substring(1).equals(player.getmPositionValide().get(i+1).substring(1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i-4))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i-4));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                    }
                }
                if(i+5 <= 24) {
                    if (!player.getmListPositionString().contains(player.getmPositionValide().get(i + 5))) {
                        if(i-1 >= 0 && player.getmPositionValide().get(i - 1).substring(0,1).equals(player.getmPositionValide().get(i).substring(0,1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i-1))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i-1));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape()) || domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i+1 <= 24 && player.getmPositionValide().get(i +1 ).substring(0,1).equals(player.getmPositionValide().get(i).substring(0,1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i+1))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i+1));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape()) || domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i+4 <= 24 && player.getmPositionValide().get(i +4 ).substring(0,1).equals(player.getmPositionValide().get(i+5).substring(0,1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i+4))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i+4));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i+6 <= 24 && player.getmPositionValide().get(i +6).substring(0,1).equals(player.getmPositionValide().get(i+5).substring(0,1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i+6))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i+6));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i+10 <= 24 && player.getmPositionValide().get(i +10).substring(1).equals(player.getmPositionValide().get(i).substring(1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i+10))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i+10));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i-5 >= 0 && player.getmPositionValide().get(i - 5).substring(1).equals(player.getmPositionValide().get(i).substring(1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i-5))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i-5));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                    }
                }
                if(i-1 >= 0) {
                    if (!player.getmListPositionString().contains(player.getmPositionValide().get(i - 1))) {
                        if(i-2 >= 0 && player.getmPositionValide().get(i - 2).substring(0,1).equals(player.getmPositionValide().get(i).substring(0,1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i-2))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i-2));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i+1 <= 24 && player.getmPositionValide().get(i + 1).substring(0,1).equals(player.getmPositionValide().get(i).substring(0,1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i+1))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i+1));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i+4 <= 24 && player.getmPositionValide().get(i +4 ).substring(1).equals(player.getmPositionValide().get(i-1).substring(1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i+4))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i+4));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i+5 <= 24 && player.getmPositionValide().get(i + 5).substring(1).equals(player.getmPositionValide().get(i).substring(1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i+5))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i+5));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i-6 >= 0 && player.getmPositionValide().get(i - 6).substring(1).equals(player.getmPositionValide().get(i-1).substring(1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i-6))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i-6));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i-5 >= 0 && player.getmPositionValide().get(i - 5).substring(1).equals(player.getmPositionValide().get(i).substring(1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i-5))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i-5));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                    }
                }
                if(i-5 >= 0) {
                    if (!player.getmListPositionString().contains(player.getmPositionValide().get(i - 5))) {
                        if(player.getmPositionValide().get(i - 1).substring(0,1).equals(player.getmPositionValide().get(i).substring(0,1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i-1))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i-1));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i+1 <= 24 && player.getmPositionValide().get(i + 1).substring(0,1).equals(player.getmPositionValide().get(i).substring(0,1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i+1))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i+1));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i+5 <= 24 && player.getmPositionValide().get(i + 5).substring(1).equals(player.getmPositionValide().get(i).substring(1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i+5))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i+5));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(player.getmPositionValide().get(i - 4).substring(0,1).equals(player.getmPositionValide().get(i-5).substring(0,1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i-4))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i-4));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i-6 >= 0 && player.getmPositionValide().get(i - 6).substring(0,1).equals(player.getmPositionValide().get(i-5).substring(0,1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i-6))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i-6));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                        if(i-10 >= 0 && player.getmPositionValide().get(i - 10).substring(1).equals(player.getmPositionValide().get(i).substring(1)) && player.getmListPositionString().contains(player.getmPositionValide().get(i-10))) {
                            index = player.getmListPositionString().indexOf(player.getmPositionValide().get(i-10));
                            if(domino.getmLandscapeLeft().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())|| domino.getmLandscapeRight().getmType().equals(player.getmListPosition().get(index).getmTypeLandscape())){
                                chiffre=1;
                            }
                        }
                    }
                }
            }
        }
        return chiffre;
    }

    // Compte les points de chaque joueurs
    public void comptagePoint()
    {

        for(Player element : mListPlayer){

            System.out.println("------- POINT DE "+element.getmName()+" -------");

            int nbPoint = 0;

            System.out.print("mListPosition avant ajout : ");
            for(Position i : element.getmListPosition()) {
                System.out.print(" "+i.getmNameCase());
            }

            Set set = new HashSet() ;
            set.addAll(element.getmPositionValide()) ;
            ArrayList<String> mPositionValide = new ArrayList<String>(set) ;

            // ajout des cases vides à mListPosition
            for(String i : mPositionValide){
                if(!element.getmListPositionString().contains(i)){
                    Position position = new Position(i);
                    position.setmTypeLandscape("null");
                    element.getmListPosition().add(position);
                }
            }

            System.out.print("mListPosition après ajout : ");
            for(Position i : element.getmListPosition()) {
                System.out.print(" "+i.getmNameCase());
            }

            // Trie de mListPosition

            for(Position i : element.getmListPosition()){
                String lettre = i.getmNameCase().substring(0,1);
                if (lettre.equals("a"))
                    i.setmNameCase("1"+i.getmNameCase().substring(1));
                if (lettre.equals("b"))
                    i.setmNameCase("2"+i.getmNameCase().substring(1));
                if (lettre.equals("c"))
                    i.setmNameCase("3"+i.getmNameCase().substring(1));
                if (lettre.equals("d"))
                    i.setmNameCase("4"+i.getmNameCase().substring(1));
                if (lettre.equals("e"))
                    i.setmNameCase("5"+i.getmNameCase().substring(1));
                if (lettre.equals("f"))
                    i.setmNameCase("6"+i.getmNameCase().substring(1));
                if (lettre.equals("g"))
                    i.setmNameCase("7"+i.getmNameCase().substring(1));
                if (lettre.equals("h"))
                    i.setmNameCase("8"+i.getmNameCase().substring(1));
                if (lettre.equals("i"))
                    i.setmNameCase("9"+i.getmNameCase().substring(1));
            }

            // trie la liste de chiffre
            for (int i = 1; i < element.getmListPosition().size(); i++) {
                Position positionATrier = element.getmListPosition().get(i);
                int elementATrier = Integer.parseInt(element.getmListPosition().get(i).getmNameCase());
                int j = i;
                while (j > 0 && Integer.parseInt(element.getmListPosition().get(j-1).getmNameCase()) > elementATrier) {
                    element.getmListPosition().set(j, element.getmListPosition().get(j - 1));
                    j--;
                }
                element.getmListPosition().set(j, positionATrier);
            }

            System.out.print("mListPosition après tri : ");
            for(Position i : element.getmListPosition()) {
                System.out.print(" "+i.getmNameCase());
            }

            for(Position i : element.getmListPosition()){
                String chiffre = i.getmNameCase().substring(0,1);
                if (chiffre.equals("1"))
                    i.setmNameCase("a"+i.getmNameCase().substring(1));
                if (chiffre.equals("2"))
                    i.setmNameCase("b"+i.getmNameCase().substring(1));
                if (chiffre.equals("3"))
                    i.setmNameCase("c"+i.getmNameCase().substring(1));
                if (chiffre.equals("4"))
                    i.setmNameCase("d"+i.getmNameCase().substring(1));
                if (chiffre.equals("5"))
                    i.setmNameCase("e"+i.getmNameCase().substring(1));
                if (chiffre.equals("6"))
                    i.setmNameCase("f"+i.getmNameCase().substring(1));
                if (chiffre.equals("7"))
                    i.setmNameCase("g"+i.getmNameCase().substring(1));
                if (chiffre.equals("8"))
                    i.setmNameCase("h"+i.getmNameCase().substring(1));
                if (chiffre.equals("9"))
                    i.setmNameCase("i"+i.getmNameCase().substring(1));
            }
            // Fin du tri de mListPosition


            System.out.print("mListPosition après après tri : ");
            for(Position i : element.getmListPosition()) {
                System.out.print(" "+i.getmNameCase());
            }

            ArrayList<ArrayList<Position>> royaumes = new ArrayList<>();

            for(int i=0; i<=24;i++) {
                ArrayList<Position> royaume = new ArrayList<>();
                if (element.getmListPosition().get(i).getmTypeLandscape().equals("Champs")) {
                    royaume.add(element.getmListPosition().get(i));
                    if (i + 1 <= 24) {
                        if (element.getmListPosition().get(i+1).getmNameCase().substring(0,1).equals(element.getmListPosition().get(i).getmNameCase().substring(0,1)) && element.getmListPosition().get(i + 1).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i + 1));
                        }
                    }
                    if (i - 1 >= 0) {
                        if (element.getmListPosition().get(i-1).getmNameCase().substring(0,1).equals(element.getmListPosition().get(i).getmNameCase().substring(0,1)) && element.getmListPosition().get(i - 1).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i - 1));
                        }
                    }
                    if (i - 5 >= 0 ) {
                        if (element.getmListPosition().get(i-5).getmNameCase().substring(1).equals(element.getmListPosition().get(i).getmNameCase().substring(1)) && element.getmListPosition().get(i - 5).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i - 5));
                        }
                    }
                    if (i + 5 <= 24) {
                        if (element.getmListPosition().get(i+5).getmNameCase().substring(1).equals(element.getmListPosition().get(i).getmNameCase().substring(1)) && element.getmListPosition().get(i + 5).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i + 5));
                        }
                    }
                }
                if (element.getmListPosition().get(i).getmTypeLandscape().equals("Prairie")) {
                    royaume.add(element.getmListPosition().get(i));
                    if (i + 1 <= 24) {
                        if (element.getmListPosition().get(i+1).getmNameCase().substring(0,1).equals(element.getmListPosition().get(i).getmNameCase().substring(0,1)) && element.getmListPosition().get(i + 1).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i + 1));
                        }
                    }
                    if (i - 1 >= 0) {
                        if (element.getmListPosition().get(i-1).getmNameCase().substring(0,1).equals(element.getmListPosition().get(i).getmNameCase().substring(0,1)) && element.getmListPosition().get(i - 1).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i - 1));
                        }
                    }
                    if (i - 5 >= 0) {
                        if (element.getmListPosition().get(i-5).getmNameCase().substring(1).equals(element.getmListPosition().get(i).getmNameCase().substring(1)) && element.getmListPosition().get(i - 5).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i - 5));
                        }
                    }
                    if (i + 5 <= 24) {
                        if (element.getmListPosition().get(i+5).getmNameCase().substring(1).equals(element.getmListPosition().get(i).getmNameCase().substring(1)) && element.getmListPosition().get(i + 5).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i + 5));
                        }
                    }
                }
                if (element.getmListPosition().get(i).getmTypeLandscape().equals("Foret")) {
                    royaume.add(element.getmListPosition().get(i));
                    if (i + 1 <= 24) {
                        if (element.getmListPosition().get(i+1).getmNameCase().substring(0,1).equals(element.getmListPosition().get(i).getmNameCase().substring(0,1)) && element.getmListPosition().get(i + 1).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i + 1));
                        }
                    }
                    if (i - 1 >= 0 ) {
                        if (element.getmListPosition().get(i-1).getmNameCase().substring(0,1).equals(element.getmListPosition().get(i).getmNameCase().substring(0,1)) && element.getmListPosition().get(i - 1).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i - 1));
                        }
                    }
                    if (i - 5 >= 0) {
                        if (element.getmListPosition().get(i-5).getmNameCase().substring(1).equals(element.getmListPosition().get(i).getmNameCase().substring(1)) && element.getmListPosition().get(i - 5).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i - 5));
                        }
                    }
                    if (i + 5 <= 24) {
                        if (element.getmListPosition().get(i+5).getmNameCase().substring(1).equals(element.getmListPosition().get(i).getmNameCase().substring(1)) && element.getmListPosition().get(i + 5).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i + 5));
                        }
                    }
                }
                if (element.getmListPosition().get(i).getmTypeLandscape().equals("Mine")) {
                    royaume.add(element.getmListPosition().get(i));
                    if (i + 1 <= 24) {
                        if (element.getmListPosition().get(i+1).getmNameCase().substring(0,1).equals(element.getmListPosition().get(i).getmNameCase().substring(0,1)) && element.getmListPosition().get(i + 1).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i + 1));
                        }
                    }
                    if (i - 1 >= 0) {
                        if (element.getmListPosition().get(i-1).getmNameCase().substring(0,1).equals(element.getmListPosition().get(i).getmNameCase().substring(0,1)) && element.getmListPosition().get(i - 1).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i - 1));
                        }
                    }
                    if (i - 5 >= 0) {
                        if (element.getmListPosition().get(i-5).getmNameCase().substring(1).equals(element.getmListPosition().get(i).getmNameCase().substring(1)) && element.getmListPosition().get(i - 5).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i - 5));
                        }
                    }
                    if (i + 5 <= 24) {
                        if (element.getmListPosition().get(i+5).getmNameCase().substring(1).equals(element.getmListPosition().get(i).getmNameCase().substring(1)) && element.getmListPosition().get(i + 5).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i + 5));
                        }
                    }
                }
                if (element.getmListPosition().get(i).getmTypeLandscape().equals("Montagne")) {
                    royaume.add(element.getmListPosition().get(i));
                    if (i + 1 <= 24) {
                        if (element.getmListPosition().get(i+1).getmNameCase().substring(0,1).equals(element.getmListPosition().get(i).getmNameCase().substring(0,1)) && element.getmListPosition().get(i + 1).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i + 1));
                        }
                    }
                    if (i - 1 >= 0) {
                        if (element.getmListPosition().get(i-1).getmNameCase().substring(0,1).equals(element.getmListPosition().get(i).getmNameCase().substring(0,1)) && element.getmListPosition().get(i - 1).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i - 1));
                        }
                    }
                    if (i - 5 >= 0 ) {
                        if (element.getmListPosition().get(i-5).getmNameCase().substring(1).equals(element.getmListPosition().get(i).getmNameCase().substring(1)) && element.getmListPosition().get(i - 5).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i - 5));
                        }
                    }
                    if (i + 5 <= 24) {
                        if (element.getmListPosition().get(i+5).getmNameCase().substring(1).equals(element.getmListPosition().get(i).getmNameCase().substring(1)) && element.getmListPosition().get(i + 5).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i + 5));
                        }
                    }
                }
                if (element.getmListPosition().get(i).getmTypeLandscape().equals("Mer")) {
                    royaume.add(element.getmListPosition().get(i));
                    if (i + 1 <= 24) {
                        if (element.getmListPosition().get(i+1).getmNameCase().substring(0,1).equals(element.getmListPosition().get(i).getmNameCase().substring(0,1)) && element.getmListPosition().get(i + 1).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i + 1));
                        }
                    }
                    if (i - 1 >= 0) {
                        if (element.getmListPosition().get(i-1).getmNameCase().substring(0,1).equals(element.getmListPosition().get(i).getmNameCase().substring(0,1)) && element.getmListPosition().get(i - 1).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i - 1));
                        }
                    }
                    if (i - 5 >= 0) {
                        if (element.getmListPosition().get(i-5).getmNameCase().substring(1).equals(element.getmListPosition().get(i).getmNameCase().substring(1)) && element.getmListPosition().get(i - 5).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i - 5));
                        }
                    }
                    if (i + 5 <= 24) {
                        if (element.getmListPosition().get(i+5).getmNameCase().substring(1).equals(element.getmListPosition().get(i).getmNameCase().substring(1)) && element.getmListPosition().get(i + 5).getmTypeLandscape().equals(element.getmListPosition().get(i).getmTypeLandscape())) {
                            royaume.add(element.getmListPosition().get(i + 5));
                        }
                    }
                }
                royaumes.add(royaume);
            }

            for(int i=0; i<royaumes.size();i++){
                System.out.println(" royaumes : "+royaumes.get(i));
            }

            for(int i=0;i<royaumes.size()-1;i++){
                for(int j = i;j< royaumes.size();j++) {
                    for(int h=0;h<royaumes.get(i).size();h++) {
                        if (royaumes.get(i + 1).contains(royaumes.get(i).get(j))) {
                            Set<Position> newRoyaumeSet = new HashSet<>();
                            newRoyaumeSet.addAll(royaumes.get(i + 1));
                            newRoyaumeSet.addAll(royaumes.get(i));
                            royaumes.remove(i + 1);
                            royaumes.remove(i);
                            ArrayList<Position> newRoyaume = new ArrayList<>(newRoyaumeSet);
                            royaumes.add(newRoyaume);
                            break;
                        }
                    }
                }
            }


            for(int i = 0; i<royaumes.size();i++){
                System.out.println(" ");
                System.out.print("royaume "+i);
                for(int j=0; j<royaumes.get(i).size();j++){
                    System.out.print(" "+royaumes.get(i).get(j).getmNameCase());
                }
            }

            for(int i = 0; i<royaumes.size();i++){
                for(Position j : royaumes.get(i)){
                    int nbCrown = 0;
                    nbCrown += j.getmLandscape().getmCrown();
                    nbPoint += nbCrown*royaumes.get(i).size();
                }
            }

            element.setmNbPoint(nbPoint);
        }
    }

    // Affiche le gagnant de la partie
    public void gameOver()
    {
        ArrayList<Integer> pointsPlayers = new ArrayList<>();
        for(Player element : mListPlayer){
            pointsPlayers.add(element.getmNbPoint());
        }
        int index = pointsPlayers.indexOf(Collections.max(pointsPlayers));
        System.out.println("!!!! Le gagnant est " + mListPlayer.get(index).getmName() + " !!!!");
    }
}
