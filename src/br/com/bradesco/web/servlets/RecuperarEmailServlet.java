package br.com.bradesco.web.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import br.com.bradesco.web.dao.UsuarioDao;
import br.com.bradesco.web.entitie.Usuario;

@WebServlet(value="/recuperar_email")
public class RecuperarEmailServlet extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());
	
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		try{
			
			String uid = req.getParameter("uid");
			logger.debug("uid:"+uid);
		
			UsuarioDao dao = new UsuarioDao();
			
			Usuario u = dao.validarRecuperarSenhaMD5(uid);
			
			if(u!=null){
				req.getSession(true).setAttribute("recuperar_email", u);
				resp.sendRedirect(req.getContextPath()+"/pages/home/recuperar_senha/senha.jsf");
			}else{
				resp.sendRedirect(req.getContextPath()+"/pages/home/login.jsf");
			}
			
		}catch(Throwable e){
			logger.error(e.getMessage(), e);
		}
	}

}
