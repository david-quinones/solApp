package entitats;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**Classe que representa l'entitat Persona
 *
 * @author Pau Castell Galtes
 */
public class Persona {
    private int idPersona;
    private String nom;
    private String cognom1;
    private String cognom2;
    private String data_naixement;
    private String dni;
    private String telefon;
    private String mail;

    
    /**Constructors, diferents segons les necessitats de cada cas.
     * 
     * @param id
     * @param nom
     * @param cognom1
     * @param cognom2
     * @param data_naixement
     * @param dni
     * @param telefon
     * @param mail 
     */
    public Persona(int id, String nom, String cognom1, String cognom2,
            String data_naixement, String dni, String telefon, String mail) {
        this.idPersona = id;
        this.nom = nom;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.data_naixement = data_naixement;
        this.dni = dni;
        this.telefon = telefon;
        this.mail = mail;
    }

    public Persona(String nom, String cognom1, String cognom2, String data_naixement,
            String dni, String telefon, String mail) {
        this.nom = nom;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.data_naixement = data_naixement;
        this.dni = dni;
        this.telefon = telefon;
        this.mail = mail;
    }

    public Persona(int id, String nom, String cognom1, String cognom2, String data_naixement) {
        this.idPersona = id;
        this.nom = nom;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.data_naixement = data_naixement;
    }

    public Persona(String nom, String cognom1, String cognom2, String data_naixement) {
        this.nom = nom;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.data_naixement = data_naixement;
    }
    
    public Persona(){
        
    }

    
    //Getters i Setters
    
    public int getIdPersona() {
        return idPersona;
    }

    public String getNom() {
        return nom;
    }

    public String getCognom1() {
        return cognom1;
    }

    public String getCognom2() {
        return cognom2;
    }

    public String getData_naixement() {
        return data_naixement;
    }

    public void setIdPersona(int id) {
        this.idPersona = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setCognom1(String cognom1) {
        this.cognom1 = cognom1;
    }

    public void setCognom2(String cognom2) {
        this.cognom2 = cognom2;
    }

    public void setData_naixement(String data) {
        data_naixement = data;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDni() {
        return dni;
    }

    public String getTelefon() {
        return telefon;
    }

    public String getMail() {
        return mail;
    }
     
}
