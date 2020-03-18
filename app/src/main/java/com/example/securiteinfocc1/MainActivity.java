package com.example.securiteinfocc1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MainActivity extends AppCompatActivity {

    ListView listeViewSecu;

    String[] listeSecu =  {"Atbash","César","Vigenère" , "Homophone" , "Hill" , "Transposition Rectangulaire" , "DES" , "Surprise !"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        listeViewSecu = findViewById(R.id.liste);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this , android.R.layout.simple_list_item_1 , listeSecu);

        listeViewSecu.setAdapter(adapter);

        final Intent defaultCrypto = new Intent(this , CryptoDefaultActivity.class);

        listeViewSecu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                defaultCrypto.putExtra("chiffrementType" , position);
                defaultCrypto.putExtra("chiffrementName" , listeSecu[position]);
                startActivity(defaultCrypto);

            }
        });

    }


}
