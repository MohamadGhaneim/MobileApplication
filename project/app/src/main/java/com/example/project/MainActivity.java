package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    SQLiteDatabase mydb;
    EditText txtsearch;
    ImageButton search;
    Button add;
    ListView ListContacts;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        mydb = openOrCreateDatabase("contactDB",MODE_PRIVATE,null);

        mydb.execSQL("CREATE TABLE IF NOT EXISTS CONTACTS_TABLE (ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME VARCHAR, NUMBER VARCHAR)");


        add = findViewById(R.id.btnAdd);
        ListContacts = findViewById(R.id.lvcontact);
        search = findViewById(R.id.ibsearch);
        txtsearch = findViewById(R.id.etsearch);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txtsearch.getText().toString().trim().length() ==0){

                    display("SELECT NAME FROM CONTACTS_TABLE");
                }
                else{
                    display("SELECT NAME FROM CONTACTS_TABLE WHERE NAME LIKE'%"+txtsearch.getText().toString()+"%'");
                }
            }
        });
        //display list of contacts
        display("SELECT NAME FROM CONTACTS_TABLE");


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this , registration.class);
                i.putExtra("id",-1);
                startActivity(i);
            }
        });

        ListContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name =ListContacts.getItemAtPosition(position).toString();
                Cursor c= mydb.rawQuery("SELECT * FROM CONTACTS_TABLE WHERE NAME ='"+name+"'", null);



                if (c.moveToFirst()) {
                    String sendNAME = c.getString(c.getColumnIndexOrThrow("NAME"));
                    String sendNumber = c.getString(c.getColumnIndexOrThrow("NUMBER"));
                    int sendid = c.getInt(c.getColumnIndexOrThrow("ID"));
                    Intent i = new Intent(MainActivity.this , registration.class);
                    i.putExtra( "NameFromList",sendNAME);
                    i.putExtra("NumberFromList" ,sendNumber );
                    i.putExtra("true",true);
                    i.putExtra("id",sendid);
                    startActivity(i);
                    c.close();
                } else {
                    Toast.makeText(MainActivity.this, "Contact not found", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
    private void display(String query) {
        try {
            Cursor c = mydb.rawQuery(query, null);
            String[] contacts = new String[c.getCount()];
            int i = 0;
            if (c.moveToFirst()) {

                do {

                    contacts[i] = c.getString(0);
                    i++;
                } while (c.moveToNext());
            }
           ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, R.layout.items, contacts);
           ListContacts.setAdapter(adapter);
            c.close();
        } catch (Exception e) {
            Toast.makeText(MainActivity.this ,"Non contacts" ,Toast.LENGTH_LONG).show();
        }
    }
}