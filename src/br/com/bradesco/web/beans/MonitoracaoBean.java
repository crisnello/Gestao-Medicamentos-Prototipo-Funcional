package br.com.bradesco.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.log4j.Logger;

import br.com.bradesco.web.dao.ClienteDao;
import br.com.bradesco.web.entitie.Cliente;

@ManagedBean(name="monitoracaoBean")
@RequestScoped
public class MonitoracaoBean implements Serializable {

	private static final long serialVersionUID = 5439352856687176305L;
	
	private final transient Logger logger = Logger.getLogger(this.getClass());
	
	//lista de clientes, apenas usuarios com idPerfil == 1 podem visualizar
	private List<Cliente> clientes;

	
	public List<Cliente> getClientes() {
		if(clientes==null){
			try {
				clientes = new ClienteDao().buscarClientes();
			} catch (Throwable e) {
				logger.error("Erro ao buscar lista de clientes", e);
			}
		}
		return clientes;
	}

	public void setClientes(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	
	
}
