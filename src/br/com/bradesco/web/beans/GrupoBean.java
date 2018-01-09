package br.com.bradesco.web.beans;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import br.com.bradesco.web.dao.GrupoDao;
import br.com.bradesco.web.entitie.Grupo;
import br.com.bradesco.web.entitie.Usuario;
import br.com.bradesco.web.util.Utils;

@ManagedBean(name="grupoBean")
@RequestScoped
public class GrupoBean implements Serializable{

	private static final long serialVersionUID = -7369164844505204822L;

	protected Logger logger = Logger.getLogger(GrupoBean.class);
	
	
	private List<Grupo> grupos;
	
	private Grupo grupo;

	public String abrirEditar(){
		try{
			String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			
			GrupoDao dao = new GrupoDao();
			
			grupo = dao.buscarGrupo(Long.parseLong(id));
		}catch(Throwable e){
			logger.error("erro buscando grupo");
			Utils.addMessageErro("Falha ao buscar grupo.");
		}
		return "/pages/grupo/grupoEditar";
	}
	
	
	public String excluir(){
		try{
			GrupoDao dao = new GrupoDao();
			
			dao.excluir(grupo);
			setGrupo(new Grupo());
			return "/pages/grupo/grupoAdd";
		}catch(Throwable e){
			logger.error("erro buscando grupo");
			Utils.addMessageErro("Falha ao buscar grupo.");
		}
		return "/pages/grupo/grupoEditar";
	}
	
	public void salvarEditar(ActionEvent ev){
		try{
			GrupoDao dao = new GrupoDao();
			
			dao.atualizar(grupo);
			
			Utils.addMessageSucesso("Grupo editado com sucesso.");
		}catch(Throwable e){
			logger.error("erro atualizar grupo");
			Utils.addMessageErro("Falha ao atualizar grupo.");
		}
	}
	
	
	public void adicionar(ActionEvent e){
		try{
			GrupoDao dao = new GrupoDao();
			
			grupo.setIdCliente(((Usuario)Utils.buscarSessao("usuario")).getIdCliente());
			
			dao.adicionar(grupo);
			
			setGrupo(new Grupo());
			
			Utils.addMessageSucesso("Grupo cadastrado com sucesso.");
			
		}catch(Throwable t){
			logger.error("erro ao gravar grupo");
			Utils.addMessageErro("Falha ao gravar grupo.");
		}
		
	}
	
	
	public List<Grupo> getGrupos() {
		try{
			GrupoDao dao = new GrupoDao();
			grupos = dao.buscarGrupos(((Usuario)Utils.buscarSessao("usuario")).getIdCliente());
		}catch(Throwable t){
			logger.error("erro buscando grupos");
		}
		
		return grupos;
	}

	public void setGrupos(List<Grupo> grupos) {
		this.grupos = grupos;
	}

	public Grupo getGrupo() {
		if(grupo==null){
			grupo = new Grupo();
		}
		return grupo;
	}

	public void setGrupo(Grupo grupo) {
		this.grupo = grupo;
	}
	
}
