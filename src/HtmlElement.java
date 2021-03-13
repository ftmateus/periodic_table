import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class HtmlElement 
{
    private final FileOutputStream fos;
    private String category;

    private StringBuilder table_1 = new StringBuilder(30*1024);
    private StringBuilder table_2 = new StringBuilder();
    private StringBuilder table_3 = new StringBuilder(10*1024);
    private ResultSet rs;

    public HtmlElement(ResultSet rs, String category) throws IOException
    {
        this.rs = rs;
        this.category = category;
        File f = new File("docs/" + category + ".html");
        f.createNewFile();
        fos = new FileOutputStream(f);
        fos.write(readFileContents("base_html.html"));
    }

    public void build() throws SQLException, IOException
    {
        while(rs.next())
        {
            int atomic_number = rs.getInt("atomic_number");
            String symbol = rs.getString("symbol");
            int element_period = rs.getInt("element_period");
            int element_group = rs.getInt("element_group");
            float atomic_mass = rs.getFloat("atomic_mass");
            String block = rs.getString("block");
            String name = rs.getString("name");
            String occurence = rs.getString("natural_occurence_id");
            int type = rs.getInt("element_type_id");
            int radioactive = rs.getInt("radioactive");
            if(element_group < 0)
            {
                switch(category)
                {
                    case "block": table_3.append(createHtmlElementBlock(atomic_number, symbol, element_period - 5, -element_group, atomic_mass, name, occurence, block)); break;
                    case "type": table_3.append(createHtmlElementType(atomic_number, symbol, element_period - 5, -element_group, atomic_mass, name, occurence, type)); break;
                    case "radioactivity": table_3.append(createHtmlElementRadioactive(atomic_number, symbol, element_period - 5, -element_group, atomic_mass, name, occurence, radioactive)); break;
                }
                
            }
            else
            {
                switch(category)
                {
                    case "block": table_1.append(createHtmlElementBlock(atomic_number, symbol, element_period, element_group, atomic_mass, name, occurence, block)); break;
                    case "type": table_1.append(createHtmlElementType(atomic_number, symbol, element_period, element_group, atomic_mass, name, occurence, type)); break;
                    case "radioactivity": table_1.append(createHtmlElementRadioactive(atomic_number, symbol, element_period, element_group, atomic_mass, name, occurence, radioactive)); break;
                }
            }
        }
        fos.write("\t\t<div class=\"table_container\">\n".getBytes());
        fos.write(table_1.toString().getBytes());
        fos.write("\t\t</div>\n".getBytes());
        fos.write("\t\t<div class=\"table3_container\">\n".getBytes());
        fos.write(table_3.toString().getBytes());
        fos.write("\t\t</div>\n".getBytes());
        fos.write("\t</body>\n</html>".getBytes());
        fos.close();
    }

    private String createHtmlElementType(int atomic_number, String symbol, int element_period, int element_group,
                                    float atomic_mass, String name, String occurence, int type)
    {
        
        String color = "";
        switch (type)
        {
            case 0: color = "white"; break; //"tomato"
            case 1: color = "red"; break;
            case 2: color = "yellow"; break;
            case 3: color = "blue"; break;
        }

        String border_style = "";
        switch(occurence)
        {
            case "P": border_style = "border-style: solid"; break;
            case "D": border_style = "border-style: dashed"; break;
            case "S": border_style = "border-style: dotted"; break;
        }

        String atomic_mass_text;
        if(atomic_number >= 100)
        {
            atomic_mass_text = String.format("%.1f", atomic_mass);
        }
        else
        {
            atomic_mass_text = String.format("%.2f", atomic_mass);
        }

        return "\t\t\t<div class=\"element_container\" style=\"grid-row-start:" + element_period + "; grid-column-start: " + element_group + "; background-color: " + color + "; " + border_style + ";\" onclick=\"changeIframe('" + name +"');\">\n"
                    +    "\t\t\t\t<h1 class=\"element_symbol\">" + symbol + "</h1>\n"
                    +    "\t\t\t\t<h1 class=\"atomic_number\">" + atomic_number + "</h1>\n"
                    +    "\t\t\t\t<h1 class=\"atomic_mass\">" + atomic_mass_text + "</h1>\n"
                    +    "\t\t\t</div>\n";
    }

    private String createHtmlElementBlock(int atomic_number, String symbol, int element_period, int element_group,
                                     float atomic_mass, String name, String occurence, String block)
    {
        String color = "";
        switch (block)
        {
            case "S": color = "red"; break; //"tomato"
            case "P": color = "yellow"; break;
            case "D": color = "blue"; break;
            case "F": color = "green"; break;
        }

        String border_style = "";
        switch(occurence)
        {
            case "P": border_style = "border-style: solid"; break;
            case "D": border_style = "border-style: dashed"; break;
            case "S": border_style = "border-style: dotted"; break;
        }

        String atomic_mass_text;
        if(atomic_number >= 100)
        {
            atomic_mass_text = String.format("%.1f", atomic_mass);
        }
        else
        {
            atomic_mass_text = String.format("%.2f", atomic_mass);
        }

        return "\t\t\t<div class=\"element_container\" style=\"grid-row-start:" + element_period + "; grid-column-start: " + element_group + "; background-color: " + color + "; " + border_style + ";\" onclick=\"changeIframe('" + name +"');\">\n"
                    +    "\t\t\t\t<h1 class=\"element_symbol\">" + symbol + "</h1>\n"
                    +    "\t\t\t\t<h1 class=\"atomic_number\">" + atomic_number + "</h1>\n"
                    +    "\t\t\t\t<h1 class=\"atomic_mass\">" + atomic_mass_text + "</h1>\n"
                    +    "\t\t\t</div>\n";
    }

    private String createHtmlElementRadioactive(int atomic_number, String symbol, int element_period, int element_group,
                                    float atomic_mass, String name, String occurence, int radioactive)
    {
        
        String color = "";
        switch (radioactive)
        {
            case 0: color = "green"; break; 
            case 1: color = "red"; break; 
        }

        String border_style = "";
        switch(occurence)
        {
            case "P": border_style = "border-style: solid"; break;
            case "D": border_style = "border-style: dashed"; break;
            case "S": border_style = "border-style: dotted"; break;
        }

        String atomic_mass_text;
        if(atomic_number >= 100)
        {
            atomic_mass_text = String.format("%.1f", atomic_mass);
        }
        else
        {
            atomic_mass_text = String.format("%.2f", atomic_mass);
        }

        return "\t\t\t<div class=\"element_container\" style=\"grid-row-start:" + element_period + "; grid-column-start: " + element_group + "; background-color: " + color + "; " + border_style + ";\" onclick=\"changeIframe('" + name +"');\">\n"
                    +    "\t\t\t\t<h1 class=\"element_symbol\">" + symbol + "</h1>\n"
                    +    "\t\t\t\t<h1 class=\"atomic_number\">" + atomic_number + "</h1>\n"
                    +    "\t\t\t\t<h1 class=\"atomic_mass\">" + atomic_mass_text + "</h1>\n"
                    +    "\t\t\t</div>\n";
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