package br.com.bradesco.web.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

import br.com.bradesco.web.util.Conexao;

public class BaseDao {
	
	protected Connection con;
	
	private Context ctx;
	
	protected PreparedStatement pstm;
	
	protected ResultSet rs;
	
	protected ResultSet rs2;
	
	protected Logger logger = Logger.getLogger(this.getClass());
	
	private long inicio = 0;
	
	private static final String contexto = "bradescosolicitaremedio";
	
	/**
	 * criar um data source do MySql no glassfish com as configuracoes abaixo
	 * 
	 * portNumber = 3306
	 * databaseName = bradescosolicitaremedio
	 * URL = jdbc:mysql://localhost:3306/bradescosolicitaremedio 
	 * serverName = localhost
	 * user = root
	 * password = root@123
	 * 
	 * @throws Exception
	 */
//	protected void conectar() throws Exception{
//		inicio = System.currentTimeMillis();
//		Context ctx = new InitialContext();
//		DataSource ds = (DataSource) ctx.lookup(contexto);
//		con = ds.getConnection();
//		con.setAutoCommit(false);
//	}

	protected void conectar() throws Exception{
		try {
            if(con == null || con.isClosed())
            {
            	con = Conexao.getConexao("root", "root@123");
        		con.setAutoCommit(false); 
            }
               
            }catch (SQLException e) {
                String respBd = e.getMessage();
                if(respBd.indexOf("Communications link failure") >= 0 ||
                    respBd.indexOf("No operations allowed after connection closed") >= 0){
                	con = null;
                	//throw new Exception(e.getMessage());
                }
                else
                    throw e;

           }
		
	}
	
	protected void desconectar(){
		if(rs!=null){
			try {
				rs.close();
			} catch (Exception e) {
				//logger.debug(e);
			}
		}
		if(rs2!=null){
			try {
				rs2.close();
			} catch (Exception e) {
				//logger.debug(e);
			}
		}
		if(pstm!=null){
			try {
				pstm.close();
			} catch (Exception e) {
				//logger.debug(e);
			}
		}
		try {
        	con.commit();
        	con.close();
        } catch (SQLException e) {
        	//logger.debug(e);
        }
		try{
			ctx.close();
		}catch(Exception e){
			//logger.debug(e);
		}
		
		StringBuilder t = new StringBuilder();
		t.append("Processamento da query: ");
		t.append(System.currentTimeMillis()-inicio);
		t.append(" millis");
		
		logger.debug(t.toString());
	}

}
