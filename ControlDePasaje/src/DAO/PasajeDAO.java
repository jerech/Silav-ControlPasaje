package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import VO.Pasaje;
import Conexion.ConexionBD;

/**
 * Clase que permite el acceso a la base de datos
 *
 */
public class PasajeDAO{
	
	public static ArrayList<Pasaje> getPasajesEnCurso() {
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
			    pasaje.setLatitud(Double.parseDouble(resultado.getString("latDireccion")));
			    pasaje.setLongitud(Double.parseDouble(resultado.getString("lonDireccion")));
			    pasaje.setFecha(resultado.getString("fecha"));
			    pasajesEnCurso.add(pasaje);
		    }
		    resultado.close();
		    consulta.close();
		   
		} catch(Exception e) {
			System.out.println("Error al consultar datos de pasajes: "+e.getMessage());
		}
		ConexionBD.desconectar();
		return pasajesEnCurso;
	}
	
	public static void actualizarEstado(Pasaje pasaje){
		try {
  			PreparedStatement statement = ConexionBD.getConnection().prepareStatement("UPDATE PasajesEnCurso SET estado = 'en_espera' WHERE id = "+pasaje.getId());
  			int rowsUpdated = statement.executeUpdate();
  			if(rowsUpdated == 0){
  				//error
  			}
  			statement.close();
    
  		} catch (SQLException e) {
  			System.out.println("Error al actualizar estado de pasaje: "+e.getMessage());
  		}
  		ConexionBD.desconectar();
	}
}