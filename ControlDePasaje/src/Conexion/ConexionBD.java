package Conexion;
import java.sql.*;
import java.util.Calendar;

/**
 * Clase que permite conectar con la base de datos
 *
 */
public class ConexionBD {
	/**Parametros de conexion*/
	private final static String user = "root";
	private final static String password = "Silav_2015";
	private final static String url = "jdbc:mysql://localhost/silav";
	 
	private static Connection connection = null;
	
	
	   /**Permite retornar la conexión*/
	   public static Connection getConnection() throws SQLException{
		   //System.out.println(Calendar.getInstance().getTime().toString()+".Se abre conexion, la url es: "+url);
		   try{
				//obtenemos el driver de para mysql
		         Class.forName("com.mysql.jdbc.Driver");
		         //obtenemos la conexión
		         connection = DriverManager.getConnection(url,user,password);
		      }
		      catch(Exception e){
		         System.err.println(e.getMessage());
		         e.printStackTrace();
		         throw new SQLException(); 
		      }
	      return connection;
	   }
	 
	   public static void desconectar(){
		  if(connection != null){
			  
		  
		      try {
				connection.close();
				
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		      connection = null;
		  }
	      
	      
	   }
	   
	   
}