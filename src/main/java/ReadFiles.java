import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ReadFiles
{
    public static List<Path> readFilesWithinFolders(Path folderPath) throws IOException
    {
        List<Path> fileList = Files.walk(Paths.get(String.valueOf(folderPath)))
                .filter(Files::isRegularFile)
                .collect(Collectors.toList());
        return fileList;
    }
//    public static void main(String args[])throws IOException
//    {
//        List<Path> fileList = readFilesWithinFolders(Paths.get("src/main/resources/fr94"));
//        for(Path p : fileList)
//        {
//            System.out.println(p);
//        }
//
//    }

}
