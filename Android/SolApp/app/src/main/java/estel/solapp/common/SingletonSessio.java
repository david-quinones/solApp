package estel.solapp.common;

import estel.solapp.models.User;

public class SingletonSessio {

    //unica instancia de Singleton
    private static SingletonSessio instance;
    private String key;
    private User userConnectat;

    /**
     * Metode que retorna la unica instancia de SingletonConnection
     * @return instancia SingletonConnection
     */
    public static SingletonSessio getInstance(){
        if(instance == null) {instance = new SingletonSessio();}
        return instance;
    }

    private SingletonSessio(){}

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
    public void setUserConnectat(User usuari){
        this.userConnectat = usuari;
    }

    /**
     *
     * @return usuari connectat
     */
    public User getUserConnectat(){
        return this.userConnectat;
    }

    /**
     * Tanca la connexió (assigna valor null a la key, per poder se reassignada si es logina un nou usuari)
     */
    public void closeConnection(){
        instance = null;
    }
}
