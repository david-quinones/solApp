package entitats;

import com.google.gson.annotations.Expose;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**Classe per representar l'entitat missatge
 *
 * @author Pau Castell Galtes
 */
public class Missatge {
    private int idMissatge;
    private int idRemitentPersona;
    private ArrayList<Persona> destinataris;
    private String dataEnviament;
    private String contingut;
    private boolean remitentEsborrat;
    private boolean destinatariEsborrat;

    
    //Constructor de la classe

    public Missatge(int idMissatge, int idRemitent, ArrayList<Persona> destinataris, String dataEnviament, String contingut) {
        this.idMissatge = idMissatge;
        this.idRemitentPersona = idRemitent;
        this.destinataris = destinataris;
        this.dataEnviament = dataEnviament;
        this.contingut = contingut;
    }

    //Constructor enviar missatge

    public Missatge(ArrayList<Persona> destinataris, String contingut) {
        this.destinataris = destinataris;
        this.contingut = contingut;
    }
    
    
    //Constructor sense idMissatge per a nove altes

    public Missatge(int idRemitent, ArrayList<Persona> destinataris, String dataEnviament, String contingut) {
        this.idRemitentPersona = idRemitent;
        this.destinataris = destinataris;
        this.dataEnviament = dataEnviament;
        this.contingut = contingut;
    }

    //Constructor buit

    public Missatge() {
    }
    
    //Getter i Setters

    public int getIdMissatge() {
        return idMissatge;
    }

    public void setIdMissatge(int idMissatge) {
        this.idMissatge = idMissatge;
    }

    public ArrayList<Persona> getDestinataris() {
        return destinataris;
    }

    public void setDestinataris(ArrayList<Persona> destinataris) {
        this.destinataris = destinataris;
    }

    public String getDataEnviament() {
        return dataEnviament;
    }

    public void setDataEnviament(String dataEnviament) {
        this.dataEnviament = dataEnviament;
    }

    public String getContingut() {
        return contingut;
    }

    public void setContingut(String contingut) {
        this.contingut = contingut;
    }

    public int getIdRemitentPersona() {
        return idRemitentPersona;
    }

    public void setIdRemitentPersona(int idRemitentPersona) {
        this.idRemitentPersona = idRemitentPersona;
    }

    public boolean isRemitentEsborrat() {
        return remitentEsborrat;
    }

    public void setRemitentEsborrat(boolean remitentEsborrat) {
        this.remitentEsborrat = remitentEsborrat;
    }

    public boolean isDestinatariEsborrat() {
        return destinatariEsborrat;
    }

    public void setDestinatariEsborrat(boolean destinatariEsborrat) {
        this.destinatariEsborrat = destinatariEsborrat;
    }
    

    @Override
    public String toString(){
        String destinatarisText = "";
        for(Persona persona: destinataris){
            destinatarisText = persona.getIdPersona() +"\n";
        }
        return "Missatge: \n"
                + "Id del remitent: " + idRemitentPersona + "\n"+
                "Id del destinatari: " + destinatarisText +
                "Data enviament: " + getDataEnviament() + "\n"+
                "Contingut: " + contingut;
    }
    
}
