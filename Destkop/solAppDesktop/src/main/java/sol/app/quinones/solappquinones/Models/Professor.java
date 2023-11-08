package sol.app.quinones.solappquinones.Models;

import com.google.gson.Gson;

public class Professor extends Persona {

    private int idEmpleat;
    private String iniciContracte;
    private String finalContracte;
    private boolean actiu;

    public Professor(String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, int idEmpleat, String iniciContracte, String finalContracte, boolean actiu) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idEmpleat = idEmpleat;
        this.iniciContracte = iniciContracte;
        this.finalContracte = finalContracte;
        this.actiu = actiu;
    }

    public Professor(int idPersona, String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, int idEmpleat, String iniciContracte, String finalContracte, boolean actiu) {
        super(idPersona, nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idEmpleat = idEmpleat;
        this.iniciContracte = iniciContracte;
        this.finalContracte = finalContracte;
        this.actiu = actiu;
    }

    public Professor(int idEmpleat, String iniciContracte, String finalContracte, boolean actiu) {
        this.idEmpleat = idEmpleat;
        this.iniciContracte = iniciContracte;
        this.finalContracte = finalContracte;
        this.actiu = actiu;
    }

    public Professor() {
    }

    public int getIdEmpleat() {
        return idEmpleat;
    }

    public void setIdEmpleat(int idEmpleat) {
        this.idEmpleat = idEmpleat;
    }

    public String getIniciContracte() {
        return iniciContracte;
    }

    public void setIniciContracte(String iniciContracte) {
        this.iniciContracte = iniciContracte;
    }

    public String getFinalContracte() {
        return finalContracte;
    }

    public void setFinalContracte(String finalContracte) {
        this.finalContracte = finalContracte;
    }

    public boolean isActiu() {
        return actiu;
    }

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }

    /**
     * Converteix una cadena "JSON" a un objecte Professor
     * Static per no tindre que instanciar objectes, i cridar-lo directament
     * @param json cadena "JSON"
     * @return objecte Professor
     */
    public static Professor fromJson (String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Professor.class);
    }
}
