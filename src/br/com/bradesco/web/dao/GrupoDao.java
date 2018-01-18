package br.com.bradesco.web.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import br.com.bradesco.web.entitie.Grupo;
import br.com.bradesco.web.exceptions.DaoException;

public class GrupoDao extends BaseDao{

	public void adicionar(Grupo g) throws Throwable{
		try{
			conectar();
			
			String query = "insert into grupo(nome,data_cadastro,id_cliente) values (?,?,?)";
			
			PreparedStatement pstm = con.prepareStatement(query);
			
			pstm.setString(1, g.getNome());
			pstm.setDate(2, new Date(System.currentTimeMillis()));
			pstm.setLong(3, g.getIdCliente());
			
			pstm.executeUpdate();
			
		}catch (Throwable e) {
			getLogger().error("erro cadastrar grupo", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
	}
	
	
	public void atualizar(Grupo g) throws Throwable{
		try{
			conectar();
			
			String query = "update grupo set nome=? where id=?";
			
			PreparedStatement pstm = con.prepareStatement(query);
			
			pstm.setString(1, g.getNome());
			pstm.setLong(2, g.getId());
			
			pstm.executeUpdate();
			
		}catch (Throwable e) {
			getLogger().error("erro atualizar grupo", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}
	
	
	public void excluir(Grupo g) throws Throwable{
		try{
			conectar();
			
			String query = "delete from grupo where id=?";
			
			PreparedStatement pstm = con.prepareStatement(query);
			
			pstm.setLong(1, g.getId());
			
			pstm.executeUpdate();
			
		}catch (Throwable e) {
			getLogger().error("erro excluir grupo", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}
	
	public Grupo buscarGrupo(long id) throws Throwable{
		Grupo g = null;
		try{
			conectar();
			
			String query = "select * from grupo where id="+id;
			
			ResultSet rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				g = new Grupo();
				g.setId(rs.getLong("id"));
				g.setNome(rs.getString("nome"));
				g.setIdCliente(rs.getLong("id_cliente"));
				g.setDataCadastro(rs.getDate("data_cadastro"));
			}
			
		}catch (Throwable e) {
			getLogger().error("erro buscando grupos", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return g;
		
	}
	
	public List<Grupo> buscarGrupos(long idCliente) throws Throwable{
		List<Grupo> gs = new ArrayList<Grupo>();
		try{
			conectar();
			
			String query = "select id,nome,1 as pertence from grupo where id_cliente="+idCliente+" order by nome asc";
			
			ResultSet rs = con.createStatement().executeQuery(query);
			
			while(rs.next()){
				Grupo g = new Grupo();
				g.setId(rs.getLong("id"));
				g.setNome(rs.getString("nome"));
				g.setPertence(rs.getInt("pertence"));
				gs.add(g);
			}
			
		}catch (Throwable e) {
			getLogger().error("erro buscando grupos", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return gs;
		
	}
	
}
