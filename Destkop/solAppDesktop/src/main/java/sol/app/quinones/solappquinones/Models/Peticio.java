package sol.app.quinones.solappquinones.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe que representa una petició amb un tipus especifc i dades associades
 *
 * @author david
 */
public class Peticio {

    /* Tipus de la petició, exemple "LOGIN", "PERFIL", "LOGOUT" */
    private String peticio;
    /*Llista de dades o parametres associats a la petició*/
    private ArrayList<String> dades = new ArrayList<>();

    /**
     * Contrucctor per defecte
     */
    public Peticio() {

    }

    /**
     * Contructor que estableix el tipus de petició
     *
     * @param peticio tipus petició
     */
    public Peticio(String peticio) {this.peticio = peticio;}

    /**
     * Retorna la llista de dades associades a la solicitud
     *
     * @return el tipus de petició
     */
    public String getPeticio() {
        return peticio;
    }

    /**
     * Estableix tipus petició
     *
     * @param peticio tipus petició
     */
    public void setPeticio(String peticio) {
        this.peticio = peticio;
    }

    /**
     * Retorna la llista de dades associades a la solicitud
     *
     * @return Llista dades
     */
    public ArrayList<String> getDades() {
        return dades;
    }

    /**
     * Afagueix un nou element a la llista
     *
     * @param dades Dada afegida
     */
    public void addDades(String dades){
        this.dades.add(dades);
    }

    /**
     * Neteja la llista
     */
    public void dropDades(){
        this.dades.clear();
    }


}
