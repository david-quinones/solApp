package estructurapr;

/**
 *Classe que contindrà les dades de les peticions de cada client
 * @author Pau Castell Galtes
 */
public class PeticioClient extends DadesPR{
    private String peticio;
    
    /**Constructor per defecte
     * 
     */
    public PeticioClient(){
        super();
    }
    
    
    /**Constructor amb el text específic de la petició, les dades es poden afegir
     * mes tard
     * @param peticio , String amb el nom de la petició
     */
    public PeticioClient(String peticio){
        super();
        this.peticio = peticio;
    }

    
    /**Retorna el tipus de petició
     * 
     * @return peticio
     */
    public String getPeticio() {
        return peticio;
    }
    
    
}
