package estel.solapp.models;

public class Empleat extends Persona{

    private int idEmpleat;
    private boolean actiu;
    private String iniciContracte;
    private String finalContracte;

    private String professor;

    @Override
    public String toString() {
        this.professor = this.professor = super.getNom()+" "+ super.getCognom1()+" "+ super.getCognom2();
        return professor;
    }

    /**************
     * Constructors
     **************
     *
     * @param id
     * @param actiu
     * @param iniciContracte
     * @param finalContracte
     */
    public Empleat(int idPersona, String nom, String cognom1, String cognom2, String data_naixement,
                   String dni, String telefon, String mail, int id, boolean actiu, String iniciContracte,
                   String finalContracte) {

        super(idPersona, nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idEmpleat = id;
        this.actiu = actiu;
        this.iniciContracte = iniciContracte;
        this.finalContracte = finalContracte;

    }

    public Empleat(String nom, String cognom1, String cognom2, String data_naixement,
                   String dni, String telefon, String mail,boolean actiu, String iniciContracte, String finalContracte) {

        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.actiu = actiu;
        this.iniciContracte = iniciContracte;
        this.finalContracte = finalContracte;

    }
    public Empleat(String nom, String cognom1, String cognom2, String data_naixement,
                   String dni, String telefon, String mail) {

        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);


    }

    public Empleat(){

    }

    /*******************
     * Getters i Setters
    ********************/

    public int getIdEmpleat() {
        return idEmpleat;
    }

    public void setIdEmpleat(int id) {
        this.idEmpleat = id;
    }

    public boolean isActiu() {
        return actiu;
    }

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
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


}



