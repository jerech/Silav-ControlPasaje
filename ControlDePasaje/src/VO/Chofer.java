package VO;

public class Chofer {

	private String usuario;
	private double longitud;
	private double latitud;
	private double distanciaADestino;
	private String claveGCM;
	private int numeroDeMovil;
	
	public String getUsuario(){
		return this.usuario;
	}
	public void setUsuario(String usuario){
		this.usuario = usuario;
	}
	
	public double getLongitud(){
		return this.longitud;
	}
	public void setLongitud(double longitud){
		this.longitud = longitud;
	}
	
	public double getDistanciaADestino(){
		return this.distanciaADestino;
	}
	public void setDistanciaADestino(double distanciaADestino){
		this.distanciaADestino = distanciaADestino;
	}
	
	public String getClaveGCM(){
		return this.claveGCM;
	}
	public void setClaveGCM(String claveGCM){
		this.claveGCM = claveGCM;
	}

	public double getLatitud(){
		return this.latitud;
	}
	public void setLatitud(double latitud){
		this.latitud = latitud;
	}
	
	public int getNumeroDeMovil(){
		return this.numeroDeMovil;
	}
	public void setNumeroDeMovil(int numeroDeMovil){
		this.numeroDeMovil = numeroDeMovil;
	}
}