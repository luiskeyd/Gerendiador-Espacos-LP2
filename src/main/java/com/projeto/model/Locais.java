package com.projeto.model;

public abstract class Locais {
    private String nome;
    private String horario_disponivel;
    private int capacidade;
    private String localizacao;
    private String reservado;

    // Construtor que aceita String para reservado
    public Locais(String nome, String horario_disponivel, int capacidade,
                  String localizacao, String reservado){
        setNome(nome);
        setHorarioDisponivel(horario_disponivel);
        setCapacidade(capacidade);
        setLocalizacao(localizacao);
        setReservado(reservado);
    }

    // Construtor que aceita boolean para reservado (para compatibilidade)
    public Locais(String nome, String horario_disponivel, int capacidade,
                  String localizacao, boolean reservado){
        this(nome, horario_disponivel, capacidade, localizacao,
                reservado ? "Ocupada" : "Disponível");
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }

    public void setHorarioDisponivel(String horario_disponivel){
        this.horario_disponivel = horario_disponivel;
    }

    public String getHorarioDisponivel(){
        return this.horario_disponivel;
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