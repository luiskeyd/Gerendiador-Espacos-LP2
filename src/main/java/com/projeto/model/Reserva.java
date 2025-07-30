package com.projeto.model;

import java.time.LocalDateTime;

/**
 * Classe que representa uma reserva de espaço feita por um usuário.
 * Armazena informações do usuário, do espaço reservado e os horários de início e fim da reserva.
 */
public class Reserva {

    // Nome do usuário que fez a reserva
    private String nomeUsuario;

    // Nome do espaço reservado
    private String nomeEspaco;

    // Data e hora de início da reserva
    private LocalDateTime horarioInicio;

    // Data e hora de término da reserva
    private LocalDateTime horarioFim;

    // Construtor que inicializa os campos da reserva
    public Reserva(String nomeUsuario, String nomeEspaco, LocalDateTime inicio, LocalDateTime fim) {
        this.nomeUsuario = nomeUsuario;
        this.nomeEspaco = nomeEspaco;
        this.horarioInicio = inicio;
        this.horarioFim = fim;
    }

    // Retorna o nome do usuário que realizou a reserva
    public String getNomeUsuario() {
        return nomeUsuario;
    }

    // Retorna o nome do espaço reservado
    public String getNomeEspaco() {
        return nomeEspaco;
    }

    // Retorna o horário de início da reserva
    public LocalDateTime getHorarioInicio() {
        return horarioInicio;
    }

    // Retorna o horário de término da reserva
    public LocalDateTime getHorarioFim() {
        return horarioFim;
    }

    // Define o nome do usuário que realizou a reserva
    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    // Define o nome do espaço reservado
    public void setNomeEspaco(String nomeEspaco) {
        this.nomeEspaco = nomeEspaco;
    }

    // Define o horário de início da reserva
    public void setHorarioInicio(LocalDateTime horarioInicio) {
        this.horarioInicio = horarioInicio;
    }

    // Define o horário de término da reserva
    public void setHorarioFim(LocalDateTime horarioFim) {
        this.horarioFim = horarioFim;
    }
}
