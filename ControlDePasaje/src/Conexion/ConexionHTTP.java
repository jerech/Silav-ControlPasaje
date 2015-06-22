package Conexion;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import VO.Chofer;
import VO.Pasaje;

public class ConexionHTTP {
	
	private final static String urlProjectOSRM = "http://router.project-osrm.org/viaroute?";
	
	public static String obtenerDatosDeRuta(Chofer chofer, Pasaje pasaje){
		String respuesta = "";
		String destino = "loc=" + pasaje.getLatitud() + "," + pasaje.getLongitud();
		String ubicacionChofer = "loc=" + chofer.getLatitud() + "," + chofer.getLongitud();
		
		String url = urlProjectOSRM + ubicacionChofer + "&" + destino;
		CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpGet httpget = new HttpGet(url);
		try{
			CloseableHttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			if (entity != null) {
				respuesta = EntityUtils.toString(entity);
            }
		}catch(ClientProtocolException e){
			System.out.println("Error de protocolo: "+e.getMessage());
		}catch (IOException e) {
			System.out.println("Error IO en la conexion HTTP: "+e.getMessage());
		}
		return respuesta;
	}
}
