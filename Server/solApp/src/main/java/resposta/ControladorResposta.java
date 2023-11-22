package resposta;

import entitats.*;
import estructurapr.PeticioClient;
import estructurapr.RetornDades;
import java.util.logging.Logger;
import persistencia.ConexioBBDD;
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
    private static final Logger LOGGER = Logger.getLogger(ControladorResposta.class.getName());
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
                resposta = dadesResposta.respostaAltaEmpleat((Empleat) peticio.getDades(1, Empleat.class),
                        (Usuari) peticio.getDades(2, Usuari.class));
                return resposta;
            case "CONSULTA_PERFIL":
                //Generem resposta amb les dades de la Persona vinculada al usuari
                String numSessio = (String) peticio.getDades(0, String.class);
                resposta = dadesResposta.respostaConsultaPersona(numSessio);
                return resposta;
            case "MODIFICAR_PERFIL":
                //Generem resposta amb les noves dades de la Persona SIMULAT
                Persona personaNova = (Persona) peticio.getDades(1, Persona.class);
                resposta =  dadesResposta.respostaModificarPersona(personaNova);
                return resposta;
            case "LLISTAR_EMPLEATS":
                //Generem la resposta amb les dades de tots els empleats
                resposta = dadesResposta.respostaLlistarEmpleats();
                return resposta;
            case "ELIMINAR_USUARI":
                //Generem la resposta adient al desactivar un usuari.
                Persona personaEliminar = (Persona) peticio.getDades(1, Persona.class);
                return resposta = dadesResposta.respostaEliminarUsuari(personaEliminar);
            case "MODIFICAR_EMPLEAT":
                //Generem la resposta corresponent a la modificació
                Empleat empleatModificar = (Empleat) peticio.getDades(1, Empleat.class);
                return resposta = dadesResposta.respostaModificarEmpleat(empleatModificar);
            case "LLISTAR_ALUMNES":
                //Generem la resposta amb les dades de tots els alumnes
                resposta = dadesResposta.respostaLlistarAlumnes();
                return resposta;
            case "ALTA_ALUMNE":
                //Generem la resposta al donar d'alta un alumne
                resposta = dadesResposta.respostaAltaAlumne((Alumne)peticio.getDades(1, Alumne.class),
                        (Usuari)peticio.getDades(2, Usuari.class));
                return resposta;
            case "MODIFICAR_ALUMNE":
                //Generem la resposta a la crida modificar_alumne
                resposta = dadesResposta.respostaModificarAlumne((Alumne)peticio.getDades(1, Alumne.class));
                return resposta;
            case "LLISTAR_USUARIS":
                //Generem la resposta a la crida llistar_usuaris
                resposta = dadesResposta.respostaLlistarUsuaris();
                return resposta;
            case "MODIFICAR_USUARI":
                //Generem resposta a la crida modificar_usuari
                resposta = dadesResposta.respostaModificarUsuari((Usuari)peticio.getDades(1, Usuari.class));
                return resposta;
            case "ALTA_AULA":
                //Generem resposta a la crida alta_aula
                resposta = dadesResposta.respostaAltaAula((Aula) peticio.getDades(1, Aula.class));
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
