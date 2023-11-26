package sol.app.quinones.solappquinones.Models;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Aula {

    private int id;
    private String nomAula;
    private Professor empleat;
    private List<Alumne> alumnes = new ArrayList<>();

    public Aula(){}

    public Aula(int id, String nomAula, Professor professor, List<Alumne> alumnes){
        this.id = id;
        this.nomAula = nomAula;
        this.empleat = professor;
        this.alumnes = new ArrayList<>(alumnes);
    }

    public Aula(int id, String nomAula, Professor professor) {
        this.id = id;
        this.nomAula = nomAula;
        this.empleat = professor;
    }

    public Aula(String nomAula){
        this.nomAula = nomAula;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomAula() {
        return nomAula;
    }

    public void setNomAula(String nomAula) {
        this.nomAula = nomAula;
    }

    public Professor getEmpleat() {
        return empleat;
    }

    public void setEmpleat(Professor empleat) {
        this.empleat = empleat;
    }

    public List<Alumne> getAlumnes() {
        return alumnes;
    }

    public void setAlumnes(List<Alumne> alumnes) {
        this.alumnes = new ArrayList<>(alumnes);
    }

    /**
     * Converteix una cadena "JSON" a un objecte Aula
     * Static per no tindre que instanciar objectes, i cridar-lo directament
     * @param json cadena "JSON"
     * @return objecte Aula
     */
    public static Aula fromJson (String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Aula.class);
    }

}
