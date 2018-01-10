package br.com.bradesco.web.dao;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import br.com.bradesco.web.entitie.Imagem;
import br.com.bradesco.web.exceptions.DaoException;



public class ImagemDao extends BaseDao {
	
    public String getNextIdImagem() throws Throwable{
        long resp = -1;
        try{
        	conectar();

        	String query = "select max(id) from imagem";
            
        	rs = con.createStatement().executeQuery(query);

			if(rs != null && rs.next()){
				resp = rs.getLong(1);
			}
        	
			}catch (Throwable e) {
				logger.error("Erro processado buscar id imagem", e);
				throw new DaoException(e.getMessage(), e.getCause());
			}finally{
				desconectar();
			}

        return String.valueOf(resp+1);
    }
	
	public void excluir(long idImagem) throws Throwable{
		try{
			conectar();
			
			String q1 = "delete from imagem where id = ?" ;
			
			pstm = con.prepareStatement(q1);
			pstm.setLong(1, idImagem);
			
			pstm.executeUpdate();

			
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado excluir", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}
	
   
	
	public Imagem buscarImagem(long id) throws Throwable{
		Imagem u = null;
		try{
			conectar();
			
			String query = "select * from imagem where id="+id;
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Imagem();
				u.setId(rs.getLong("id"));
				u.setIdCliente(rs.getInt("id_cliente"));
				u.setNome(rs.getString("nome"));
				u.setCaminhoCompleto(rs.getString("caminho_completo"));
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setStatus(rs.getString("status"));
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar imagem", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}
		
		
		return u;
		
	}
	
	public boolean allProcessado(){
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			String query = "select * from imagem where status='Novo'";
			pstm = con.prepareStatement(query);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				return false;
			}
			
		}catch (Throwable e) {
			e.printStackTrace();
			logger.error("Erro allProcessado imagem", e);
//			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return true;
		
		
	}


	public List<Imagem> buscarImagemsByPedido(long idPedido) throws Throwable{
		List<Imagem> us = new ArrayList<Imagem>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from pedido_imagem where id_pedido=?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idPedido);
			
			rs2 = pstm.executeQuery();
			
			long idImagem;
			while(rs2.next()){
				idImagem = rs2.getLong("id_imagem");
				us.add(buscarImagem(idImagem));
			}
			
			
		}catch (Throwable e) {
			e.printStackTrace();
			logger.error("Erro processado buscar imagem", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
		
	}
	
	
	
	public List<Imagem> buscarImagemsNovas(long idCliente) throws Throwable{
		List<Imagem> us = new ArrayList<Imagem>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from imagem where id_cliente=? and status='Novo' ORDER BY id ASC";
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idCliente);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Imagem u = new Imagem();
				u.setId(rs.getLong("id"));
				u.setIdCliente(rs.getInt("id_cliente"));
				u.setNome(rs.getString("nome"));
				u.setCaminhoCompleto(rs.getString("caminho_completo"));
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setStatus(rs.getString("status"));				
				us.add(u);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar imagem", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
		
	}
	
	
	public List<Imagem> buscarImagems(long idCliente) throws Throwable{
		List<Imagem> us = new ArrayList<Imagem>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from imagem where id_cliente=? and status='Inserido' ORDER BY id DESC";
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idCliente);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Imagem u = new Imagem();
				u.setId(rs.getLong("id"));
				u.setIdCliente(rs.getInt("id_cliente"));
				u.setNome(rs.getString("nome"));
				u.setCaminhoCompleto(rs.getString("caminho_completo"));
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setStatus(rs.getString("status"));				
				us.add(u);
			}
			
		}catch (Throwable e) {
			logger.error("Erro processado buscar imagem", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
		
	}

	
	public void updateStatus(long idImagem,String pStatus) throws Throwable{
		try{
			conectar();
			
			String q1 = "update imagem set status='" +pStatus+"'"+
					" where id = ?" ;
			
			pstm = con.prepareStatement(q1);
			pstm.setLong(1, idImagem);
			
			pstm.executeUpdate();

			
		}catch (Throwable e) {
			con.rollback();
			logger.error("Erro processado update status", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}
	
	public void adicionar(Imagem a) throws Throwable{
	try{
		conectar();
		
		String str = "insert into imagem(nome,caminho_completo,data_cadastro,id_cliente,status)" + 
		" values(?,?,?,?,?)";
		
		logger.debug(ReflectionToStringBuilder.toString(a, ToStringStyle.MULTI_LINE_STYLE));
		
		pstm = con.prepareStatement(str, Statement.RETURN_GENERATED_KEYS);
		
		pstm.setString(1, a.getNome());
		pstm.setString(2, a.getCaminhoCompleto());
		pstm.setTimestamp(3, new Timestamp(System.currentTimeMillis()));
		pstm.setInt(4, a.getIdCliente());
		pstm.setString(5,"Novo");

		int affectedRows = pstm.executeUpdate();
		if (affectedRows == 0) {
            throw new SQLException("Creating imagem failed, no rows affected.");
        }

		rs = pstm.getGeneratedKeys();
        if (rs.next()) {
            a.setId(rs.getLong(1));
        } else {
            throw new SQLException("Creating imagem failed, no generated key obtained.");
        }
		
	}catch (Throwable e) {
		con.rollback();
		logger.error("Erro processado adicionar imagem", e);
		throw new DaoException(e.getMessage(), e.getCause());
	}finally{
		desconectar();
	}
		
  }

}
