package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**Clase per poder fer la conexió amb la base de dades i la desconexió de forma
 * segura
 * @author Pau Castell Galtes
 */
public class ConexioBBDD {
    private static final String JDBD_URL = "jdbc:mysql://localhost:3306/escola_bressol?useSSL=false";
    private static final String USUARI =  "root";
    private static final String PASSWORD = "1234";
    private static final Logger LOGGER = Logger.getLogger(ConexioBBDD.class.getName());
    private Connection conexio;
    
    
    /**Métode per realizar la conexió amb la base de dades utilitzant les credencials
     * USUARI i PASSWORD.
     * @return la conexió establerta amb la base de dades.
     * @throws SQLException si hi ha algun error al intentar conectar amb la base de dades.
     */
    public Connection conectar() throws SQLException{

        System.setProperty("https.protocols", "TLSv1.3");
        
        conexio = DriverManager.getConnection(JDBD_URL,USUARI,PASSWORD);
        
        LOGGER.info("Connexió a la base de dades.");
        
        return conexio;       
    }
    
    
    /**Métode per tancar la conexió amb la base de dades.
     * @throws SQLException, si hi ha algun error al tancar la conexió.
     */
    public void tancarConexio(){
        try{
            
            if(conexio != null){
                conexio.close();
                LOGGER.info("Conexió a la base de dades tancada.");
            }else{
                LOGGER.info("Conexió a la base de dades tancada.");
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(ConexioBBDD.class.getName()).log(Level.SEVERE,
                    "Error al intentar tancar la conexió en el métode tancarConexio", ex);
        }
    }
}
