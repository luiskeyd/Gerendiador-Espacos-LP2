package com.projeto.controller;

import com.projeto.DAOs.UsuarioDAO;
import com.projeto.model.Usuario;
import com.projeto.model.Sessao;
import com.projeto.view.Login;

public class UsuarioController {
    private UsuarioDAO model = new UsuarioDAO();
    private Login view;

    public UsuarioController(Login view) {
        this.view = view;
    }

    public boolean processarLogin() {
        String email = view.getEmailLogin();
        String senha = view.getSenhaLogin();

        if (email.isEmpty() || senha.isEmpty()) {
            return false;
        }

        try {
            Usuario usuario = model.buscarPorCredenciais(email, senha);
            if (usuario != null) {
                Sessao.getInstancia().setUsuarioLogado(usuario);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Erro no processo de login: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean processarCadastro(String nome, String email, String senha, String tipo) {
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            return false;
        }

        try {
            // Verificar se usuário já existe
            Usuario usuarioExistente = model.buscar(email);
            if (usuarioExistente != null) {
                return false; // Usuário já existe
            }

            Usuario novoUsuario = new Usuario(nome, email, tipo, senha);
            model.adicionar(novoUsuario);
            return true;
        } catch (Exception e) {
            System.err.println("Erro no processo de cadastro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}