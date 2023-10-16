package entitats;

/**
 *Clase que representa l'entitat Usuari a l'aplicació.
 * @author Pau Castell Galtes
 */
public class Usuari {
    private int id;
    private String nomUsuari;
    private String password;
    private boolean isAdmin;
    private boolean isTeacher;
    
    
    //Constructors:
    /**Constructor per crear l'usuari amb els camps nomUsuari i password.
     * 
     * @param nomUsuari
     * @param password 
     */
    public Usuari(String nomUsuari, String password) {
        this.nomUsuari = nomUsuari;
        this.password = password;
    }

    /**Constructor per crear la l'objecte Usuari amb tots els camps
     * 
     * @param id
     * @param nomUsuari
     * @param password
     * @param isAdmin
     * @param isTeacher 
     */
    public Usuari(int id, String nomUsuari, String password, boolean isAdmin, boolean isTeacher) {
        this.id = id;
        this.nomUsuari = nomUsuari;
        this.password = password;
        this.isAdmin = isAdmin;
        this.isTeacher = isTeacher;
        
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomUsuari() {
        return nomUsuari;
    }

    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    public boolean isIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(boolean isTeacher) {
        this.isTeacher = isTeacher;
    }
  
}
