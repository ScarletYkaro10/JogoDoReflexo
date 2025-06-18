package com.example.jogodereflexo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private TextView textViewRecorde;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewRecorde = findViewById(R.id.textViewRecorde);
        Button buttonIniciar = findViewById(R.id.buttonIniciar);
        Button buttonIniciarHardcore = findViewById(R.id.buttonIniciarHardcore);
        Button buttonHistorico = findViewById(R.id.buttonHistorico);

        buttonIniciar.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, JogoActivity.class);
            intent.putExtra(JogoActivity.EXTRA_IS_HARDCORE, false);
            startActivity(intent);
        });

        buttonIniciarHardcore.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, JogoActivity.class);
            intent.putExtra(JogoActivity.EXTRA_IS_HARDCORE, true);
            startActivity(intent);
        });

        buttonHistorico.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, HistoricoActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        carregarRecorde();
    }

    private void carregarRecorde() {
        SharedPreferences prefs = getSharedPreferences("JogoReflexoPrefs", Context.MODE_PRIVATE);
        int recorde = prefs.getInt("RECORDE", 0);
        textViewRecorde.setText(String.format(Locale.getDefault(), getString(R.string.label_recorde), recorde));
    }
}