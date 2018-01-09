package br.com.bradesco.web.entitie;

import java.io.Serializable;
import java.util.Date;

public class Cliente implements Serializable{

	private static final long serialVersionUID = -6495501848870675920L;

	private long id;
	
	private String nome;
	
	private Date dataCadastro;

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

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
}
