package resposta;

import entitats.Empleat;
import entitats.Usuari;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.util.logging.Logger;
import persistencia.PersonaDAO;
import seguretat.GestorSessions;

/**
 *La classe determinará la resposta corresponent segons la petició rebuda.
 * @author Pau Castell Galtes
 */
public class ControladorResposta {
    private PeticioClient peticio;
    private GenerarResposta dadesResposta;
    private RetornDades resposta;
    private String numSessio;
    private GestorSessions sessions = GestorSessions.obtindreInstancia();
    private static final Logger LOGGER = Logger.getLogger(PersonaDAO.class.getName());
    private static final int CODI_ERROR = 0;

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
        String ordre = comprobarSessio(peticio);

        switch (ordre) {
            //Demanem les dades a dadesResposta en funció de l'ordre de la petició
            case "LOGIN":
                resposta = dadesResposta.respostaLogin((Usuari) peticio.getDades(0, Usuari.class));
                return resposta;
                
            case "LOGOUT":
                //Generem resposta de LOGOUT amb el número de sessió rebut.
                resposta = dadesResposta.respostaLogout(numSessio);
                return resposta;
            case "ALTA_EMPLEAT":
                //Generem resposat a la solicitut d'alta d'un empleat
                resposta = dadesResposta.respostaAltaEmpleat((Empleat) peticio.getDades(1, Empleat.class));
                return resposta;
                
            default:
                return resposta = new RetornDades(CODI_ERROR);
        }
 
    }

    /**Métode per comprobar que la sessió existeix abans de donar una resposta
     * 
     * @param peticio
     * @return 
     */
    
    public String comprobarSessio(PeticioClient peticio){
        //Obtenim numero de sessió
        numSessio = (String) peticio.getDades(0, String.class);
        //Si la sessió existeix i no es una petició de LOGIN solicitem resposta
        if(!peticio.getPeticio().equals("LOGIN")){
            if(sessions.verificarSessio(numSessio)){
                return peticio.getPeticio();
            }else{
                LOGGER.info("La sessió no existeix");
                return "ERROR";
            }
        }
        return peticio.getPeticio();
    }
    
}
