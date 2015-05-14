package Principal;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import Conexion.ConexionBD;
import DAO.ChoferDAO;
import DAO.PasajeDAO;
import VO.Chofer;
import VO.Pasaje;

public class Principal{

	private ArrayList<Pasaje> pasajes;
	private ArrayList<Chofer> choferes;
	private ArrayList<Pasaje> pasajesAnteriores;
	private ArrayList<Chofer> choferesAnteriores;
	private PasajeDAO pasajeDAO;
	//private ChoferDAO choferDAO;
	private Observable temporizador;
	private final String apiKey = "AIzaSyBRy8ZJ8bpiqg9Dny05p24WsKCDGLQLYSs";
	
	public static void main (String [] args)
    {
        Temporizador temporizador = new Temporizador();
        Principal principal = new Principal();
        principal.inicializarVariables(temporizador);
        principal.observar();
    }
	
	public void inicializarVariables(Observable temporizador){
		this.pasajeDAO = new PasajeDAO();
		//this.choferDAO = new ChoferDAO();
		this.pasajesAnteriores = new ArrayList<Pasaje>();
		this.choferesAnteriores = new ArrayList<Chofer>();
		this.temporizador = temporizador;
    }
	
	public void observar(){
    	// Suscripción al cambio de fecha/hora en el modelo recibido.
    	temporizador.addObserver (new Observer()
        {
            public void update (Observable unObservable, Object dato)
            {
            	iniciar();
            }
        });
    }
	
private void iniciar(){
		
		pasajes = pasajeDAO.getPasajesEnCurso();
		if(pasajes.size() == 0){
			//System.out.println("No hay pasajes..");
		}
		else{
			//System.out.println("Hay pasajes pendientes..");
			choferes = ChoferDAO.getChoferesConectados();
			if(choferes.size() == 0){
				//System.out.println("No hay choferes conectados..");
			}
			else{
				
				if(listaDePasajesIguales(pasajes, pasajesAnteriores) && listaDeChoferesIguales(choferes, choferesAnteriores)){
					//System.out.println("No hay cambios en las listas de pasajes y choferes..");
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
	}
	
	private void asignarPasaje(Pasaje pasaje){
		boolean pasajeEnviado = false;
		for(int i=0 ; i<choferes.size() ; i++){
			if(pasaje.getChofer().equals(choferes.get(i).getUsuario())){
				//System.out.println("Asignando pasaje..");
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
			if(result.getErrorCodeName()!=null){
				//System.out.println("Se envio el pasaje correctamente");
			}else{
				System.out.println("No se envio el pasaje correctamente. "+result.getErrorCodeName());
			}
			
			envioExitoso = true;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR:"+e.getMessage());
			e.printStackTrace();
			envioExitoso = false;
		}
		
		return (envioExitoso);
	}
	
	private void actualizarEstadoPasaje(Pasaje pasaje){
		  
  		try {
  			PreparedStatement statement = ConexionBD.getConnection().prepareStatement("UPDATE PasajesEnCurso SET estado = 'en_espera' WHERE id = "+pasaje.getId());
  			int rowsUpdated = statement.executeUpdate();
  			statement.close();
    
  		} catch (SQLException e) {
  			System.out.println("Error al cambiar el estado del pasaje");
  		}
  		ConexionBD.desconectar();
	}

	private void actualizarEstadoChofer(Chofer chofer){
		  
  		try {
  			PreparedStatement statement = ConexionBD.getConnection().prepareStatement("UPDATE ChoferesConectados SET estado_movil = 'OCUPADO' WHERE usuario = '"+chofer.getUsuario()+"'");
  			int rowsUpdated = statement.executeUpdate();
  			statement.close();
    
  		} catch (SQLException e) {
  			System.out.println("Error al cambiar el estado del chofer");
  		}
  		ConexionBD.desconectar();
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
