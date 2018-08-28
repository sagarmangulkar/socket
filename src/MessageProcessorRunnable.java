import javax.activation.MimetypesFileTypeMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class MessageProcessorRunnable implements Runnable {
    private Socket socketClient;

    public MessageProcessorRunnable(Socket socketClient) {
        this.socketClient = socketClient;
    }

    @Override
    public void run() {
        PrintWriter out = null;
        try {
            out = new PrintWriter(new OutputStreamWriter(socketClient.getOutputStream(), StandardCharsets.ISO_8859_1), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socketClient.getInputStream(), StandardCharsets.ISO_8859_1));
            String text = in.readLine();
            String[] stringArray = text.split(" ");
            String pageName = stringArray[1];
            if (hashMap.containsKey(pageName)){
                IncrementCount(pageName);
            }
            else {
                hashMap.put(pageName, 1);
            }
            String wwwDirectory = System.getProperty("user.dir") + "/www";
            File directory = new File(wwwDirectory);
            if (!(directory.exists() && directory.isDirectory())){
                System.out.println("Error: www directory is not present.");
                System.exit(-1);
            }
            String fileName = wwwDirectory + pageName;
            File file = new File(fileName);
            if(file.exists()) {
                int[] fileContent = ReadFileContent(file, out);
                out.write("HTTP/1.1 200 OK\n");
                out.write("Date: " + GetRFC7321DateFormatFromLong(Calendar.getInstance().getTimeInMillis()) + "\r\n");
                out.write("Server: Distributed System\r\n");
                out.write("Last-Modified: " + GetRFC7321DateFormatFromLong(file.lastModified()) + "\r\n");
                out.write("Accept-Ranges: bytes\r\n");
                out.write("Content-Length: " + fileContent.length + "\r\n");
                out.write("Content-Type: " + new MimetypesFileTypeMap().getContentType(file) + "\r\r\n\n");
                for (int i = 0; i < fileContent.length; i++) {
                    out.write(fileContent[i]);
                }
                out.println();
            }
            else {
                out.write("HTTP/1.1 404 OK\n");
                out.write("Date: " + GetRFC7321DateFormatFromLong(Calendar.getInstance().getTimeInMillis()) + "\r\n");
                out.write("Server: Distributed System\r\n");
                out.write("Last-Modified: " + GetRFC7321DateFormatFromLong(file.lastModified()) + "\r\n");
                out.write("Accept-Ranges: bytes\r\n");
                out.write("Content-Length: 22\r\n");
                out.write("Content-Type: " + new MimetypesFileTypeMap().getContentType(file) + "\r\r\n\n");
                out.println("<h1>404 Not Found</h1>");
            }
            System.out.println(pageName + " |" + (socketClient.getInetAddress().toString().replace(':', '.')).replace('/', ' ') + " | " + socketClient.getPort() + " | " + hashMap.get(pageName));
            socketClient.close();
            out.flush();
            out.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            System.out.println("Blank request from Client.");
        }
    }

    public static String GetRFC7321DateFormatFromLong(Long date){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        return dateFormat.format(date);
    }

    public static int[] ReadFileContent(File file, PrintWriter out){
        BufferedReader bufferedReader = null;
        int[] fileContentIntArray = new int[(int)file.length()];
        int fileNextLine;
        int i = 0;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.ISO_8859_1));
            while ((fileNextLine = bufferedReader.read()) != -1) {
                fileContentIntArray[i++] = fileNextLine;
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileContentIntArray;
    }

    public static synchronized void IncrementCount(String pageName){
        Integer newCount = hashMap.get(pageName) + 1;
        hashMap.replace(pageName, newCount);
    }

    private static HashMap<String, Integer> hashMap = new HashMap<>();
}
