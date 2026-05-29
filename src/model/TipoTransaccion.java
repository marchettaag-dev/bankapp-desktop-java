package model;

public enum TipoTransaccion {
	DEPOSITO("deposito")
	, RETIRO("retiro")
	, TRANSFERENCIA("transferencia");
	
	private final String mensaje;
	
	TipoTransaccion(String mensaje) {
		this.mensaje= mensaje;
	}
	
	public String getMensaje() {
		return mensaje;
	}
	
	
}


