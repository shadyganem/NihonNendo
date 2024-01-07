
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HttpHtmlParser {

	public List<MyCandle> parse(String stock) {
    // TODO Auto-generated method stub

		List<MyCandle> candles = null;
		String url = "https://finance.yahoo.com/quote/" + stock + "/history?p=" + stock;

        try {
            // Make HTTP call and get HTML response
            String html = makeHttpRequest(url);

            // Parse HTML
            Document doc = parseHtml(html);

            // Example: Extract the title
        candles =  printTableData(doc);
        } catch (IOException e) {
            e.printStackTrace();
        }
		return candles;
    }

	// Function to make an HTTP request and get HTML response
    public static String makeHttpRequest(String url) throws IOException {
        HttpURLConnection connection = null;
        BufferedReader reader = null;

        try {
            // Create URL object
            URL requestUrl = new URL(url);

            // Open connection
            connection = (HttpURLConnection) requestUrl.openConnection();

            // Set request method
            connection.setRequestMethod("GET");

            // Get input stream from the connection
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            // Read the HTML response
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        } finally {
            // Close resources
            if (reader != null) {
                reader.close();
            }
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    // Function to parse HTML using Jsoup
    public static Document parseHtml(String html) {
        return Jsoup.parse(html);
    }
	    
    public static List<MyCandle> printTableData(Document doc) {
    	
    	List<MyCandle> candles = new ArrayList<MyCandle>();
    	
        Elements tables = doc.select("table");
        for (Element table : tables) {
            Elements rows = table.select("tr");
            
            for (Element row : rows) {
                Elements cells = row.select("td");
                
                boolean skip = false;
                for (Element cell : cells) {
                	if (cell.text().contains("Dividend") || cell.text().contains("adjusted")) {
                		skip = true;
                	}

                }
                if (skip) {
                	continue;
                }
                
                LocalDate localDate = null;
                double open = 0, high = 0, low = 0, close = 0;
                int i = 0;
                for (Element cell : cells) {
                    System.out.print(cell.text() + "\t");
                    
                    switch(i) {   
                    case 0:
                    	String[] strings = cell.text().split(" ");
                    	strings[1] = strings[1].substring(0, 2);
                    	
                    	int month = 1;
                    	if (strings[0].equals("Feb")) {
                    		month = 2;
                    	}
                    	if (strings[0].equals("Mar")) {
                    		month = 3;
                    	}
                    	if (strings[0].equals("Apr")) {
                    		month = 4;
                    	}
                    	if (strings[0].equals("May")) {
                    		month = 5;
                    	}
                    	if (strings[0].equals("Jun")) {
                    		month = 6;
                    	}
                    	if (strings[0].equals("Jul")) {
                    		month = 7;
                    	}
                    	if (strings[0].equals("Aug")) {
                    		month = 8;
                    	}
                    	if (strings[0].equals("Sep")) {
                    		month = 9;
                    	}
                    	if (strings[0].equals("Oct")) {
                    		month = 10;
                    	}
                    	if (strings[0].equals("Nov")) {
                    		month = 11;
                    	}
                    	if (strings[0].equals("Dec")) {
                    		month = 12;
                    	}
                    	localDate = LocalDate.of(Integer.parseInt(strings[2]), month, Integer.parseInt(strings[1]));
                    	
                    	break;
                    
                    case 1:
                    	open = Float.parseFloat(cell.text());
                    	break;
                    	
                    case 2:
                    	high = Float.parseFloat(cell.text());
                    	break;
                    	
                    case 3:
                    	low = Float.parseFloat(cell.text());
                    	break;
                    	
                    case 4:
                    	close = Float.parseFloat(cell.text());
                    	break;
                    }
                    i++;
                    
                    if (i == 5) {
                    	break;
                    }
                }
                if (localDate != null) {
                	MyCandle c = new MyCandle(open, high, low, close, localDate);
                	candles.add(c);
                }
                
                System.out.println();
            }
            System.out.println();
        }
        
        System.out.println("ss " + candles.size());
        return candles;
    }
}
