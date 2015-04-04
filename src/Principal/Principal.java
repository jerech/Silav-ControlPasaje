package Principal;

import java.util.ArrayList;

import DAO.PasajeDAO;
import DAO.ChoferDAO;
import VO.Pasaje;
import VO.Chofer;

public class Principal {

	private ArrayList<Pasaje> pasajes;
	private ArrayList<Chofer> choferes;
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Principal principal = new Principal();
		principal.iniciar();
	}

	private void iniciar(){

		pasajes = obtenerPasajes();
		if(pasajes.size() == 0){
			esperar(10);
		}
		else{
			choferes = obtenerChoferes();//VER FRECUENCIA DE CONSULTA
			while(pasajes.size() != 0){
				asignarPasaje(pasajes.get(0));
				pasajes.remove(0);
			}
		}
		iniciar();
	}
	
	private ArrayList<Pasaje> obtenerPasajes(){
		
		PasajeDAO pasajeDAO = new PasajeDAO();
		ArrayList<Pasaje> pasajesEnCurso = pasajeDAO.getPasajesEnCurso();
		return pasajesEnCurso;
	}

	private ArrayList<Chofer> obtenerChoferes(){
		
		ChoferDAO choferDAO = new ChoferDAO();
		ArrayList<Chofer> choferesConectados = choferDAO.getChoferesConectados();
		return choferesConectados;
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
					//ENVIAR PASAJE
				}
			}
		}
	}
}
