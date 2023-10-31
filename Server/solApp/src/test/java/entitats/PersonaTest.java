package entitats;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

/**Classe per fer el joc de probes de la entitat Persona
 *
 * @author Pau Castell Galtes
 */
public class PersonaTest {
    private Persona persona;
    
    /**Iniciem l'objecte Persona abans de cada test
     * 
     */
    @Before
    public void setUp(){
        persona = new Persona(1, "Pau", "Castell", "Galtes", "1983-08-07",
                "46797529G", "645248965", "pau.bateria@gmail.com");
    }
    
    /**Proba del getter del id de Persona
     * 
     */
    @Test
    public void testGetId(){
        assertEquals(1, persona.getId());
    }
    
     /**Proba del getter del nom de Persona
     * 
     */
    @Test
    public void testGetNom(){
        assertEquals("Pau", persona.getNom());
    }
    
     /**Proba del getter del nom de Persona
     * 
     */
    @Test
    public void testGetCognom1(){
        assertEquals("Castell", persona.getCognom1());
    }
    
     /**Proba del getter del cognom1 de Persona
     * 
     */
    @Test
    public void testGetCognom2(){
        assertEquals("Galtes", persona.getCognom2());
    }
    
     /**Proba del getter de la data de naixement de Persona
     * 
     */
    @Test
    public void testGetData_Naixement(){
        assertEquals("1983-08-07", persona.getData_naixement());
    }
   
     /**Proba del getter del dni de Persona
     * 
     */
    @Test
    public void testGetDni(){
        assertEquals("46797529G", persona.getDni());
    }
    
     /**Proba del getter del teléfon de Persona
     * 
     */
    @Test
    public void testGetTelefon(){
        assertEquals("645248965", persona.getTelefon());
    }
    
     /**Proba del getter del teléfon de Persona
     * 
     */
    @Test
    public void testGetMail(){
        assertEquals("pau.bateria@gmail.com", persona.getMail());
    }
    
    
    
    /**Test per probar el setter del id
     * 
     */
    @Test
    public void testSetId(){
        persona.setId(25);
        assertEquals(25, persona.getId());
    }
    
    /**Test per probar el setter del nom
     * 
     */
    @Test
    public void testSetNom(){
        persona.setNom("Jan");
        assertEquals("Jan", persona.getNom());
    }
    
    /**Test per probar el setter del cognom1
     * 
     */
    @Test
    public void testSetCognom1(){
        persona.setCognom1("Puigvert");
        assertEquals("Puigvert", persona.getCognom1());
    }
    
    /**Test per probar el setter del cognom2
     * 
     */
    @Test
    public void testSetCognom2(){
        persona.setCognom2("Garijo");
        assertEquals("Garijo", persona.getCognom2());
    }
    /**Test per probar el setter del data de naixement
     * 
     */
    @Test
    public void testSetDataNaixement(){
        persona.setData_naixement("2016-10-02");
        assertEquals("2016/10/02", persona.getData_naixement());
    }
    
    /**Test per probar el setter del dni
     * 
     */
    @Test
    public void testSetDni(){
        persona.setDni("48752544L");
        assertEquals("48752544L", persona.getDni());
    }
    
    /**Test per probar el setter del telefon
     * 
     */
    @Test
    public void testSetTelefon(){
        persona.setTelefon("999999999");
        assertEquals("999999999", persona.getTelefon());
    }
    
    /**Test per probar el setter del mail
     * 
     */
    @Test
    public void testSetMail(){
        persona.setMail("jan@gmail.com");
        assertEquals("jan@gmail.com", persona.getMail());
    }

}

