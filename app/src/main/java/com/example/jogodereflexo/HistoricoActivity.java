package com.example.jogodereflexo;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class HistoricoActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private HistoricoAdapter adapterTopRecordes;
    private HistoricoAdapter adapterUltimasJogadas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.titulo_historico));
        }
        dbHelper = new DatabaseHelper(this);
        RecyclerView recyclerTopRecordes = findViewById(R.id.recyclerViewTopRecordes);
        adapterTopRecordes = new HistoricoAdapter();
        recyclerTopRecordes.setLayoutManager(new LinearLayoutManager(this));
        recyclerTopRecordes.setAdapter(adapterTopRecordes);
        RecyclerView recyclerUltimasJogadas = findViewById(R.id.recyclerViewUltimasJogadas);
        adapterUltimasJogadas = new HistoricoAdapter();
        recyclerUltimasJogadas.setLayoutManager(new LinearLayoutManager(this));
        recyclerUltimasJogadas.setAdapter(adapterUltimasJogadas);
        carregarDados();
    }

    private void carregarDados() {
        List<Jogada> topRecordes = dbHelper.getTop3Recordes();
        adapterTopRecordes.submitList(topRecordes);

        List<Jogada> ultimasJogadas = dbHelper.getUltimasJogadas(10);
        adapterUltimasJogadas.submitList(ultimasJogadas);
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