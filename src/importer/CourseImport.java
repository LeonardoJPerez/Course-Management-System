package cms.importer;

import java.util.List;

/**
 * Created by Leonardo on 9/24/2016.
 */
public class CourseImport extends BaseImport {

    public CourseImport(String importFilePath) {
        super(importFilePath);
    }

    @Override
    public void process() {
        List<String> fileLines = this.readFile();

    }


}
