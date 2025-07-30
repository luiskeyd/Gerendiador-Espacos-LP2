package com.projeto.model;
public class Auditorio extends Locais{
    private int numCaixaSom;
    public Auditorio(String nome, int capacidade, 
    String localizacao, String reservado){
        super(nome, capacidade, localizacao, reservado);

    }
}