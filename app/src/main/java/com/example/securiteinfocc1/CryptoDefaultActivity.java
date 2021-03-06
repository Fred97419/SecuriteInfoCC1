package com.example.securiteinfocc1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.vdurmont.emoji.Emoji;
import com.vdurmont.emoji.EmojiManager;

import java.nio.charset.Charset;


public class CryptoDefaultActivity extends AppCompatActivity {

    EditText cleEdit;
    EditText messageClairEdit;
    EditText messageChiffreEdit;

    TextView texteCleHill;
    EditText cle_hill_a;
    EditText cle_hill_b;
    EditText cle_hill_c;
    EditText cle_hill_d;

    EditText P;
    EditText Q;

    CheckBox hexaCheck;

    int chiffrementID;
    String chiffrementName;

    boolean chiffre = true;
    boolean hexa = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crypto_default);


        cleEdit = findViewById(R.id.cle);

       messageClairEdit = findViewById(R.id.message_clair);
       messageChiffreEdit = findViewById(R.id.message_chiffre);


       texteCleHill = findViewById(R.id.text_cle_hill);
       cle_hill_a = findViewById(R.id.cle_hill_a);
       cle_hill_b = findViewById(R.id.cle_hill_b);
       cle_hill_c = findViewById(R.id.cle_hill_c);
       cle_hill_d = findViewById(R.id.cle_hill_d);

       P = findViewById(R.id.P);
       Q = findViewById(R.id.Q);

       hexaCheck = findViewById(R.id.hexa);

        chiffrementID = getIntent().getIntExtra("chiffrementType" , 0);
        chiffrementName = getIntent().getStringExtra("chiffrementName" );

        Log.println(Log.ASSERT , "TYPE CHIFFREMENT ID : " , Integer.toString(chiffrementID));

        setTitle(chiffrementName);

        if (chiffrementID ==0) cleEdit.setVisibility(View.INVISIBLE); //cache le bouton clé pour Atbash et César

        if (chiffrementID ==1) cleEdit.setHint("Décalage");

        /* Affiche la clé sous forme de quatre nombres pour Hill*/
        if(chiffrementID != 5){

            texteCleHill.setVisibility(View.INVISIBLE);
            cle_hill_a.setVisibility(View.INVISIBLE);
            cle_hill_b.setVisibility(View.INVISIBLE);
            cle_hill_c.setVisibility(View.INVISIBLE);
            cle_hill_d.setVisibility(View.INVISIBLE);
        }

        if(chiffrementID==5) cleEdit.setVisibility(View.INVISIBLE);

        if(chiffrementID!=9){

            Q.setVisibility(View.INVISIBLE);
            P.setVisibility(View.INVISIBLE);
        }

        if(chiffrementID==9) cleEdit.setVisibility(View.INVISIBLE);

        if(chiffrementID==7) hexaCheck.setVisibility(View.VISIBLE);
        if(chiffrementID!=7) hexaCheck.setVisibility(View.INVISIBLE);

    }

    public void chiffreDechiffre(){

        String message = messageClairEdit.getText().toString();
        String cle = cleEdit.getText().toString();



        if (!(message.isEmpty())){

            switch (chiffrementID){

                /* Atbash */
                case 0 :
                    messageChiffreEdit.setText(Crypto.atbash(message));
                    break;

                /*César 3*/
                case 1 :

                    int decalage = Integer.parseInt(cle);
                    messageChiffreEdit.setText(Crypto.cesar(message , decalage ,chiffre));
                    break;

                /*Vigenère*/
                case 2 :

                    if(!(cle.isEmpty())) messageChiffreEdit.setText(Crypto.vigenere(message , cle , chiffre));
                    break;

                /*Playfair*/
                case 4:
                    if(!(cle.isEmpty())) messageChiffreEdit.setText(Crypto.playfair(message,cle,chiffre));
                    break;

                /*Hill avec m=2*/
                case 5:
                    String a = cle_hill_a.getText().toString();
                    String b = cle_hill_b.getText().toString();
                    String c = cle_hill_c.getText().toString();
                    String d = cle_hill_d.getText().toString();

                    if(!(a.isEmpty() || b.isEmpty() || c.isEmpty() || d.isEmpty())){

                        int[][] cleHill = {{Integer.parseInt(a),Integer.parseInt(b)},{Integer.parseInt(c),Integer.parseInt(d)}};

                        messageChiffreEdit.setText(Crypto.hill(message , cleHill , chiffre));
                        break;
                    }
                /*Transposition Rectangulaire*/
                case 6 :
                    if(!(cle.isEmpty())) messageChiffreEdit.setText(Crypto.transpositionRectangulaire(message , cle , chiffre));
                    break;


                case 7 :
                    if(!(cle.isEmpty())) messageChiffreEdit.setText(Crypto.DES(message , cle , chiffre , hexa));
                    break;


                case 8 :
                    if(!(cle.isEmpty())) messageChiffreEdit.setText(Crypto.suprise(message , cle , chiffre));
                    break;

                case 9 :
                    String p = P.getText().toString();
                    String q = Q.getText().toString();
                    String M = message;

                    if(!(p.isEmpty() || q.isEmpty() || M.isEmpty())){

                        int p_int = Integer.parseInt(p);
                        int q_int = Integer.parseInt(q);
                        int M_int = Integer.parseInt(M);

                        messageChiffreEdit.setText(Crypto.RSA(M_int , p_int , q_int , chiffre));

                    }

            }


            }


        }

    public void messageHexa(View v){

        hexa = !hexa;

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
