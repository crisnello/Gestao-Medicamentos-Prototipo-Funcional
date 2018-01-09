package br.com.bradesco.web.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Logger;

import br.com.bradesco.web.dao.PedidoDao;
import br.com.bradesco.web.entitie.Medicamento;
import br.com.bradesco.web.entitie.Pedido;
import br.com.bradesco.web.entitie.Usuario;
import br.com.bradesco.web.util.Utils;

@ManagedBean(name="pedidoBean")
@RequestScoped
public class PedidoBean implements Serializable{

	protected Logger logger = Logger.getLogger(this.getClass());
	
	private static final long serialVersionUID = 2502337141354668367L;

	private List<Pedido> pedidos;
	
	private Pedido pedido;
	
	
	private Map<String,Boolean> med;
		public Map<String, Boolean> getMed() {
		return med;
	}
	public void setMed(Map<String, Boolean> med) {
		this.med = med;
	}

	public PedidoBean() {
		atualizarpedidos();
		setPedido(new Pedido());
	}
	
	private void atualizarpedidos(){
		try{
			Usuario u = (Usuario) Utils.buscarSessao("usuario");
			PedidoDao dao = new PedidoDao();
			if(u.getIdPerfil() == 1)
				pedidos = dao.buscarPedidos(u.getIdCliente());
			else
				pedidos = dao.buscarPedidos(u.getIdCliente(),u.getCpf());
		}catch(Throwable e){
			Utils.addMessageErro("Falha ao obter pedidos.");
		}
	}
	
	
	public String template(){
		return "/pages/pedido/template";
	}
	
	public String pedidoAdd(){
		atualizarpedidos();
		return "/pages/pedido/pedidoAdd";
	}
	
	public String excluir(){
		try{
			PedidoDao dao = new PedidoDao();
			
			String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			
			logger.debug(id);
			
			dao.excluir(Long.parseLong(id));
			
			atualizarpedidos();
			Utils.addMessageSucesso("Pedido excluído com sucesso.");
		}catch(Throwable e){
			Utils.addMessageErro("Falha ao excluir pedido.");
			return "/pages/pedido/pedidoEditar";
		}
		return "/pages/pedido/template";
	}
	
	public String abrirEditar(){
		try{
//			Usuario u = (Usuario) Utils.buscarSessao("usuario");
			
			String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			
			PedidoDao dao = new PedidoDao();
			
			setPedido(dao.buscarPedido(Long.parseLong(id)));
		

		}catch(Throwable e){
			logger.error("", e);
			Utils.addMessageErro("Falha ao obter pedido.");
			return "/pages/pedido/template";
		}
		
		return "/pages/pedido/pedidoEditar";
	}
	
	public String salvarEditar(){
		try{
			Usuario u = (Usuario) Utils.buscarSessao("usuario");
			PedidoDao dao = new PedidoDao();
			
//			if(dao.existeEmail(pedido.getEmail()) && !pedido.getEmail().equals(pedido.getEmailOld())){
//				Utils.addMessageErro("Já existe um pedido com esse email.");
//				return "/pages/pedido/pedidoEditar";
//			}
			
/*			if(pedido.getMedicamentos().equals("0")){
				Utils.addMessageErro("Selecione o medicamento.");
				return "/pages/pedido/pedidoEditar";
			}*/
			
			
			//pedido do mesmo cliente
			pedido.setIdCliente(u.getIdCliente());
			
			dao.alterar(pedido);
			
			Utils.addMessageSucesso("Pedido atualizado com sucesso.");
			atualizarpedidos();
		}catch(Throwable e){
			Utils.addMessageErro("Falha ao atualizado pedido.");
			return "/pages/pedido/pedidoEditar";
		}
		return "/pages/pedido/template";
	}
	
	public String adicionar(){
		
		try{
			Usuario u = (Usuario) Utils.buscarSessao("usuario");
			PedidoDao dao = new PedidoDao();
		

	        //POG - ARRUMAR depois
			ArrayList<Medicamento> meds = new ArrayList<Medicamento>();
	        for(int i=0;i<pedido.getMedicamentos().size();i++) {
//	        	logger.debug(Long.parseLong(""+pedido.getMedicamentos().get(i)));
	        	meds.add(new Medicamento(Long.parseLong(""+pedido.getMedicamentos().get(i))));
	        }
			
			pedido.setNumero_pedido(String.valueOf((new Date()).getTime()));
			
			//pedido do mesmo cliente
			pedido.setIdCliente(u.getIdCliente());
			
			dao.adicionar(pedido,meds);
			
			Utils.addMessageSucesso("Pedido adicionado com sucesso.");
			atualizarpedidos();
			return "/pages/pedido/template";
		}catch(Throwable e){
			Utils.addMessageSucesso("Falha ao adicionar pedido.");
		}
		return "/pages/pedido/pedidoAdd";
		
	}
	
	public void buscarCEP(AjaxBehaviorEvent event){
		
		if(!"".equals(pedido.getCep())){
			logger.debug("processar CEP: "+pedido.getCep());
			PedidoDao dao = new PedidoDao();
			try {
				pedido = dao.atualizarEnderecoCep(pedido.getCep(), pedido);
			} catch (Throwable e) {
				Utils.addMessageErro("Erro ao atualizar endereço.");
			}
		}
	}
	
	public List<Pedido> getPedidos() {
		return pedidos;
	}

	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
	
	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}




}
