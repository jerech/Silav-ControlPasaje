package VO;

public class Pasaje {

	private String direccion;
	private String cliente;
	private int numeroDeMovil;//ATRIBUTO TEMPORAL
	//preferenciasDeMovil
	
	public String getDireccion(){
		return this.direccion;
	}
	public void setDireccion(String direccion){
		this.direccion = direccion;
	}
	
	public String getCliente(){
		return this.cliente;
	}
	public void setCliente(String cliente){
		this.cliente = cliente;
	}
	
	public int getNumeroDeMovil(){
		return this.numeroDeMovil;
	}
	public void setNumeroDeMovil(int numeroDeMovil){
		this.numeroDeMovil = numeroDeMovil;
	}
}
