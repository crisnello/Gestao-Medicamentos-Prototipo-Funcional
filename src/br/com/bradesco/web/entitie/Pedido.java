package br.com.bradesco.web.entitie;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Pedido implements Serializable{
	
	private static final long serialVersionUID = 8023901815942617149L;
	
	SimpleDateFormat sdf_br = new SimpleDateFormat("dd/MM/yyyy");

	private long id;
	
	private String numero_pedido;
	
	private String nome;
	
	private String numero_cartao;
	
	private Date dataNasc;
	
	private long idade;
	
	private String sexo;
	
	private String email;
	
	private String cpf;
	
	private String cep;
	
	private String endereco;
	
	private long numero;
	
	private String uf;
	
	private String cidade;
	
	private String telefone;
	
	private String peso;
	
	private String altura;
	
	private String superficie_corporal;
	
	private String diagnostico;
	
	//private String medicamento;
	
	private ArrayList<Medicamento> medicamentos;
	
	private ArrayList<Long> id_medicamentos; 
	
	private String medico;
	
	private String crm_medico;
	
	private String uf_medico;
	
	private String receita;
	
	private Date dataReceita;
	
	private String cid;
	
	private String procedimento;
	
	private String regime;
	
	private long quantidade;
	
	private String motivo_cancelamento;
	
	private String status;
	
	private int idCliente;
	
	private Date dataCadastro;
	
	private String dataCadastroBr;
	
	public Pedido() {
		setCid("0");
		setProcedimento("Cadastro Inicial");
		setRegime("SADT terapia oral");
	}
	
	
	public String getDataCadastroBr() {
		return dataCadastroBr;
	}

	public void setDataCadastroBr(String dataCadastroBr) {
		this.dataCadastroBr = dataCadastroBr;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	
	
	public String getNumero_pedido() {
		return numero_pedido;
	}

	public void setNumero_pedido(String numero_pedido) {
		this.numero_pedido = numero_pedido;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNumero_cartao() {
		return numero_cartao;
	}

	public void setNumero_cartao(String numero_cartao) {
		this.numero_cartao = numero_cartao;
	}

	

	public Date getDataNasc() {
		return dataNasc;
	}

	public void setDataNasc(Date dataNasc) {
		this.dataNasc = dataNasc;
	}

	public long getIdade() {
		return idade;
	}

	public void setIdade(long idade) {
		this.idade = idade;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public long getNumero() {
		return numero;
	}

	public void setNumero(long numero) {
		this.numero = numero;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getPeso() {
		return peso;
	}

	public void setPeso(String peso) {
		this.peso = peso;
	}

	public String getAltura() {
		return altura;
	}

	public void setAltura(String altura) {
		this.altura = altura;
	}

	public String getSuperficie_corporal() {
		return superficie_corporal;
	}

	public void setSuperficie_corporal(String superficie_corporal) {
		this.superficie_corporal = superficie_corporal;
	}

	public String getDiagnostico() {
		return diagnostico;
	}

	public void setDiagnostico(String diagnostico) {
		this.diagnostico = diagnostico;
	}

	
	
//	public String getMedicamento() {
//		return medicamento;
//	}
//
//	public void setMedicamento(String medicamento) {
//		this.medicamento = medicamento;
//	}

	public ArrayList<Long> getId_medicamentos() {
		return id_medicamentos;
	}

	public void setId_medicamentos(ArrayList<Long> id_medicamentos) {
		this.id_medicamentos = id_medicamentos;
	}

	
	public ArrayList<Medicamento> getMedicamentos() {
		return medicamentos;
	}


	public void setMedicamentos(ArrayList<Medicamento> medicamentos) {
		this.medicamentos = medicamentos;
	}

	public String getMedico() {
		return medico;
	}

	public void setMedico(String medico) {
		this.medico = medico;
	}

	
	
	public String getCrm_medico() {
		return crm_medico;
	}

	public void setCrm_medico(String crm_medico) {
		this.crm_medico = crm_medico;
	}

	public String getUf_medico() {
		return uf_medico;
	}

	public void setUf_medico(String uf_medico) {
		this.uf_medico = uf_medico;
	}

	public String getReceita() {
		
		return receita;
	}

	public void setReceita(String receita) {
		this.receita = receita;
	}
	
	
	public Date getDataReceita() {
		
		return dataReceita;
	}


	public void setDataReceita(Date dataReceita) {
		
		this.receita = sdf_br.format(dataReceita);
		
		this.dataReceita = dataReceita;
	}


	public String getCid() {
		return cid;
	}

	public void setCid(String cid) {
		this.cid = cid;
	}

	public String getProcedimento() {
		return procedimento;
	}

	public void setProcedimento(String procedimento) {
		this.procedimento = procedimento;
	}

	public String getRegime() {
		return regime;
	}

	public void setRegime(String regime) {
		this.regime = regime;
	}

	public long getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(long quantidade) {
		this.quantidade = quantidade;
	}

	public String getMotivo_cancelamento() {
		return motivo_cancelamento;
	}

	public void setMotivo_cancelamento(String motivo_cancelamento) {
		this.motivo_cancelamento = motivo_cancelamento;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getIdCliente() {
		return idCliente;
	}

	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	
	

}
