package sol.app.quinones.solappquinones.Models;

import com.google.gson.Gson;

/**
 * Classe model que representa un professor
 * Extend de Persona incloent els atribut especifics
 *
 * @author david
 */
public class Professor extends Persona {

    private int idEmpleat;
    private String iniciContracte;
    private String finalContracte;
    private boolean actiu;

    /**
     * Instantiates a new Professor.
     *
     * @param nom            the nom
     * @param cognom1        the cognom 1
     * @param cognom2        the cognom 2
     * @param data_naixement the data naixement
     * @param dni            the dni
     * @param telefon        the telefon
     * @param mail           the mail
     * @param idEmpleat      the id empleat
     * @param iniciContracte the inici contracte
     * @param finalContracte the final contracte
     * @param actiu          the actiu
     */
    public Professor(String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, int idEmpleat, String iniciContracte, String finalContracte, boolean actiu) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idEmpleat = idEmpleat;
        this.iniciContracte = iniciContracte;
        this.finalContracte = finalContracte;
        this.actiu = actiu;
    }

    /**
     * Instantiates a new Professor.
     *
     * @param idPersona      the id persona
     * @param nom            the nom
     * @param cognom1        the cognom 1
     * @param cognom2        the cognom 2
     * @param data_naixement the data naixement
     * @param dni            the dni
     * @param telefon        the telefon
     * @param mail           the mail
     * @param iniciContracte the inici contracte
     * @param finalContracte the final contracte
     * @param actiu          the actiu
     */
    public Professor(int idPersona, String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, String iniciContracte, String finalContracte, boolean actiu) {
        super(idPersona, nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.iniciContracte = iniciContracte;
        this.finalContracte = finalContracte;
        this.actiu = actiu;
    }

    /**
     * Instantiates a new Professor.
     *
     * @param idPersona      the id persona
     * @param nom            the nom
     * @param cognom1        the cognom 1
     * @param cognom2        the cognom 2
     * @param data_naixement the data naixement
     * @param dni            the dni
     * @param telefon        the telefon
     * @param mail           the mail
     * @param idEmpleat      the id empleat
     * @param iniciContracte the inici contracte
     * @param finalContracte the final contracte
     * @param actiu          the actiu
     */
    public Professor(int idPersona, String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, int idEmpleat, String iniciContracte, String finalContracte, boolean actiu) {
        super(idPersona, nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idEmpleat = idEmpleat;
        this.iniciContracte = iniciContracte;
        this.finalContracte = finalContracte;
        this.actiu = actiu;
    }

    /**
     * Instantiates a new Professor.
     *
     * @param nom            the nom
     * @param cognom1        the cognom 1
     * @param cognom2        the cognom 2
     * @param data_naixement the data naixement
     * @param dni            the dni
     * @param telefon        the telefon
     * @param mail           the mail
     * @param iniciContracte the inici contracte
     * @param finalContracte the final contracte
     * @param actiu          the actiu
     */
    public Professor(String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, String iniciContracte, String finalContracte, boolean actiu) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.iniciContracte = iniciContracte;
        this.finalContracte = finalContracte;
        this.actiu = actiu;
    }

    /**
     * Instantiates a new Professor.
     *
     * @param idEmpleat      the id empleat
     * @param iniciContracte the inici contracte
     * @param finalContracte the final contracte
     * @param actiu          the actiu
     */
    public Professor(int idEmpleat, String iniciContracte, String finalContracte, boolean actiu) {
        this.idEmpleat = idEmpleat;
        this.iniciContracte = iniciContracte;
        this.finalContracte = finalContracte;
        this.actiu = actiu;
    }

    /**
     * Instantiates a new Professor.
     */
    public Professor() {
    }

    /**
     * Gets id empleat.
     *
     * @return the id empleat
     */
    public int getIdEmpleat() {
        return idEmpleat;
    }

    /**
     * Sets id empleat.
     *
     * @param idEmpleat the id empleat
     */
    public void setIdEmpleat(int idEmpleat) {
        this.idEmpleat = idEmpleat;
    }

    /**
     * Gets inici contracte.
     *
     * @return the inici contracte
     */
    public String getIniciContracte() {
        return iniciContracte;
    }

    /**
     * Sets inici contracte.
     *
     * @param iniciContracte the inici contracte
     */
    public void setIniciContracte(String iniciContracte) {
        this.iniciContracte = iniciContracte;
    }

    /**
     * Gets final contracte.
     *
     * @return the final contracte
     */
    public String getFinalContracte() {
        return finalContracte;
    }

    /**
     * Sets final contracte.
     *
     * @param finalContracte the final contracte
     */
    public void setFinalContracte(String finalContracte) {
        this.finalContracte = finalContracte;
    }

    /**
     * Is actiu boolean.
     *
     * @return the boolean
     */
    public boolean isActiu() {
        return actiu;
    }

    /**
     * Sets actiu.
     *
     * @param actiu the actiu
     */
    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }

    /**
     * Converteix una cadena "JSON" a un objecte Professor
     * Static per no tindre que instanciar objectes, i cridar-lo directament
     *
     * @param json cadena "JSON"
     * @return objecte Professor
     */
    public static Professor fromJson (String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Professor.class);
    }
}
