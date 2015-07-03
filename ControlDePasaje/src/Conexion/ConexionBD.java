package Conexion;
import java.sql.*;

/**
 * Clase que permite conectar con la base de datos
 *
 */
public class ConexionBD {
	/**Parametros de conexion*/
	private final static String user = "root";
	private final static String password = "Silav_2015";
	private final static String url = "jdbc:mysql://localhost/silav?characterEncoding=utf8";
	 
	private static Connection connection = null;
		
	/**Permite retornar la conexión*/
	public static Connection getConnection() throws SQLException{
		try{
			//se obtiene el driver para mysql
		    Class.forName("com.mysql.jdbc.Driver");
		    //se obtiene la conexión
		    connection = DriverManager.getConnection(url,user,password);
		} catch(Exception e){
			System.out.println("Error al conectar con la base de datos: "+e.getMessage());
			//System.err.println(e.getMessage());
		    //e.printStackTrace();
		    throw new SQLException(); 
		}
	    return connection;
	}
	 
	public static void desconectar(){
		if(connection != null){  
			try {
				connection.close();
				} catch(SQLException e) {
					System.out.println("Error al desconectar la base de datos: "+e.getMessage());
					//e.printStackTrace();
				}
			connection = null;
		}   
	}   
}