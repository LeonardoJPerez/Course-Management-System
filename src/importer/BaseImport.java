package cms.importer;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo on 9/24/2016.
 */
public abstract class BaseImport {

    private String importFilePath;

    public BaseImport(String importFilePath){
        this.importFilePath = importFilePath;
    }

    public abstract void process();

    protected List<String> readFile(){
        List<String> lines = new ArrayList<String>();

        try {
            Path filePath = Paths.get(this.importFilePath);
            lines = Files.readAllLines(filePath, Charset.forName("ISO-8859-1"));
        } catch (Exception e) {
            System.out.println(e);
        }

        return lines;
    }
}
