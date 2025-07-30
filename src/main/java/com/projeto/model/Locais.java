package com.projeto.model;

public abstract class Locais {
    // Atributos
    private String nome;
    private int capacidade;
    private String localizacao;
    private String reservado;

    // Construtor que aceita String para reservado
    public Locais(String nome, int capacidade,
                  String localizacao, String reservado){
        setNome(nome);
        setCapacidade(capacidade);
        setLocalizacao(localizacao);
        setReservado(reservado);
    }

    // Getters e Setters
    public void setNome(String nome){
        this.nome = nome;
    }
    public String getNome(){
        return this.nome;
    }
    public void setCapacidade(int capacidade){
        this.capacidade = capacidade;
    }
    public int getCapacidade(){
        return this.capacidade;
    }
    public void setLocalizacao(String localizacao){
        this.localizacao = localizacao;
    }
    public String getLocalizacao(){
        return this.localizacao;
    }
    public void setReservado(String reservado){
        this.reservado = reservado;
    }
    public String getReservado(){
        return this.reservado;
    }
}