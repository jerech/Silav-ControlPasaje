package Principal;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Observable;

public class Temporizador extends Observable{
	/**
      * Lanza un timer cada segundo.
      */
    public Temporizador(){
    	Timer timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 6000);
    }
     
    /**
      * Clase que se mete en Timer, para que se le avise cada segundo.
      */
    TimerTask timerTask = new TimerTask(){
    
    	/**
    	 * Método al que Timer llamará cada segundo. Se encarga de avisar
    	 * a los observadores de este modelo.
    	 */
    	public void run() {
    		setChanged();
    		notifyObservers();
    	}
    };
}