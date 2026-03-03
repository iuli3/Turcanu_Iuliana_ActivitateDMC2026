package com.example.laborator3;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity {

    private int val1 = 0;
    private int val2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        Bundle b=getIntent().getExtras();
        if (b !=null)
        {
            String msgPrim=b.getString("mesaj");
            val1=b.getInt("val1");
            val2=b.getInt("val2");
            String text="msg: "+msgPrim+ " |nr: "+" "+val1+" "+val2;
            Toast.makeText(this,text,Toast.LENGTH_LONG).show();
        }

        Button btn=findViewById(R.id.btnactivitate3);
        btn.setOnClickListener(v -> {
            int suma=val1+val2;
            Intent intent=new Intent();
            intent.putExtra("msg3","rezultat Activitate3");
            intent.putExtra("rez",suma);
            setResult(RESULT_OK,intent);
            finish();

        });
    }




}