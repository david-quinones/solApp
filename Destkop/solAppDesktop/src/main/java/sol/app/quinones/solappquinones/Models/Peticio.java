package sol.app.quinones.solappquinones.Models;

import java.util.ArrayList;
import java.util.List;

public class Peticio {

    private String peticio;
    private ArrayList<String> dades = new ArrayList<>();

    public Peticio() {

    }
    public Peticio(String peticio) {this.peticio = peticio;}

    public String getPeticio() {
        return peticio;
    }

    public void setPeticio(String peticio) {
        this.peticio = peticio;
    }

    public ArrayList<String> getDades() {
        return dades;
    }

    public void addDades(String dades){
        this.dades.add(dades);
    }


}
