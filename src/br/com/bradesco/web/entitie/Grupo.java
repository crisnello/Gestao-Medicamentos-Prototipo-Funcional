package br.com.bradesco.web.entitie;

import java.io.Serializable;
import java.util.Date;

public class Grupo implements Serializable{

	private static final long serialVersionUID = 8670395330729859319L;

	private long id;
	
	private long idCliente;
	
	private Date dataCadastro;
	
	private String nome;
	
	private int pertence;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getPertence() {
		return pertence;
	}

	public void setPertence(int pertence) {
		this.pertence = pertence;
	}
	
}
