package sol.app.quinones.solappquinones.Models;


import com.google.gson.Gson;

/**
 * Classe que representa un usuari
 *
 * @author david
 */
public class Usuari {

    private int id;
    private String nomUsuari;
    private String password;
    private boolean isTeacher;
    private boolean isAdmin;
    private boolean isActive;

    /**
     * Constrctor per defecte
     */
    public Usuari() {
    }

    /**
     * Contructor que estableix nom usuari i contrasenya
     * @param username
     * @param password
     */
    public Usuari(String username, String password) {
        this.nomUsuari = username;
        this.password = password;
        this.isActive = true;
    }

    /**
     * Contructor que estableix Id, nom usuari i contrasenya
     * @param id
     * @param username
     * @param password
     */
    public Usuari(int id, String username, String password) {
        this.id = id;
        this.nomUsuari = username;
        this.password = password;
    }

    /**
     * Contstructor que estableix Id, nomusuari, contrasenya, boolen segon rol
     * @param id
     * @param username
     * @param password
     * @param isTeacher
     * @param isAdmin
     */
    public Usuari(int id, String username, String password, boolean isTeacher, boolean isAdmin) {
        this.id = id;
        this.nomUsuari = username;
        this.password = password;
        this.isTeacher = isTeacher;
        this.isAdmin = isAdmin;
    }

    /**
     * Contstructor que estableix Id, nomusuari, contrasenya, boolen segon rol
     * @param id
     * @param username
     * @param password
     * @param isTeacher
     * @param isAdmin
     * @parm isAtive
     */
    public Usuari(int id, String username, String password, boolean isTeacher, boolean isAdmin, boolean isActive) {
        this.id = id;
        this.nomUsuari = username;
        this.password = password;
        this.isTeacher = isTeacher;
        this.isAdmin = isAdmin;
        this.isActive = isActive;
    }


    /**
     * Converteix una cadena "JSON" a un objecte Usuari
     * Static per no tindre que instanciar objectes, i cridar-lo directament
     * @param json cadena "JSON"
     * @return objecte usuari
     */
    public static Usuari fromJson (String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Usuari.class);
    }

    /**
     *
     * @return id usuari
     */
    public int getId() {
        return id;
    }

    /**
     * Assigna Id
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * @return nomusuari
     */
    public String getNomUsuari() {
        return nomUsuari;
    }

    /**
     * Assigna nom usuari
     * @param nomUsuari
     */
    public void setNomUsuari(String nomUsuari) {
        this.nomUsuari = nomUsuari;
    }

    /**
     *
     * @return contrasenya
     */
    public String getPassword() {
        return password;
    }

    /**
     * Assigna contrasenya
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     *
     * @return boolea
     */
    public boolean getIsTeacher() {
        return isTeacher;
    }

    /**
     * Assigna boolea
     * @param teacher
     */
    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    /**
     *
     * @return boolea
     */
    public boolean getIsAdmin() {
        return isAdmin;
    }

    /**
     * Assigna boolea
     * @param admin
     */
    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean getIsActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
