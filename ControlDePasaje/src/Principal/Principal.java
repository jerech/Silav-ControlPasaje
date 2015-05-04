package Principal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import java.sql.PreparedStatement;
import java.sql.SQLException;


import Conexion.ConexionBD;
import DAO.PasajeDAO;
import DAO.ChoferDAO;
import VO.Pasaje;
import VO.Chofer;

public class Principal {

	private ArrayList<Pasaje> pasajes;
	private ArrayList<Chofer> choferes;
	private ArrayList<Pasaje> pasajesAnteriores;
	private ArrayList<Chofer> choferesAnteriores;
	private PasajeDAO pasajeDAO;
	private ChoferDAO choferDAO;
	private ConexionBD conexion;
	private final String apiKey = "AIzaSyBRy8ZJ8bpiqg9Dny05p24WsKCDGLQLYSs";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Principal principal = new Principal();
		principal.conexion = new ConexionBD();
		principal.pasajeDAO = new PasajeDAO(principal.conexion);
		principal.choferDAO = new ChoferDAO(principal.conexion);
		principal.pasajesAnteriores = new ArrayList<Pasaje>();
		principal.choferesAnteriores = new ArrayList<Chofer>();
		principal.iniciar();
	}

	private void iniciar(){
		if(conexion.sinConexion()){
			System.out.println(Calendar.getInstance().getTime().toString()+"Se instancia una nueva conexion DB");
			conexion = new ConexionBD();
			pasajeDAO.setConexion(conexion);
			choferDAO.setConexion(conexion);
		}
		pasajes = pasajeDAO.getPasajesEnCurso();
		if(pasajes.size() == 0){
			System.out.println("No hay pasajes..");
			esperar(10);
		}
		else{
			System.out.println("Hay pasajes pendientes..");
			choferes = choferDAO.getChoferesConectados();
			if(choferes.size() == 0){
				System.out.println("No hay choferes conectados..");
				esperar(10);
			}
			else{
				
				if(listaDePasajesIguales(pasajes, pasajesAnteriores) && listaDeChoferesIguales(choferes, choferesAnteriores)){
					System.out.println("No hay cambios en las listas de pasajes y choferes..");
					esperar(10);
				}
				else{
					pasajesAnteriores.clear();
					copiarListaPasajes(pasajes, pasajesAnteriores);
					choferesAnteriores.clear();
					copiarListaChoferes(choferes, choferesAnteriores);
					
					while(pasajes.size() != 0){
						
						asignarPasaje(pasajes.get(0));
						pasajes.remove(0);
					}
				}
			}
			
		}
		iniciar();
	}
	
	private void esperar (int segundos) {
		try {
			Thread.sleep (segundos*1000);
		} catch (Exception e) {
		// Mensaje en caso de que falle
		}
	}
	
	private void asignarPasaje(Pasaje pasaje){
		boolean pasajeEnviado = false;
		for(int i=0 ; i<choferes.size() ; i++){
			if(pasaje.getChofer().equals(choferes.get(i).getUsuario())){
				System.out.println("Asignando pasaje..");
				pasajeEnviado = enviarPasaje(pasaje, choferes.get(i));
				if(pasajeEnviado == true){
					actualizarEstadoPasaje(pasaje);
					actualizarEstadoChofer(choferes.get(i));
				}
				else{
					System.out.println("Problemas al enviar el pasaje..");
				}
			}
		}
	}
	
	private boolean enviarPasaje(Pasaje pasaje, Chofer chofer){
		boolean envioExitoso = false;
		
		Sender sender = new Sender(apiKey);
		Message message = new Message.Builder()
		    .addData("direccion", pasaje.getDireccion())
		    .addData("cliente", pasaje.getCliente())
		    .addData("id", pasaje.getId())
		    .addData("fecha", pasaje.getFecha())
		    .build();
		try {
			Result result = sender.sendNoRetry(message, chofer.getClaveGCM());
			if(result.getErrorCodeName().length() == 0){
				System.out.println("Se envio el pasaje correctamente");
			}else{
				System.out.println("Error al enviar pasaje. "+result.getErrorCodeName());
			}
			
			envioExitoso = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR:"+e);
			e.printStackTrace();
			envioExitoso = false;
		}
		
		envioExitoso = true;
		return (envioExitoso);
	}
	
	private void actualizarEstadoPasaje(Pasaje pasaje){
		  
  		try {
  			PreparedStatement statement = conexion.getConnection().prepareStatement("UPDATE PasajesEnCurso SET estado = 'en_espera' WHERE id = "+pasaje.getId());
  			int rowsUpdated = statement.executeUpdate();
  			statement.close();
    
  		} catch (SQLException e) {
  			System.out.println("Error al cambiar el estado del pasaje");
  		}
	}

	private void actualizarEstadoChofer(Chofer chofer){
		  
  		try {
  			PreparedStatement statement = conexion.getConnection().prepareStatement("UPDATE ChoferesConectados SET estado_movil = 'OCUPADO' WHERE usuario = '"+chofer.getUsuario()+"'");
  			int rowsUpdated = statement.executeUpdate();
  			statement.close();
    
  		} catch (SQLException e) {
  			System.out.println("Error al cambiar el estado del chofer");
  		}
	}
	
	private boolean listaDePasajesIguales(ArrayList<Pasaje> lista1, ArrayList<Pasaje> lista2){
		//compara dos arraylist de IGUAL longitud
		boolean resultado = true;

		if(lista1.size() == lista2.size()){
			for(int i=0 ; i<lista1.size() ; i++){
				if(!lista1.get(i).getId().equals(lista2.get(i).getId())){
					resultado = false;
				}
			}
		}
		else{
			resultado = false;
		}
		return (resultado);
	}
	
	private boolean listaDeChoferesIguales(ArrayList<Chofer> lista1, ArrayList<Chofer> lista2){
		//compara dos arraylist de IGUAL longitud
		boolean resultado = true;

		if(lista1.size() == lista2.size()){
			for(int i=0 ; i<lista1.size() ; i++){
				if(!lista1.get(i).getUsuario().equals(lista2.get(i).getUsuario())){
					System.out.println(lista1.get(i).getUsuario()+"::"+lista2.get(i).getUsuario());
					resultado = false;
				}
			}
		}
		else{
			resultado = false;
		}
		return (resultado);
	}
	
	private ArrayList<Pasaje> copiarListaPasajes(ArrayList<Pasaje> original, ArrayList<Pasaje> copia){
		for(int i=0 ; i< original.size() ; i++){
			copia.add(original.get(i));
		}
		return (copia);
	}
	
	private ArrayList<Chofer> copiarListaChoferes(ArrayList<Chofer> original, ArrayList<Chofer> copia){
		for(int i=0 ; i< original.size() ; i++){
			copia.add(original.get(i));
		}
		return (copia);
	}
}
