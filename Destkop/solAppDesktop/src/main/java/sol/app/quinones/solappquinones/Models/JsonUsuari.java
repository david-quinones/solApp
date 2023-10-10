package sol.app.quinones.solappquinones.Models;


import com.google.gson.Gson;

public class JsonUsuari {

    private int id;
    private String nomUsuari;
    private String password;

    private boolean isTeacher;
    private boolean isAdmin;

    public JsonUsuari() {
    }

    public JsonUsuari(String username, String password) {
        this.nomUsuari = username;
        this.password = password;
    }

    public JsonUsuari(int id, String username, String password) {
        this.id = id;
        this.nomUsuari = username;
        this.password = password;
    }

    public JsonUsuari(int id, String username, String password, boolean isTeacher, boolean isAdmin) {
        this.id = id;
        this.nomUsuari = username;
        this.password = password;
        this.isTeacher = isTeacher;
        this.isAdmin = isAdmin;
    }

    //static para llamar directamente sin necessidad de crear un nuevo objeto Usuari
    public static Usuari fromJson (Object json){
        Gson gson = new Gson();
        return gson.fromJson(json.toString(), Usuari.class);
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

    public boolean isTeacher() {
        return isTeacher;
    }

    public void setTeacher(boolean teacher) {
        isTeacher = teacher;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }
}
