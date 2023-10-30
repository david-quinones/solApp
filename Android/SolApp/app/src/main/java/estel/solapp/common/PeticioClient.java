package estel.solapp.common;

public class PeticioClient extends JsonData {

    private String peticio;

    public PeticioClient() {super();}

    public PeticioClient(String peticio) {

        super();
        this.peticio = peticio;

    }

    public String getPeticio() {return peticio;}

    public void setPeticio(String query) {this.peticio = peticio;}

}
