package com.projeto.model;
public class Usuario {
    // Atributos
    private String nome;
    private String email;
    private String tipo;
    private String senha;

    // Construtor
    public Usuario(String nome, String email, String tipo, String senha){
        setNome(nome);
        setEmail(email);
        setTipo(tipo);
        setSenha(senha);
    }

    // Getters e Setters
    public void setNome(String nome){
        this.nome = nome;
    }
    public String getNome(){
        return this.nome;
    }
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return this.email;
    }
    public void setTipo(String tipo){
        this.tipo = tipo;
    }
    public String getTipo(){
        return this.tipo;
    }
    public void setSenha(String senha){
        this.senha = senha;
    }
    public String getSenha() {return this.senha;}
}