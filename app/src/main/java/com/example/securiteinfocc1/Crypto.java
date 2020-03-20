package com.example.securiteinfocc1;

import android.util.Log;

import java.util.Arrays;

public class Crypto {

    //------------------------[FONCTIONS APPELÉES PAR L'ACTIVITÉ]-----------------------------------

    /**
     * [Substitution]
     * Fonction Atbash qui va prendre chaque caractère du message et le substituer
     * par l'inverse de celui ci dans le table ASCII etendue
     *
     * @param message message a chiffrer

     *
     * @return le message chiffré ou déchiffré
     */
    public static final String atbash(String message){

        String resultat ="";

            for (int i=0 ; i<message.length() ; i++){


                int numeroCarac = ExtendedAscii.getASCIICode(message.charAt(i));

                resultat += ExtendedAscii.getChar(254 - (numeroCarac) , 0);



            }

            return resultat;
    }

    //----------------------------------------------------------------------------------------------

    /**
     * [Substitution]
     * Fonction césar qui prend chaque caractère et qui les décale
     * de 3 caractères dans la table ASCII etendue
     *
     * @param message message à chiffrer
     * @param chiffre vrai si on chiffre, faux si on dechiffre
     *
     * @return message à déchiffrer
     */
    public static final String cesar(String message , boolean chiffre){

        String resultat="";

        for (int i = 0 ; i< message.length() ; i++){

            int numeroCarac = ExtendedAscii.getASCIICode(message.charAt(i));

            if(chiffre) numeroCarac = (numeroCarac+3)%256; //evite de sortir de la table si le code ASCII est supérieur à 255

            if (!chiffre){

                numeroCarac-=3;
                if(numeroCarac<0) numeroCarac = 255-numeroCarac; //evite de sortir de la table si le code ASCII est négatif


            }

            resultat+=ExtendedAscii.getChar(numeroCarac , 0);


        }

        return resultat;

    }

    //----------------------------------------------------------------------------------------------

    /**
     * [Substitution]
     * Fonction vigenère, elle décale chaque caractère du message par le rang de chaque
     * caractère de la clé dans la table ASCII étendue
     *
     * @param message message à chiffrer/déchiffrer
     * @param cle clé qui va servir au décalage des lettres
     * @param chiffre vrai si on chiffre, faux si on déchiffre
     * @return
     */
    public static final String vigenere (String message , String cle , boolean chiffre){

        String resultat = "";

        for (int i = 0 ; i< message.length() ; i++){

            int decalage = ExtendedAscii.getASCIICode(cle.charAt(i%(cle.length())));
            int numeroCarac = ExtendedAscii.getASCIICode(message.charAt(i));

            if(chiffre) numeroCarac = (numeroCarac+decalage)%256; //evite de sortir de la table si le code ASCII est supérieur à 255

            if (!chiffre){

                numeroCarac-=decalage;
                if(numeroCarac<0) numeroCarac = 255-numeroCarac; //evite de sortir de la table si le code ASCII est négatif


            }

            resultat+= ExtendedAscii.getChar(numeroCarac , 0);



        }

        return resultat;

    }


    /**
     * Chiffrage homophone utilisant un carré de Polybe
     *
     * @param message à chiffrer/déchiffrer
     * @param cle cle transformée en carré de polybe 6x6
     * @param chiffre vrai pour chiffrer, faux pour déchiffrer
     * @return
     */
    public static final String homophonePolybe(String message , String cle , boolean chiffre){

        String resultat ="";

        String message_upper = message.toUpperCase();

        char[][] carrePolybe = cleToPolybe(cle);




        return "";

    }

    //----------------------------------------------------------------------------------------------

