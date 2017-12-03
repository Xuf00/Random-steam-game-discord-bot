import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Logger;

public class BotUtils {

    public static ArrayList<String> loadAllDlc(String filepath) {
        ArrayList<String> temp = new ArrayList<>();
        try {
            Scanner s = new Scanner(new File(filepath));
            while (s.hasNextLine()){
                temp.add(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException f) {
            System.out.println("The list of DLC file could not be found. Check the filepath provided.");
        }
        return temp;
    }

    public static String loadBotToken(String filepath) {
        String temp = "";
        try {
            Scanner s = new Scanner(new File(filepath));
            while (s.hasNextLine()){
                temp = s.nextLine();
            }
            s.close();
        } catch (FileNotFoundException f) {
            System.out.println("Bot token could not be found, ensure you have added it into botconfig.txt.");
        }
        return temp;
    }

}
