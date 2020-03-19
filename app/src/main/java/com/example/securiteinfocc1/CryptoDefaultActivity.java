package com.example.securiteinfocc1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class CryptoDefaultActivity extends AppCompatActivity {

    EditText cleEdit;
    EditText messageClairEdit;
    EditText messageChiffreEdit;

    int chiffrementID;
    String chiffrementName;

    boolean chiffre = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_default);


        cleEdit = findViewById(R.id.cle);

       messageClairEdit = findViewById(R.id.message_clair);
       messageChiffreEdit = findViewById(R.id.message_chiffre);

        chiffrementID = getIntent().getIntExtra("chiffrementType" , 0);
        chiffrementName = getIntent().getStringExtra("chiffrementName" );

        Log.println(Log.ASSERT , "TYPE CHIFFREMENT ID : " , Integer.toString(chiffrementID));

        setTitle(chiffrementName);

        if (chiffrementID ==0 || chiffrementID == 1) cleEdit.setVisibility(View.INVISIBLE); //cache le bouton clé pour Atbash et César


        char[][] test = Crypto.cleToPolybe("TEST1");

        Crypto.showPolybe(test);




    }

    public void chiffreDechiffre(){

        String message = messageClairEdit.getText().toString();
        String cle = cleEdit.getText().toString();

        if (!(message.isEmpty())){

            switch (chiffrementID){

                /* Atbash */
                case 0 :
                    messageChiffreEdit.setText(Crypto.atbash(message));

                /*César 3*/
                case 1 :

                    messageChiffreEdit.setText(Crypto.cesar(message , chiffre));

                /*Vigenère*/
                case 2 :

                    if(!(cle.isEmpty())) messageChiffreEdit.setText(Crypto.vigenere(message , cle , chiffre));



                }





            }


        }



    public void chiffre(View v){
        chiffre = true;
        chiffreDechiffre();


    }

    public void dechiffre(View v){
        chiffre=false;
        chiffreDechiffre();
    }

    public void retour(View v){

        Intent intent = new Intent(this , MainActivity.class);
        startActivity(intent);

    }




}
