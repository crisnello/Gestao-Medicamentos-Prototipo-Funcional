package br.com.bradesco.web.beans.converters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import org.apache.log4j.Logger;

import br.com.bradesco.web.entitie.Permissao;
import br.com.bradesco.web.util.Utils;

@FacesConverter(value="dateConverter")
public class DateConverter implements Converter {
	
	private final transient Logger logger = Logger.getLogger(this.getClass());

	SimpleDateFormat sdf_br = new SimpleDateFormat("dd/MM/yyyy");
	
	public Object getAsObject(FacesContext ctx, UIComponent ui, String valor) {
		Date v = null;
		
		try{
			logger.debug("DateConverter.getAsObject "+valor);
			v = sdf_br.parse(valor);
			logger.debug("DateConverter.getAsObject "+v.toString());
		}catch(ParseException e){
//			Utils.addMessageErro("Erro ao converter .");
			e.printStackTrace();
		}
		
		return v;
	}

	public String getAsString(FacesContext ctx, UIComponent ui, Object valor) {
		if(valor!=null){
			
			Date pDate = null;
			try {
				logger.debug("DateConverter.getAsString "+valor);
				Date xDate =(Date)valor;
				pDate = sdf_br.parse(sdf_br.format(xDate));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			return sdf_br.format(pDate);
		}
		return null;
	}

}
