package br.com.bradesco.web.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import br.com.bradesco.web.entitie.Medicamento;
import br.com.bradesco.web.entitie.Pedido;
import br.com.bradesco.web.entitie.PedidoMedicamento;
import br.com.bradesco.web.exceptions.DaoException;

public class PedidoDao extends BaseDao{
	
	SimpleDateFormat sdf_br = new SimpleDateFormat("dd/MM/yyyy");
	
	public Pedido atualizarEnderecoCep(String cep, Pedido u) throws Throwable{
		
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from cep where cep='"+cep+"'";
			
			rs = con.createStatement().executeQuery(query);
			if(rs.next()){
				u.setCidade(rs.getString("cidade"));
				u.setUf(rs.getString("uf"));
				u.setEndereco(rs.getString("tipo")+" "+rs.getString("logradouro"));
			}
			
		}catch (Throwable e) {
			getLogger().error("Erro processado atualizarEnderecoCep", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		return u;
	}
	
	public void adicionarPedidoImagem(long id_pedido,long id_imagem) throws Throwable{
		
		try{
			conectar();
			
			String q1 = "insert into pedido_imagem(id_pedido,id_imagem)" +
					" values(?,?)" ;
			
			//getLogger().debug(ReflectionToStringBuilder.toString(u, ToStringStyle.MULTI_LINE_STYLE));
			
			pstm = con.prepareStatement(q1);
			
			pstm.setLong(1, id_pedido);
			pstm.setLong(2, id_imagem);
	
			
			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Creating nota_imagem failed, no rows affected.");
	        }
	        
		}catch (Throwable e) {
			con.rollback();
			getLogger().error("Erro processado adicionar PedidoImagem", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
	}
	
	
	public void excluir(long idPedido) throws Throwable{
		try{
			conectar();

			//------verificar se não é melhor fazer o delete on cascade----
			
			String q1 = "delete from pedido_medicamento where id_pedido = ?" ; 
			
			pstm = con.prepareStatement(q1);
			pstm.setLong(1, idPedido);
			
			pstm.executeUpdate();
			
			q1 = "delete from pedido_imagem where id_pedido = ?" ;
			
			pstm = con.prepareStatement(q1);
			pstm.setLong(1, idPedido);
			
			pstm.executeUpdate();
			
			//------------------------------------------------------------

			
			String q2 = "delete from pedido where id = ?" ;
			
			pstm = con.prepareStatement(q2);
			pstm.setLong(1, idPedido);
			
			pstm.executeUpdate();
			
		}catch (Throwable e) {
			con.rollback();
			getLogger().error("Erro processado excluir", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
	}
	

	
	
	public Pedido buscarPedido(String numero_pedido) throws Throwable{
		Pedido u = null;
		try{
			conectar();
			
			String query = "select * from pedido where numero_pedido='"+numero_pedido+"'";
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Pedido();
				u.setId(rs.getLong("id"));
				u.setNumero_pedido(rs.getString("numero_pedido"));
				u.setNome(rs.getString("nome"));
				u.setNumero_cartao(rs.getString("numero_cartao"));
				u.setCpf(rs.getString("cpf"));
				u.setTelefone(rs.getString("telefone"));
				u.setEmail(rs.getString("email"));
				u.setDataNasc(rs.getDate("data_nasc"));
				u.setIdade(rs.getLong("idade"));
				u.setSexo(rs.getString("sexo"));
				u.setCep(rs.getString("cep"));
				u.setEndereco(rs.getString("endereco"));
				u.setNumero(rs.getLong("numero"));
				u.setCidade(rs.getString("cidade"));
				u.setUf(rs.getString("uf"));
				
				u.setPeso(rs.getString("peso"));
				u.setAltura(rs.getString("altura"));
				u.setSuperficie_corporal(rs.getString("superficie_corporal"));
				u.setDiagnostico(rs.getString("diagnostico"));
				
				//u.setMedicamento(rs.getString("medicamento"));
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setDataCadastroBr(sdf_br.format(u.getDataCadastro()));
				
				u.setMedico(rs.getString("medico"));
				u.setCrm_medico(rs.getString("crm_medico"));
				u.setUf_medico(rs.getString("uf_medico"));
				u.setReceita(rs.getString("receita"));
				u.setCid(rs.getString("cid"));
				u.setProcedimento(rs.getString("procedimento"));
				u.setRegime(rs.getString("regime"));
				u.setQuantidade(rs.getLong("quantidade"));	
				u.setMotivo_cancelamento(rs.getString("motivo_cancelamento"));
				u.setStatus(rs.getString("status"));
				u.setIdCliente(rs.getInt("id_cliente"));

			}
			
		}catch (Throwable e) {
			getLogger().error("Erro processado buscar pedido", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}
	
	
	
	

	
	

	
	public void alterar(Pedido u) throws Throwable{
		try{
			conectar();
			
			//String q1 = "update pedido set nome=?,numero_cartao=?,cpf=?,telefone=?,email=?,data_nasc=?,idade=?,sexo=?,cep=?,endereco=?,numero=?,cidade=?,uf=?,peso=?,altura=?,superficie_corporal=?,diagnostico=?,medicamento=?,medico=?,crm_medico=?,uf_medico=?,receita=?,cid=?,procedimento=?,regime=?,quantidade=?,motivo_cancelamento=? where id = ?";
			String q1 = "update pedido set nome=?,numero_cartao=?,cpf=?,telefone=?,email=?,data_nasc=?,idade=?,sexo=?,cep=?,endereco=?,numero=?,cidade=?,uf=?,peso=?,altura=?,superficie_corporal=?,diagnostico=?,medico=?,crm_medico=?,uf_medico=?,receita=?,cid=?,procedimento=?,regime=?,quantidade=?,motivo_cancelamento=? where id = ?";
			
			pstm = con.prepareStatement(q1);
			
			pstm.setString(1, u.getNome());
			pstm.setString(2, u.getNumero_cartao());
			pstm.setString(3, u.getCpf());
			pstm.setString(4, u.getTelefone());
			pstm.setString(5, u.getEmail());
			pstm.setDate(6, new Date(u.getDataNasc().getTime())); //verificar se não é melhor salvar como string
			pstm.setLong(7, u.getIdade());
			pstm.setString(8, u.getSexo());
			pstm.setString(9, u.getCep());
			pstm.setString(10, u.getEndereco());
			pstm.setLong(11, u.getNumero());
			pstm.setString(12, u.getCidade());
			pstm.setString(13, u.getUf());
			pstm.setString(14, u.getPeso());
			pstm.setString(15, u.getAltura());
			pstm.setString(16, u.getSuperficie_corporal());
			pstm.setString(17, u.getDiagnostico());
//			pstm.setString(18, u.getMedicamento());
			pstm.setString(18, u.getMedico());
			pstm.setString(19, u.getCrm_medico());
			pstm.setString(20, u.getUf_medico());
			pstm.setString(21, u.getReceita());
			pstm.setString(22, u.getCid());
			pstm.setString(23, u.getProcedimento());
			pstm.setString(24, u.getRegime());
			pstm.setLong(25, u.getQuantidade());
			pstm.setString(26, u.getMotivo_cancelamento());
			
			
			pstm.setLong(27, u.getId());
			
			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Updating pedido failed, no rows affected.");
	        }
			
					
		}catch (Throwable e) {
			con.rollback();
			getLogger().error("Erro processado atualizar pedido", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		
	}
	
	
	public void adicionar(Pedido u,ArrayList<Medicamento> meds) throws Throwable{
		try{
			conectar();
			
			String q1 = "insert into pedido(nome,numero_cartao,cpf,telefone,email,data_nasc,idade,sexo,cep,endereco,numero,cidade,uf,peso,altura,superficie_corporal,diagnostico,medico,crm_medico,uf_medico,receita,cid,procedimento,regime,quantidade,motivo_cancelamento,status,id_cliente,numero_pedido,data_cadastro)" +
					" values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)" ;
			
			getLogger().debug(ReflectionToStringBuilder.toString(u, ToStringStyle.MULTI_LINE_STYLE));
			
			pstm = con.prepareStatement(q1, Statement.RETURN_GENERATED_KEYS);
			
			pstm.setString(1, u.getNome());
			pstm.setString(2, u.getNumero_cartao());
			pstm.setString(3, u.getCpf());
			pstm.setString(4, u.getTelefone());
			pstm.setString(5, u.getEmail());
			pstm.setDate(6, new Date(u.getDataNasc().getTime())); //verificar se não é melhor salvar como string
			pstm.setLong(7, u.getIdade());
			pstm.setString(8, u.getSexo());
			pstm.setString(9, u.getCep());
			pstm.setString(10, u.getEndereco());
			pstm.setLong(11, u.getNumero());
			pstm.setString(12, u.getCidade());
			pstm.setString(13, u.getUf());
			pstm.setString(14, u.getPeso());
			pstm.setString(15, u.getAltura());
			pstm.setString(16, u.getSuperficie_corporal());
			pstm.setString(17, u.getDiagnostico());
//			pstm.setString(18, u.getMedicamento());
			pstm.setString(18, u.getMedico());
			pstm.setString(19, u.getCrm_medico());
			pstm.setString(20, u.getUf_medico());
			pstm.setString(21, u.getReceita());
			pstm.setString(22, u.getCid());
			pstm.setString(23, u.getProcedimento());
			pstm.setString(24, u.getRegime());
			pstm.setLong(25, u.getQuantidade());
			pstm.setString(26, u.getMotivo_cancelamento());
			
			pstm.setString(27, "Pedido Em Analise");
			pstm.setLong(28, u.getIdCliente());
			pstm.setString(29, u.getNumero_pedido());
			
			pstm.setTimestamp(30, new Timestamp(System.currentTimeMillis()));
			
			int affectedRows = pstm.executeUpdate();
			if (affectedRows == 0) {
	            throw new SQLException("Creating pedido failed, no rows affected.");
	        }
			rs = pstm.getGeneratedKeys();
	        if (rs.next()) {
	            u.setId(rs.getLong(1));
	        } else {
	            throw new SQLException("Creating pedido failed, no generated key obtained.");
	        }
	          
	        
	        for(int i=0;i<meds.size();i++) {
	        	
	        	Medicamento p = meds.get(i);
	        	
//	        	getLogger().debug("vai inserir medicamento "+p.getId());
	        	
	        	String q2 = "insert into pedido_medicamento(id_medicamento,id_pedido) values (?,?)";
	        	
	        	pstm = con.prepareStatement(q2, Statement.RETURN_GENERATED_KEYS);
	        	
				pstm.setLong(1, p.getId());
				pstm.setLong(2, u.getId());
				
				int affectedRows2 = pstm.executeUpdate();
				if (affectedRows2 == 0) {
		            throw new SQLException("Creating pedido failed, no rows affected.");
		        }
	        }
	        
	        
	        
		}catch (Throwable e) {
			con.rollback();
			getLogger().error("Erro processado adicionar pedido", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		
	}
	
	public boolean existePedido(String id) throws Throwable{
		boolean retorno = false;
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select count(id) from pedido where id='"+id+"'";
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				int total = rs.getInt(1);
				if(total>0){
					retorno = true;
				}
			}
			
		}catch (Throwable e) {
			getLogger().error("Erro processado existeEmail", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return retorno;
		
	}
	
	public Pedido buscarPedido(long id) throws Throwable{
		Pedido u = null;
		
		try{
			conectar();
			
			String query = "select * from pedido where id="+id;
			
			rs = con.createStatement().executeQuery(query);
			
			if(rs.next()){
				u = new Pedido();
				u.setId(rs.getLong("id"));
				u.setNumero_pedido(rs.getString("numero_pedido"));
				u.setNome(rs.getString("nome"));
				u.setNumero_cartao(rs.getString("numero_cartao"));
				u.setCpf(rs.getString("cpf"));
				u.setTelefone(rs.getString("telefone"));
				u.setEmail(rs.getString("email"));
				u.setDataNasc(rs.getDate("data_nasc"));
				u.setIdade(rs.getLong("idade"));
				u.setSexo(rs.getString("sexo"));
				u.setCep(rs.getString("cep"));
				u.setEndereco(rs.getString("endereco"));
				u.setNumero(rs.getLong("numero"));
				u.setCidade(rs.getString("cidade"));
				u.setUf(rs.getString("uf"));
				
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setDataCadastroBr(sdf_br.format(u.getDataCadastro()));				
				
				u.setPeso(rs.getString("peso"));
				u.setAltura(rs.getString("altura"));
				u.setSuperficie_corporal(rs.getString("superficie_corporal"));
				u.setDiagnostico(rs.getString("diagnostico"));
//				u.setMedicamento(rs.getString("medicamento"));
				u.setMedico(rs.getString("medico"));
				u.setCrm_medico(rs.getString("crm_medico"));
				u.setUf_medico(rs.getString("uf_medico"));
				u.setReceita(rs.getString("receita"));
				u.setCid(rs.getString("cid"));
				u.setProcedimento(rs.getString("procedimento"));
				u.setRegime(rs.getString("regime"));
				u.setQuantidade(rs.getLong("quantidade"));	
				u.setMotivo_cancelamento(rs.getString("motivo_cancelamento"));
				u.setStatus(rs.getString("status"));
				u.setIdCliente(rs.getInt("id_cliente"));

				
				ArrayList<PedidoMedicamento> pmeds = new ArrayList<PedidoMedicamento>();
				
				ArrayList<Medicamento> meds = new ArrayList<Medicamento>();
				String query2 = "select * from pedido_medicamento where id_pedido="+u.getId();
				
//				getLogger().debug("PEDIDO : "+u.getId()+" NP : "+u.getNumero_pedido());
				
				rs2 = con.createStatement().executeQuery(query2);
				while(rs2.next()){
					Medicamento medicamento	= (new MedicamentoDao()).buscarMedicamento(rs2.getInt("id_medicamento"));
//					getLogger().debug("MEDICAMENTO "+medicamento.getId()+" nome :"+medicamento.getMedicamento());
					meds.add(medicamento);
					
					PedidoMedicamento pm = new PedidoMedicamento();
					pm.setId(rs2.getInt("id"));
					pm.setMedicamento(medicamento);
					pm.setPedido(u);
					pm.setQuantidade(rs2.getInt("quantidade"));
					
//					getLogger().debug(pm);
					
					pmeds.add(pm);
					
				}
				u.setMedicamentos(meds);
				u.setPedidomedicamento(pmeds);
								
			}
			
		}catch (Throwable e) {
			getLogger().error("Erro processado buscar pedido", e);
			throw new DaoException(e.getMessage(), e.getCause());
		}finally{
			desconectar();
		}
		
		return u;
		
	}
	
	public List<Pedido> buscarPedidos(long idCliente,String cpf) throws Throwable{
		List<Pedido> us = new ArrayList<Pedido>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from pedido where id_cliente=? and cpf = ?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idCliente);
			pstm.setString(2, cpf);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Pedido u = new Pedido();
				u.setId(rs.getLong("id"));
				u.setNome(rs.getString("nome"));
				u.setNumero_pedido(rs.getString("numero_pedido"));
				u.setNumero_cartao(rs.getString("numero_cartao"));
				u.setCpf(rs.getString("cpf"));
				u.setTelefone(rs.getString("telefone"));
				u.setEmail(rs.getString("email"));
				u.setDataNasc(rs.getDate("data_nasc"));
				u.setIdade(rs.getLong("idade"));
				u.setSexo(rs.getString("sexo"));
				u.setCep(rs.getString("cep"));
				u.setEndereco(rs.getString("endereco"));
				u.setNumero(rs.getLong("numero"));
				u.setCidade(rs.getString("cidade"));
				u.setUf(rs.getString("uf"));
				
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setDataCadastroBr(sdf_br.format(u.getDataCadastro()));
				
				u.setPeso(rs.getString("peso"));
				u.setAltura(rs.getString("altura"));
				u.setSuperficie_corporal(rs.getString("superficie_corporal"));
				u.setDiagnostico(rs.getString("diagnostico"));
//				u.setMedicamento(rs.getString("medicamento"));
				u.setMedico(rs.getString("medico"));
				u.setCrm_medico(rs.getString("crm_medico"));
				u.setUf_medico(rs.getString("uf_medico"));
				u.setReceita(rs.getString("receita"));
				u.setCid(rs.getString("cid"));
				u.setProcedimento(rs.getString("procedimento"));
				u.setRegime(rs.getString("regime"));
				u.setQuantidade(rs.getLong("quantidade"));	
				
				u.setMotivo_cancelamento(rs.getString("motivo_cancelamento"));
				u.setStatus(rs.getString("status"));
				u.setIdCliente(rs.getInt("id_cliente"));
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
	
	public List<Pedido> buscarPedidos(long idCliente) throws Throwable{
		List<Pedido> us = new ArrayList<Pedido>();
		try{
			conectar();
			
			con.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			
			String query = "select * from pedido where id_cliente=?";
			
			pstm = con.prepareStatement(query);
			
			pstm.setLong(1, idCliente);
			
			rs = pstm.executeQuery();
			
			while(rs.next()){
				Pedido u = new Pedido();
				u.setId(rs.getLong("id"));
				u.setNome(rs.getString("nome"));
				u.setNumero_pedido(rs.getString("numero_pedido"));
				u.setNumero_cartao(rs.getString("numero_cartao"));
				u.setCpf(rs.getString("cpf"));
				u.setTelefone(rs.getString("telefone"));
				u.setEmail(rs.getString("email"));
				u.setDataNasc(rs.getDate("data_nasc"));
				u.setIdade(rs.getLong("idade"));
				u.setSexo(rs.getString("sexo"));
				u.setCep(rs.getString("cep"));
				u.setEndereco(rs.getString("endereco"));
				u.setNumero(rs.getLong("numero"));
				u.setCidade(rs.getString("cidade"));
				u.setUf(rs.getString("uf"));
				
				u.setDataCadastro(rs.getDate("data_cadastro"));
				u.setDataCadastroBr(sdf_br.format(u.getDataCadastro()));
				
				u.setPeso(rs.getString("peso"));
				u.setAltura(rs.getString("altura"));
				u.setSuperficie_corporal(rs.getString("superficie_corporal"));
				u.setDiagnostico(rs.getString("diagnostico"));
//				u.setMedicamento(rs.getString("medicamento"));
				u.setMedico(rs.getString("medico"));
				u.setCrm_medico(rs.getString("crm_medico"));
				u.setUf_medico(rs.getString("uf_medico"));
				u.setReceita(rs.getString("receita"));
				u.setCid(rs.getString("cid"));
				u.setProcedimento(rs.getString("procedimento"));
				u.setRegime(rs.getString("regime"));
				u.setQuantidade(rs.getLong("quantidade"));	
				
				u.setMotivo_cancelamento(rs.getString("motivo_cancelamento"));
				u.setStatus(rs.getString("status"));
				u.setIdCliente(rs.getInt("id_cliente"));
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
