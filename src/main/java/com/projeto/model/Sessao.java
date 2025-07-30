package com.projeto.model;

public class Sessao {

    // Instância única da classe Sessao para garantir o padrão Singleton
    private static Sessao instancia;

    // Armazena o usuário que está logado atualmente na sessão
    private Usuario usuarioLogado;

    // Construtor privado para evitar criação direta de instâncias
    private Sessao() {}

    // Metodo para obter a instância única da sessão
    // Se ainda não existir, cria uma nova instância
    public static Sessao getInstancia() {
        if (instancia == null) {
            instancia = new Sessao();
        }
        return instancia;
    }

    // Define qual é o usuário que está logado na sessão
    public void setUsuarioLogado(Usuario usuario) {
        this.usuarioLogado = usuario;
    }

    // Retorna o usuário que está logado atualmente na sessão
    public Usuario getUsuarioLogado() {
        return this.usuarioLogado;
    }

    // Verifica se o usuário logado é um administrador
    // Retorna true se o usuário existir e seu tipo for "ADMIN" (ignorando maiúsculas/minúsculas)
    public boolean isAdmin() {
        return usuarioLogado != null && "ADMIN".equalsIgnoreCase(usuarioLogado.getTipo());
    }
}
