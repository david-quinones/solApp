package estel.solapp.models;

import java.util.ArrayList;

public class Aula {

    private int id;
    private String nomAula;
    private Empleat empleat;
    private int nombreAlumnes;
    private ArrayList<Alumne> alumnes;

    /**Constructors segons cada necessitat.
     *
     * @param id
     * @param nomAula
     * @param empleat
     * @param alumnes
     */
    public Aula(int id, String nomAula, Empleat empleat, ArrayList<Alumne> alumnes) {
        this.id = id;
        this.nomAula = nomAula;
        this.empleat = empleat;
        this.alumnes = alumnes;
    }

    public Aula(String nomAula, Empleat empleat, ArrayList<Alumne> alumnes) {

        this.nomAula = nomAula;
        this.empleat = empleat;
        this.alumnes = alumnes;
    }

    public Aula(String nomAula) {
        this.nomAula = nomAula;
    }


    /**Getters i Setters
     *
     */
    public Aula() {
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

    public Empleat getEmpleat() {
        return empleat;
    }

    public void setEmpleat(Empleat empleat) {
        this.empleat = empleat;
    }

    public ArrayList<Alumne> getAlumnes() {
        return alumnes;
    }

    public void setAlumnes(ArrayList<Alumne> alumnes) {
        this.alumnes = alumnes;
    }




}
