package Principal;

import java.io.IOException;
import java.util.ArrayList;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.Result;
import com.google.android.gcm.server.Sender;

import DAO.PasajeDAO;
import DAO.ChoferDAO;
import VO.Pasaje;
import VO.Chofer;

public class Principal {

	private ArrayList<Pasaje> pasajes;
	private ArrayList<Chofer> choferes;
	private PasajeDAO pasajeDAO;
	private ChoferDAO choferDAO;
	private final String apiKey = "AIzaSyBRy8ZJ8bpiqg9Dny05p24WsKCDGLQLYSs";
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Principal principal = new Principal();
		principal.pasajeDAO = new PasajeDAO();
		principal.choferDAO = new ChoferDAO();
		principal.iniciar();
	}

	private void iniciar(){

		pasajes = pasajeDAO.getPasajesEnCurso();
		if(pasajes.size() == 0){
			esperar(10);
		}
		else{
			choferes = choferDAO.getChoferesConectados();
			while(pasajes.size() != 0){
				asignarPasaje(pasajes.get(0));
				pasajes.remove(0);
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
		if(choferes.size() == 0){
			System.out.println("No hay choferes conectados");
		}
		else{
			for(int i=0 ; i<choferes.size() ; i++){
				if(pasaje.getNumeroDeMovil() == choferes.get(i).getNumeroDeMovil()){
					enviarPasaje(pasaje, choferes.get(i));
				}

			}
		}
	}
	
	private void enviarPasaje(Pasaje pasaje, Chofer chofer){
		Sender sender = new Sender(apiKey);
		Message message = new Message.Builder()
		    .addData("direccion", pasaje.getDireccion())
		    .addData("cliente", pasaje.getCliente())
		    .addData("id", pasaje.getId())
		    .build();
		try {
			Result result = sender.sendNoRetry(message, chofer.getClaveGCM());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("ERROR:"+e);
			e.printStackTrace();
		}
	}
}
