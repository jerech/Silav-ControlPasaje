package DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import Conexion.ConexionBD;
import VO.Chofer;

/**
 * Clase que permite el acceso a la base de datos
 *
 */
public class ChoferDAO {
	
	public ChoferDAO(){
		
	}
	
	public static ArrayList<Chofer> getChoferesConectados() {

		  ArrayList<Chofer> choferesConectados = new ArrayList<Chofer>();
		     
		  try {
		   PreparedStatement consulta = ConexionBD.getConnection().prepareStatement("SELECT * FROM ChoferesConectados WHERE estado_movil = 'LIBRE'");
		   ResultSet resultado = consulta.executeQuery();
		   while(resultado.next()){
			   Chofer chofer = new Chofer();
			   chofer.setUsuario(resultado.getString("usuario"));
			   chofer.setLongitud(resultado.getString("ubicacion_lon"));
			   chofer.setLatitud(resultado.getString("ubicacion_lat"));
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
					   System.out.println("No existe la clave GCM");
				   }
			   } catch(Exception e){
				   System.out.println("no se pudo consultar la clave GCM\n"+e);
			   }
			   
			   choferesConectados.add(chofer);
		   }
		   resultado.close();
		   consulta.close();
		   
		  } catch (Exception e) {
		   System.out.println("no se pudo consultar el chofer\n"+e);
		  }
		  ConexionBD.desconectar();
		  return choferesConectados;
		 }

	
	
}