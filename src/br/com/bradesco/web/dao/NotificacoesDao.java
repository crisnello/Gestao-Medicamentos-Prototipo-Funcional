package br.com.bradesco.web.dao;

import java.sql.Connection;

import br.com.bradesco.web.exceptions.DaoException;

public class NotificacoesDao extends BaseDao{
	
	public int totalNotificacoes(long idUsuario) throws Throwable{
		int total = 0;
		try {
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select count(n.id) as total from notificacoes as n " +
						   "inner join notificacoes_usuario as nu where nu.id_notificacao=n.id and id_status=1 and id_usuario=?"; 
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idUsuario);
			
			rs = pstm.executeQuery();
			
			if(rs.next()){
				total = rs.getInt("total");
			}
			
		} catch (Throwable e) {
			getLogger().error("erro buscando totalNotificacoes", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		return total;
	}

}
