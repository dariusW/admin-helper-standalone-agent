package pl.edu.agh.adminmanager.monitor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

public class SystemPropertyScannerUtil {

    public static void main(String[] args) throws IOException {
    	BufferedWriter n = new BufferedWriter(new FileWriter(new File("systemPropertys.txt")));
        for (Map.Entry<Object, Object> e : System.getProperties().entrySet()) {
        	System.out.println(e);
            n.write(e.toString()+"\n");
        }
        n.close();
    }
}
