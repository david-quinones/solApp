package sol.app.quinones.solappquinones.Models;

import java.util.List;

public class JsonPeticio {

    private String peticio;
    private List<?> dades;

    public JsonPeticio() {

    }
    public JsonPeticio(String peticio) {this.peticio = peticio;}

    public String getPeticio() {
        return peticio;
    }

    public void setPeticio(String peticio) {
        this.peticio = peticio;
    }

    public List<?> getDades() {
        return dades;
    }

    public void setDades(List<?> dades) {
        this.dades = dades;
    }
}
