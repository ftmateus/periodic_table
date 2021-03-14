import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.ws.rs.core.Response.Status;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import static main.SystemUtils.*;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class PeriodicTableServer {

    //private static Logger Log = Logger.getLogger(EmailServerRest.class.getName());
    private static final String LOG_FILENAME = "Log.txt";

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s\n");
    }

    public static void main(String[] args)
            throws NoSuchAlgorithmException, URISyntaxException, ClassNotFoundException, SQLException, IOException 
    {
        
        // if(System.console() == null)
        //     System.setOut(new PrintStream(new File(APP_LOCATION + OS_SLASH + LOG_FILENAME)));

        // String tlsFolderLocation = APP_LOCATION + OS_SLASH + "tls";

        // System.setProperty("javax.net.ssl.keyStore", tlsFolderLocation + OS_SLASH + "server.ks");
        // System.setProperty("javax.net.ssl.keyStorePassword", "password");
        // System.setProperty("javax.net.ssl.trustStore", tlsFolderLocation + OS_SLASH + "truststore.ks");
        // System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

        // InetAddress localHost = InetAddress.getLocalHost();
        // String domain = localHost.getHostName();

        // HttpsURLConnection.setDefaultHostnameVerifier(new InsecureHostnameVerifier());

        // ResourceConfig config = new ResourceConfig();

        // config.register(Server.class);

        // String httpsURI = String.format("https://%s/", "0.0.0.0");
        // JdkHttpServerFactory.createHttpServer(URI.create(httpsURI), config, SSLContext.getDefault());
        // System.out.println(String.format("%s REST Server ready @ %s\n", domain, httpsURI));

        // String httpURI = String.format("http://%s/", "0.0.0.0");
        // JdkHttpServerFactory.createHttpServer(URI.create(httpURI), config);
        // System.out.println(String.format("%s REST Server ready @ %s\n", domain, httpURI));

        Class.forName("org.sqlite.JDBC");
        
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:periodic_table.db"))
        {
            PreparedStatement st = connection.prepareStatement("SELECT * FROM PeriodicTable");
            st.setQueryTimeout(30);  // set timeout to 30 sec.
            ResultSet rs = st.executeQuery();

            new HtmlElement(rs, "block").build();
            rs = st.executeQuery();
            new HtmlElement(rs, "type").build();
            rs = st.executeQuery();
            new HtmlElement(rs, "radioactivity").build();
        }
    }

}
