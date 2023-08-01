import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utilities {

    /** 
    * clean up input string by removing eveything except a-z, A-Z, 0-9 and whitespace
    * @param String  word to be cleaned.
    * @return String  cleaned word.
    */
    public static String cleanString(String str){
        Pattern pt = Pattern.compile("[^a-zA-Z0-9.,:\\-\\s]");
        Matcher match= pt.matcher(str);
        while(match.find()) {
            String s = match.group();
            str = str.replaceAll("\\"+s, "");
        }
        return str;
    }

    /** 
    * read lines from a file and return them as a list of String
    * @param String  path of the file to be read.
    * @return List<String>  list of lines read from the file.
    */
    public static List<String> readStringListFromFile(String filePath) {
        List<String> result = new ArrayList<String>();
        try {
            System.out.println("Reading from file " + filePath);
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
               result.add(Utilities.cleanString(line).trim());
            }
            reader.close();
            System.out.println("Completed reading from file " + filePath);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    
    /** 
    * read lines from a file and return them as a list of Taps
    * @param String  path of the file to be read.
    * @return List<Taps>  list of Taps read from the file.
    */
    public static List<Taps> readTapsListFromFile(String filePath) {
        List<Taps> result = new ArrayList<Taps>();
        try {
            System.out.println("Reading from file " + filePath);
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line;
            boolean headerSkipped = false;
            while ((line = reader.readLine()) != null) {
                if(headerSkipped){
                    result.add(new Taps(Utilities.cleanString(line).trim()));
                }
                headerSkipped = true;
            }
            reader.close();
            System.out.println("Completed reading from file " + filePath);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }

    /** 
    * write lines to a file and from a list of String
    * @param String  path of the file to be written to.
    * @param List<String>  list of lines writte to the file.
    */
    public static void writeToFileFromStringList(String filePath, List<String> strList) {
        try {
            System.out.println("Writing to file " + filePath);
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            for(String str:strList){
                writer.write(str + "\n");
            }
            writer.close();
            System.out.println("Completed writing to file " + filePath);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
