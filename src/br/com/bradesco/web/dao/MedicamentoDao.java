package br.com.bradesco.web.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import br.com.bradesco.web.entitie.Medicamento;
import br.com.bradesco.web.exceptions.DaoException;

public class MedicamentoDao extends BaseDao{

	
	
	
	public void excluir(long idMed) throws Throwable{
		try{
			conectar();
			
			String q1 = "delete from medicamento where id = ?" ;
			
			pstm = con.prepareStatement(q1);
			pstm.setLong(1, idMed);
			
			pstm.executeUpdate();
			
		}catch (Throwable e) {
			con.rollback();
			getLogger().error("Erro processado excluir", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}
	
	
	public boolean existeMedicamento(String id) throws Throwable{
		boolean retorno = false;
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select count(id) from medicamento where id='"+id+"'";
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				int total = rs.getInt(1);
				if(total>0){
					retorno = true;
				}
			}
			
		}catch (Throwable e) {
			getLogger().error("Erro processado existeMedicamento", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return retorno;
		
	}
	
	public Medicamento buscarMedicamento(String ean) throws Throwable{
		Medicamento u = null;
		try{
			conectar();
			
			String query = "select * from medicamento where ean='"+ean+"'";
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Medicamento();
				u.setId(rs.getLong("id"));
				u.setEan(rs.getString("ean"));
				u.setMedicamento(rs.getString("medicamento"));
				u.setApresentacao(rs.getString("apresentacao"));
				u.setRol(rs.getString("rol"));
				u.setClasse_comercial(rs.getString("classe_comercial"));
			}
			
			
		}catch (Throwable e) {
			getLogger().error("Erro processado buscar medicamento", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}

	public Medicamento buscarMedicamento(long id) throws Throwable{
		Medicamento u = null;
		try{
			conectar();
			
			String query = "select * from medicamento where id="+id;
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Medicamento();
				u.setId(rs.getLong("id"));
				u.setEan(rs.getString("ean"));
				u.setMedicamento(rs.getString("medicamento"));
				u.setApresentacao(rs.getString("apresentacao"));
				u.setRol(rs.getString("rol"));
				u.setClasse_comercial(rs.getString("classe_comercial"));
			}
			
			
		}catch (Throwable e) {
			getLogger().error("Erro processado buscar medicamento", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}
//		finally{
//			desconectar();
//		}
		
		return u;
		
	}
	
	public List<Medicamento> buscarMedicamentos(long idCliente) throws Throwable{
		List<Medicamento> us = new ArrayList<Medicamento>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from medicamento where id_cliente=?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idCliente);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Medicamento u = new Medicamento();
				u.setId(rs.getLong("id"));
				
				u.setEan(rs.getString("ean"));
				u.setMedicamento(rs.getString("medicamento"));
				u.setApresentacao(rs.getString("apresentacao"));
				u.setRol(rs.getString("rol"));
				u.setClasse_comercial(rs.getString("classe_comercial"));
			
				us.add(u);
			}
			
		}catch (Throwable e) {
			getLogger().error("Erro processado buscar pedido", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return us;
		
	}
	
	
	
}
