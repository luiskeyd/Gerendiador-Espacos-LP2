package com.projeto.controller;

import com.projeto.DAOs.ReservaDAO;
import com.projeto.model.Reserva;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class ReservaController {
    private ReservaDAO reservaDAO = new ReservaDAO();

    public boolean reservarEspaco(String usuario, String espaco, LocalDateTime inicio, LocalDateTime fim) {
        try {
            boolean disponivel = reservaDAO.verificarDisponibilidade(espaco, inicio, fim);
            if (disponivel) {
                Reserva reserva = new Reserva(usuario, espaco, inicio, fim);
                reservaDAO.adicionar(reserva);
                return true;
            } else {
                return false; // já está reservado no horário
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<Reserva> listarReservas() {
        try {
            return reservaDAO.listarTodas();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
