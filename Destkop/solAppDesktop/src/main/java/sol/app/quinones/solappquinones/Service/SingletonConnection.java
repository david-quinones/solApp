package sol.app.quinones.solappquinones.Service;

import sol.app.quinones.solappquinones.Models.Usuari;

public class SingletonConnection {

    private static SingletonConnection instance;
    private String key;
    private Usuari userConnectat;

    public static SingletonConnection getInstance(){
        if(instance == null) {instance = new SingletonConnection();}
        return instance;
    }

    public void setKey(String key){
        if(this.key == null){
            this.key = key;
        }else{
            throw new UnsupportedOperationException("No es possible reassignar-lo");
        }

    }

    public String getKey(){
        return this.key;
    }

    public void setUserConnectat(Usuari usuari){
        this.userConnectat = usuari;
    }

    public Usuari getUserConnectat(){
        return this.userConnectat;
    }

    public void closeConnection(){
        instance = null;
    }

}
