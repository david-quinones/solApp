package sol.app.quinones.solappquinones.Models;

import com.google.gson.Gson;

import java.util.Objects;

/**
 * Classe model que reprenta una persona
 *
 * Conte informació basica sobre una persona i els metodes per establir i obtenir l'estat de cada atribut
 *
 * @author david
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

    /**
     * Constructor sobrecarrega
     *
     * @param nom            the nom
     * @param cognom1        the cognom 1
     * @param cognom2        the cognom 2
     * @param data_naixement the data naixement
     * @param dni            the dni
     * @param telefon        the telefon
     * @param mail           the mail
     */
    public Persona(String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail) {
        this.nom = nom;
        this.cognom1 = cognom1;
        this.cognom2 = cognom2;
        this.data_naixement = data_naixement;
        this.dni = dni;
        this.telefon = telefon;
        this.mail = mail;
    }

    /**
     * Sobrecarrega
     *
     * @param idPersona      the id persona
     * @param nom            the nom
     * @param cognom1        the cognom 1
     * @param cognom2        the cognom 2
     * @param data_naixement the data naixement
     * @param dni            the dni
     * @param telefon        the telefon
     * @param mail           the mail
     */
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

    /**
     * Constructor bui
     */
    public Persona() {
    }


    /**
     * Gets id persona.
     *
     * @return the id persona
     */
    public int getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(int idPersona) {
        this.idPersona = idPersona;
    }

    /**
     * Gets nom.
     *
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * Sets nom.
     *
     * @param nom the nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Gets cognom 1.
     *
     * @return the cognom 1
     */
    public String getCognom1() {
        return cognom1;
    }

    /**
     * Sets cognom 1.
     *
     * @param cognom1 the cognom 1
     */
    public void setCognom1(String cognom1) {
        this.cognom1 = cognom1;
    }

    /**
     * Gets cognom 2.
     *
     * @return the cognom 2
     */
    public String getCognom2() {
        return cognom2;
    }

    /**
     * Sets cognom 2.
     *
     * @param cognom2 the cognom 2
     */
    public void setCognom2(String cognom2) {
        this.cognom2 = cognom2;
    }

    /**
     * Gets data naixement.
     *
     * @return the data naixement
     */
    public String getData_naixement() {
        return data_naixement;
    }

    /**
     * Sets data naixement.
     *
     * @param data_naixement the data naixement
     */
    public void setData_naixement(String data_naixement) {
        this.data_naixement = data_naixement;
    }

    /**
     * Gets dni.
     *
     * @return the dni
     */
    public String getDni() {
        return dni;
    }

    /**
     * Sets dni.
     *
     * @param dni the dni
     */
    public void setDni(String dni) {
        this.dni = dni;
    }

    /**
     * Gets telefon.
     *
     * @return the telefon
     */
    public String getTelefon() {
        return telefon;
    }

    /**
     * Sets telefon.
     *
     * @param telefon the telefon
     */
    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    /**
     * Gets mail.
     *
     * @return the mail
     */
    public String getMail() {
        return mail;
    }

    /**
     * Sets mail.
     *
     * @param mail the mail
     */
    public void setMail(String mail) {
        this.mail = mail;
    }



    /**
     * Converteix una cadena "JSON" a un objecte Persona
     * Static per no tindre que instanciar objectes, i cridar-lo directament
     *
     * @param json cadena "JSON"
     * @return objecte Persona
     */
    public static Persona fromJson (String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Persona.class);
    }


    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return Objects.equals(idPersona, persona.idPersona);
    }


    public int hasCode(){
        return Objects.hash(idPersona);
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
