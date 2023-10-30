package entitats;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**Classe que representa la entitat Empleat
 *
 * @author Pau Castell Galtes
 */
public class Empleat extends Persona{
    private int id;
    private boolean actiu;
    private LocalDate iniciContracte;
    private LocalDate finalContracte;

    
    /**Constructors, diferents segons les necessitats
     * 
     * @param id
     * @param actiu
     * @param iniciContracte
     * @param finalContracte 
     */
    public Empleat(int idPersona, String nom, String cognom1, String cognom2, String data_naixement,
            String dni, String telefon, String mail, int id, boolean actiu, String iniciContracte,
            String finalContracte
            ) {
        super(idPersona, nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.id = id;
        this.actiu = actiu;
        this.iniciContracte = LocalDate.parse(iniciContracte, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.finalContracte = LocalDate.parse(finalContracte, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public Empleat(String nom, String cognom1, String cognom2, String data_naixement,
            String dni, String telefon, String mail,boolean actiu, String iniciContracte, String finalContracte) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.actiu = actiu;
        this.iniciContracte = LocalDate.parse(iniciContracte, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.finalContracte = LocalDate.parse(finalContracte, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    
    //Getters i Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActiu() {
        return actiu;
    }

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }

    public String getIniciContracte() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return iniciContracte.format(format);
    }

    public void setIniciContracte(String iniciContracte) {
        this.iniciContracte = LocalDate.parse(iniciContracte, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }

    public String getFinalContracte() {
        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return finalContracte.format(format);
    }

    public void setFinalContracte(String finalContracte) {
        this.finalContracte = LocalDate.parse(finalContracte, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
    
    
    
    
}
