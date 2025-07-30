package com.projeto.controller;

import com.projeto.DAOs.UsuarioDAO;
import com.projeto.model.Usuario;
import com.projeto.model.Sessao;
import com.projeto.util.LoggerTXT;
import com.projeto.view.Login;

/**
 * Classe responsável por gerenciar as ações relacionadas ao usuário,
 * como login e cadastro.
 * Interage com a view Login e com o modelo UsuarioDAO.
 */
public class UsuarioController {
    // Atributos
    final UsuarioDAO model = new UsuarioDAO();
    final Login view;

    // Construtor que recebe a tela de login para capturar os dados do usuário.
    public UsuarioController(Login view) {
        this.view = view;
    }

    // Processa a tentativa de login do usuário.
    public boolean processarLogin() {
        if (view == null) {
            return false;
        }

        String email = view.getEmailLogin();
        String senha = view.getSenhaLogin();

        // Verifica se os campos de email e senha foram preenchidos corretamente
        String erroValidacao = validarCredenciais(email, senha);
        if (erroValidacao != null) {
            System.err.println("Erro de validação: " + erroValidacao);
            return false;
        }

        try {
            Usuario usuario = model.buscarPorCredenciais(email, senha);
            if (usuario != null) {
                Sessao.getInstancia().setUsuarioLogado(usuario);
                LoggerTXT.registrar("O usuário " + usuario.getNome() + " se conectou!");
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

    // Processa o cadastro de um novo usuário no sistema.
    public boolean processarCadastro(String nome, String email, String senha, String tipo) {
        // Verifica se os dados informados são válidos
        String erroValidacao = validarDadosCadastro(nome, email, senha, tipo);
        if (erroValidacao != null) {
            System.err.println("Erro de validação no cadastro: " + erroValidacao);
            return false;
        }

        try {
            // Verifica se já existe um usuário com o email informado
            Usuario usuarioExistente = model.buscar(email);
            if (usuarioExistente != null) {
                System.err.println("Usuário já existe com este email: " + email);
                return false;
            }

            // Cria e salva o novo usuário
            Usuario novoUsuario = new Usuario(nome.trim(), email.trim(), tipo, senha);
            model.adicionar(novoUsuario);
            LoggerTXT.registrar("O usuário " + novoUsuario.getNome() + " se cadastrou!");
            return true;

        } catch (Exception e) {
            System.err.println("Erro no processo de cadastro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    // Valida os dados informados no login.
    private String validarCredenciais(String email, String senha) {
        if (email == null || email.trim().isEmpty()) {
            return "Email é obrigatório";
        }
        if (senha == null || senha.trim().isEmpty()) {
            return "Senha é obrigatória";
        }
        return null;
    }

    // Valida os dados fornecidos para o cadastro de usuário.
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

    //Verifica se o email fornecido possui um formato considerado válido.
    private boolean isEmailValido(String email) {
        return email != null &&
                email.contains("@") &&
                email.contains(".") &&
                email.indexOf("@") > 0 &&
                email.lastIndexOf(".") > email.indexOf("@");
    }

    //Verifica se a chave fornecida corresponde à chave de administrador.
    public boolean validarChaveAdmin(String chave) {
        final String CHAVE_ADMIN = "1234";
        return CHAVE_ADMIN.equals(chave);
    }
}
