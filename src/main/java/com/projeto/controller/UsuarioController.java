public class UsuarioController {
    private UsuarioDAO model = new UsuarioDAO();
    private Login view;

    public boolean processarLogin() {
        String email = view.getEmailLogin();
        String senha = view.getSenhaLogin();

        if (email.isEmpty() || senha.isEmpty()) {
            return false;
        }

        try {
            Usuario usuario = model.buscar(email, senha);
            if (usuario != null) {
                Sessao.getInstancia().setUsuarioLogado(usuario);
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
