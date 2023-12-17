package sol.app.quinones.solappquinones.Models;

import com.google.gson.Gson;
import java.util.*;

/**
 * Classe model que represena un missatge
 *
 * @author david
 */
public class Message {

    private int idMissatge;
    private Persona RemitentPersona;
    private List<Persona> destinataris = new ArrayList<>();
    private String dataEnviament;
    private String contingut;
    private boolean destinetariEsborrat;
    private boolean remitentEsborrat;

    //controlar estat
    private boolean enviat;
    private boolean rebut;

    /**
     * Contructors
     */
    public Message() {
    }

    /**
     * Instantiates a new Message.
     *
     * @param remitentPersona     the remitent persona
     * @param destinataris        the destinataris
     * @param dataEnviament       the data enviament
     * @param contingut           the contingut
     * @param destinetariEsborrat the destinetari esborrat
     * @param remitentEsborrat    the remitent esborrat
     */
    public Message(Persona remitentPersona, List<Persona> destinataris, String dataEnviament, String contingut, boolean destinetariEsborrat, boolean remitentEsborrat) {
        this.RemitentPersona = remitentPersona;
        this.destinataris = destinataris;
        this.dataEnviament = dataEnviament;
        this.contingut = contingut;
        this.destinetariEsborrat = destinetariEsborrat;
        this.remitentEsborrat = remitentEsborrat;
    }

    /**
     * Instantiates a new Message.
     *
     * @param idMissatge          the id missatge
     * @param remitentPersona     the remitent persona
     * @param destinataris        the destinataris
     * @param dataEnviament       the data enviament
     * @param contingut           the contingut
     * @param destinetariEsborrat the destinetari esborrat
     * @param remitentEsborrat    the remitent esborrat
     */
    public Message(int idMissatge, Persona remitentPersona, List<Persona> destinataris, String dataEnviament, String contingut, boolean destinetariEsborrat, boolean remitentEsborrat) {
        this.idMissatge = idMissatge;
        this.RemitentPersona = remitentPersona;
        this.destinataris = destinataris;
        this.dataEnviament = dataEnviament;
        this.contingut = contingut;
        this.destinetariEsborrat = destinetariEsborrat;
        this.remitentEsborrat = remitentEsborrat;
    }

    /**
     * Gets id missatge.
     *
     * @return the id missatge
     */
    public int getIdMissatge() {
        return idMissatge;
    }

    /**
     * Sets id missatge.
     *
     * @param idMissatge the id missatge
     */
    public void setIdMissatge(int idMissatge) {
        this.idMissatge = idMissatge;
    }

    /**
     * Gets remitent persona.
     *
     * @return the remitent persona
     */
    public Persona getRemitentPersona() {
        return RemitentPersona;
    }

    /**
     * Sets remitent persona.
     *
     * @param remitentPersona the remitent persona
     */
    public void setRemitentPersona(Persona remitentPersona) {
        this.RemitentPersona = remitentPersona;
    }

    /**
     * Gets destinataris.
     *
     * @return the destinataris
     */
    public List<Persona> getDestinataris() {
        return destinataris;
    }

    /**
     * Sets destinataris.
     *
     * @param destinataris the destinataris
     */
    public void setDestinataris(List<Persona> destinataris) {
        this.destinataris = destinataris;
    }

    /**
     * Gets data enviament.
     *
     * @return the data enviament
     */
    public String getDataEnviament() {
        return dataEnviament;
    }

    /**
     * Sets data enviament.
     *
     * @param dataEnviament the data enviament
     */
    public void setDataEnviament(String dataEnviament) {
        this.dataEnviament = dataEnviament;
    }

    /**
     * Gets contingut.
     *
     * @return the contingut
     */
    public String getContingut() {
        return contingut;
    }

    /**
     * Sets contingut.
     *
     * @param contingut the contingut
     */
    public void setContingut(String contingut) {
        this.contingut = contingut;
    }

    /**
     * Is destinetari esborrat boolean.
     *
     * @return the boolean
     */
    public boolean isDestinetariEsborrat() {
        return destinetariEsborrat;
    }

    /**
     * Sets destinetari esborrat.
     *
     * @param destinetariEsborrat the destinetari esborrat
     */
    public void setDestinetariEsborrat(boolean destinetariEsborrat) {
        this.destinetariEsborrat = destinetariEsborrat;
    }

    /**
     * Is remitent esborrat boolean.
     *
     * @return the boolean
     */
    public boolean isRemitentEsborrat() {
        return remitentEsborrat;
    }

    /**
     * Sets remitent esborrat.
     *
     * @param remitentEsborrat the remitent esborrat
     */
    public void setRemitentEsborrat(boolean remitentEsborrat) {
        this.remitentEsborrat = remitentEsborrat;
    }

    public boolean isEnviat() {
        return enviat;
    }

    public void setEnviat(boolean enviat) {
        this.enviat = enviat;
    }

    public boolean isRebut() {
        return rebut;
    }

    public void setRebut(boolean rebut) {
        this.rebut = rebut;
    }

    /**
     * Converteix una cadena "JSON" a un objecte Message
     * Static per no tindre que instanciar objectes, i cridar-lo directament
     *
     * @param json cadena "JSON"
     * @return objecte Message
     */
    public static Message fromJson (String json){
        Gson gson = new Gson();
        return gson.fromJson(json, Message.class);
    }

    @Override
    public String toString() {
        return "Message{" +
                "idMissatge=" + idMissatge +
                ", RemitentPersona=" + RemitentPersona +
                ", destinataris=" + destinataris +
                ", dataEnviament='" + dataEnviament + '\'' +
                ", contingut='" + contingut + '\'' +
                ", destinetariEsborrat=" + destinetariEsborrat +
                ", remitentEsborrat=" + remitentEsborrat +
                ", enviat=" + enviat +
                ", rebut=" + rebut +
                '}';
    }
}
