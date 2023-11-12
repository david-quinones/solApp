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
    public Alumne(int idAlumne, boolean actiu, boolean menjador, boolean acollida, int id, String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail) {
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
     */
    public Alumne(boolean actiu, boolean menjador, boolean acollida, String nom, String cognom1, String cognom2, String data_naixement, String dni, String telefon, String mail) {
        super(nom, cognom1, cognom2, data_naixement, dni, telefon, mail);
        this.actiu = actiu;
        this.menjador = menjador;
        this.acollida = acollida;
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
    
    
}
