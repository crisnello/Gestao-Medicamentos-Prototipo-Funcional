package br.com.bradesco.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;

import org.apache.log4j.Logger;

import br.com.bradesco.web.dao.MedicamentoDao;
import br.com.bradesco.web.entitie.Medicamento;
import br.com.bradesco.web.entitie.Usuario;
import br.com.bradesco.web.util.Utils;

@ManagedBean(name="medicamentoBean")
public class MedicamentoBean implements Serializable {

	private static final long serialVersionUID = 5439352856687176305L;
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	private List<Medicamento> medicamentos;
	
	private Medicamento medicamento;
	
	private Medicamento[] selectedMedicamentos;
	
	public MedicamentoBean() {
//		atualizarMedicamentos();
	}
	
	@PostConstruct
	private void atualizarMedicamentos(){
		try{
			getMedicamentos();
			//logger.debug("Carregou os medicamentos!!!");
		}catch(Throwable e){
			Utils.addMessageErro("Falha ao obter medicamentos.");
		}
	}
	
	
		
	public Medicamento getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
	}

	public Medicamento[] getSelectedMedicamentos() {
		return selectedMedicamentos;
	}

	public void setSelectedMedicamentos(Medicamento[] selectedMedicamentos) {
		this.selectedMedicamentos = selectedMedicamentos;
	}

	public List<Medicamento> getMedicamentos() {
		if(medicamentos==null){
			try {
				Usuario u = (Usuario) Utils.buscarSessao("usuario");
				medicamentos = new MedicamentoDao().buscarMedicamentos(u.getIdCliente());
			} catch (Throwable e) {
				logger.error("Erro ao buscar lista de medicamentos", e);
			}
		}
		return medicamentos;
	}

	public void setMedicamentos(List<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}

	
	
}
