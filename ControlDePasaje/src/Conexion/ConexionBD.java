package Conexion;
import java.sql.*;
import java.util.Calendar;

/**
 * Clase que permite conectar con la base de datos
 *
 */
public class ConexionBD {
	/**Parametros de conexion*/
	private static String bd = "silav";
	private static String login = "root";
	private static String password = "Silav_2015";
	private static String url = "jdbc:mysql://localhost/"+bd;
	 
	private static Connection connection = null;
	
	
	   /**Permite retornar la conexión*/
	   public static Connection getConnection(){
		   try{
				//obtenemos el driver de para mysql
		         Class.forName("com.mysql.jdbc.Driver");
		         //obtenemos la conexión
		         connection = DriverManager.getConnection(url,login,password);
		      }
		      catch(SQLException e){
		    	  System.out.println("El error se produjo el: "+Calendar.getInstance().getTime().toString());
		         System.out.println(e.getMessage());
		      }catch(ClassNotFoundException e){
		    	  System.out.println("El error se produjo el: "+Calendar.getInstance().getTime().toString());
		         System.out.println(e.getMessage());
		      }catch(Exception e){
		    	  System.out.println("El error se produjo el: "+Calendar.getInstance().getTime().toString());
		         System.out.println(e.getMessage());
		      }
	      return connection;
	   }
	 
	   public static void desconectar(){
	      try {
			connection.close();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	      
	      connection = null;
	   }
	   
	   
}