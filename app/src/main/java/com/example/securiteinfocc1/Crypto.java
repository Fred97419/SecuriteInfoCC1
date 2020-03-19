package com.example.securiteinfocc1;

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

        String cle = deleteDoublons(cleTempo).toUpperCase();

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

                    if (compteur_chiffre < 10 && compteur_lettre==26) {

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


    public static final String homophonePolybe(String message , String cle , boolean chiffre){

        char[][] carrePolybe = cleToPolybe(cle);



        return "";

    }



}
