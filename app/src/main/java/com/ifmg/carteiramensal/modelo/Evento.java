package com.ifmg.carteiramensal.modelo;

import java.util.Date;

public class Evento {
    private long id;
    private String nome, caminhoFoto;
    private double valor;
    private Date cadastro, valida, ocorreu;


    public Evento(long id, String nome, String caminhoFoto, double valor, Date cadastro, Date valida, Date ocorreu) {
        this.id = id;
        this.nome = nome;
        this.caminhoFoto = caminhoFoto;
        this.valor = valor;
        this.cadastro = cadastro;
        this.valida = valida;
        this.ocorreu = ocorreu;
    }

    public Evento(String nome, String caminhoFoto, double valor, Date cadastro, Date valida, Date ocorreu) {
        this.nome = nome;
        this.caminhoFoto = caminhoFoto;
        this.valor = valor;
        this.cadastro = cadastro;
        this.valida = valida;
        this.ocorreu = ocorreu;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getCadastro() {
        return cadastro;
    }

    public void setCadastro(Date cadastro) {
        this.cadastro = cadastro;
    }

    public Date getValida() {
        return valida;
    }

    public void setValida(Date valida) {
        this.valida = valida;
    }

    public Date getOcorreu() {
        return ocorreu;
    }

    public void setOcorreu(Date ocorreu) {
        this.ocorreu = ocorreu;
    }
}
