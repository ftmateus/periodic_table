import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonConverter {

    private JSONArray json = new JSONArray();
    private ResultSet rs;
    private final FileOutputStream fosJson;

    public JsonConverter(ResultSet rs) throws IOException
    {       
        this.rs = rs;

        File fjson = new File("elements.json");
        fjson.createNewFile();
        fosJson = new FileOutputStream(fjson);
    }    

    public void build() throws SQLException, IOException
    {
        int i = 0;
        while(rs.next())
        {
            JSONObject jsonObj = new JSONObject();
            int atomic_number = rs.getInt("atomic_number");
            String symbol = rs.getString("symbol");
            int element_period = rs.getInt("element_period");
            int element_group = rs.getInt("element_group");
            float atomic_mass = rs.getFloat("atomic_mass");
            String block = rs.getString("block");
            String name = rs.getString("name_en");
            String occurence = rs.getString("natural_occurence");
            String type = rs.getString("type_name");
            boolean radioactive = rs.getInt("radioactive") == 1;
            
            jsonObj.put("name", name);
            jsonObj.put("atomic_number", atomic_number);
            jsonObj.put("symbol", symbol);
            jsonObj.put("element_period", element_period);
            jsonObj.put("element_group", element_group);
            jsonObj.put("atomic_mass", atomic_mass);
            jsonObj.put("atomic_mass", atomic_mass);
            jsonObj.put("block", block);
            jsonObj.put("occurence", occurence);
            jsonObj.put("type", type);
            jsonObj.put("radioactive", radioactive);

            json.put(i, jsonObj);

            i++;
        }

        fosJson.write(json.toString(3).getBytes());

        fosJson.close();
        

    }
}
