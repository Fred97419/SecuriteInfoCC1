package com.example.securiteinfocc1;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;


public class EmojiTable {

    Emoji[] emojitable =  EmojiManager.getAll().toArray(new Emoji[EmojiManager.getAll().size()]);
    Emoji[] emojitable_simplified = new Emoji[848];


    public EmojiTable(){

        //Emoji de [0,143] [146,627] [638,836] [1048,1073]
        int compteur=0;
        for (int i=0 ; i<emojitable.length; i++){

            if( (i>=0 && i<=143) || (i>=146 && i<=627) || (i>=641 && i<=836) || (i>=1048 && i<=1073)){

                emojitable_simplified[compteur] = emojitable[i];
                compteur++;

            }

        }

    }


    public Emoji[] getEmojitable_simplified(){return  this.emojitable_simplified;}


    public Emoji getEmoji(int i){return emojitable_simplified[i];}





}
