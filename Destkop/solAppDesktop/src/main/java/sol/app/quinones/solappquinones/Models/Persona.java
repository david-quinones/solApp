package sol.app.quinones.solappquinones.Models;

import com.google.gson.Gson;

public class Persona {

    private int idPersona;
    private String nom;
    private String cognom1;
    private String cognom2;
    private String data_naixement;
    private String dni;
    private String telefon;
    private String mail;

    public Persona(String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail) {
        this.nom = nom;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.data_naixement = data_naixement;
        this.dni = dni;
        this.telefon = telefon;
        this.mail = mail;
    }

    public Persona(int idPersona, String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail) {
        this.idPersona = idPersona;
        this.nom = nom;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.data_naixement = data_naixement;
        this.dni = dni;
        this.telefon = telefon;
        this.mail = mail;
    }

    public Persona() {
    }

    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getCognom1() {
        return cognom1;
    }

    public void setCognom1(String cognom1) {
        this.cognom1 = cognom1;
    }

    public String getCognom2() {
        return cognom2;
    }

    public void setCognom2(String cognom2) {
        this.cognom2 = cognom2;
    }

    public String getData_naixement() {
        return data_naixement;
    }

    public void setData_naixement(String data_naixement) {
        this.data_naixement = data_naixement;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    /**
     * Converteix una cadena "JSON" a un objecte Persona
     * Static per no tindre que instanciar objectes, i cridar-lo directament
     * @param json cadena "JSON"
     * @return objecte Persona
     */
    public static Persona fromJson (String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Persona.class);
    }

    @Override
    public String toString() {
        return "Persona{" +
                "idPersona=" + idPersona +
                ", nom='" + nom + '\'' +
                ", cognom1='" + cognom1 + '\'' +
                ", cognom2='" + cognom2 + '\'' +
                ", data_naixement='" + data_naixement + '\'' +
                ", dni='" + dni + '\'' +
                ", telefon='" + telefon + '\'' +
                ", mail='" + mail + '\'' +
                '}';
    }
}
