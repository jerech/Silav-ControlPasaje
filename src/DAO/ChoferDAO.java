package DAO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import Conexion.ConexionBD;
import VO.Chofer;

/**
 * Clase que permite el acceso a la base de datos
 *
 */
public class ChoferDAO {

	public ArrayList<Chofer> getChoferesConectados() {
		
		  ArrayList<Chofer> choferesConectados = new ArrayList<Chofer>();
		  ConexionBD conexion = new ConexionBD();
		     
		  try {
		   PreparedStatement consulta = conexion.getConnection().prepareStatement("SELECT * FROM ChoferesConectados");
		   ResultSet resultado = consulta.executeQuery();
		   while(resultado.next()){
			   Chofer chofer = new Chofer();
			   chofer.setUsuario(resultado.getString("usuario"));
			   chofer.setLongitud(resultado.getString("longitud"));
			   chofer.setLatitud(resultado.getString("latitud"));
			   chofer.setNumeroDeMovil(Integer.parseInt(resultado.getString("numero_movil")));
			   choferesConectados.add(chofer);
		   }
		   resultado.close();
		   consulta.close();
		   conexion.desconectar();
		   
		  } catch (Exception e) {
		   JOptionPane.showMessageDialog(null, "no se pudo consultar el chofer\n"+e);
		  }
		  return choferesConectados;
		 }
}
