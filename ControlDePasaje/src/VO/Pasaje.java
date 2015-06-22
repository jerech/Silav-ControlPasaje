package VO;

public class Pasaje {

	private String direccion;
	private String cliente;
	private String id;
	private String fecha;
	private String usuarioChofer;
	private double longitud;
	private double latitud;
	
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
	
	public String getId(){
		return this.id;
	}
	public void setId(String id){
		this.id = id;
	}
	
	public String getChofer(){
		return this.usuarioChofer;
	}
	public void setChofer(String usuarioChofer){
		this.usuarioChofer = usuarioChofer;
	}
	
	public String getFecha() {
		return fecha;
	}
	public void setFecha(String fecha) {
		this.fecha = fecha;
	}
	
	public double getLatitud() {
		return latitud;
	}
	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}
	
	public double getLongitud() {
		return longitud;
	}
	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}
}