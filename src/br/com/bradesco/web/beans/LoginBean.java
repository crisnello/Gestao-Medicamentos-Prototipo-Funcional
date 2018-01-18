package br.com.bradesco.web.beans;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import br.com.bradesco.web.dao.NotificacoesDao;
import br.com.bradesco.web.dao.UsuarioDao;
import br.com.bradesco.web.entitie.Permissao;
import br.com.bradesco.web.entitie.Usuario;
import br.com.bradesco.web.util.Utils;


@ManagedBean(name="loginBean")
@RequestScoped
public class LoginBean implements Serializable{

	private static final long serialVersionUID = -8997426223735944924L;
	
	private final transient Logger logger = Logger.getLogger(this.getClass());

	private Usuario usuario;
	
	private int totalNotificacoes;
	
	public LoginBean() {
		setUsuario(new Usuario());
	}
	
	/**
	 * realizar logout do sistema
	 * @return
	 */
	public String sair(){
		try{
			FacesContext facesContext = FacesContext.getCurrentInstance();
			HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest(); 
			HttpSession session = request.getSession(true);
			session.invalidate();
		}catch(Throwable e){
			logger.error(e,e);
		}
		return "/pages/home/login";
	}
	

	public void alterarCliente(AjaxBehaviorEvent event){
		Usuario u = (Usuario) Utils.buscarSessao("usuario");
		
		logger.debug("alterando cliente atual "+u.getIdCliente()+" para "+usuario.getIdCliente());
		
		u.setIdCliente(usuario.getIdCliente());
		Utils.adicionarSessao("usuario", u);
	}
	
	
	public String alterarRecuperarSenha(){
		try {
			Usuario u = (Usuario) Utils.buscarSessao("recuperar_email");
			if(u!=null){
				if(!usuario.getSenha().equals(usuario.getSenhaConfirm())){
					Utils.addMessageErro("Senhas não conferem.");
				}else{
					UsuarioDao dao = new UsuarioDao();
					dao.alterarSenha(u.getId(), usuario.getSenha());
					
					dao.removerRecuperarSenha(u.getId());
					
					Utils.removerSessao("recuperar_email");
					
					Utils.addMessageSucesso("Senha alterada com sucesso.");
					
					setUsuario(new Usuario());
					return "/pages/home/login";
				}
			}else{
				Utils.addMessageErro("Operação não permitida.");
			}			
		} catch (Throwable e) {
			logger.error("erro alterando senha", e);
		}
		return "/pages/home/recuperar_senha/senha";
	}
	
	public String enviarEmailRecuperarSenha(){
		try{
			UsuarioDao dao = new UsuarioDao();
			
			Usuario u = dao.buscarUsuario(usuario.getEmail());
			
			if(u==null){
				Utils.addMessageErro("Email não encontrado.");
			}else{
				
				FacesContext facesContext = FacesContext.getCurrentInstance();
				HttpServletRequest request = (HttpServletRequest) facesContext.getExternalContext().getRequest(); 
				
				dao.enviarEmailRecuperarSenha(u,request.getContextPath());
				
				setUsuario(new Usuario());
				Utils.addMessageSucesso("Um email foi enviado para "+u.getEmail() +" com o link de autorização.");
			}
		}catch(Throwable e){
			Utils.addMessageErro("Sistema temporariamente indisponível, tente novamente mais tarde.");
		}
		return "/pages/home/recuperar_senha/informar_email";
	}
	
	/**
	 * realizar login no sistema
	 * @return
	 */
	public String validarAcesso(){
		try{
			UsuarioDao dao = new UsuarioDao();
			
			Usuario u = dao.buscarUsuario(usuario.getEmail(), usuario.getSenha());
			
			if(u == null){
				Utils.addMessageErro("Email ou senha incorreto.");
			}else{
				SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				u.setHoraAcesso(f.format(new Date()));

				List<Permissao> permissoesTotal = dao.listaTotasPermissoes();
				List<Permissao> permissoes = dao.listaPermissoesUsuario(u.getId());
				Map<String, Boolean> permissoesSessao = new HashMap<String,Boolean>();
				
				for (Iterator iterator = permissoesTotal.iterator(); iterator.hasNext();) {
					Permissao pT = (Permissao) iterator.next();
					boolean contem = false;
					for (Iterator iterator2 = permissoes.iterator(); iterator2.hasNext();) {
						Permissao p = (Permissao) iterator2.next();
						if(p.getId()==pT.getId()){
							contem = true;
							break;
						}
					}
					permissoesSessao.put( String.valueOf(pT.getId()), new Boolean(contem));
				}
				
				Utils.adicionarSessao("permissoes", permissoesSessao);
				Utils.adicionarSessao("usuario", u);
				
				//System.out.println("CLIENTE ID: "+u.getIdCliente());
				
				String home = Utils.encaminharPara();
				Utils.adicionarSessao("home", home);
				
				
				return home;
			}
		}catch(Throwable t){
			Utils.addMessageErro("Falha ao validar login.");
		}
		return "/pages/home/login";
	}

	public Usuario getUsuario() {
		if(usuario==null){
			usuario = new Usuario();
		}
		
		Usuario u = (Usuario) Utils.buscarSessao("usuario");
		if(u!=null)
		usuario.setIdCliente(u.getIdCliente());
		
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public int getTotalNotificacoes() {
		if(totalNotificacoes==0){
			try{
				Usuario u = (Usuario) Utils.buscarSessao("usuario");
				NotificacoesDao dao = new NotificacoesDao();
				setTotalNotificacoes(dao.totalNotificacoes(u.getId()));
			}catch(Throwable t){
				Utils.addMessageErro("Falha getTotalNotificacoes.");
			}
		}
		return totalNotificacoes;
	}

	public void setTotalNotificacoes(int totalNotificacoes) {
		this.totalNotificacoes = totalNotificacoes;
	}
	
}
