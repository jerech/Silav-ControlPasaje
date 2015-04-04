package VO;

public class Chofer {

	private String usuario;
	private String longitud;
	private String latitud;
	private int numeroDeMovil;//ATRIBUTO TEMPORAL
	
	public String getUsuario(){
		return this.usuario;
	}
	public void setUsuario(String usuario){
		this.usuario = usuario;
	}
	
	public String getLongitud(){
		return this.longitud;
	}
	public void setLongitud(String longitud){
		this.longitud = longitud;
	}
	
	public String getLatitud(){
		return this.latitud;
	}
	public void setLatitud(String latitud){
		this.latitud = latitud;
	}
	
	public int getNumeroDeMovil(){
		return this.numeroDeMovil;
	}
	public void setNumeroDeMovil(int numeroDeMovil){
		this.numeroDeMovil = numeroDeMovil;
	}
}
