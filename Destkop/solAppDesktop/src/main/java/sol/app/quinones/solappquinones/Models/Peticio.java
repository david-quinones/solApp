package sol.app.quinones.solappquinones.Models;

import java.util.ArrayList;
import java.util.List;

/**
 * The type Peticio.
 */
public class Peticio {

    private String peticio;
    private ArrayList<String> dades = new ArrayList<>();

    /**
     * Instantiates a new Peticio.
     */
    public Peticio() {

    }

    /**
     * Instantiates a new Peticio.
     *
     * @param peticio the peticio
     */
    public Peticio(String peticio) {this.peticio = peticio;}

    /**
     * Gets peticio.
     *
     * @return the peticio
     */
    public String getPeticio() {
        return peticio;
    }

    /**
     * Sets peticio.
     *
     * @param peticio the peticio
     */
    public void setPeticio(String peticio) {
        this.peticio = peticio;
    }

    /**
     * Gets dades.
     *
     * @return the dades
     */
    public ArrayList<String> getDades() {
        return dades;
    }

    /**
     * Add dades.
     *
     * @param dades the dades
     */
    public void addDades(String dades){
        this.dades.add(dades);
    }

    /**
     * Drop dades.
     */
    public void dropDades(){
        this.dades.clear();
    }


}
