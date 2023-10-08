package seguretat;

import entitats.Usuari;
import java.util.HashMap;
import java.util.UUID;

/**
 *Classe que gestiona les sesions actives a l'aplicació
 * @author pau
 */
public class GestorSessions {
    private static GestorSessions instancia = null;
    private HashMap<Integer,String> sesionsActives = new HashMap<>();
    
    
    
    /**Métode per obtindre una instancia única del gestor de sessions
     * 
     * @return la instancia única del GestorSessions
     */
    public static synchronized GestorSessions obtindreInstancia(){
        if(instancia == null){
            instancia = new GestorSessions();
        }
        //Retornem la instancia única del GestorSessions
        return instancia;
    }
    
       
    /**Agrega sessió activa al gestor
     * 
     * @param usuari que ha iniciat la sessió
     */
    public void agregarSessio(Usuari usuari){
        //Generem un número únic i aleatori de sessió
        String numeroSessio = UUID.randomUUID().toString();
        //Afegim el número de sessió al HashMap de sessions actives
        sesionsActives.put(usuari.getId(),numeroSessio );
    }
    
    
    /**Métode que retorna el número de sessió a partir del id d'un usuari
     * 
     * @param idUsuari identificador del usuari conectat
     * @return el número de sessió
     */
    public String retornaSessio(int idUsuari){
        return sesionsActives.get(idUsuari);
    }
    
    
    /**Verifica si una sessió está activa
     * 
     * @param numSessio que s'ha de verificar
     * @return 
     */
    public boolean verificarSessio(int idUsuari){
        return sesionsActives.containsKey(idUsuari);
    }
    
    
    /**Eliminar una sessió del GestorSessions
     * 
     * @param numSessio que cal eliminar
     */
    public void eliminarSessio(int idUsuari){
        sesionsActives.remove(idUsuari);
    }
}
