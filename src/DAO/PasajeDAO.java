package DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import Conexion.ConexionBD;
import VO.Pasaje;

/**
 * Clase que permite el acceso a la base de datos
 *
 */
public class PasajeDAO {
	
	public ArrayList<Pasaje> getPasajesEnCurso() {
		
		  ArrayList<Pasaje> pasajesEnCurso = new ArrayList<Pasaje>();
		  ConexionBD conexion = new ConexionBD();
		     
		  try {
		   PreparedStatement consulta = conexion.getConnection().prepareStatement("SELECT * FROM PasajesEnCurso WHERE estado = 'por_asignar'");
		   ResultSet resultado = consulta.executeQuery();
		   while(resultado.next()){
			   Pasaje pasaje= new Pasaje();
			   pasaje.setDireccion(resultado.getString("direccion"));
			   pasaje.setCliente(resultado.getString("nombreCliente"));
			   pasaje.setNumeroDeMovil(Integer.parseInt(resultado.getString("numeroMovil")));
			   pasajesEnCurso.add(pasaje);
		   }
		   resultado.close();
		   consulta.close();
		   conexion.desconectar();
		   
		  } catch (Exception e) {
		   JOptionPane.showMessageDialog(null, "no se pudo consultar el pasaje\n"+e);
		  }
		  return pasajesEnCurso;
		 }	
}
