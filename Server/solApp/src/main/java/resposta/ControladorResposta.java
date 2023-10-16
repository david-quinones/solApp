package resposta;

import entitats.Usuari;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;

/**
 *La classe determinará la resposta corresponent segons la petició rebuda.
 * @author Pau Castell Galtes
 */
public class ControladorResposta {
    private PeticioClient peticio;
    private GenerarResposta dadesResposta;
    private RetornDades resposta;

    /**Constructor que rep la petició del client i inicialitza la clase dadesResposta
     * per poder sol·licitar les dades de retorn
     * 
     * @param peticio del client que necessita resposta
     */
    public ControladorResposta(PeticioClient peticio) {
        this.peticio = peticio;
        dadesResposta = new GenerarResposta();
    }
    
    
    /**Métode que gestiona el tipus de resposta que s'ha de donar per la petició
     * rebuda.
     * 
     * @return RetornDades amb les dades de la resposta.
     */
    public RetornDades gestionarResposta(){
        String ordre = peticio.getPeticio();

        switch (ordre) {
            //Demanem les dades a dadesResposta en funció de l'ordre de la petició
            case "LOGIN":
                resposta = dadesResposta.respostaLogin((Usuari) peticio.getDades(0, Usuari.class));
                return resposta;
                
            case "LOGOUT":
                //Número de sessió que envia el client
                String numSessio = (String) peticio.getDades(0, String.class);
                //Generem resposta de LOGOUT amb el número de sessió rebut.
                resposta = dadesResposta.respostaLogout(numSessio);
                return resposta;
                
            default:
                throw new AssertionError();
        }
 
    }
    
}
