package com.example.jogodereflexo;

public class Jogada {
    private final long id;
    private final String data;
    private final int pontuacao;
    private final boolean foiRecorde;
    private final String modoDeJogo;

    public Jogada(long id, String data, int pontuacao, boolean foiRecorde, String modoDeJogo) {
        this.id = id;
        this.data = data;
        this.pontuacao = pontuacao;
        this.foiRecorde = foiRecorde;
        this.modoDeJogo = modoDeJogo;
    }

    public long getId() { return id; }
    public String getData() { return data; }
    public int getPontuacao() { return pontuacao; }
    public boolean foiRecorde() { return foiRecorde; }
    public String getModoDeJogo() { return modoDeJogo; }
}