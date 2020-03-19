package com.example.securiteinfocc1;

public class Crypto {

    /**
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

            if(chiffre) numeroCarac = (numeroCarac+3)%255; //evite de sortir de la table si le code ASCII est supérieur à 255

            if (!chiffre){

                numeroCarac-=3;
                if(numeroCarac<0) numeroCarac = 255-numeroCarac; //evite de sortir de la table si le code ASCII est négatif


            }

            resultat+=ExtendedAscii.getChar(numeroCarac , 0);


        }

        return resultat;

    }



}
