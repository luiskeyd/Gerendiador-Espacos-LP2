public abstract class Locais {
    private String nome;
    private String horario_disponivel;
    private int capacidade;
    private String localizacao;
    private boolean reservado;

    public Locais(String nome, String horaio_disponivel, int capacidade, 
    String localizacao, boolean reservado){
        setNome(nome);
        setHorarioDisponivel(horaio_disponivel);
        setCapacidade(capacidade);
        setLocalizacao(localizacao);
        setReservado(reservado);
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public String getNome(){
        return this.nome;
    }

    public void setHorarioDisponivel(String horaio_disponivel){
        this.horario_disponivel = horaio_disponivel;
    }

    public String getHorarioDisponive(){
        return this.horario_disponivel;
    }

    public void setCapacidade(int capacidade){
        this.capacidade = capacidade;
    }

    public int getCapacidade(){
        return this.capacidade;
    }

    public void setLocalizacao(String localizacao){
        this.localizacao = localizacao;
    }

    public String getLocalizacao(){
        return this.localizacao;
    }

    public void setReservado(boolean reservado){
        this.reservado = reservado;
    }

    public boolean getReservado(){
        return this.reservado;
    }
}