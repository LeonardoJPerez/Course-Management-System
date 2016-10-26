package cms.importer;

import cms.core.models.Address;

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
    private int lineCount;

    public BaseImport(String importFilePath){
        this.importFilePath = importFilePath;
    }

    public abstract void process();

    protected List<String> readFile(){
        List<String> lines = new ArrayList<>();

        try {
            Path filePath = Paths.get(this.importFilePath);
            List<String> allLines = new ArrayList<>();
            allLines = Files.readAllLines(filePath, Charset.forName("UTF-8"));

            for (Integer i = 0; i < allLines.size(); i++){
                if (allLines.get(i).length() > 0){
                    lines.add(allLines.get(i));
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }

        this.lineCount = lines.size();
        return lines;
    }

    protected Address buildAddress(String addressStr){

        Address address = null;
        if (addressStr.length() > 0) {
            String[] splitted = addressStr.split(" ");
            String zipCode = splitted[splitted.length - 1].trim();
            String line1 = addressStr.replace(zipCode, "").trim();

            address = new Address(line1, "", "", zipCode, "US");
        }

        return address;
    }

    protected int getLineCount() {
        return lineCount;
    }
}
