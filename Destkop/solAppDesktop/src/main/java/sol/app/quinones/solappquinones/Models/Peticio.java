package sol.app.quinones.solappquinones.Models;

import java.util.List;

public class Peticio {

    private String peticio;
    private List<String> dades;

    public Peticio() {

    }
    public Peticio(String peticio) {this.peticio = peticio;}

    public String getPeticio() {
        return peticio;
    }

    public void setPeticio(String peticio) {
        this.peticio = peticio;
    }

    public List<String> getDades() {
        return dades;
    }

    public void setDades(List<String> dades) {
        this.dades = dades;
    }
}
