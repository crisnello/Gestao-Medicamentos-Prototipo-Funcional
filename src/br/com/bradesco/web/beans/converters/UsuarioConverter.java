package br.com.bradesco.web.beans.converters;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.com.bradesco.web.dao.UsuarioDao;
import br.com.bradesco.web.entitie.Usuario;
import br.com.bradesco.web.util.Utils;

@FacesConverter(value="usuarioConverter")
public class UsuarioConverter implements Converter {

	public Object getAsObject(FacesContext ctx, UIComponent ui, String valor) {
		Usuario u = null;
		try{
			UsuarioDao dao = new UsuarioDao();
			u = dao.buscarUsuario(Long.parseLong(valor));
		}catch(Throwable e){
			Utils.addMessageErro("Erro ao converter .");
		}
		
		return u;
	}

	public String getAsString(FacesContext ctx, UIComponent ui, Object valor) {
		if(valor!=null){
			Usuario u = (Usuario) valor;
			return String.valueOf(u.getId());
		}
		return null;
	}

}