    /**
     *
     * Chiffrement de Playfair, on prendre un carré de polybe puis on inverse celui-ci
     * en le lisant de haut en bas. On va ensuite échanger chaque paire de lettres du message
     * ce qui va créer le message chiffré.
     *
     * @param message à chiffrer/déchiffrer
     * @param cle ici un String transformé en chiffre de Playfair
     * @param chiffre Vrai pour chiffrer, Faux pour déchiffrer
     * @return
     */
    public static final String playfair (String message , String cle , boolean chiffre){

        String message_uper = message.toUpperCase();

        String result ="";

        char[][] carrePolybe = cleToPolybe(cle);
        char[][] chiffrePlayfair = polybeToPlayfair(carrePolybe);



        //enleve les espaces et caractères spéciaux
        String message_only = deleteNoLettersAndNumbers(message_uper);

        if(message_only.length()%2 !=0) message_only+='X';

        //vérifie si un couple de lettre ne contient pas la même lettre, si oui rajoute une lettre entre

        //objet Java qui nous permet d'insérer où l'on souhaite un caractère
        StringBuffer stringbuffer = new StringBuffer(message_only);

        for (int i=0 ; i<message_only.length()-2 ; i+=2) {

            if (message_only.charAt(i) == message_only.charAt(i + 1)) {

                stringbuffer.insert(i+1 , 'Q');

            }
        }


        String message_ok = stringbuffer.toString();

        if(message_ok.length()%2 !=0) message_ok+='X';

        Log.println(Log.ASSERT , "PLAYFAIR A CODER:" , message_ok);

        Log.println(Log.ASSERT , "CHIFFRE PLAYFAIR -> " , "------------");
        showPolybe(chiffrePlayfair);


        /* Traitement des couples de lettres */

            String coupleLettre;
            for (int i=0 ; i<message_ok.length() ; i+=2) {

                coupleLettre="";

                coupleLettre += message_ok.charAt(i);
                coupleLettre += message_ok.charAt(i+1);

                Log.println(Log.ASSERT , "PLAYFAIR -> COUPLE : "+i , coupleLettre );


                int[] coordLettre0 = letterCoordInPlayfair(coupleLettre.charAt(0), chiffrePlayfair);
                int[] coordLettre1 = letterCoordInPlayfair(coupleLettre.charAt(1), chiffrePlayfair);


                    //si les deux lettres sont sur des lignes et des colonnes différentes
                    if (coordLettre0[0] != coordLettre1[0] && coordLettre0[1] != coordLettre1[1]) {

                        result += chiffrePlayfair[coordLettre0[0]][coordLettre1[1]];
                        result += chiffrePlayfair[coordLettre1[0]][coordLettre0[1]];

                    }

                    /*On chiffre le message*/
                    if(chiffre){

                        //si les deux lettres sont sur les mêmes lignes
                        if (coordLettre0[0] == coordLettre1[0]) {

                            result += chiffrePlayfair[coordLettre0[0]][(coordLettre0[1] + 1) % 6];
                            result += chiffrePlayfair[coordLettre0[0]][(coordLettre1[1] + 1) % 6];

                        }

                        //si les deux lettres sont sur les mêmes colonnes
                        if (coordLettre0[1] == coordLettre1[1]) {

                            result += chiffrePlayfair[(coordLettre0[0] + 1) % 6][coordLettre0[1]];
                            result += chiffrePlayfair[(coordLettre1[0] + 1) % 6][coordLettre0[1]];

                        }

                    }

                    /*On déchiffre le message*/
                    if(!chiffre){

                        //si les deux lettres sont sur les mêmes lignes
                        if (coordLettre0[0] == coordLettre1[0]) {

                            result += chiffrePlayfair[coordLettre0[0]][(coordLettre0[1] - 1) % 6];
                            result += chiffrePlayfair[coordLettre0[0]][(coordLettre1[1] - 1) % 6];

                        }

                        //si les deux lettres sont sur les mêmes colonnes
                        if (coordLettre0[1] == coordLettre1[1]) {

                            //evite que l'indice soit négatif (le modulo en java ne gérant pas les nombres négatifs comme python)
                            int colonne0 = (coordLettre0[0] -1);
                            int colonne1 = (coordLettre1[0] -1);

                            if(colonne0 < 0) colonne0 = 6+colonne0;
                            if(colonne1 < 0) colonne1 = 6+colonne1;

                            result += chiffrePlayfair[colonne0][coordLettre0[1]];
                            result += chiffrePlayfair[colonne1][coordLettre0[1]];

                        }

                    }
            }

        /*Gestion caractères spéciaux et espace */

        StringBuffer result_string_buffer = new StringBuffer(result);

        // rajoute le caractère spécial ou l'espace dans la chaine resultat à l'indice où il le trouve dans le message d'origine
        for (int i=0 ; i<message_uper.length() ; i++){

            if( ! isALetterOrNumber(message_uper.charAt(i))) result_string_buffer.insert(i , message_uper.charAt(i));

        }

        String result_final = result_string_buffer.toString();


        return result_final;

    }

