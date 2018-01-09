package br.com.bradesco.web.beans;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name="appBean")
@ViewScoped
public class AplicativoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	//versão dos resources, para evitar cache do browser
	private double versao = 4;

	public double getVersao() {
		return versao;
	}

	public void setVersao(double versao) {
		this.versao = versao;
	}
	
}
