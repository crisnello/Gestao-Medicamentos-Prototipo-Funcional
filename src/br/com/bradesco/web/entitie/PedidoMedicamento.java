package br.com.bradesco.web.entitie;

public class PedidoMedicamento {
	
	private long id;
	
	private Pedido pedido;
	
	private Medicamento medicamento;
	
	private long quantidade;
	
	

	@Override
	public String toString() {
		return getId() + " - " + getPedido().getId() + " - " + getMedicamento().getId() + " - " + getQuantidade() ;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public Medicamento getMedicamento() {
		return medicamento;
	}

	public void setMedicamento(Medicamento medicamento) {
		this.medicamento = medicamento;
	}

	public long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
	}
	
	
	
	
	

}