    //----------------------------------------------------------------------------------------------

    /**
     * Chiffrement de Hill, on prend le message qu'on divise par bloc de deux
     * caractères. Chaque caractère est associé à un numéro dans la table ASCII
     * étendue. On chanque la valeur de ces numéros en faisant le produit matriciel
     * de la clé avec les les numéros.
     *
     *
     *
     * @param message message à chiffrer/déchiffrer
     * @param cle ici une matrice 2X2 d'int servant au chiffrage déchiffrage
     * @param chiffre Vrai pour chiffrer, Faux pour déchiffrer
     * @return
     */
    public static final String hill(String message , int[][] cle , boolean chiffre){

        String resultat="";

        //Rajout d'un caractère '*' si le message n'est pas de longueur pair
        if(message.length() %2 !=0) message+= '*';

        //det = a*d - b*c
        int det = (cle[0][0]*cle[1][1]) - (cle[0][1]*cle[1][0]);

        if(pgcd(det , 256) != 1){

            resultat="Matrice de chiffrement incorrecte";

            return resultat;

        }

        if(!chiffre){

            int[][] cle_inverse = new int[2][2];

            cle_inverse[0][0] = modInverse(det,256)*(cle[1][1]);
            cle_inverse[0][1] = modInverse(det,256)*(-(cle[0][1]));
            cle_inverse[1][0] = modInverse(det,256)*(-(cle[1][0]));
            cle_inverse[1][1] = modInverse(det,256)*(cle[0][0]);



            cle = Arrays.copyOf(cle_inverse , cle_inverse.length);

            Log.println(Log.ASSERT , "M-1 : a --> " , Integer.toString(cle[0][0]));
            Log.println(Log.ASSERT , "M-1 : b --> " , Integer.toString(cle[0][1]));
            Log.println(Log.ASSERT , "M-1 : c --> " , Integer.toString(cle[1][0]));
            Log.println(Log.ASSERT , "M-1 : d --> " , Integer.toString(cle[1][1]));


        }


        /* On parcourt les blocs de deux lettres et on éffectue le produit matriciel entre la clé et le bloc */
        String blocLettre="";
        int x_bloc[];
        int y_bloc[];
        for (int i=0 ; i<message.length(); i+=2){

            blocLettre="";
            x_bloc = new int[2];

            blocLettre += message.charAt(i);
            blocLettre += message.charAt(i+1);

            x_bloc[0] = ExtendedAscii.getASCIICode(blocLettre.charAt(0)); //x0
            x_bloc[1] = ExtendedAscii.getASCIICode(blocLettre.charAt(1)); //x1

            Log.println(Log.ASSERT , "VALEUR X0 -> " , Integer.toString(x_bloc[0]));
            Log.println(Log.ASSERT , "VALEUR X1 -> " , Integer.toString(x_bloc[1]));

            y_bloc = produitMatricielHill(cle , x_bloc);

            Log.println(Log.ASSERT , "VALEUR Y0 -> " , Integer.toString(y_bloc[0]));
            Log.println(Log.ASSERT , "VALEUR Y1 -> " , Integer.toString(y_bloc[1]));



            resultat+= ExtendedAscii.getChar(y_bloc[0] , 0);
            resultat+= ExtendedAscii.getChar(y_bloc[1] , 0);


        }

        return resultat;

    }


