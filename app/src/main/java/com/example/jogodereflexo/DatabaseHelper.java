package com.example.jogodereflexo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "JogoReflexo.db";
    private static final int DATABASE_VERSION = 2;
    public static final String TABLE_JOGADAS = "Jogadas";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_DATA_PARTIDA = "data_partida";
    public static final String COLUMN_PONTUACAO = "pontuacao";
    public static final String COLUMN_DURACAO = "duracao";
    public static final String COLUMN_FOI_RECORDE = "foiRecorde";
    public static final String COLUMN_MODO_DE_JOGO = "modo_de_jogo";

    private static final String CREATE_TABLE_JOGADAS = "CREATE TABLE " + TABLE_JOGADAS + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            COLUMN_DATA_PARTIDA + " TEXT, " +
            COLUMN_PONTUACAO + " INTEGER, " +
            COLUMN_DURACAO + " INTEGER, " +
            COLUMN_FOI_RECORDE + " INTEGER, " +
            COLUMN_MODO_DE_JOGO + " TEXT);";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_JOGADAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOGADAS);
        onCreate(db);
    }

    public void addJogada(String data, int pontuacao, int duracao, boolean foiRecorde, String modoDeJogo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_DATA_PARTIDA, data);
        cv.put(COLUMN_PONTUACAO, pontuacao);
        cv.put(COLUMN_DURACAO, duracao);
        cv.put(COLUMN_FOI_RECORDE, foiRecorde ? 1 : 0);
        cv.put(COLUMN_MODO_DE_JOGO, modoDeJogo);
        db.insert(TABLE_JOGADAS, null, cv);
        db.close();
    }

    public List<Jogada> getUltimasJogadas(int limite) {
        List<Jogada> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_JOGADAS, null, null, null, null, null, COLUMN_ID + " DESC", String.valueOf(limite));
        if (cursor.moveToFirst()) {
            do {
                lista.add(criarJogadaDoCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    public List<Jogada> getTop3Recordes() {
        List<Jogada> lista = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_JOGADAS, null, COLUMN_FOI_RECORDE + "=?", new String[]{"1"}, null, null, COLUMN_PONTUACAO + " DESC", "3");
        if (cursor.moveToFirst()) {
            do {
                lista.add(criarJogadaDoCursor(cursor));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return lista;
    }

    private Jogada criarJogadaDoCursor(Cursor cursor) {
        int idIndex = cursor.getColumnIndex(COLUMN_ID);
        int dataIndex = cursor.getColumnIndex(COLUMN_DATA_PARTIDA);
        int pontuacaoIndex = cursor.getColumnIndex(COLUMN_PONTUACAO);
        int recordeIndex = cursor.getColumnIndex(COLUMN_FOI_RECORDE);
        int modoIndex = cursor.getColumnIndex(COLUMN_MODO_DE_JOGO);


        if (idIndex == -1 || dataIndex == -1 || pontuacaoIndex == -1 || recordeIndex == -1 || modoIndex == -1) {
            return null;
        }

        return new Jogada(
                cursor.getLong(idIndex),
                cursor.getString(dataIndex),
                cursor.getInt(pontuacaoIndex),
                cursor.getInt(recordeIndex) == 1,
                cursor.getString(modoIndex)
        );
    }
}