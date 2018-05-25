package com.example.henrique.previsodotempo;

import java.io.Serializable;

public class Cidade implements Serializable{
    private String chave;
    private String nome;

    public Cidade(String chave, String nome) {
        this.setChave(chave);
        this.setNome(nome);
    }

    public Cidade() {

    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getChave() {
        return chave;
    }

    public void setChave(String chave) {
        this.chave = chave;
    }

    @Override
    public String toString() {
        return getNome();
    }
}
