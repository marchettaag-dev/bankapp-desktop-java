package model;

import java.util.Objects;

public abstract class Usuario {
	
	protected long ID;
	
	protected String nombre;
	
	protected String email;
	
	protected String contraseña;
	
	protected Rol rol;
	
	public Usuario(String nombre, String email, String contraseña, Rol rol) {
		this.ID= 0;
		this.nombre= nombre;
		this.email= email;
		this.contraseña= contraseña;
		this.rol= rol;
	}
	
	public long getId() {
		return this.ID;
	}
	
	public void setId(long id) {
		this.ID= id;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
	public void setNombre(String nombre) {
		this.nombre= nombre;
	}
	
	public String getEmail() {
		return this.email;
	}
	
	public void setEmail(String email) {
		this.email= email;
	}
	
	public String getContraseña() {
		return this.contraseña;
	}
	
	public void setContraseña(String contraseña) {
		this.contraseña= contraseña;
	}
	
	public Rol getRol() {
		return this.rol;
	}
	
	public void setRol(Rol rol) {
		this.rol= rol;
	}

	@Override
	public int hashCode() {
		return Objects.hash(ID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuario other = (Usuario) obj;
		return ID == other.ID;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Rol= ");
		builder.append(this.getRol());
		builder.append(", ID= ");
		builder.append(this.getId());
		builder.append(", nombre= ");
		builder.append(this.getNombre());
		builder.append(", email= ");
		builder.append(this.getEmail());
		return builder.toString();
	}
}