    public static final String transpositionRectangulaire(String message , String cle , boolean chiffre){

        String resultat ="";

        int nombre_ligne = (message.length()/cle.length())+1;

        char[][] tableau_chiffrage = new char[(message.length()/cle.length())+1][cle.length()];

        int[] numeroTab = cleToNumeroAssocie(cle);


        int compteur_i=0;
        int compteur_j=0;

        //remplis le reste du tableau
        for (int i=0 ; i<message.length() ; i++){

            if(compteur_j==cle.length()){

                compteur_i++;
                compteur_j=0;
            }

            tableau_chiffrage[compteur_i][compteur_j] = message.charAt(i);

            compteur_j++;

        }
        /*TEST -------->*/
        for (int i=0 ; i<nombre_ligne; i++){


            for (int j=0 ; j<cle.length(); j++){

                try{
                    System.out.print(tableau_chiffrage[i][j]);
                }
                catch (Exception e){}


            }

            System.out.println("-----------------------");

        }
        /*------------------------>*/

        //Chiffrage du message

        Log.println(Log.ASSERT  , "tab numero -----> " , "vvvvvv");
        showTab(numeroTab);

        for (int i=0 ; i< cle.length(); i++){

            int colonne_a_selectionner = indexOf(i , numeroTab);

            Log.println(Log.ASSERT  , "COLONNE A SELECTIONNER" , Integer.toString(colonne_a_selectionner));

            for (int j=0 ; j< (message.length()/cle.length())+1; j++){

                try{



                    resultat+= tableau_chiffrage[j][colonne_a_selectionner];

                }catch(Exception e){}

            }

        }

        return resultat;

    }



    //------------------------------------[FONCTIONS INTERNES]--------------------------------------

