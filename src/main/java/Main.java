import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.security.SecureRandom;

public class Main {

    public static void main( String[] args ) throws IOException {
        for (int i = 0; i<50; i++){
            new Parser().start();
        }
    }
}
