package com.example.jogodereflexo;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class JogoActivity extends AppCompatActivity {

    public static final String EXTRA_IS_HARDCORE = "com.example.jogodereflexo.IS_HARDCORE";
    private long tempoDeJogo = 60000;
    private long intervaloDoAlvo = 900;
    private boolean isHardcoreMode = false;

    private RelativeLayout layoutRaizJogo;
    private ImageView imageViewAlvo;
    private TextView textViewPontuacao;
    private TextView textViewTempo;

    private int pontuacao = 0;
    private boolean jogoAtivo = false;
    private final Random random = new Random();
    private CountDownTimer gameTimer;
    private final Handler targetHandler = new Handler(Looper.getMainLooper());

    private SoundPool soundPool;
    private int somAcertoId, somErroId, somParabensId;

    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.app_name));
        }

        isHardcoreMode = getIntent().getBooleanExtra(EXTRA_IS_HARDCORE, false);
        if (isHardcoreMode) {
            tempoDeJogo = 30000;
            intervaloDoAlvo = 400;
        }

        dbHelper = new DatabaseHelper(this);
        layoutRaizJogo = findViewById(R.id.layoutRaizJogo);
        imageViewAlvo = findViewById(R.id.imageViewAlvo);
        textViewPontuacao = findViewById(R.id.textViewPontuacao);
        textViewTempo = findViewById(R.id.textViewTempo);

        configurarSons();

        imageViewAlvo.setOnClickListener(v -> {
            if (jogoAtivo) {
                pontuacao++;
                textViewPontuacao.setText(String.format(Locale.getDefault(), getString(R.string.jogo_pontos), pontuacao));
                soundPool.play(somAcertoId, 1, 1, 0, 0, 1);
                targetHandler.removeCallbacks(targetRunnable);
                moverAlvo();
                targetHandler.postDelayed(targetRunnable, intervaloDoAlvo);
            }
        });

        layoutRaizJogo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layoutRaizJogo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                iniciarJogo();
            }
        });
    }

    private void configurarSons() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build();
        somAcertoId = soundPool.load(this, R.raw.acertou, 1);
        somErroId = soundPool.load(this, R.raw.errou, 1);
        somParabensId = soundPool.load(this, R.raw.parabens, 1);
    }

    private void iniciarJogo() {
        pontuacao = 0;
        jogoAtivo = true;
        textViewPontuacao.setText(String.format(Locale.getDefault(), getString(R.string.jogo_pontos), pontuacao));

        gameTimer = new CountDownTimer(tempoDeJogo, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                textViewTempo.setText(String.format(Locale.getDefault(), getString(R.string.jogo_tempo), (millisUntilFinished / 1000)));
            }
            @Override
            public void onFinish() {
                textViewTempo.setText(String.format(Locale.getDefault(), getString(R.string.jogo_tempo), 0));
                fimDeJogo();
            }
        }.start();

        targetHandler.post(targetRunnable);
    }

    private final Runnable targetRunnable = new Runnable() {
        @Override
        public void run() {
            if (!jogoAtivo) return;
            if (imageViewAlvo.getVisibility() == View.VISIBLE) {
                soundPool.play(somErroId, 1, 1, 0, 0, 1);
            }
            moverAlvo();
            targetHandler.postDelayed(this, intervaloDoAlvo);
        }
    };

    private void moverAlvo() {
        int screenWidth = layoutRaizJogo.getWidth();
        int screenHeight = layoutRaizJogo.getHeight();
        int targetWidth = imageViewAlvo.getWidth();
        int targetHeight = imageViewAlvo.getHeight();
        if (screenWidth <= targetWidth || screenHeight <= targetHeight) return;
        int randomX = random.nextInt(screenWidth - targetWidth);
        int randomY = random.nextInt(screenHeight - targetHeight);
        imageViewAlvo.setX(randomX);
        imageViewAlvo.setY(randomY);
        imageViewAlvo.setVisibility(View.VISIBLE);
    }

    private void fimDeJogo() {
        jogoAtivo = false;
        targetHandler.removeCallbacks(targetRunnable);
        imageViewAlvo.setVisibility(View.GONE);
        soundPool.play(somParabensId, 1, 1, 0, 0, 1);
        SharedPreferences prefs = getSharedPreferences("JogoReflexoPrefs", Context.MODE_PRIVATE);
        int recordeAtual = prefs.getInt("RECORDE", 0);
        boolean novoRecorde = pontuacao > recordeAtual;
        if (novoRecorde) {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("RECORDE", pontuacao);
            editor.apply();
        }
        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
        String modo = isHardcoreMode ? "Hardcore" : "Normal";
        dbHelper.addJogada(data, pontuacao, (int) (tempoDeJogo / 1000), novoRecorde, modo);
        String mensagemDialog;
        if (novoRecorde) {
            mensagemDialog = String.format(Locale.getDefault(), getString(R.string.jogo_pontuacao_final_recorde), pontuacao);
        } else {
            mensagemDialog = String.format(Locale.getDefault(), getString(R.string.jogo_pontuacao_final), pontuacao);
        }

        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.jogo_fim_de_jogo))
                .setMessage(mensagemDialog)
                .setPositiveButton(getString(R.string.botao_jogar_novamente), (dialog, which) -> iniciarJogo())
                .setNegativeButton(getString(R.string.botao_voltar_menu), (dialog, which) -> finish())
                .setCancelable(false)
                .show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (gameTimer != null) {
            gameTimer.cancel();
        }
        targetHandler.removeCallbacks(targetRunnable);
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}