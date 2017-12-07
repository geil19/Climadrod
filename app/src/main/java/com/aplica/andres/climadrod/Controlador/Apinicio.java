package com.aplica.andres.climadrod.Controlador;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.EditText;

import com.aplica.andres.climadrod.R;

/**
 * Created by andres on 06/12/2017.
 */

public class Apinicio extends Activity{
    private EditText Response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_ap);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inicio_ap);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Apinicio.this, ClimaActivity.class);
                startActivity(intent);
                finish();

            }
        }, 3000);
    }
}