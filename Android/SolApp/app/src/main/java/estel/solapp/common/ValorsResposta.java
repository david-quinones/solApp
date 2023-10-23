package estel.solapp.common;

public class ValorsResposta extends JsonData {

    private int codiResultat;


    /**
     * Constructor per defecte
     */
    public ValorsResposta() {
        super();
    }

    /**
     * Constructor amb el codi resultant.
     * @param codiResultat
     *
     */
    public ValorsResposta(int codiResultat) {
        super();
        this.codiResultat = codiResultat;
    }

    /**
     * Gets the response's return code.
     * @return return code
     */
    public int getReturnCode() {
        return codiResultat;
    }



}
