import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import java.nio.file.Files;

public class Sender extends Thread {
    private final String toId = "120988325";
    private String link;

    public Sender(String link) {
        this.link = link;
    }

    private String boundary = Long.toHexString(System.currentTimeMillis()); // Just generate some unique random value.
    private URLConnection connections;

    public void run() {
        URL url = null;
        try {
            url = new URL(this.link);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        File file = new File(url.getPath());
        sendPhotoTo(file, toId);
       /* try {
            URL url = new URL("https://api.telegram.org/bot479139427:AAFfFSL6q5hwuYXA_JceJKEh9CxIajUu740/sendMessage?text=" + link + "&chat_id=120988325");
            HttpURLConnection uc = (HttpURLConnection) url.openConnection();
            uc.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
            uc.setRequestProperty("Content-Language", "en-US");
            uc.setRequestMethod("GET");
            uc.connect();
            System.out.println(uc.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }

    public void sendPhotoTo(File binaryFile, String chat_id) {
        {
            try {
                String url = "https://api.telegram.org/bot479139427:AAFfFSL6q5hwuYXA_JceJKEh9CxIajUu740/sendPhoto?chat_id=" + chat_id;
                connections = new URL(url).openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        connections.setDoOutput(true);
        connections.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
        String charset = "UTF-8";
        try (
                OutputStream output = connections.getOutputStream();
                PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
        ) {
            // Send binary file.
            String CRLF = "\r\n";
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"photo\"; filename=\"" + binaryFile.getName() + "\"").append(CRLF);
            writer.append("Content-Type: " + URLConnection.guessContentTypeFromName(binaryFile.getName())).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            Files.copy(binaryFile.toPath(), output);
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.

            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream response = connections.getInputStream();
            System.out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


