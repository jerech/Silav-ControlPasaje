package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import Conexion.ConexionBD;
import VO.Chofer;

/**
 * Clase que permite el acceso a la base de datos
 *
 */
public class ChoferDAO {
	
	public static ArrayList<Chofer> getChoferesConectados() {
		ArrayList<Chofer> choferesConectados = new ArrayList<Chofer>();
		try {
			PreparedStatement consulta = ConexionBD.getConnection().prepareStatement("SELECT * FROM ChoferesConectados WHERE estado_movil = 'LIBRE'");
		    ResultSet resultado = consulta.executeQuery();
		    while(resultado.next()){
		    	Chofer chofer = new Chofer();
			    chofer.setUsuario(resultado.getString("usuario"));
			    chofer.setLongitud(Double.parseDouble(resultado.getString("ubicacion_lon")));
			    chofer.setLatitud(Double.parseDouble(resultado.getString("ubicacion_lat")));
			    chofer.setNumeroDeMovil(Integer.parseInt(resultado.getString("numero_movil")));
			   
			    try{
			    	PreparedStatement consulta2 = ConexionBD.getConnection().prepareStatement("SELECT clave_gcm FROM Choferes WHERE usuario='"+chofer.getUsuario()+"'");
				    ResultSet resultado2 = consulta2.executeQuery();
				    if(resultado2.next()){
				    	chofer.setClaveGCM(resultado2.getString("clave_gcm"));
					    resultado2.close();
					    consulta2.close();
				    }
				    else{
				    	
				    }
			     } catch(Exception e){
			    	 System.out.println("Error al consultar clave GCM: "+e.getMessage());
			     }
			     choferesConectados.add(chofer);
		     }
		     resultado.close();
		     consulta.close();
	    } catch (Exception e) {
	    	System.out.println("Error al consultar datos de choferes: "+e.getMessage());
		}
		ConexionBD.desconectar();
		return choferesConectados;
	}
	
	public static void actualizarEstado(Chofer chofer){
		try {
  			PreparedStatement statement = ConexionBD.getConnection().prepareStatement("UPDATE ChoferesConectados SET estado_movil = 'OCUPADO' WHERE usuario = '"+chofer.getUsuario()+"'");
  			int rowsUpdated = statement.executeUpdate();
  			if(rowsUpdated == 0){
  				//error
  			}
  			statement.close();
    
  		} catch(SQLException e) {
  			System.out.println("Error al actualizar estado de chofer: "+e.getMessage());
  		}
  		ConexionBD.desconectar();
	}
}