package br.com.bradesco.web.entitie;

import java.io.Serializable;
import java.util.Date;

public class Medicamento implements Serializable{
	
	private static final long serialVersionUID = 8023901812592617149L;

	private long id;
	
	private String ean;
	
	private String medicamento;
	
	private String apresentacao;
	
	private String rol;
	
	private String classe_comercial;
	
	public Medicamento() {
	}
	
	public Medicamento(long pId) {
		setId(pId);
	}
	

	@Override
	public String toString() {
		return getId() + " - "+getEan()+" - "+getMedicamento();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getEan() {
		return ean;
	}

	public void setEan(String ean) {
		this.ean = ean;
	}

	public String getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(String medicamento) {
		this.medicamento = medicamento;
	}

	public String getApresentacao() {
		return apresentacao;
	}

	public void setApresentacao(String apresentacao) {
		this.apresentacao = apresentacao;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getClasse_comercial() {
		return classe_comercial;
	}

	public void setClasse_comercial(String classe_comercial) {
		this.classe_comercial = classe_comercial;
	}
	
	

}
