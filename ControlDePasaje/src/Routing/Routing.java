package Routing;

import java.util.ArrayList;

import Conexion.ConexionHTTP;
import VO.Chofer;
import VO.Pasaje;

public class Routing {
	
	public static double getDistancia(Chofer chofer, Pasaje pasaje){
		double respuesta = -1;
		String respuestaJson;
		ArrayList<String> informacionDeRuta;
		respuestaJson = ConexionHTTP.obtenerDatosDeRuta(chofer, pasaje);
		if (!respuestaJson.equals("")){
			informacionDeRuta = JsonParser.getResumenDeRuta(respuestaJson);
			if(informacionDeRuta.size() > 0){
				if(informacionDeRuta.get(0).equals("Found route between points")){
					respuesta = Double.parseDouble(informacionDeRuta.get(3));
				}
			}
		}
		else{
			
		}
		return respuesta;
	}
}
