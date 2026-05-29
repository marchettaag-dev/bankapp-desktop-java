package model;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Objects;
import java.util.UUID;

public class Transaccion {
	
	private long id;
	
	private final UUID numeroTransaccion;
	
	private Cuenta cuentaOrigen;
	
	private Cuenta cuentaDestino;
	
	private BigDecimal monto;
	
	private TipoTransaccion tipo;
	
	private ZonedDateTime fechaYHora;
	
	private Long idCuentaOrigen;
	
	private Long idCuentaDestino;
	
	public Transaccion(Long idCuentaOrigen, Long idCuentaDestino, TipoTransaccion tipo) {
		this.id=0;
		this.numeroTransaccion= UUID.randomUUID();
		
		this.idCuentaOrigen= idCuentaOrigen;
		this.idCuentaDestino= idCuentaDestino;
		this.monto= new BigDecimal("0.00");
		this.tipo= tipo;
		this.fechaYHora= ZonedDateTime.now();
	}
	
	public Transaccion(Long idCuentaOrigen, Long idCuentaDestino, TipoTransaccion tipo, BigDecimal monto) {
		this(idCuentaOrigen, idCuentaDestino, tipo);
		this.monto= monto;
	}
	
	public Transaccion(Long idCuentaOrigen, Long idCuentaDestino, TipoTransaccion tipo, BigDecimal monto,ZonedDateTime fechaYHora ) {
		this(idCuentaOrigen, idCuentaDestino, tipo, monto);
		this.fechaYHora = fechaYHora;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}
	/*
	public Cuenta getCuentaOrigen() {
		return this.cuentaOrigen;
	}

	public void setCuentaOrigen(Cuenta cuentaOrigen) {
		this.cuentaOrigen = cuentaOrigen;
	}

	public Cuenta getCuentaDestino() {
		return this.cuentaDestino;
	}

	public void setCuentaDestino(Cuenta cuentaDestino) {
		this.cuentaDestino = cuentaDestino;
	}
	*/
	public BigDecimal getMonto() {
		return this.monto;
	}

	public void setMonto(BigDecimal monto) {
		this.monto = monto;
	}

	public TipoTransaccion getTipo() {
		return this.tipo;
	}
	
	public String getTipoMensaje() {
		return this.tipo.getMensaje();
	}

	public void setTipo(TipoTransaccion tipo) {
		this.tipo = tipo;
	}

	public String getNumeroTransaccion() {
		return this.numeroTransaccion.toString().replace("-", "");
	}

	public ZonedDateTime getFechaYHora() {
		return this.fechaYHora;
	}

	public Long getIdCuentaOrigen() {
		return this.idCuentaOrigen;
	}

	public void setIdCuentaOrigen(Long idCuentaOrigen) {
		this.idCuentaOrigen = idCuentaOrigen;
	}

	public Long getIdCuentaDestino() {
		return this.idCuentaDestino;
	}

	public void setIdCuentaDestino(Long idCuentaDestino) {
		this.idCuentaDestino = idCuentaDestino;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numeroTransaccion);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Transaccion other = (Transaccion) obj;
		return Objects.equals(numeroTransaccion, other.numeroTransaccion);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Transaccion id= ");
		builder.append(this.getId());
		builder.append(", numero de Transaccion= ");
		builder.append(this.getNumeroTransaccion());
		//builder.append(", cuentaOrigen= ");
		//builder.append(this.getCuentaOrigen());
		//builder.append(", cuentaDestino= ");
		//builder.append(this.cuentaDestino);
		builder.append(", monto= ");
		builder.append(this.getMonto());
		builder.append(", tipo= ");
		builder.append(this.getTipo());
		builder.append(", fechaYHora= ");
		builder.append(this.getFechaYHora());
		return builder.toString();
	}
}
