package idealist.util;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Charles Cui<lfylccxm@hotmail.com> on 2021-05-19 00:12:54
 */
public abstract class FileUtils {
    public static File[] listFiles(File file, boolean includeSubDirectory) {
        if (includeSubDirectory) {
            ArrayList<File> files = new ArrayList<>();
            List<File> list = new LinkedList<>();
            list.add(file);
            for (int i = 0; i < list.size(); i++) {
                File[] temp = list.get(i).listFiles();
                if (temp == null) {
                    continue;
                }
                for (File fileTemp : temp) {
                    files.add(fileTemp);
                    if (fileTemp.isDirectory()) {
                        list.add(fileTemp);
                    }
                }
            }
            return files.toArray(new File[0]);
        } else {
            return file.listFiles();
        }
    }
}
