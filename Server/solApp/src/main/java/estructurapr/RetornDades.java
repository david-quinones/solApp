package estructurapr;

/**
 *Dades retornades pel servidor
 * @author pau
 */
public class RetornDades extends DadesPR{
    private int codiResultat;
    
    
    /**Constructor per defecte
     * 
     */
    public RetornDades(){
        super();
    }


    
    /**Constructor amb el codi del resultat. Les dades es poden afegir mes tard.
     * 
     * @param codiResultat , enter amb el codi del resultat d'una petició concreta.
     */
    public RetornDades(int codiResultat){
        super();
        this.codiResultat = codiResultat;
    }

    
    /**Retorna el codi del resultat
     * 
     * @return codiResultat
     */
    public int getCodiResultat() {
        return codiResultat;
    }

  
}
