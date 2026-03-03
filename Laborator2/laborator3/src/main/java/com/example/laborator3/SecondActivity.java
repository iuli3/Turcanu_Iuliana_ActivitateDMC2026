package com.example.laborator3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

public class SecondActivity extends AppCompatActivity {

    private static final String LOG_TAG ="Lab3";

    private final ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String mesaj = data.getStringExtra("msg3");
                        int suma = data.getIntExtra("rez", 0);
                        Toast.makeText(this, mesaj + " | Suma: " + suma, Toast.LENGTH_LONG).show();
                    }
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        Button btn=findViewById(R.id.openbtn);
        btn.setOnClickListener(v -> {
            Intent intent=new Intent(SecondActivity.this,ThirdActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("mesaj","MSGGGG");
            bundle.putInt("val1",5);
            bundle.putInt("val2",10);
            intent.putExtras(bundle);
            mStartForResult.launch(intent);
        });



        Log.d(LOG_TAG, "oncreate a fost apelata");
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        Log.i(LOG_TAG, "onStart apelata");
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Log.v(LOG_TAG, "onResume apelata");

    }

    @Override
    protected void onStop()
    {
        super.onStop();
        Log.w(LOG_TAG, "onStop apelata");

    }

    @Override
    protected void onPause()
    {
        super.onPause();
        Log.e(LOG_TAG, "onPause apelata");
    }


}