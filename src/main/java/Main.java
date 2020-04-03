//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.io.IOException;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.util.List;
//
//public class Main
//{
//    public static void main(String args[]) throws Exception
//    {
//        List<Path> fileList = ReadFiles.readFilesWithinFolders(Paths.get("src/main/resources/Assignment Two/"));
//        for (Path p : fileList) {
//            if (!(p.endsWith("readchg.txt") ||
//                    p.endsWith("readmefb.txt") ||
//                    p.endsWith("readmela.txt") ||
//                    p.endsWith("readmefr") ||
//                    p.endsWith("ReadMe.txt") ||
//                    p.endsWith("fbisdtd.dtd") ||
//                    p.endsWith("fr94dtd") ||
//                    p.endsWith("ftdtd") ||
//                    p.endsWith("latimesdtd.dtd") ||
//                    p.endsWith("readchg") ||
//                    p.endsWith("readfrcg") ||
//                    p.endsWith("readmeft") ||
//                    p.endsWith(".DS_Store")))
//            {
//                Indexer.parse(p);
//            }
//        }
//        File file = new File("src/main/resources/topics");
//        BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
//        Searcher.search(bufferedReader);
//    }
//}
