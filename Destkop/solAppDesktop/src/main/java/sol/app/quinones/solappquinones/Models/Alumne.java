package sol.app.quinones.solappquinones.Models;

import com.google.gson.Gson;

public class Alumne extends Persona{

    private int idAlumne;
    private boolean menjador, actiu, acollida;

    public Alumne() {
    }

    public Alumne(String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, int idAlumne, boolean menjador, boolean actiu, boolean acollida) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idAlumne = idAlumne;
        this.menjador = menjador;
        this.actiu = actiu;
        this.acollida = acollida;
    }

    public Alumne(int idPersona, String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, int idAlumne, boolean menjador, boolean actiu, boolean acollida) {
        super(idPersona, nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idAlumne = idAlumne;
        this.menjador = menjador;
        this.actiu = actiu;
        this.acollida = acollida;
    }

    public Alumne( String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, boolean menjador, boolean actiu, boolean acollida) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.menjador = menjador;
        this.actiu = actiu;
        this.acollida = acollida;
    }

    public Alumne( String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.actiu = true;
    }

    public Alumne(int idPersona, String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, int idAlumne) {
        super(idPersona, nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idAlumne = idAlumne;
        this.actiu = true;
    }

    public int getIdAlumne() {
        return idAlumne;
    }

    public void setIdAlumne(int idAlumne) {
        this.idAlumne = idAlumne;
    }

    public boolean getIsMenjador() {
        return menjador;
    }

    public void setMenjador(boolean menjador) {
        this.menjador = menjador;
    }

    public boolean getIsActiu() {
        return actiu;
    }

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }

    public boolean getIsAcollida() {
        return acollida;
    }

    public void setAcollida(boolean acollida) {
        this.acollida = acollida;
    }


    /**
     * Converteix una cadena "JSON" a un objecte Alumne
     * Static per no tindre que instanciar objectes, i cridar-lo directament
     * @param json cadena "JSON"
     * @return objecte Alumne
     */
    public static Alumne fromJson (String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Alumne.class);
    }
}
