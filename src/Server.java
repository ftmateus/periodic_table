import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.Path;
import java.io.File;
import java.io.FileInputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.ws.rs.WebApplicationException;
import java.net.URI;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.net.InetAddress;
import java.io.File;

import javax.ws.rs.PathParam;

import static main.SystemUtils.*;

@Path("/")
public class Server 
{
    /**https://en.wikipedia.org/wiki/Regular_expression*/
    private static final String IMAGE_TYPES_REGEX = ".(png|jpeg|jpg|gif|tiff)";

    @GET
    @Path("/")
    @Produces(MediaType.TEXT_HTML)
    public static byte[] getIndexPage(@HeaderParam("host") String host, @HeaderParam("origin") String origin) throws IOException
    {
        return getDocument("index.html", host, origin);
    }

    @GET
    @Path("{path : .+" + IMAGE_TYPES_REGEX + "}")
    @Produces("image/*")
    public static byte[] getImage(@PathParam("path") String path,
    @HeaderParam("host") String host, @HeaderParam("origin") String origin) throws IOException
    {
        return getDocument(path, host, origin);
    }

    @GET
    @Path("{path : .+.pdf}")
    @Produces("application/pdf")
    public static byte[] getPDF(@PathParam("path") String path,
    @HeaderParam("host") String host, @HeaderParam("origin") String origin) throws IOException
    {
        return getDocument(path, host, origin);
    
    }

    @GET
    @Path("{path : .+.txt}")
    @Produces(MediaType.TEXT_PLAIN)
    public static byte[] getTxt(@PathParam("path") String path,
    @HeaderParam("host") String host, @HeaderParam("origin") String origin) throws IOException
    {
        return getDocument(path, host, origin);
    }

    @GET
    @Path("{path : .+.json}")
    @Produces(MediaType.APPLICATION_JSON)
    public static byte[] getJSON(@PathParam("path") String path,
    @HeaderParam("host") String host, @HeaderParam("origin") String origin) throws IOException
    {
        return getDocument(path, host, origin);
    }

    @GET
    @Path("{path : .+.xml}")
    @Produces(MediaType.APPLICATION_XML)
    public static byte[] getXML(@PathParam("path") String path,
    @HeaderParam("host") String host, @HeaderParam("origin") String origin) throws IOException
    {
        return getDocument(path, host, origin);
    }


    @GET
    @Path("{path : .+}")
    public static byte[] getDocument(@PathParam("path") String path, 
    @HeaderParam("host") String host, @HeaderParam("origin") String origin) throws IOException
    {
        try {
            System.out.println("Received request to get file " + path);
            System.out.println("Host: " + host);
            //System.out.println("Origin: " + origin);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
            LocalDateTime now = LocalDateTime.now();  
            System.out.println("Time: " + dtf.format(now));

            byte[] fileContents = readFileContents(APP_LOCATION + OS_SLASH + path);
            System.out.println();
            return fileContents;
        }
        catch(FileNotFoundException e)
        {
            if(path.equals("favicon.ico"))
            {
                System.out.println("Page icon not available.\n");
                throw new WebApplicationException(
                    Response.status(404).build());
            }
            System.out.println("File " + path + " was not found");
            System.out.println("Redirecting to main page...\n");

            throw new WebApplicationException(
            Response.status(Status.MOVED_PERMANENTLY).
            header("Location", "https://"+host).build());
            //temporaryRedirect(URI.create(uriRedirect)).build());

        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new WebApplicationException(
            Response.status(500).build());
        } 
    }


    private static byte[] readFileContents(String path) throws IOException
    {
        File f = new File(path);
        FileInputStream fis = new FileInputStream(f); 
        byte[] fileContents = new byte[(int) f.length()];
        fis.read(fileContents, 0, fileContents.length);
        fis.close();
        return fileContents;
    }
}