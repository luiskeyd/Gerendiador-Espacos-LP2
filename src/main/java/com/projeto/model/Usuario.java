public class Usuario {
    private String nome;
    private String email;
    private boolean is_adm;
    private String senha;

    public Usuario(String nome, String email, boolean is_adm, String senha){
        setNome(nome);
        setEmail(email);
        setIsAdm(is_adm);
        setSenha(senha);
    }

    public void setNome(String nome){
        this.nome = nome;
    } 

    public String getNome(){
        return this.nome;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public String getEmail(){
        return this.email;
    }

    public void setIsAdm(boolean is_adm){
        this.is_adm = is_adm;
    }

    public boolean getIsAdm(){
        return this.is_adm;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public String getSenha(){
        return this.senha;
    }

    
}