package br.com.estudos.alura.domain;

public class Pet {

    private long id;
    private String tipo;
    private String nome;
    private String raca;
    private int idade;
    private String cor;
    private Float peso;
    private long abrigoId;

    public Pet() {
    }

    public Pet(String tipo, String nome, String raca, int idade, String cor, Float peso, long abrigId) {
        this.tipo = tipo;
        this.nome = nome;
        this.raca = raca;
        this.idade = idade;
        this.cor = cor;
        this.peso = peso;
        this.abrigoId = abrigId;
    }

    public long getId() {
        return id;
    }

    public String getTipo() {
        return tipo;
    }

    public String getNome() {
        return nome;
    }

    public String getRaca() {
        return raca;
    }

    public int getIdade() {
        return idade;
    }

    public String getCor() {
        return cor;
    }

    public Float getPeso() {
        return peso;
    }
    public long getAbrigoId() {
        return abrigoId;
    }

    public void setId(long id) {
        this.id = id;
    }
}
