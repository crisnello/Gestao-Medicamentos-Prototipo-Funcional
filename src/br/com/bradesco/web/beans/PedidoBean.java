package br.com.bradesco.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;

import org.apache.log4j.Logger;
import org.primefaces.model.UploadedFile;

import br.com.bradesco.web.dao.ImagemDao;
import br.com.bradesco.web.dao.PedidoDao;
import br.com.bradesco.web.entitie.Imagem;
import br.com.bradesco.web.entitie.Medicamento;
import br.com.bradesco.web.entitie.Pedido;
import br.com.bradesco.web.entitie.Usuario;
import br.com.bradesco.web.util.Utils;

@ManagedBean(name="pedidoBean")
@RequestScoped
public class PedidoBean implements Serializable{
	
	
	private List<Imagem> imagens;

	protected Logger logger = Logger.getLogger(this.getClass());
	
	private static final long serialVersionUID = 2502337141354668367L;

	private List<Pedido> pedidos;
	
	private Pedido pedido;
	


	public List<Imagem> getImagens() {
		return imagens;
	}

	public void setImagens(List<Imagem> imagens) {
		this.imagens = imagens;
	}

	private List<String> med;
	
	public List<String> getMed() {
		return med;
	}

	public void setMed(List<String> med) {
		this.med = med;
	}

	public PedidoBean() {
		med = new ArrayList<String>();
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
			
			Pedido pPedido = dao.buscarPedido(Long.parseLong(id));
			
			setPedido(pPedido);
			
			ImagemDao iDao = new ImagemDao();
			setImagens(iDao.buscarImagemsByPedido(pPedido.getId()));
		

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

			
			if(pedido.getMedicamentos().size() == 0) {
				Utils.addMessageSucesso("Selecione pelo menos um medicamento");
				return "/pages/pedido/pedidoAdd";
			}
			
	        //POG
			ArrayList<Medicamento> meds = new ArrayList<Medicamento>();
	        for(int i=0;i<pedido.getMedicamentos().size();i++) {
//	        	logger.debug(Long.parseLong(""+pedido.getMedicamentos().get(i)));
	        	meds.add(new Medicamento(Long.parseLong(""+pedido.getMedicamentos().get(i))));
	        }
			
//			logger.debug("MEDICAMENTOS size = "+pedido.getId_medicamentos().size());
			
/*			ArrayList<Medicamento> meds = new ArrayList<Medicamento>();
	        for(int i=0;i<pedido.getId_medicamentos().size();i++) {
	        	Long pIdMed = Long.valueOf(""+pedido.getId_medicamentos().get(i));
	        	meds.add(new Medicamento(pIdMed.longValue()));
	        }*/
			
			pedido.setNumero_pedido(String.valueOf((new Date()).getTime()));
			
			//pedido do mesmo cliente
			pedido.setIdCliente(u.getIdCliente());
			
			ImagemDao iDao = new ImagemDao();
			List<Imagem> lImagem = iDao.buscarImagemsNovas(u.getIdCliente(),u.getId());
			
			if(lImagem.size() <= 0){
				Utils.addMessageSucesso("Faça primeiramente o upload da imagem da prescrição médica");
				return "/pages/pedido/pedidoAdd";
			}else{
				dao.adicionar(pedido,meds);
				
				//Inserindo e Processando todas imagens 
				for(int z=0;z < lImagem.size();z++){
					Imagem imagem = lImagem.get(z);
					dao.adicionarPedidoImagem(pedido.getId(), imagem.getId());
					iDao.updateStatus(imagem.getId(), "Inserido");
				}
			}
			
			
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
