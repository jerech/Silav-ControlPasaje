package Principal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Observable;
import java.util.Observer;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import DAO.ChoferDAO;
import DAO.PasajeDAO;
import Routing.Routing;
import VO.Chofer;
import VO.Pasaje;

public class Principal {

	private ArrayList<Pasaje> pasajes;
	private ArrayList<Chofer> choferes;
	private Observable temporizador;
	private final String apiKey = "AIzaSyBRy8ZJ8bpiqg9Dny05p24WsKCDGLQLYSs";
	
	public static void main (String [] args){
		Temporizador temporizador = new Temporizador();
        Principal principal = new Principal(temporizador);
        principal.observar();
    }
	
	public Principal(Observable temporizador){
		this.temporizador = temporizador;
	}
	
	public void observar(){
		// Suscripción al cambio en el modelo recibido.
    	temporizador.addObserver (new Observer()
        {
            public void update (Observable unObservable, Object dato)
            {
            	iniciar();
            }
        });
    }
	
	private void iniciar(){
		pasajes = PasajeDAO.getPasajesEnCurso();
		if(pasajes.size() > 0){
			choferes = ChoferDAO.getChoferesConectados();
			if(choferes.size() > 0){
				while(pasajes.size() != 0){
					if(asignacionAutomatica(pasajes.get(0)) == true){
						asignarPasajeAutomatico(pasajes.get(0));
					}
					else{
						asignarPasajeManual(pasajes.get(0));
					}
					pasajes.remove(0);
				}
			}
			else{
				//no hay choferes conectados
			}
		}
		else{
			//no hay pasajes
		}
	}
	
	private boolean asignacionAutomatica(Pasaje pasaje){
		boolean respuesta = false;
		if(pasaje.getChofer() == null){
			respuesta = true;
		}
		else{
			if(pasaje.getChofer().equals("")){
				respuesta = true;
			}
		}
		return respuesta;
	}
	
	private void asignarPasajeManual(Pasaje pasaje){
		boolean pasajeEnviado = false;
		for(int i=0 ; i<choferes.size() ; i++){
			if(pasaje.getChofer().equals(choferes.get(i).getUsuario())){
				pasajeEnviado = enviarPasaje(pasaje, choferes.get(i));
				if(pasajeEnviado == true){
					PasajeDAO.actualizarEstado(pasaje);
					ChoferDAO.actualizarEstado(choferes.get(i));
					choferes.remove(i);
				}
				else{

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
			if(result.getErrorCodeName() != null){
				//Se envió el pasaje correctamente
			}else{
				System.out.println("No se envio el pasaje correctamente. "+result.getErrorCodeName());
			}
			envioExitoso = true;
		} catch(IOException e) {
			System.out.println("Error IO al enviar pasaje: "+e.getMessage());
			//System.out.println("ERROR:"+e.getMessage());
			//e.printStackTrace();
			envioExitoso = false;
		}
		return (envioExitoso);
	}
	
	private void asignarPasajeAutomatico(Pasaje pasaje){
		Chofer chofer;
		boolean pasajeEnviado = false;
		calcularDistanciasLineales(pasaje);
		choferes = ordenarChoferes(choferes);
		chofer = obtenerChoferMasCercano(pasaje);
		if(chofer.getDistanciaADestino() >= 0){
			pasajeEnviado = enviarPasaje(pasaje, chofer);
			if(pasajeEnviado == true){
				PasajeDAO.actualizarEstado(pasaje);
				ChoferDAO.actualizarEstado(chofer);
				borrarChofer(chofer);
			}
			else{
				
			}
		}
		
	}
	
	private void calcularDistanciasLineales(Pasaje pasaje){
		double catetoLatitud;
		double catetoLongitud;
		double diagonal;
		for(int i=0 ; i<choferes.size() ; i++){
			catetoLatitud = pasaje.getLatitud() - choferes.get(i).getLatitud();
			catetoLongitud = pasaje.getLongitud() - choferes.get(i).getLongitud();
			diagonal = Math.sqrt(Math.pow(catetoLatitud,2) + Math.pow(catetoLongitud,2));
			choferes.get(i).setDistanciaADestino(diagonal);
		}
	}
	
	private ArrayList<Chofer> ordenarChoferes(ArrayList<Chofer> listaDeChoferes){
		Collections.sort(listaDeChoferes, new Comparator<Chofer>(){
	        @Override
	        public int compare(Chofer  chofer1, Chofer  chofer2){
	            return  Double.compare(chofer1.getDistanciaADestino(), chofer2.getDistanciaADestino());
	        }
	    });
		return listaDeChoferes;
	}
	
	private Chofer obtenerChoferMasCercano(Pasaje pasaje){
		//tomar los 5 primeros de la lista y calcular ruta real.
		int limiteDeCalculo = 5;
		int i = 0;
		int limite = 0;
		double distancia;
		Chofer choferCercano;
		ArrayList<Chofer> choferesCercanos = new ArrayList<Chofer>();
		while(limite <= limiteDeCalculo && i < choferes.size()){
			distancia = Routing.getDistancia(choferes.get(i), pasaje);
			if(distancia >= 0){
				choferes.get(i).setDistanciaADestino(distancia);
				choferesCercanos.add(choferes.get(i));
				limite++;
			}
			i++;
		}
		if (choferesCercanos.size() > 1){
			choferesCercanos = ordenarChoferes(choferesCercanos);
			choferCercano = choferesCercanos.get(0);
		}
		else{
			if (choferesCercanos.size() > 0){
				choferCercano = choferesCercanos.get(0);
			}
			else {
				choferCercano = new Chofer();
				choferCercano.setDistanciaADestino(-1);
			}
		}
		return choferCercano;
	}
	
	private void borrarChofer(Chofer chofer){
		for(int i=0 ; i<choferes.size() ; i++){
			if(choferes.get(i).getUsuario().equals(chofer.getUsuario())){
				choferes.remove(i);
			}
		}
	}
}