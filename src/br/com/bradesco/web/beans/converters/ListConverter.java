package br.com.bradesco.web.beans.converters;

import java.util.ArrayList;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

@FacesConverter(value="listConverter")
public class ListConverter implements Converter {
	
	private final transient Logger logger = Logger.getLogger(this.getClass());

	public Object getAsObject(FacesContext ctx, UIComponent ui, String valor) {
		ArrayList<String> v = new  ArrayList<String>();
		
		try{
			logger.debug("ListConverter.getAsObject "+valor);
			v.add(valor);
			logger.debug("ListConverter.getAsObject "+v);
		}catch(Exception e){
//			Utils.addMessageErro("Erro ao converter .");
			e.printStackTrace();
		}
		
		return v;
	}

	public String getAsString(FacesContext ctx, UIComponent ui, Object valor) {
		if(valor!=null){
			
			String pStr = "[";
			try {
				logger.debug("ListConverter.getAsString "+valor);
				
				ArrayList<String> strValor = (ArrayList<String>) valor;
				for (String string : strValor) {
					pStr = pStr + string + ",";
				}
				
				pStr = pStr.substring(0, pStr.length() - 1);
				pStr = pStr + "]";
				
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			
			return pStr;
		}
		return null;
	}

}
