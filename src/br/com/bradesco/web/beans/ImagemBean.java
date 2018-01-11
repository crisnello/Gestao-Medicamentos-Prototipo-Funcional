package br.com.bradesco.web.beans;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

import br.com.bradesco.web.dao.ClienteDao;
import br.com.bradesco.web.dao.ImagemDao;
import br.com.bradesco.web.entitie.Cliente;
import br.com.bradesco.web.entitie.Imagem;
import br.com.bradesco.web.entitie.Usuario;
import br.com.bradesco.web.util.Utils;


@ManagedBean(name="imagemBean")
@RequestScoped
public class ImagemBean implements Serializable {
	
	private static final long serialVersionUID = 6117293584439445078L;
	protected Logger logger = Logger.getLogger(ImagemBean.class);
    
	private UploadedFile file;  
	
	private String extensao;
	
	
    public String getExtensao() {
		return extensao;
	}
	public void setExtensao(String extensao) {
		this.extensao = extensao;
	}
	public UploadedFile getFile() {  
        return file;  
    }  
    public void setFile(UploadedFile file) {  
        this.file = file;  
    }  
    
    public void handleFileUpload(FileUploadEvent event) {
//    	logger.debug("imagemBean.handleFileUpload");
    	try {
	        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO,event.getFile().getFileName() + " foi salvo.","Sucesso");  
	        FacesContext.getCurrentInstance().addMessage("sucesso", msg);  
	        
	        file = event.getFile();
	        writeFile();
	        adicionar();
    	}catch(Exception e) {
    		e.printStackTrace();
    	}
        
    }  
    
	
    public void writeFile(){
    	
    	
    	Object path = FacesContext.getCurrentInstance().getExternalContext().getRealPath("/WebContent");
//    	logger.debug(path.toString());
    	
    	String tempDir = "C:/Users/f.negrello/eclipse-workspace/bradescosolicitaremedio/WebContent"; 
    	//String tempDir = caminho + "/WebContent";
    	
    	extensao = file.getFileName().substring(file.getFileName().lastIndexOf("."), file.getFileName().length());
    	ImagemDao dao = new ImagemDao();

    	
    	try {
        	Usuario u = (Usuario) Utils.buscarSessao("usuario");
			Cliente cliente = new ClienteDao().buscarCliente(u.getIdCliente());
			
			File dirCliente = new File(tempDir+"/FILES/"+cliente.getNome()+"/");
        	if(!dirCliente.exists()){
        		//dirCliente.mkdir();
        		tempDir = path.toString();
        	}
        	
        	logger.debug("Diretorio das IMAGENS:"+tempDir+"/FILES/"+cliente.getNome()+"/");
        	//Utils.addMessageSucesso("Diretorio das IMAGENS:"+tempDir);
        	
        	File backupFile = new File(tempDir+"/FILES/"+cliente.getNome()+"/"+"img"+dao.getNextIdImagem()+extensao); 
           // FileWriter fw = new FileWriter(backupFile);
           FileUtils.copyInputStreamToFile(file.getInputstream(), backupFile);
           //fw.close();
           
        }catch (IOException ex) {
            ex.printStackTrace();
        }catch(Throwable e){
			Utils.addMessageSucesso("Falha no upload do Imagem.");
		}


    }

	public String template(){
		return "/pages/imagem/template";
	}
    
	public String adicionar(){		
		try{
			
			Usuario u = (Usuario) Utils.buscarSessao("usuario");
			ImagemDao dao = new ImagemDao();
			Cliente cliente = new ClienteDao().buscarCliente(u.getIdCliente());
			//imagem do mesmo cliente
			imagem.setIdCliente(u.getIdCliente());
			imagem.setNome("img"+dao.getNextIdImagem()+extensao);
			imagem.setCaminhoCompleto("/FILES/"+cliente.getNome()+"/"+"img"+dao.getNextIdImagem()+extensao);
			
			imagem.setUsuario(String.valueOf(u.getId()));
			
			dao.adicionar(imagem);
			Utils.addMessageSucesso("Imagem img"+dao.getNextIdImagem()+extensao + " adicionado com sucesso.");
			atualizarImagens();
			
			
		}catch(Throwable e){
			e.printStackTrace();
			Utils.addMessageSucesso("Falha ao adicionar imagem.");
		}
		return "/pages/imagem/template";
		
	}
	
		private Imagem imagem;
		private List<Imagem> arqs;

		public ImagemBean(){
			atualizarImagens();
			setImagem(new Imagem());
		}
	
		public String imagemAdd(){
			
			atualizarImagens();
			return "/pages/imagem/imagemAdd";
		}

		
		private void atualizarImagens(){
			try{
				Usuario u = (Usuario) Utils.buscarSessao("usuario");
				ImagemDao dao = new ImagemDao();
				arqs = dao.buscarImagems(u.getIdCliente());
			}catch(Throwable e){
				Utils.addMessageErro("Falha ao obter imagems.");
			}
		}
		

		public String excluir(){
			try{
				ImagemDao dao = new ImagemDao();
				
				String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
				
				logger.debug(id);
				
				dao.excluir(Long.parseLong(id));
				
				atualizarImagens();
				Utils.addMessageSucesso("Imagem excluída com sucesso.");
			}catch(Throwable e){
				Utils.addMessageErro("Falha ao excluir imagem.");
				return "/pages/imagem/imagemEditar";
			}
			return "/pages/imagem/template";
		}
			
		public String abrirEditar(){
			try{
//				Arquivo u = (Arquivo) Utils.buscarSessao("arquivo");
				String id = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("id");
				ImagemDao dao = new ImagemDao();
				setImagem(dao.buscarImagem(Long.parseLong(id)));
				
			}catch(Throwable e){
				logger.error("", e);
				Utils.addMessageErro("Falha ao obter imagem.");
				return "/pages/imagem/template";
			}
			
			return "/pages/imagem/imagemEditar";
		}
		public Imagem getImagem() {
			return imagem;
		}
		public void setImagem(Imagem imagem) {
			this.imagem = imagem;
		}
		public List<Imagem> getArqs() {
			return arqs;
		}
		public void setArqs(List<Imagem> arqs) {
			this.arqs = arqs;
		}

		
	


}



