package estel.solapp.common;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import estel.solapp.models.Alumne;
import estel.solapp.models.Empleat;
import estel.solapp.models.Persona;
import estel.solapp.models.User;

public class CommControllerTest {

    User user;
    Empleat empleat;

    Alumne alumne;

    Persona persona;

    SingletonSessio singletonSessio;

    @Before
    public void setUp(){

        user=new User("juan","hola");


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
}