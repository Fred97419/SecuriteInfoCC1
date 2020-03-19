package com.example.securiteinfocc1;

import android.util.Log;

import java.util.Hashtable;

public class Crypto {

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

    //----------------------------------------------------------------------------------------------

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
    public static final char[][] cleToPolybe (String cleTempo) {

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

    /*Transforme le carré de polybe en chiffre de playfair*/
    public static final char[][] polybeToPlayfair (char[][] polybe){

        char[][] resultat = new char[6][6];

        for (int i=0 ; i<6 ; i++){


            for (int j=0 ; j<6 ; j++){

                resultat[j][i] = polybe[i][j];

            }


        }

        return resultat;
    }



    private static final String deleteNoLettersAndNumbers(String s){

        String result="";

        for (int i=0 ; i< s.length() ; i++){

            if( (s.charAt(i) >='A' && s.charAt(i) <= 'Z') || (s.charAt(i)>='0' && s.charAt(i)<='9')) result+=s.charAt(i);


        }
        return result;
    }

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
     * !NOTE! : A faire -> Gérer la gestion des espaces et autres caractères dans le message de sortie
     *
     * Chiffrement de Playfair, on prendre un carré de polybe puis on inverse celui-ci
     * en le lisant de haut en bas. On va ensuite échanger chaque paire de lettres du message
     * ce qui va créer le message chiffré.
     *
     * @param message à chiffrer
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

                            result += chiffrePlayfair[(coordLettre0[0] - 1) % 6][coordLettre0[1]];
                            result += chiffrePlayfair[(coordLettre1[0] - 1) % 6][coordLettre0[1]];

                        }

                    }
            }



        return result;

    }







}
