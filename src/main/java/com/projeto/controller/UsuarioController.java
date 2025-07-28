package com.projeto.controller;
import com.projeto.model.Usuario;

public class UsuarioController {
    private UsuarioDAO model;
    private Login view;

    public boolean processarLogin(){
        String email = view.getEmailLogin();
        String senha = view.getSenhaLogin();

        if(email.isEmpty() || senha.isEmpty()){
            return false;
        }

        Usuario usuario = model.buscar(email, senha);

        if(usuario != null){
            return true;
        }else{
            return false;
        }
    }


    public boolean processarCadastro(){
        String email = view.getEmail();
        String senha = view.getPassword();
        boolean isadmin = view.isAdmin();

        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        if (senha == null || senha.trim().isEmpty()) {
            return false;
        }

        
        // Tentar cadastrar
        Usuario novoUsuario =  model.adicionar(email.trim(), senha);
        if (novoUsuario != null){
            return true;
        } else {
            return false;
        }
    }
}
