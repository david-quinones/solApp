package sol.app.quinones.solappquinones.Models;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

/**
 * Model de Aula
 *
 * @author david
 */
public class Aula {

    private int id;
    private String nomAula;
    private Professor empleat;
    private List<Alumne> alumnes = new ArrayList<>();

    /**
     * Instantiates a new Aula.
     */
    public Aula(){}

    /**
     * Instantiates a new Aula.
     *
     * @param id        the id
     * @param nomAula   the nom aula
     * @param professor the professor
     * @param alumnes   the alumnes
     */
    public Aula(int id, String nomAula, Professor professor, List<Alumne> alumnes){
        this.id = id;
        this.nomAula = nomAula;
        this.empleat = professor;
        this.alumnes = new ArrayList<>(alumnes);
    }

    /**
     * Instantiates a new Aula.
     *
     * @param id        the id
     * @param nomAula   the nom aula
     * @param professor the professor
     */
    public Aula(int id, String nomAula, Professor professor) {
        this.id = id;
        this.nomAula = nomAula;
        this.empleat = professor;
    }

    /**
     * Instantiates a new Aula.
     *
     * @param nomAula the nom aula
     */
    public Aula(String nomAula){
        this.nomAula = nomAula;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets nom aula.
     *
     * @return the nom aula
     */
    public String getNomAula() {
        return nomAula;
    }

    /**
     * Sets nom aula.
     *
     * @param nomAula the nom aula
     */
    public void setNomAula(String nomAula) {
        this.nomAula = nomAula;
    }

    /**
     * Gets empleat.
     *
     * @return the empleat
     */
    public Professor getEmpleat() {
        return empleat;
    }

    /**
     * Sets empleat.
     *
     * @param empleat the empleat
     */
    public void setEmpleat(Professor empleat) {
        this.empleat = empleat;
    }

    /**
     * Gets alumnes.
     *
     * @return the alumnes
     */
    public List<Alumne> getAlumnes() {
        return alumnes;
    }

    /**
     * Sets alumnes.
     *
     * @param alumnes the alumnes
     */
    public void setAlumnes(List<Alumne> alumnes) {
        this.alumnes = new ArrayList<>(alumnes);
    }

    /**
     * Converteix una cadena "JSON" a un objecte Aula
     * Static per no tindre que instanciar objectes, i cridar-lo directament
     *
     * @param json cadena "JSON"
     * @return objecte Aula
     */
    public static Aula fromJson (String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Aula.class);
    }

}
