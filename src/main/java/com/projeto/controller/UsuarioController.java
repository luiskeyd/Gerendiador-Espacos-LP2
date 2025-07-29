package com.projeto.controller;

import com.projeto.DAOs.UsuarioDAO;
import com.projeto.model.Usuario;
import com.projeto.model.Sessao;
import com.projeto.view.Login;

public class UsuarioController {
    final UsuarioDAO model = new UsuarioDAO();
    final Login view;

    public UsuarioController(Login view) {
        this.view = view;
    }

    public boolean processarLogin() {
        if (view == null) {
            return false;
        }

        String email = view.getEmailLogin();
        String senha = view.getSenhaLogin();

        // Validar campos vazios
        String erroValidacao = validarCredenciais(email, senha);
        if (erroValidacao != null) {
            System.err.println("Erro de validação: " + erroValidacao);
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
        // Validar dados de entrada
        String erroValidacao = validarDadosCadastro(nome, email, senha, tipo);
        if (erroValidacao != null) {
            System.err.println("Erro de validação no cadastro: " + erroValidacao);
            return false;
        }

        try {
            // Verificar se usuário já existe
            Usuario usuarioExistente = model.buscar(email);
            if (usuarioExistente != null) {
                System.err.println("Usuário já existe com este email: " + email);
                return false;
            }

            // Criar e adicionar novo usuário
            Usuario novoUsuario = new Usuario(nome.trim(), email.trim(), tipo, senha);
            model.adicionar(novoUsuario);
            return true;

        } catch (Exception e) {
            System.err.println("Erro no processo de cadastro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private String validarCredenciais(String email, String senha) {
        if (email == null || email.trim().isEmpty()) {
            return "Email é obrigatório";
        }
        if (senha == null || senha.trim().isEmpty()) {
            return "Senha é obrigatória";
        }
        return null;
    }

    private String validarDadosCadastro(String nome, String email, String senha, String tipo) {
        if (nome == null || nome.trim().isEmpty()) {
            return "Nome é obrigatório";
        }
        if (nome.trim().length() < 2) {
            return "Nome deve ter pelo menos 2 caracteres";
        }
        if (email == null || email.trim().isEmpty()) {
            return "Email é obrigatório";
        }
        if (!isEmailValido(email)) {
            return "Email inválido";
        }
        if (senha == null || senha.trim().isEmpty()) {
            return "Senha é obrigatória";
        }
        if (senha.length() < 4) {
            return "Senha deve ter pelo menos 4 caracteres";
        }
        if (tipo == null || (!tipo.equals("ADMIN") && !tipo.equals("COMUM"))) {
            return "Tipo de usuário inválido";
        }
        return null;
    }

    private boolean isEmailValido(String email) {
        return email != null &&
                email.contains("@") &&
                email.contains(".") &&
                email.indexOf("@") > 0 &&
                email.lastIndexOf(".") > email.indexOf("@");
    }

    public boolean validarChaveAdmin(String chave) {
        // Você pode definir sua chave de admin aqui ou em um arquivo de configuração
        final String CHAVE_ADMIN = "1234";
        return CHAVE_ADMIN.equals(chave);
    }
}