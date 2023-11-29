package sol.app.quinones.solappquinones.Models;

import com.google.gson.Gson;

import java.util.Objects;

/**
 * Classe model que representa un alumne
 * Extend de la classe persona i inclou atribuits propis
 *
 * @author david
 */
public class Alumne extends Persona {

    private int idAlumne, idAula;
    private boolean menjador, actiu, acollida;

    /**
     * Contructor buit
     */
    public Alumne() {
    }

    /**
     * Sobrecarreg del constructor
     * @param nom
     * @param cognom1
     * @param cognom2
     * @param data_naixement
     * @param dni
     * @param telefon
     * @param mail
     * @param idAlumne
     * @param menjador
     * @param actiu
     * @param acollida
     */
    public Alumne(String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, int idAlumne, boolean menjador, boolean actiu, boolean acollida, int idAula) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idAlumne = idAlumne;
        this.menjador = menjador;
        this.actiu = actiu;
        this.acollida = acollida;
        this.idAula = idAula;
    }

    /**
     * Sobre carrega del contructor
     * @param idPersona
     * @param nom
     * @param cognom1
     * @param cognom2
     * @param data_naixement
     * @param dni
     * @param telefon
     * @param mail
     * @param idAlumne
     * @param menjador
     * @param actiu
     * @param acollida
     */
    public Alumne(int idPersona, String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, int idAlumne, boolean menjador, boolean actiu, boolean acollida, int idAula) {
        super(idPersona, nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idAlumne = idAlumne;
        this.menjador = menjador;
        this.actiu = actiu;
        this.acollida = acollida;
        this.idAula = idAula;
    }

    /**
     * Sobrecarrega del constryucot
     * @param nom
     * @param cognom1
     * @param cognom2
     * @param data_naixement
     * @param dni
     * @param telefon
     * @param mail
     * @param menjador
     * @param actiu
     * @param acollida
     */
    public Alumne( String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, boolean menjador, boolean actiu, boolean acollida) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.menjador = menjador;
        this.actiu = actiu;
        this.acollida = acollida;
    }

    /**
     * sobre carrega del contructor
     * @param nom
     * @param cognom1
     * @param cognom2
     * @param data_naixement
     * @param dni
     * @param telefon
     * @param mail
     */
    public Alumne( String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.actiu = true;
    }

    /**
     * Sobre carrega del constructor
     * @param idPersona
     * @param nom
     * @param cognom1
     * @param cognom2
     * @param data_naixement
     * @param dni
     * @param telefon
     * @param mail
     * @param idAlumne
     */
    public Alumne(int idPersona, String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail, int idAlumne, boolean actiu) {
        super(idPersona, nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idAlumne = idAlumne;
        this.actiu = actiu;
    }



    /**
     * Obte Identificador Alumne
     * @return idAlumne
     */
    public int getIdAlumne() {
        return idAlumne;
    }

    /**
     * Assigna identificador alumne
     * @param idAlumne Identificador a establir
     */
    public void setIdAlumne(int idAlumne) {
        this.idAlumne = idAlumne;
    }

    /**
     * Obte si es queda al menjador amb un boolea
     * @return conficio del bolea
     */
    public boolean getIsMenjador() {
        return menjador;
    }

    /**
     * Estableix si es queda al menjador
     * @param menjador valor assignar
     */
    public void setMenjador(boolean menjador) {
        this.menjador = menjador;
    }

    /**
     * Obte si es actiu
     * @return retorna l'estat
     */
    public boolean getIsActiu() {
        return actiu;
    }

    /**
     * Estableix si es actiu
     * @param actiu valor assignar
     */
    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }

    /**
     *
     * @return
     */
    public boolean getIsAcollida() {
        return acollida;
    }

    /**
     *
     * @param acollida
     */
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

    public int getIdAula() {
        return idAula;
    }

    public void setIdAula(int idAula) {
        this.idAula = idAula;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alumne alumne = (Alumne) o;
        return Objects.equals(idAlumne, alumne.idAlumne);
    }

    @Override
    public int hasCode(){
        return Objects.hash(idAlumne);
    }



}
