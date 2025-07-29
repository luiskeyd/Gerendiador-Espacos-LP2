package com.projeto.model;

public class Sessao {
    private static Sessao instancia;
    private Usuario usuarioLogado;

    private Sessao() {}

    public static Sessao getInstancia() {
        if (instancia == null) {
            instancia = new Sessao();
        }
        return instancia;
    }
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    public boolean isAdmin() {
        return usuarioLogado != null && "ADMIN".equalsIgnoreCase(usuarioLogado.getTipo());
    }
}