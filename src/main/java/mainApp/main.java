package mainApp;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class main {

    private final static String url = "https://acg.rip/.xml";
    private static String filterListFilePath = "./filter.csv";
    private static final String downloadLocation = "Y:\\temp\\torrentFile/";
    private static int sleepMin;

    public static void main(String[] args) throws InterruptedException{
        System.out.println("helloworld");

        while (true) {

            List<Item> itemList = getRssList();
            List<Filter> filterList = getFilterList();

            List<Item> newList = filterItemList(itemList, filterList);

            downloadItem(newList);

            Thread.sleep(1000 * sleepMin);
        }
    }

    private static void downloadItem(List<Item> list){
        for (Item item : list) {
            try (BufferedInputStream in = new BufferedInputStream(new URL(item.getTorrentLink()).openStream());
                 FileOutputStream fileOutputStream = new FileOutputStream(downloadLocation + item.getTitle() + ".torrent")) {
                byte dataBuffer[] = new byte[1024];
                int bytesRead;
                while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                    fileOutputStream.write(dataBuffer, 0, bytesRead);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static List<Item> filterItemList(List<Item> itemList, List<Filter> filterList){
        List<Item> result = new ArrayList<>();
        for (Item item : itemList){
            for (Filter filter : filterList){
                if (filter.filter(item)) {
                    result.add(item);
                    System.out.println("Found: " + item.getTitle());
                }
            }
        }
        return result;
    }

    private static List<Filter> getFilterList(){
        List<Filter> result = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(filterListFilePath))){
            String line;
            while ((line = reader.readLine()) != null){
//                System.out.println(line);
                result.add(new Filter(line));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    private static List<Item> getRssList(){
        List<Item> updatedList = new ArrayList();

        try {
            URL urlItem = new URL(url);
            URLConnection con = urlItem.openConnection();
            InputStream is = con.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));

            String line;
            while ((line = br.readLine())!= null){
                line = line.trim();
                if (line.startsWith("<ttl>")){
                    sleepMin = Integer.parseInt(line.replaceAll("<[/]*ttl>", ""));
                    System.out.println("Sleep timer set to " + sleepMin);
                }
                if (line.trim().equals("<item>")){
                    Item item = new Item();
                    while ((line = br.readLine()) != null){
                        line = line.trim();
                        if (line.startsWith("<title>")){
                            line = line.replaceAll("<[/]*title>", "");
//                            System.out.println("The title is: " + line.replaceAll("<[/]*title>", ""));
                            item.setTitle(line);
                        }
                        else if (line.startsWith("<enclosure")){
                            line = line.replaceAll("<[/]*enclosure[>]*", "").trim();
//                            System.out.println("The enclosure is: " + line);
                            String[] tabs = line.split(" ", -1);
                            String torrentUrl = tabs[0].replace("url=\"", "").replaceAll("\"", "");
//                            System.out.println(torrentUrl);
                            item.setTorrentLink(torrentUrl);
                        }
                        if (line.contains("</item>")) {
                            updatedList.add(item);
                            break;
                        }
                    }
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
        return updatedList;
    }
}
