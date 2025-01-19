package com.example.project;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.jar.Attributes;

public class registration extends AppCompatActivity {
    EditText name , number;
    Button AddNumber , DeleteNumber ,UpdateNumber , Back;
    SQLiteDatabase mydb;
    Intent i;
    int _IDcondition ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_registration);

        mydb = openOrCreateDatabase("contactDB",MODE_PRIVATE,null);
        mydb.execSQL("CREATE TABLE IF NOT EXISTS CONTACTS_TABLE(ID INT PRIMARY KEY AUTOINCREMENT , NAME VARCHAR , NUMBER VARCHAR )");

        name = findViewById(R.id.etName);
        number = findViewById(R.id.etNumber);
        AddNumber = findViewById(R.id.btnAdd);
        DeleteNumber = findViewById(R.id.btnDelete);
        UpdateNumber = findViewById(R.id.btnUpdate);
        Back = findViewById(R.id.btnBack);
        mydb = openOrCreateDatabase("contactDB",MODE_PRIVATE,null);


        //  get intent

        i =  getIntent();
        if (i.getIntExtra("id",-1) != -1) {
            _IDcondition = i.getIntExtra("id",0);
            name.setText(i.getStringExtra("NameFromList"));
            number.setText(i.getStringExtra("NumberFromList"));
            UpdateNumber.setEnabled(true);
            DeleteNumber.setEnabled(true);
        }else{
            UpdateNumber.setEnabled(false);
            DeleteNumber.setEnabled(false);
        }
        //add
        AddNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().trim().isEmpty() || number.getText().toString().trim().isEmpty()){
                    Toast.makeText(registration.this ,"all fields are required" ,Toast.LENGTH_LONG).show();
                }
                else{
                     Cursor c = mydb.rawQuery("SELECT * FROM CONTACTS_TABLE WHERE NUMBER='"+number.getText().toString()+"' " +
                             "OR NAME ='"+name.getText().toString()+"'",null);
                     if(c.getCount()>0){
                         Toast.makeText(registration.this ,"Name or number , alredy exist" ,Toast.LENGTH_LONG).show();
                     }else{
                         mydb.execSQL("INSERT INTO CONTACTS_TABLE(NAME , NUMBER) VALUES ('"+name.getText().toString()+"','"
                                 +number.getText().toString()+"')");
                         clear();
                         Toast.makeText(registration.this ,"Number is added" ,Toast.LENGTH_LONG).show();
                     }

                }
            }
        });

        //delete
        DeleteNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( number.getText().toString().trim().length()==0){
                    Toast.makeText(registration.this ,"all fields are required" ,Toast.LENGTH_LONG).show();
                }
                else{
                    Cursor c = mydb.rawQuery("SELECT * FROM CONTACTS_TABLE WHERE NUMBER='"+number.getText().toString()+"' " +
                            "OR NAME ='"+name.getText().toString()+"'",null);
                    if(c.getCount()==0){
                        Toast.makeText(registration.this ,"not exist" ,Toast.LENGTH_LONG).show();
                    }else{
                mydb.execSQL("DELETE FROM CONTACTS_TABLE WHERE NUMBER = '"+number.getText().toString()+"'");
                clear();
                Toast.makeText(registration.this ,"Number is deleted" ,Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        //update
        UpdateNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(name.getText().toString().trim().length()==0 && number.getText().toString().trim().length()==0){
                    Toast.makeText(registration.this ,"all fields are required" ,Toast.LENGTH_LONG).show();

                }
                else{
                    Cursor c = mydb.rawQuery("SELECT * FROM CONTACTS_TABLE WHERE NUMBER='" + number.getText().toString() + "' OR " +
                            "NAME ='"+name.getText().toString()+"'", null);

                    if (c.getCount() > 1) {
                        Toast.makeText(registration.this, "alredy exist", Toast.LENGTH_LONG).show();

                    }
                    else{
                        mydb.execSQL("UPDATE CONTACTS_TABLE SET NAME ='"+name.getText().toString()+"' , NUMBER ='" +number.getText().toString()
                                +"' WHERE ID = " + _IDcondition );
                        Toast.makeText(registration.this, "contact updated", Toast.LENGTH_LONG).show();
                    }

                }



            }
        });
        Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                Intent x= new Intent(registration.this,MainActivity.class);
                i=null;
                startActivity(x);
            }
        });
    }
    public void clear(){
        name.setText("");
        number.setText("");
    }

}