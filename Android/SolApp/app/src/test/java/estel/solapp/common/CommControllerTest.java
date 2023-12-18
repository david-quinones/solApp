package estel.solapp.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import estel.solapp.models.Alumne;
import estel.solapp.models.Aula;
import estel.solapp.models.Empleat;
import estel.solapp.models.Missatge;
import estel.solapp.models.Persona;
import estel.solapp.models.User;

public class CommControllerTest {

    User user;
    Empleat empleat;

    Alumne alumne;

    Persona persona;

    SingletonSessio singletonSessio;

    Aula aula;

    Missatge missatge;

    @Before
    public void setUp(){

        user=new User("juan","hola");
        empleat = new Empleat();
        alumne =new Alumne();
        persona = new Persona();
        aula = new Aula();
        missatge = new Missatge();

    }

    @Test
    public void doLogin() {

        assertEquals(1,CommController.doLogin("Juan","hola").getReturnCode());

    }
    @Test
    public void isLogged() {

        assertEquals(true,CommController.isLogged());

    }
    @Test
    public void doLogout() {

        assertEquals(1,CommController.doLogout().getReturnCode());
    }

    @Test
    public void consultaPerfil() {

        assertEquals(1,CommController.consultaPerfil().getReturnCode());

    }

    @Test
    public void modificaPerfil() {

        assertEquals(1,CommController.modificaPerfil(persona).getReturnCode());

    }

    @Test
    public void llistarUsuaris() {

        assertEquals(1,CommController.llistarUsuaris().getReturnCode());

    }

    @Test
    public void afegirUsuari() {

        assertEquals(1,CommController.afegirUsuari(user).getReturnCode());

    }

    @Test
    public void modificarUsuari() {

        assertEquals(1,CommController.modificarUsuari(user).getReturnCode());

    }

    @Test
    public void afegirEmpleat() {

        assertEquals(1,CommController.afegirEmpleat(empleat,user).getReturnCode());

    }

    @Test
    public void modificarEmpleat() {

        assertEquals(1,CommController.modificarEmpleat(empleat).getReturnCode());

    }

    @Test
    public void eliminarEmpleat() {

        assertEquals(1,CommController.eliminarEmpleat(empleat).getReturnCode());

    }

    @Test
    public void llistarEmpleats() {

        assertEquals(1,CommController.llistarEmpleats().getReturnCode());

    }

    @Test
    public void afegirAlumne() {

        assertEquals(1,CommController.afegirAlumne(alumne,user).getReturnCode());

    }

    @Test
    public void llistarAlumnes() {

        assertEquals(1,CommController.llistarAlumnes().getReturnCode());

    }

    @Test
    public void modificarAlumne() {

        assertEquals(1,CommController.modificarAlumne(alumne).getReturnCode());

    }

    @Test
    public void eliminarAlumne() {

        assertEquals(1,CommController.eliminarAlumne(alumne).getReturnCode());

    }

    @Test
    public void afegirAula() {

        assertEquals(1,CommController.afegirAula(aula).getReturnCode());

    }

    @Test
    public void llistarAules() {

        assertEquals(1,CommController.llistarAules().getReturnCode());

    }

    @Test
    public void modificarAula() {

        assertEquals(1,CommController.modificarAula(aula).getReturnCode());

    }

    @Test
    public void eliminarAula() {

        assertEquals(1,CommController.eliminarAula(aula).getReturnCode());

    }

    @Test
    public void safataEntrada() {

        assertEquals(1,CommController.safataEntrada().getReturnCode());

    }

    @Test
    public void safataSortida() {

        assertEquals(1,CommController.safataSortida().getReturnCode());

    }

    @Test
    public void eliminarMissatge() {

        assertEquals(1,CommController.eliminarMissatge(missatge).getReturnCode());

    }

    @Test
    public void enviarMissatge() {

        assertEquals(1,CommController.enviarMissatge(missatge).getReturnCode());

    }
}