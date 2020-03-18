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

                resultat += ExtendedAscii.getChar(254 - (numeroCarac));



            }

            return resultat;
    }



}
