import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class BotUtils {

    private static Logger logger = LoggerFactory.getLogger(BotUtils.class);

    public static ArrayList<String> loadAllDlc(String filepath) {
        ArrayList<String> temp = new ArrayList<>();
        try {
            Scanner s = new Scanner(new File(filepath));
            while (s.hasNextLine()){
                temp.add(s.nextLine());
            }
            s.close();
        } catch (FileNotFoundException f) {
            logger.error("File with the DLC could not be found, check the path.");
        }
        return temp;
    }

    public static IDiscordClient createClient(String token, boolean login) {
        ClientBuilder clientBuilder = new ClientBuilder();
        clientBuilder.withToken(token);
        try {
            if (login) {
                return clientBuilder.login();
            } else {
                return clientBuilder.build();
            }
        } catch (DiscordException e) {
            logger.error("Couldn't create the discord client.");
            return null;
        }
    }

}
