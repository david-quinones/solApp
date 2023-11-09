package estel.solapp.models;
/**
 * Classe model Usuari
 * @author Juan Antonio
 */
public class User {

    private int id;
    private String nomUsuari;
    private String password;
    private boolean isAdmin;
    private boolean isTeacher;
    private int personaId;




    //Constructors:

    /**
     * Constructor per crear l'usuari amb els camps nomUsuari i password.
     *
     * @param nomUsuari Nom d'usuari pel login
     * @param password Contrasenya pel login
     */
    public User(String nomUsuari, String password) {

        this.nomUsuari = nomUsuari;
        this.password = password;
    }

    /**
     * Constructor per crear la l'objecte Usuari amb tots els atributs
     *
     * @param id id de l'usuari
     * @param nomUsuari nom d'usuari
     * @param password contrasenya
     * @param isAdmin
     * @param isTeacher
     * @param personaId
     */
    public User (int id, String nomUsuari, String password, boolean isAdmin, boolean isTeacher, int personaId) {

        this.id = id;
        this.nomUsuari = nomUsuari;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isTeacher = isTeacher;
        this.personaId = personaId;

    }

    /*
    * Getters i Setters
     */
    public int getId() {return id;}

    public void setId(int id) {this.id = id;}

    public String getNomUsuari() {return nomUsuari;}

    public void setNomUsuari(String nomUsuari) {this.nomUsuari = nomUsuari;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public boolean isAdmin() {return isAdmin;}

    public void setIsAdmin(boolean isAdmin) {this.isAdmin = isAdmin;}

    public boolean isTeacher() {return isTeacher;}

    public void setIsTeacher(boolean isTeacher) {this.isTeacher = isTeacher;}

    public int getPersonaId() {return personaId;}

    public void setPersonaId(int personaId) {this.personaId = personaId;}


}