    /*Enleve les doublons*/
    private static final String deleteDoublons(String s) {
        String result = "";


        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);
            if (result.indexOf(c) < 0)  result += c;
        }

        return result;
    }

    /*Vérifie si le caractère existe dans la clé*/
    private static final boolean isInCle(char c , String cle){

        for (int i = 0 ; i< cle.length() ; i++){

            if (cle.charAt(i) == c) return true;

        }

        return false;
    }

    /*Transforme la cle en carré de polybe*/
    private static final char[][] cleToPolybe (String cleTempo) {

        String cle_upper = cleTempo.toUpperCase();
        String cle = deleteDoublons(cle_upper);

        char[][] resultat = new char[6][6]; //carre de polybe

        int compteur_cle = 0;
        int compteur_lettre = 0;
        int compteur_chiffre = 0;

        int i =0;
        int j =0;

        while(i<6) {

            j=0;

            while(j<6) {

                // On remplit les première cases avec la clé sans doublons
                if (compteur_cle < cle.length()) {

                    resultat[i][j] = cle.charAt(compteur_cle);
                    compteur_cle++;

                    j++;
                }

                else {

                    //On remplie avec les lettres n'etant pas dans la clé
                    if (compteur_lettre < 26 && compteur_cle == cle.length() ) {

                        char lettre = ExtendedAscii.getChar(65 + compteur_lettre, 0);


                        if (!isInCle(lettre, cle)){

                            resultat[i][j] = lettre;
                            j++;
                        }

                        compteur_lettre++;


                    }
                    //On remplit avec les chiffres n'étant pas dans la clé
                    if (compteur_chiffre < 10 && compteur_lettre==26 && compteur_cle == cle.length()) {

                        char chiffre = ExtendedAscii.getChar(48 + compteur_chiffre, 0);

                        if (!isInCle(chiffre, cle)){

                            resultat[i][j] = chiffre;
                            j++;
                        }

                        compteur_chiffre++;
                    }

                }


            }

            i++;

        }

        return resultat;
    }

    public static final void showPolybe(char[][] polybe){

        for (int i=0; i< 6 ; i++){

            System.out.println("-------------------");


            for (int j=0 ; j<6 ; j++){

                System.out.print(polybe[i][j] + "|");
            }

            System.out.println("");

        }


    }

    public static final void showTab(int[] t){

        for (int i=0 ; i< t.length ; i++){

            Log.println(Log.ASSERT , "ELEMENT TABLEAU "+i, Integer.toString(t[i]));
        }



    }

    /*Transforme le carré de polybe en chiffre de playfair*/
    private static final char[][] polybeToPlayfair (char[][] polybe){

        char[][] resultat = new char[6][6];

        for (int i=0 ; i<6 ; i++){


            for (int j=0 ; j<6 ; j++){

                resultat[j][i] = polybe[i][j];

            }


        }

        return resultat;
    }

    //vérifie si le caractère est une lettre ou un chiffre
    private static final boolean isALetterOrNumber(char c){

        if( (c >='A' && c <= 'Z') || (c>='0' && c<='9')) return true;

        else {

            return false;
        }


    }

    //enlève les caractères qui ne sont pas des lettres ou des chiffres
    private static final String deleteNoLettersAndNumbers(String s){

        String result="";

        for (int i=0 ; i< s.length() ; i++){

            if( (isALetterOrNumber(s.charAt(i)))) result+=s.charAt(i);


        }
        return result;
    }

    //donne les coordonnées (x,y) d'un caractère dans un carré de polybe ou chiffre de playfair
    private static final int[] letterCoordInPlayfair(char l , char[][] playfair){

        int[] resultat = new int[2];

        for (int i=0 ; i< 6 ; i++){

            for (int j=0 ; j<6 ; j++){

                if(l == playfair[i][j]){

                    resultat[0] = i;
                    resultat[1] = j;

                }

            }
        }

        return resultat;

    }

    // donne l'inverse de a modulo m
    private static int modInverse(int a, int m){

        int m0 = m;
        int y = 0, x = 1;

        if (m == 1)
            return 0;

        while (a > 1)
        {
            //quotient
            int q = a / m;

            int t = m;

            // m reste
            // Algo d'euclide etendue
            m = a % m;
            a = t;
            t = y;

            // Update x and y
            y = x - q * y;
            x = t;
        }

        //Evite les valeurs négatives
        if (x < 0)
            x += m0;

        return x;
    }

    //modulo pouvant prendre en compte les a%b avec a<0
    private static int mod(int a, int b)
    {
        int ret = a % b;
        if (ret < 0)
            ret += b;
        return ret;
    }

    //fait le produit matrice entre une matrice 2x2 et un bloc 1X2 et renvoyant le resultat modulo 256
    private static int[] produitMatricielHill(int[][] cle , int[] x){

        int[] y = new int[2];

        int y0,y1;

        y0 =((cle[0][0]*x[0]) + (cle[0][1]*x[1]));
        y1 =((cle[1][0]*x[0]) + (cle[1][1]*x[1]));


        //utilisation d'une fonction modulo capable de gérer les nombres négatifs (ce que Java ne fait pas)
        y[0] = mod(y0,256);
        y[1] = mod(y1,256);

        Log.println(Log.ASSERT , "MODULO test -> " , Integer.toString(mod(-428 , 256)));



        return y;

    }

    private static int pgcd (int m,int n)
    {
        int r=0;
        while(n!=0)
        {
            r=m%n;
            m=n;
            n=r;
        }
        return m;
    }

    private static boolean isInTab(int n , int[] tab){

        for(int i=0 ; i<tab.length ; i++){

            if (n == tab[i]) return true;

        }
        return false;
    }

    public static int[] cleToNumeroAssocie(String cle){

        int[] resultat = new int[cle.length()];

        for (int i=0 ; i<resultat.length ; i++){
            resultat[i] = -1;
        }

        char[] cle_char = cle.toCharArray();

        char[] cle_char_sort = cle.toCharArray();

        Arrays.sort(cle_char_sort);

        for (int i=0 ; i<cle_char.length ; i++){

            //renvoie l'indice de l'élement cherché
            int indice = Arrays.binarySearch(cle_char_sort , cle_char[i]);

            if(isInTab(indice , resultat)) indice++;

            resultat[i] = indice;

        }

        return resultat;
    }

    //renvoie l'indice d'un element dans un tableau de int
    private static int indexOf(int n , int[] t){

        for (int i=0 ; i<t.length ; i++){

            if(t[i] == n) return i;

        }

        return -1;

    }

    //----------------------------------------------------------------------------------------------




}
