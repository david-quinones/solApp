package sol.app.quinones.solappquinones.Models;


import com.google.gson.Gson;

public class Usuari {

    private int id;
    private String username;
    private String password;

    private boolean isTeacher;
    private boolean isAdmin;

    public Usuari() {
    }

    public Usuari(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public Usuari(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Usuari(int id, String username, String password, boolean isTeacher, boolean isAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.isTeacher = isTeacher;
        this.isAdmin = isAdmin;
    }

    //static para llamar directamente sin necessidad de crear un nuevo objeto Usuari
    public static Usuari fromJson (String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Usuari.class);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
