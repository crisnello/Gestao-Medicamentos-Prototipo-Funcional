package br.com.bradesco.web.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;

import br.com.bradesco.web.dao.ImagemDao;
import br.com.bradesco.web.dao.MedicamentoDao;
import br.com.bradesco.web.dao.PedidoDao;
import br.com.bradesco.web.entitie.Imagem;
import br.com.bradesco.web.entitie.Medicamento;
import br.com.bradesco.web.entitie.Pedido;
import br.com.bradesco.web.entitie.PedidoMedicamento;
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

	private List<String> quantidades;

	
	

	public List<String> getQuantidades() {
		return quantidades;
	}

	public void setQuantidades(List<String> quantidades) {
		this.quantidades = quantidades;
	}

	public PedidoBean() {
		quantidades = new ArrayList<String>();
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

	public String pedidoAddMedicamento(){
		
		if(pedido.getMedicamentos().size() == 0) {
			Utils.addMessageSucesso("Selecione pelo menos um medicamento");
			return "/pages/pedido/pedidoAdd";
		}
		
		ArrayList<PedidoMedicamento> pmeds = new ArrayList<PedidoMedicamento>();
        for(int i=0;i<pedido.getMedicamentos().size();i++) {
//        	logger.debug(Long.parseLong(""+pedido.getMedicamentos().get(i)));
        	
        	
        	Medicamento med = null;
			try {
				Long pIdMed = Long.valueOf(""+pedido.getMedicamentos().get(i));
				med = new MedicamentoDao().buscarMedicamento(pIdMed.intValue());
			} catch (Exception e) {
				e.printStackTrace();
			} catch(Throwable t) {
				t.printStackTrace();
			}
        	
        	PedidoMedicamento pMed = new PedidoMedicamento();
        	pMed.setMedicamento(med);
        	pMed.setQuantidade(1);
        	
        	pmeds.add(pMed);
        }
        
        pedido.setPedidomedicamento(pmeds);
        
//        logger.debug("vai redirecionar para pedidoAddMedicamento");
		
		
		return "/pages/pedido/pedidoAddMedicamento";
	}

	
	public String pedidoAdd(){
		atualizarpedidos();
		return "/pages/pedido/pedidoAdd";
	}
	
	public String excluir(){
		try{
			PedidoDao dao = new PedidoDao();
			
			String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
			
//			logger.debug(id);
			
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
			
			
/*			ArrayList<PedidoMedicamento> pms = pPedido.getPedidomedicamento();
			
			for(int j=0;j < pms.size();j++) {
				
			}*/
			
		

		}catch(Throwable e){
//			logger.error("", e);
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
			
//			logger.debug(pedido.getReceita());
			
			pedido.setIdade(Utils.getIdade(pedido.getDataNasc()));
			
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

			logger.debug("adicionar");
			
			if(pedido.getMedicamentos().size() == 0) {
				Utils.addMessageSucesso("Selecione pelo menos um medicamento");
				return "/pages/pedido/pedidoAdd";
			}
			
	        for(int i=0;i<pedido.getMedicamentos().size();i++) {
	        	Medicamento med = null;
				try {
					
					Long pIdMed = Long.valueOf(""+pedido.getMedicamentos().get(i));
					med = new MedicamentoDao().buscarMedicamento(pIdMed.intValue());
					
					FacesContext context = FacesContext.getCurrentInstance();
				    Long id = context.getApplication().evaluateExpressionGet(context, "#{item.id}", Long.class);
				   
					
				}catch(Throwable t) {
					t.printStackTrace();
				}
	        	
	        }
	        

/*			logger.debug(pedido.getPedidomedicamento().size());
			
	        for(int i=0;i<pedido.getPedidomedicamento().size();i++) {
	        	logger.debug(Long.parseLong(""+pedido.getPedidomedicamento().get(i)));
	        }*/
	        
	        
//	        logger.debug("OK");
			
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
	        
	        
	        
	        pedido.setIdade(Utils.getIdade(pedido.getDataNasc()));
			
	        
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
	
	public void onChangeQuantidadeNumber(ArrayList<PedidoMedicamento> pms) {
		for (PedidoMedicamento pm : pms) {
			logger.debug(pm.getMedicamento().getEan() + " - "+pm.getQuantidade());			
		}

	}
	
	public void save(String rowid) {
        String jsParam = getJsParam("repeat:" + rowid + ":x");
        System.out.println("jsParam: " + jsParam); //persist...
        logger.debug(jsParam);
    }
	
	public static String getJsParam(String paramName) {
	    javax.faces.context.FacesContext jsf = javax.faces.context.FacesContext.getCurrentInstance();
	    Map<String, String> requestParameterMap = jsf.getExternalContext().getRequestParameterMap();
	    String paramValue = requestParameterMap.get(paramName);
	    if (paramValue != null) {
	        paramValue = paramValue.trim();
	        if (paramValue.length() == 0) {
	            paramValue = null;
	        }
	    }
	    return paramValue;
	}
	
	public void colocarQuantidade(ValueChangeEvent event){
		
		logger.debug("colocarQuantidade");
		String input = event.getNewValue().toString();
		logger.debug(input);
		
/*		for (PedidoMedicamento pm : pedido.getPedidomedicamento()) {
			logger.debug(pm.getMedicamento().getEan() + " - "+pm.getQuantidade());			
		}*/

		
/*		logger.debug(pedido.getPedidomedicamento().size());
		
        for(int i=0;i<pedido.getPedidomedicamento().size();i++) {
        	PedidoMedicamento pm = pedido.getPedidomedicamento().get(i);
        	logger.debug(pm.getMedicamento().getEan() + " - " + pm.getQuantidade());
        	System.out.println(pm.getMedicamento().getEan() + " - " + pm.getQuantidade());
        }*/
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
