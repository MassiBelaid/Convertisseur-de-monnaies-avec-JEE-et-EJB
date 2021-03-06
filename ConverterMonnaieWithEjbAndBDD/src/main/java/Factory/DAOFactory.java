package Factory;

import DAO.MonnaieDAO;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOFactory {
    private static final String FICHIER_PROPERTIES       = "dao.properties";
    private static final String PROPERTY_URL             = "url";
    private static final String PROPERTY_DRIVER          = "driver";
    private static final String PROPERTY_NOM_UTILISATEUR = "nomutilisateur";
    private static final String PROPERTY_MOT_DE_PASSE    = "motdepasse";

    private String              url;
    private String              username;
    private String              password;

    DAOFactory( String url, String username, String password ) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    public static DAOFactory getInstance() {
        Properties properties = new Properties();
        String url;
        String driver;
        String nomUtilisateur;
        String motDePasse;

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        InputStream fichierProperties = classLoader.getResourceAsStream(FICHIER_PROPERTIES);

        /*if ( fichierProperties == null ) {
            throw new RuntimeException( "Le fichier properties " + FICHIER_PROPERTIES + " est introuvable." );
        }

        try {
            properties.load( fichierProperties );
            url = properties.getProperty( PROPERTY_URL );
            driver = properties.getProperty( PROPERTY_DRIVER );
            nomUtilisateur = properties.getProperty( PROPERTY_NOM_UTILISATEUR );
            motDePasse = properties.getProperty( PROPERTY_MOT_DE_PASSE );
        } catch ( IOException e ) {
            throw new RuntimeException( "Impossible de charger le fichier properties " + FICHIER_PROPERTIES, e );
        }*/

        try {
            Class.forName( "com.mysql.cj.jdbc.Driver" );
        } catch ( ClassNotFoundException e ) {
            throw new RuntimeException( "Le driver est introuvable dans le classpath.", e );
        }

        DAOFactory instance = new DAOFactory("jdbc:mysql://localhost:3306/converter", "root", "" );

        return instance;
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection( url, username, password );
    }

    public MonnaieDAO getMonnaiDao(){
        return new MonnaieDAO(this);
    }
}
