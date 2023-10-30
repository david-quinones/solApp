package sol.app.quinones.solappquinones.Service;

import sol.app.quinones.solappquinones.Models.Usuari;

/**
 * Classe que implementa el patro Singleton per tindre unica instancia
 * Guarda la clau i usuaris que s'ha connectat durant la vida util de l'aplicació
 *
 * @author david
 */
public class SingletonConnection {

    //unica instancia de Singleton
    private static SingletonConnection instance;
    private String key;
    private Usuari userConnectat;

    /**
     * Metode que retorna la unica instancia de SingletonConnection
     * @return instancia SingletonConnection
     */
    public static SingletonConnection getInstance(){
        if(instance == null) {instance = new SingletonConnection();}
        return instance;
    }

    private SingletonConnection(){}

    /**
     * Estableix la clau de connexió
     * @param key clau que retorna el servidor
     * @throws UnsupportedOperationException si s'intenta reassignar la clau, quan te valor
     */
    public void setKey(String key){
        if(this.key == null){
            this.key = key;
        }else{
            throw new UnsupportedOperationException("No es possible reassignar-lo");
        }

    }

    /**
     *
     * @return la clau
     */
    public String getKey(){
        return this.key;
    }

    /**
     * Assigna usuari connectat
     * @param usuari
     */
    public void setUserConnectat(Usuari usuari){
        this.userConnectat = usuari;
    }

    /**
     *
     * @return usuari connectat
     */
    public Usuari getUserConnectat(){
        return this.userConnectat;
    }

    /**
     * Tanca la connexió (assigna valor null a la key, per poder se reassignada si es logina un nou usuari)
     */
    public void closeConnection(){
        instance = null;
    }

}
