package sol.app.quinones.solappquinones.Controllers;

/**
 * Interficei per la delagació d'accions del menu superior a les diferents finestres
 *
 * Defineix metodes que s'han d'implementar per les clases que l'implementen
 *
 * @author david
 */
public interface ITopMenuDelegation {

    void onBtnCrear();
    void onBtnEditar();
    void onBtnEliminar();

}
