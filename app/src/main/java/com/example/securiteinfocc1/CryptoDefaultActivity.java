package com.example.securiteinfocc1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;


public class CryptoDefaultActivity extends AppCompatActivity {

    EditText cle;
    EditText messageClair;
    EditText messageChiffre;

    int chiffrementID;
    String chiffrementName;

    boolean chiffre = true;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_default);


        cle = findViewById(R.id.cle);

       messageClair = findViewById(R.id.message_clair);
       messageChiffre = findViewById(R.id.message_chiffre);

        chiffrementID = getIntent().getIntExtra("chiffrementType" , 0);
        chiffrementName = getIntent().getStringExtra("chiffrementName" );

        Log.println(Log.ASSERT , "TYPE CHIFFREMENT ID : " , Integer.toString(chiffrementID));

        setTitle(chiffrementName);

        if (chiffrementID ==0 || chiffrementID == 1) cle.setVisibility(View.INVISIBLE); //cache le bouton clé pour Atbash et César





    }

    public void chiffreDechiffre(){

        if (!(messageClair.getText().toString().isEmpty())){

            switch (chiffrementID){

                /* Atbash */
                case 0 :

                    messageChiffre.setText(Crypto.atbash(messageClair.getText().toString()));

                /*César 3*/
                case 1 :

                    messageChiffre.setText(Crypto.cesar(messageClair.getText().toString() , chiffre));


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
