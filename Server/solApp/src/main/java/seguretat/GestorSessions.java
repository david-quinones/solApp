package seguretat;

import entitats.Usuari;
import java.util.HashMap;
import java.util.UUID;

/**
 *Classe que gestiona les sesions actives a l'aplicació
 * @author Pau Castell Galtes
 */
public class GestorSessions {
    private static GestorSessions instancia = null;
    private HashMap<String,Integer> sesionsActives = new HashMap<>();
    
    
    
    /**Métode per obtindre una instancia única del gestor de sessions
     * 
     * @return la instancia única del GestorSessions
     */
    public static synchronized GestorSessions obtindreInstancia(){
        if(instancia == null){
            instancia = new GestorSessions();
            //NOMES PER FER PROVES
            Usuari usuari = new Usuari(2, "Pau", "hola", true, false);
            instancia.agregarSessio(usuari, "sessioProves");
        }
        //Retornem la instancia única del GestorSessions
        return instancia;
    }
    
       
    /**Agrega sessió activa al gestor
     * 
     * @param usuari que ha iniciat la sessió
     * @param numSessio número de sessió
     */
    public void agregarSessio(Usuari usuari, String numSessio){

        //Afegim el número de sessió al HashMap de sessions actives
        sesionsActives.put(numSessio,usuari.getId());
    }
    

    /**Verifica si una sessió está activa
     * 
     * @param numSessio que s'ha de verificar
     * @return 
     */
    public boolean verificarSessio(String numSessio){
        return sesionsActives.containsKey(numSessio);
    }
    
    
    /**Eliminar una sessió del GestorSessions
     * 
     * @param numSessio que cal eliminar
     */
    public void eliminarSessio(String numSessio){
        sesionsActives.remove(numSessio);
    }
    
    /**Métode que retorna el valor del Hash Map a partir de la clau. A partir del
     * número de sessió retorna el id de l'usuari connectat
     * 
     * @param numSessio
     * @return 
     */
    public int idUsuariConectat(String numSessio){
        int idUsuari = 0;
        if(sesionsActives.containsKey(numSessio)){
            return idUsuari = sesionsActives.get(numSessio);
        }else{
            return idUsuari;
        }
    }
}
