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
	
	public PasajeDAO(){
		
	}
	
	public ArrayList<Pasaje> getPasajesEnCurso() {
		
		  ArrayList<Pasaje> pasajesEnCurso = new ArrayList<Pasaje>();
		  
		  try {
		   PreparedStatement consulta = ConexionBD.getConnection().prepareStatement("SELECT * FROM PasajesEnCurso WHERE estado = 'por_asignar'");
		   ResultSet resultado = consulta.executeQuery();
		   while(resultado.next()){
			   Pasaje pasaje= new Pasaje();
			   pasaje.setDireccion(resultado.getString("direccion"));
			   pasaje.setCliente(resultado.getString("nombreCliente"));
			   pasaje.setId(resultado.getString("id"));
			   pasaje.setChofer(resultado.getString("usuarioChofer"));
			   pasaje.setFecha(resultado.getString("fecha"));
			   pasajesEnCurso.add(pasaje);
		   }
		   resultado.close();
		   consulta.close();
		   
		  } catch (Exception e) {
		   System.out.println("no se pudo consultar el pasaje\n"+e);
		  }
		  
		  ConexionBD.desconectar();
		 
		  return pasajesEnCurso;
		 }

	
	
	
}