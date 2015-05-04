package DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import VO.Pasaje;
import Conexion.ConexionBD;


/**
 * Clase que permite el acceso a la base de datos
 *
 */
public class PasajeDAO{
	
	private ConexionBD conexion;
	
	public PasajeDAO(ConexionBD conexion){
		this.conexion = conexion;
	}
	
	public ArrayList<Pasaje> getPasajesEnCurso() {
		
		  ArrayList<Pasaje> pasajesEnCurso = new ArrayList<Pasaje>();
		     
		  try {
		   PreparedStatement consulta = conexion.getConnection().prepareStatement("SELECT * FROM PasajesEnCurso WHERE estado = 'por_asignar'");
		   ResultSet resultado = consulta.executeQuery();
		   while(resultado.next()){
			   Pasaje pasaje= new Pasaje();
			   pasaje.setDireccion(resultado.getString("direccion"));
			   pasaje.setCliente(resultado.getString("nombreCliente"));
			   pasaje.setId(resultado.getString("id"));
			   pasaje.setChofer(resultado.getString("usuarioChofer"));
			   pasajesEnCurso.add(pasaje);
		   }
		   resultado.close();
		   consulta.close();
		   
		  } catch (Exception e) {
		   System.out.println("no se pudo consultar el pasaje\n"+e);
		  }
		  return pasajesEnCurso;
		 }

	public void setConexion(ConexionBD conexion) {
		this.conexion = conexion;
	}	
	
	
}