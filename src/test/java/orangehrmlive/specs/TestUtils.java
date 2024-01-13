package orangehrmlive.specs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Map;

public class TestUtils {
    public static Map<String, Object> convertJsonStringToMap(String json) {
        Map<String, Object> newObj = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            newObj = objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {
            });
        }catch (JsonProcessingException j) {
            j.printStackTrace();
        }
        return newObj;
    }
    public static Map<String, Object> readJsonFileToMap(String fileName) {
        File jsonFile = new File(System.getProperty("user.dir") + "/src/test/resources/orangehrmlive/orangehrmrequest/" + fileName);
        Map<String, Object> newObj = null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            newObj = objectMapper.readValue(jsonFile, new TypeReference<Map<String, Object>>() {
            });
        }catch (IOException j) {
            j.printStackTrace();
        }
        return newObj;
    }

    public static void deleteReportBeforeStart() {

        Path rootFolderPath = Paths.get(System.getProperty("user.dir"), "target/gatling");
        try {
            Files.walkFileTree(rootFolderPath, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    // Delete the file
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    // Handle the error, e.g., log it
                    System.err.println("Failed to visit file: " + file.toString() + ", Error: " + exc.getMessage());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    // Delete the directory after all its contents have been deleted
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
            System.out.println("All directories inside the folder deleted successfully.");
        } catch (IOException e) {
            System.out.println("Error deleting directories: " + e.getMessage());
        }
    }
}
