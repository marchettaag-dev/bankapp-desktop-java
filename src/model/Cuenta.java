package model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import utils.UUIDUtils;

public class Cuenta {
	
	private long id;
	
	private UUID numeroCuenta;
	
	private String alias;
	
	private Cliente clienteTitular;
	
	private BigDecimal saldo;
	
	private ZonedDateTime fechaCreacion;
	
	private List<Transaccion> listaTransacciones;
	
	private long idCliente;
	
	public Cuenta(long idCliente) {
		this.id= 0;
		this.numeroCuenta = UUID.randomUUID();
		this.alias = numeroCuenta.toString();
		this.clienteTitular= null;
		this.idCliente= idCliente;
		this.saldo= new BigDecimal("0.00");
		this.fechaCreacion= ZonedDateTime.now();
		this.listaTransacciones= new ArrayList<Transaccion>();
	}
	
	public Cuenta(String numeroCuenta, long idCliente, BigDecimal saldo,ZonedDateTime fechaCreacion, String alias) {
		this(idCliente);
		this.numeroCuenta = UUIDUtils.colocarGuiones(numeroCuenta);
		this.alias = alias;
		this.saldo= saldo;
		this.fechaCreacion= fechaCreacion;
	}
	
	public Cuenta(Cliente clienteTitular) {
		this(clienteTitular.getId());
		this.clienteTitular= clienteTitular;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Cliente getClienteTitular() {
		return this.clienteTitular;
	}

	public void setClienteTitular(Cliente clienteTitular) {
		this.clienteTitular = clienteTitular;
	}

	public BigDecimal getSaldo() {
		return this.saldo;
	}

	public void ingresarSaldo(BigDecimal monto) {
		this.saldo= saldo.add(monto);
	}
	
	public void extraerSaldo(BigDecimal monto) {
		this.saldo = saldo.subtract(monto);
	}

	public List<Transaccion> getListaTrasacciones() {
		return new ArrayList<>(this.listaTransacciones);
	}

	public void agregarTransaccion(Transaccion t) {
		this.listaTransacciones.add(t);
	}

	public String getNumeroCuenta() {
		return this.numeroCuenta.toString().replace("-", "");
	}
	
	public String getAlias() {
		return this.alias;
	}
	
	public void setAlias(String alias) {
		this.alias = alias;
	}

	public ZonedDateTime getFechaCreacion() {
		return this.fechaCreacion;
	}

	public long getIdCliente() {
		return this.idCliente;
	}

	public void setIdCliente(long idCliente) {
		this.idCliente = idCliente;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numeroCuenta);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cuenta other = (Cuenta) obj;
		return Objects.equals(numeroCuenta, other.numeroCuenta);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Cuenta id= ");
		builder.append(this.getId());
		builder.append(", numeroCuenta= ");
		builder.append(this.getNumeroCuenta());
		builder.append(", clienteTitular= ");
		builder.append(this.getClienteTitular());
		builder.append(", saldo= ");
		builder.append(this.getSaldo());
		builder.append(", fechaCreacion= ");
		builder.append(this.getFechaCreacion());
		builder.append(", listaTrasacciones= {");
		for(Transaccion c : this.getListaTrasacciones()) {
			builder.append(c.toString());
		}
		builder.append("}");
		return builder.toString();
	}
}
