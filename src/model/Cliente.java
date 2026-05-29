package model;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class Cliente extends Usuario {

	private String apellido;
	
	private final ZonedDateTime fechaCreacion;
	
	private List<Cuenta> listaCuentas;
	
	public Cliente(String nombre, String email, String contraseña, Rol rol, String apellido, ZonedDateTime fechaCreacion) {
		super(nombre,email,contraseña,rol);
		this.apellido= apellido;
		this.fechaCreacion= fechaCreacion;
		this.listaCuentas= new ArrayList<Cuenta>();
	}
	
	public Cliente(String nombre, String email, String contraseña, Rol rol, String apellido) {
		this(nombre, email, contraseña, rol, apellido, ZonedDateTime.now());
	}
		
	public String getApellido() {
		return this.apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public List<Cuenta> getListaCuentas() {
		return new ArrayList<>(this.listaCuentas);
	}

	public void agregarCuenta(Cuenta c) {
		this.listaCuentas.add(c);
	}

	public ZonedDateTime getFechaCreacion() {
		return this.fechaCreacion;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(", apellido= ");
		builder.append(this.getApellido());
		builder.append(", Fecha de creacion= ");
		builder.append(this.getFechaCreacion());
		builder.append(", listaCuentas= {");
		for(Cuenta c : this.getListaCuentas()) {
			builder.append(c.toString());
		}
		builder.append("}");
		return builder.toString();
	}
	
	
	
}
