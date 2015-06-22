package Routing;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {

	public static ArrayList<String> getResumenDeRuta(String respuestaJson) {
        JSONObject raiz;
        JSONObject hojaResumenDeRuta;
        String status;
        ArrayList<String> informacionDeRuta = new ArrayList<String>();
        try {
            raiz = new JSONObject(respuestaJson);
            status = raiz.getString("status_message");
            
            informacionDeRuta.add(status);
            if(status.equals("Found route between points")){
            	hojaResumenDeRuta = raiz.getJSONObject("route_summary");
            	
            	informacionDeRuta.add(hojaResumenDeRuta.getString("end_point"));
                informacionDeRuta.add(hojaResumenDeRuta.getString("start_point"));
                informacionDeRuta.add(hojaResumenDeRuta.getString("total_time"));
                informacionDeRuta.add(hojaResumenDeRuta.getString("total_distance"));
            }
        } catch(JSONException e) {
        	System.out.println("Error al parsear Json: "+e.getMessage());
        }
        return informacionDeRuta;
    }
}
