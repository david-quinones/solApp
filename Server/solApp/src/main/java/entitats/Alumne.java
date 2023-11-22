package entitats;

/**Classe per representar la entitat alumne
 *
 * @author Pau Castell Galtes
 */
public class Alumne extends Persona{
    private int idAlumne;
    private boolean actiu;
    private boolean menjador;
    private boolean acollida;
    private int idAula;

    /**Constructors 
     * 
     * @param idAlumne
     * @param actiu
     * @param menjador
     * @param acollida
     * @param id
     * @param nom
     * @param cognom1
     * @param cognom2
     * @param data_naixement
     * @param dni
     * @param telefon
     * @param mail 
     */
    public Alumne(int id, String nom, String cognom1, String cognom2, String data_naixement, 
            String dni, String telefon, String mail,int idAlumne, boolean actiu, boolean menjador, boolean acollida, int idAula) {
        super(id, nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idAlumne = idAlumne;
        this.actiu = actiu;
        this.menjador = menjador;
        this.acollida = acollida;
        this.idAula = idAula;
    }
    
    public Alumne(int id, String nom, String cognom1, String cognom2, String data_naixement, 
            String dni, String telefon, String mail,int idAlumne, boolean actiu, boolean menjador, boolean acollida) {
        super(id, nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.idAlumne = idAlumne;
        this.actiu = actiu;
        this.menjador = menjador;
        this.acollida = acollida;
    }

    /**Constructors
     * 
     * @param actiu
     * @param menjador
     * @param acollida
     * @param nom
     * @param cognom1
     * @param cognom2
     * @param data_naixement
     * @param dni
     * @param telefon
     * @param mail 
     * @param idAula 
     */
    public Alumne(String nom, String cognom1, String cognom2, String data_naixement, 
            String dni, String telefon, String mail,boolean actiu, boolean menjador, boolean acollida) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.actiu = actiu;
        this.menjador = menjador;
        this.acollida = acollida;

    }
    
    public Alumne(String nom, String cognom1, String cognom2, String data_naixement, 
            String dni, String telefon, String mail,boolean actiu, boolean menjador, boolean acollida, int idAula) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.actiu = actiu;
        this.menjador = menjador;
        this.acollida = acollida;
        this.idAula = idAula;

    }

    
    /**Constructor que servirà per fer proves
     * 
     * @param actiu
     * @param menjador
     * @param acollida 
     */
    public Alumne(boolean actiu, boolean menjador, boolean acollida, int idAula) {
        this.actiu = actiu;
        this.menjador = menjador;
        this.acollida = acollida;
        this.idAula = idAula;
    }
    
    

    public Alumne() {
    }

    /**Getter i Setters:
     * 
     * @return 
     */
    public int getIdAlumne() {
        return idAlumne;
    }

    public void setIdAlumne(int idAlumne) {
        this.idAlumne = idAlumne;
    }

    public boolean isActiu() {
        return actiu;
    }

    public void setActiu(boolean actiu) {
        this.actiu = actiu;
    }

    public boolean isMenjador() {
        return menjador;
    }

    public void setMenjador(boolean menjador) {
        this.menjador = menjador;
    }

    public boolean isAcollida() {
        return acollida;
    }

    public void setAcollida(boolean acollida) {
        this.acollida = acollida;
    }
    
    public int getIdAula() {
        return idAula;
    }

    public void setIdAula(int idAula) {
        this.idAula = idAula;
    }
    
}
