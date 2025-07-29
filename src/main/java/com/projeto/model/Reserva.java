package com.projeto.model;

import java.time.LocalDateTime;

public class Reserva {
    private int id;
    private String nomeUsuario;
    private String nomeEspaco;
    private LocalDateTime horarioInicio;
    private LocalDateTime horarioFim;

    public Reserva(String nomeUsuario, String nomeEspaco, LocalDateTime inicio, LocalDateTime fim) {
        this.nomeUsuario = nomeUsuario;
        this.nomeEspaco = nomeEspaco;
        this.horarioInicio = inicio;
        this.horarioFim = fim;
    }

    public Reserva(int id, String nomeUsuario, String nomeEspaco, LocalDateTime inicio, LocalDateTime fim) {
        this(nomeUsuario, nomeEspaco, inicio, fim);
        this.id = id;
    }

    public int getId() { return id; }
    public String getNomeUsuario() { return nomeUsuario; }
    public String getNomeEspaco() { return nomeEspaco; }
    public LocalDateTime getHorarioInicio() { return horarioInicio; }
    public LocalDateTime getHorarioFim() { return horarioFim; }

    public void setId(int id) { this.id = id; }
    public void setNomeUsuario(String nomeUsuario) { this.nomeUsuario = nomeUsuario; }
    public void setNomeEspaco(String nomeEspaco) { this.nomeEspaco = nomeEspaco; }
    public void setHorarioInicio(LocalDateTime horarioInicio) { this.horarioInicio = horarioInicio; }
    public void setHorarioFim(LocalDateTime horarioFim) { this.horarioFim = horarioFim; }
}
