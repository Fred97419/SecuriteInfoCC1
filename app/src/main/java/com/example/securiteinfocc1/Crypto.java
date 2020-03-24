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
     * [Substitution]
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


    /**
     * [Transposition]
     * Transposition rectangulaire, on prend le message d'entrée et on va échanger la place de
     * chaque lettre à l'aide de la clé.
     *
     * @param message à chiffrer/déchiffrer
     * @param cle String servant à construire la table pour transposer les lettres
     * @param chiffre Vrai pour chiffrer, Faux pour déchiffrer
     * @return
     */
    public static final String transpositionRectangulaire(String message , String cle , boolean chiffre) {

        String resultat = "";

        int nombre_ligne = (message.length() / cle.length()) + 1;

        char[][] tableau_chiffrage = new char[nombre_ligne][cle.length()];


        int[] numeroTab = cleToNumeroAssocie(cle);


        if (chiffre) {

            int compteur_i = 0;
            int compteur_j = 0;

            //remplis le tableau
            for (int i = 0; i < message.length(); i++) {

                if (compteur_j == cle.length()) {

                    compteur_i++;
                    compteur_j = 0;
                }

                tableau_chiffrage[compteur_i][compteur_j] = message.charAt(i);

                compteur_j++;

            }

            //Chiffrage du message
            for (int i = 0; i < cle.length(); i++) {

                int colonne_a_selectionner = indexOf(i, numeroTab);

                for (int j = 0; j < nombre_ligne; j++) {

                    try {

                        resultat += tableau_chiffrage[j][colonne_a_selectionner];

                    } catch (Exception e) {}
                }
            }
        }

        //Si on dechiffre
        if (!chiffre) {

            char[][] tableau_dechiffrage;

            int n = message.length();
            int c = cle.length();

            if (n % c == 0) {

                tableau_dechiffrage = new char[n / c][c];
                int compteur_i = 0;
                int compteur_j = 0;
                int colonne_a_selectionner = indexOf(compteur_j, numeroTab);

                for (int i = 0; i < message.length(); i++) {

                    if (compteur_i == n / c) {

                        compteur_j++;
                        compteur_i = 0;
                        colonne_a_selectionner = indexOf(compteur_j, numeroTab);
                    }

                    tableau_dechiffrage[compteur_i][colonne_a_selectionner] = message.charAt(i);

                    compteur_i++;
                }

            } else {

                int r = n % c;
                int q = n / c;

                tableau_dechiffrage = new char[q + 1][c];

                //les r premières colonnes seront remplies jusqu'au q+1 ième élement
                //les c-r suivantes seront remplies jusqu'au qième élement

                int compteur_i = 0;
                int compteur_j = 0;
                int colonne_a_selectionner = indexOf(compteur_j, numeroTab);


                for (int i = 0; i < message.length(); i++) {

                    if ((compteur_j < r && compteur_i == q + 1) || compteur_j >= r && compteur_i == q) {

                        compteur_j++;
                        compteur_i = 0;
                        colonne_a_selectionner = indexOf(compteur_j, numeroTab);
                    }

                    tableau_dechiffrage[compteur_i][colonne_a_selectionner] = message.charAt(i);

                    compteur_i++;
                }
            }

            //déchiffrage du message
            for (int i=0 ; i< nombre_ligne ; i++){

                for (int j=0 ; j<cle.length() ; j++){

                    try{

                        resultat+=tableau_dechiffrage[i][j];

                    }catch(Exception e){}
                }
            }
        }

                return resultat;
    }


    public static String DES(String message , String cle , boolean chiffre){

        //Si la clé fait plus de 64 bits ou que ce n'est pas un nombre hexadecimal
        if( ! cleIsCorrect(cle)) return "Clé incorrecte ! ";

        String resultat="";

        String[] tableau_G = new String[17];
        String[] tableau_D = new String[17];

        String[] blocs_message = messageToBloc(message);

        String cleK = hexaTo64Bits(cle);

        String[] tableau_sous_cleK = diversificationCle(cleK);

        //On applique pour tous les blocs de 64bits
        for (int i=0 ; i<blocs_message.length ; i++){

            Log.println(Log.ASSERT , "[DES]" , "---------------------------------[Bloc n"+i+"]---------------------------------------");

            String bloc = blocs_message[i];

            String bloc_permute = pemutationInitiale(bloc);
            Log.println(Log.ASSERT , "[DES]" , " ");


            String G0 = bloc_permute.substring(0,32);
            String D0 = bloc_permute.substring(32,64);




            Log.println(Log.ASSERT , "[DES] G0 : " , G0);
            Log.println(Log.ASSERT , "[DES] D0 : " , D0);

            tableau_G[0] = G0;
            tableau_D[0] = D0;


            for (int j=1 ; j<=16 ; j++){

            Log.println(Log.ASSERT , "[DES] " , "---------------------[TOUR "+j+"]-----------------------------------");

                tableau_G[j] = tableau_D[j-1];

                if (chiffre) tableau_D[j] = XOR(tableau_G[j-1] , fonctionConfusion(tableau_D[j-1] , tableau_sous_cleK[j-1]) );
                if (!chiffre) tableau_D[j] = XOR(tableau_G[j-1] , fonctionConfusion(tableau_D[j-1] , tableau_sous_cleK[15 - (j-1)]) );

                Log.println(Log.ASSERT , "[DES]" , " ");
                Log.println(Log.ASSERT , "[DES] G"+j+" : " , tableau_G[j]);
                Log.println(Log.ASSERT , "[DES] D"+j+" : " , tableau_D[j]);
                Log.println(Log.ASSERT , "[DES]" , " ");
            }

            String G16D16 = tableau_G[16] + tableau_D[16];
            String Z = permutationFinale(G16D16);

            Log.println(Log.ASSERT , "[DES]Bloc final" , G16D16);
            Log.println(Log.ASSERT , "[DES]Bloc final permute" , Z);


            resultat+= bits64ToString(Z);

        }



        return resultat ;

    }

    //-------------------------------[FONCTIONS RELATIVES AU DES]-----------------------------------

    private static String bits64ToString(String bloc64){

        String result ="";

        for (int i=0 ; i<8 ; i++){

            String bitsCarac = bloc64.substring(8*i , 8*(i+1));

            int numCarac = Integer.parseInt(bitsCarac , 2);


            result+=ExtendedAscii.getChar(numCarac,1);


        }

        return result;



    }

    public static String fonctionConfusion(String bloc32 , String cleDiversifie){

        String resultat="";

        //Expansion
        int[][] E = {
                {32,1,2,3,4,5},
                {4,5,6,7,8,9},
                {8,9,10,11,12,13},
                {12,13,14,15,16,17},
                {16,17,18,19,20,21},
                {20,21,22,23,24,25},
                {24,25,26,27,28,29},
                {28,29,30,31,32,1}
        };

        //Permutation Final
        int [][] P_final = {
                {16,7,20,21},
                {29,12,28,17},
                {1,15,23,26},
                {5,18,31,10},
                {2,8,24,14},
                {32,27,3,9},
                {19,13,30,6},
                {22,11,4,25}
        };

        //S-Box
        int S_box[][][] =  {
                {       {14,4,13,1,2,15,11,8,3,10,6,12,5,9,0,7},
                        {0,15,7,4,14,2,13,1,10,6,12,11,9,5,3,8},
                        {4,1,14,8,13,6,2,11,15,12,9,7,3,10,5,0},
                        {15,12,8,2,4,9,1,7,5,11,3,14,10,0,6,13}},
                {
                        {15,1,8,14,6,11,3,4,9,7,2,13,12,0,5,10},
                        {3,13,4,7,15,2,8,14,12,0,1,10,6,9,11,5},
                        {0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15},
                        {0,14,7,11,10,4,13,1,5,8,12,6,9,3,2,15}},
                {
                        {10,0,9,14,6,3,15,5,1,13,12,7,11,4,2,8},
                        {13,7,0,9,3,4,6,10,2,8,5,14,12,11,15,1},
                        {13,6,4,9,8,15,3,0,11,1,2,12,5,10,14,7},
                        {1,10,13,0,6,9,8,7,4,15,14,3,11,5,2,12}},
                {
                        {7,13,14,3,0,6,9,10,1,2,8,5,11,12,4,15},
                        {13,8,11,5,6,15,0,3,4,7,2,12,1,10,14,9},
                        {10,6,9,0,12,11,7,13,15,1,3,14,5,2,8,4},
                        {3,15,0,6,10,1,13,8,9,41,5,11,12,7,2,14}},
                {
                        {2,12,4,1,7,10,11,6,8,5,3,15,13,0,14,9},
                        {14,11,2,12,4,7,13,1,5,0,15,10,3,9,8,6},
                        {4,2,1,11,10,13,7,8,15,9,12,5,6,3,0,14},
                        {11,8,12,7,1,14,2,13,6,15,0,9,10,4,5,3}},
                {
                        {12,1,10,15,9,2,6,8,0,13,3,4,14,7,5,11},
                        {10,15,4,2,7,12,9,5,6,1,13,14,0,11,3,8},
                        {9,4,15,5,2,8,12,3,7,0,4,10,1,13,11,6},
                        {4,3,2,12,9,5,15,10,11,14,1,7,6,0,8,13}},
                {
                        {4,11,2,14,15,0,8,13,3,12,9,7,5,10,6,1},
                        {13,0,11,7,4,9,1,10,14,3,5,12,2,15,8,6},
                        {1,4,11,13,12,3,7,14,10,15,6,8,0,5,9,2},
                        {6,11,13,8,1,4,10,7,9,5,0,15,14,2,3,12}},
                {
                        {13,2,8,4,6,15,11,1,10,9,3,14,5,0,12,7},
                        {1,15,13,8,10,3,7,4,12,5,6,11,0,14,9,2},
                        {7,11,4,1,9,12,14,2,0,6,10,13,15,3,5,8},
                        {2,1,14,7,4,10,8,13,15,12,9,0,3,5,6,11}}
        };

        String[] tableau_B = new String[8];

        String B_transforme_32  = "";

        Log.println(Log.ASSERT , "[DES]", "Clé K : "+cleDiversifie);

        String bloc48= "";

        for (int i=0 ; i< 8 ; i++){

            for (int j=0 ; j<6 ; j++){

                bloc48 += bloc32.charAt( E[i][j] -1);
            }

        }



        Log.println(Log.ASSERT , "[DES]Bloc etendu E(M)",bloc48);

        //Bloc etendue (48bits) XOR Clé diversifiée

        String B = XOR(bloc48 , cleDiversifie);

        Log.println(Log.ASSERT , "[DES]E(M) XOR K",B);

        //Decoupage de B (48bits) en 8 blocs de 6bits
        for (int i=0 ; i<8 ; i++){

            tableau_B[i] = B.substring(i*6 , (i+1)*6);

        }

        //-------------------------[TRANSFORMATION LOCALES]-----------------------------------------

        //Transformation des 8 blocs Bi (8bits) en bloc de 4bits et réassemblage
        for (int i=0 ; i < 8 ; i++){

            String ligne_binary = "";
            String colonne_binary = "";

            // Construction de la chaine binaire contenant le premier et le dernier bit de Bi
            ligne_binary += tableau_B[i].charAt(0);
            ligne_binary += tableau_B[i].charAt(5);

            //Transformation de la chaine en entier compris en 0 et 3 (2 bits)
            int ligne_a_selectionner = Integer.parseInt(ligne_binary , 2);



            // Construction de la chaine binaire contenant les bits indice 1,2,3,4 de Bi
            colonne_binary+=tableau_B[i].charAt(1);
            colonne_binary+=tableau_B[i].charAt(2);
            colonne_binary+=tableau_B[i].charAt(3);
            colonne_binary+=tableau_B[i].charAt(4);

            //Transformation de la chaine en entier compris entre 0 et 15 (4 bits)
            int colonne_a_selectionner = Integer.parseInt(colonne_binary , 2);


            //on rajoute la représentation binaire (4bits) du nombre dans la table Si[ligne_a_selectionner][colonne_a_selectionner]

            String bits4 = intTo4Bits(
                    S_box[i][ligne_a_selectionner][colonne_a_selectionner]
            );

            Log.println(Log.ASSERT , "[DES] S"+i , bits4);

            B_transforme_32+=bits4;


        }

        Log.println(Log.ASSERT , "[DES] B --> " , B_transforme_32);

        //Permutation final avec la table P_Final
        for (int i=0 ; i<8  ; i++){

            for(int j=0 ; j<4 ; j++){

                resultat+=B_transforme_32.charAt( (P_final[i][j]) -1 );

            }

        }

        Log.println(Log.ASSERT , "[DES] P(B) --> " , resultat);
        return  resultat;



    }

    private static String XOR(String blocA , String blocB){

        String result="";

        for (int i=0 ; i<blocA.length() ; i++){

            if(blocA.charAt(i) == blocB.charAt(i)){

                result+='0';

            }

            else {

                result+='1';

            }

        }

        return result;


    }

    private static String pemutationInitiale(String bloc){

        String bloc_permute ="";

        int[][] P = {
                {58,50,42,34,26,18,10,2},
                {60,52,44,36,28,20,12,4},
                {62,54,46,38,30,22,14,6},
                {64,56,48,40,32,24,16,8},
                {57,49,41,33,25,17,9,1},
                {59,51,43,35,27,19,11,3},
                {61,53,47,37,29,21,13,5},
                {63,55,49,39,31,23,15,7}
        };

        for (int i=0 ; i<8 ; i++){

            for (int j=0 ; j<8 ; j++){

                bloc_permute+=bloc.charAt( P[i][j] -1 );

            }

        }

        Log.println(Log.ASSERT , "[DES] Bloc initial : ","  "+bloc);
        Log.println(Log.ASSERT , "[DES] P(Bloc initial): ",bloc_permute);
        return bloc_permute;

    }

    private static String permutationFinale(String bloc){

        String result = "";

        int [][] P = {
                {40,8,48,16,56,24,64,32},
                {39,7,47,15,55,23,63,31},
                {38,6,46,14,54,22,62,30},
                {37,5,45,13,53,21,61,29},
                {36,4,44,12,52,20,60,28},
                {35,3,43,11,51,19,59,27},
                {34,2,42,10,50,18,58,26},
                {33,1,41,9,49,17,57,25}
        };


        for (int i=0 ; i<8 ; i++){

            for (int j=0 ; j<8 ; j++){

                result+=bloc.charAt( (P[i][j]) -1);

            }

        }

        return result;

    }

    private static  String[] diversificationCle(String cle){

        String[] tableau_sous_cle = new String[17];

        String[] tableau_C = new String[17];
        String[] tableau_D = new String[17];

        String[] tableau_sous_cle_PC2 = new String[17];
        String[] tableau_sous_cle_final = new String[16];

        int[][] PC1Tab = {
                         {57,49,41,33,25,17,9},
                         {1,58,50,42,34,26,18},
                         {10,2,59,51,43,35,27},
                         {19,11,3,60,52,44,36},
                         {63,55,47,39,31,23,15},
                         {7,62,54,46,38,30,22},
                         {14,6,61,53,45,37,29},
                         {21,13,5,28,20,12,4}
        };

        int[][] PC2Tab = {
                         {14,17,11,24,1,5},
                         {3,28,15,6,21,10},
                         {23,19,12,4,26,8},
                         {16,7,27,20,13,2},
                         {41,52,31,37,47,55},
                         {30,40,51,45,33,48},
                         {44,49,39,56,34,53},
                         {46,42,50,36,29,32}

        };

        String PC1 = "";

        //permutation initiale de la clé ou PC1 fait 58 bits

        for (int i =0 ; i<8 ; i++){

            for (int j=0 ; j<7 ; j++){


                PC1+= cle.charAt(PC1Tab[i][j] -1);
            }

        }

        //premiers C0 et D0
        String C0 = PC1.substring(0,28);
        String D0 = PC1.substring(28,56);

        tableau_C[0] = C0;
        tableau_D[0] = D0;

        //15 autres Ci et Di
        for (int i=1 ; i<=16 ; i++){

            if(i==1 || i==2 | i==9 ||i==16){

                tableau_C[i] = rotateBitsLeft(tableau_C[i-1] , 1);
                tableau_D[i] = rotateBitsLeft(tableau_D[i-1] , 1);

            }

            else {

                tableau_C[i] = rotateBitsLeft(tableau_C[i-1] , 2);
                tableau_D[i] = rotateBitsLeft(tableau_D[i-1] , 2);

            }


        }

        //création des 16 sous clés avec Ki = Ci + Di
        //et deuxième permutation PC2 pour chaque Ki avec le tableau PC2

        for (int i = 0 ; i<tableau_sous_cle.length ; i++){

            tableau_sous_cle[i] = tableau_C[i] + tableau_D[i];
            String ki_PC2="";

            for (int j=0 ; j<8 ; j++){

                for(int k=0 ; k<6 ; k++){

                    ki_PC2+=tableau_sous_cle[i].charAt( (PC2Tab[j][k])-1 );

                }

            }

            tableau_sous_cle_PC2[i] = ki_PC2;


        }
        //on garde que les ki de 1 à 16
        for (int i =0 ; i< 16 ; i++){

            tableau_sous_cle_final[i] = tableau_sous_cle_PC2[i+1];

            Log.println(Log.ASSERT , "[DES] Clé K"+(i+1) , tableau_sous_cle_final[i]);

        }


        return tableau_sous_cle_final;

    }


    private static  String rotateBitsLeft(String bits , int decal){

        String resultat = "";
        char[] tableau_bits = new char[bits.length()];


        for (int i =0  ; i<bits.length() ; i++){

            tableau_bits[i] = bits.charAt(mod(i + decal , bits.length()));
            resultat+=tableau_bits[i];
        }


        return resultat;

    }

    private static  boolean cleIsCorrect(String cle){

        if(cle.length() > 16) return false;

        for (int i=0 ; i<cle.length() ; i++){

            char carac = cle.charAt(i);

            if(  !(((carac>='0' && carac <='9')) || ((carac>='a') && (carac<='f'))) ) return false;

        }

        return true;


    }

    //convertis le message en tableau de bloc de 64 bits
    private static  String[] messageToBloc (String message){

        String[]blocs;
        int nombre_blocs;

        if((message.length() % 8) ==0 ) nombre_blocs = message.length()/8;

        else {

            nombre_blocs = (message.length()/8)+1;
        }

        blocs = new String[nombre_blocs];

        int compteur_i=0;
        String bloc="";

        //sépare le message en bloc de 64 bits soit 8 caractère dans la table ASCII etendue

            for (int i = 0; i < message.length(); i++) {

                bloc+= message.charAt(i);

                if ((i % 7 == 0 && i > 0)) {

                    blocs[compteur_i] = bloc;
                    bloc = "";
                    compteur_i++;

                }
            }

            blocs[compteur_i] = bloc;


        //Traduction de chaque caractère d'un bloc (un caractère dans la table ASCII etendue correspondant à 8 bits)
        for (int i=0 ; i<blocs.length ; i++){

            Log.println(Log.ASSERT , " [DES] : Bloc du message n"+i , "Bloc -> "+ blocs[i]);

            String bloc_binaire ="";

            // remplis la chaine bloc binaire avec chaque représentation en binaire de chaque caractère
            for (int j=0 ; j<blocs[i].length() ; j++){

                bloc_binaire+=caracTo8Bits(blocs[i].charAt(j));

            }

            int reste_bits = 64-bloc_binaire.length(); //nombre de 0 à compléter pour obtenir un bloc de 64 bits

            //complète avec des 0
            for (int k=0 ; k < reste_bits ; k++){

                bloc_binaire+='0';

            }

            Log.println(Log.ASSERT , " [DES] : Bloc en binaire du message n"+i , bloc_binaire);
            blocs[i] = bloc_binaire;

        }

        return blocs;
    }

    //Convertit un caractère de la table ASCII etendue en chaine de 8 bits
    private static  String caracTo8Bits(char c){

        String result ="";
        int reste_bits;
        int carac = ExtendedAscii.getASCIICode(c);

        result+= Integer.toBinaryString(carac);

        reste_bits = 8 - result.length();

        for (int i= 0 ; i< reste_bits ; i++){

            result = '0'+result;

        }

        return result;

    }

    //Convertit un chiffre hexadecimal en chaine de 64 bits
    public static  String hexaTo64Bits(String hexa){

        String result="" ;
        int bits_préfixe;
        String zero_bits="";

        for (int i=0 ; i<hexa.length() ; i++){

            zero_bits="";
            int hex = Integer.parseInt( Character.toString(hexa.charAt(i)) , 16);

            String hex_binary = Integer.toBinaryString(hex);



            for (int j=0 ; j<4-hex_binary.length() ; j++){

                zero_bits+='0';

            }

            hex_binary = zero_bits+hex_binary;
            result += hex_binary;



        }



        zero_bits="";

        bits_préfixe = 64 - result.length();

        for (int i=0 ; i< bits_préfixe ; i++){

            zero_bits+='0';

        }

        result = zero_bits + result;

        Log.println(Log.ASSERT , " [DES] Clé->(64bits) : ",result);
        return result;

    }

    public static  String intTo4Bits(int n){

        String bits = Integer.toBinaryString(n);

        String zero_restant ="";

        for (int i=0 ; i<4-bits.length() ; i++){

            zero_restant+='0';
        }

        bits = zero_restant+bits;

        return bits;

    }





    //----------------------------------------------------------------------------------------------


    //------------------------------------[FONCTIONS INTERNES]--------------------------------------

    /*Enleve les doublons*/
    private static  String deleteDoublons(String s) {
        String result = "";


        for (int i = 0; i < s.length(); i++) {

            char c = s.charAt(i);
            if (result.indexOf(c) < 0)  result += c;
        }

        return result;
    }

    /*Vérifie si le caractère existe dans la clé*/
    private static  boolean isInCle(char c , String cle){

        for (int i = 0 ; i< cle.length() ; i++){

            if (cle.charAt(i) == c) return true;

        }

        return false;
    }

    /*Transforme la cle en carré de polybe*/
    private static  char[][] cleToPolybe (String cleTempo) {

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

    public static  void showPolybe(char[][] polybe){

        for (int i=0; i< 6 ; i++){

            System.out.println("-------------------");


            for (int j=0 ; j<6 ; j++){

                System.out.print(polybe[i][j] + "|");
            }

            System.out.println("");

        }


    }

    public static  void showTab(int[] t){

        for (int i=0 ; i< t.length ; i++){

            Log.println(Log.ASSERT , "ELEMENT TABLEAU "+i, Integer.toString(t[i]));
        }



    }

    /*Transforme le carré de polybe en chiffre de playfair*/
    private static  char[][] polybeToPlayfair (char[][] polybe){

        char[][] resultat = new char[6][6];

        for (int i=0 ; i<6 ; i++){


            for (int j=0 ; j<6 ; j++){

                resultat[j][i] = polybe[i][j];

            }


        }

        return resultat;
    }

    //vérifie si le caractère est une lettre ou un chiffre
    private static  boolean isALetterOrNumber(char c){

        if( (c >='A' && c <= 'Z') || (c>='0' && c<='9')) return true;

        else {

            return false;
        }


    }

    //enlève les caractères qui ne sont pas des lettres ou des chiffres
    private static  String deleteNoLettersAndNumbers(String s){

        String result="";

        for (int i=0 ; i< s.length() ; i++){

            if( (isALetterOrNumber(s.charAt(i)))) result+=s.charAt(i);


        }
        return result;
    }

    //donne les coordonnées (x,y) d'un caractère dans un carré de polybe ou chiffre de playfair
    private static  int[] letterCoordInPlayfair(char l , char[][] playfair){

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
