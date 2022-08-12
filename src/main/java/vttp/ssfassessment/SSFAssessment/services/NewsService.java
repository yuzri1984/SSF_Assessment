package vttp.ssfassessment.SSFAssessment.services;


import java.io.Reader;
import java.io.StringReader;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import vttp.ssfassessment.SSFAssessment.models.Articles;
import vttp.ssfassessment.SSFAssessment.repositories.NewsRepo;


@Service
public class NewsService {

    private static final String URL = "https://min-api.cryptocompare.com/data/v2/news/?lang=EN";

    
    public String Data;
    public Integer id;
    public Integer published_on;
    public String   title;
    public String   link;
    public String   imageurl;
    public String   body;
    public String   tags;
    public String   categories;



    @Autowired
    private NewsRepo newsRepo;

    public List<Articles> getArticles(String Data) {

        
        Optional<String> opt = newsRepo.get(Data);
        String payload;

        System.out.printf(">>> Data: %s\n", Data);

               if (opt.isEmpty()) {

            System.out.println("Getting latest news from CryptoCompare");

            try {
                
                String url = UriComponentsBuilder.fromUriString(URL)
                    .queryParam("Data", Data)
                    .queryParam("id", id)
                    .queryParam("published_on", published_on)
                    .queryParam("title", title)
                    .queryParam("url", link)
                    .queryParam("imageurl", imageurl)
                    .queryParam("body", body)
                    .queryParam("tags", tags)
                    .queryParam("categories", categories)


                    .toUriString();

                
                RequestEntity<Void> req = RequestEntity.get(url).build();

                                RestTemplate template = new RestTemplate();
                ResponseEntity<String> resp;

                
                resp = template.exchange(req, String.class);

                
                payload = resp.getBody();
                System.out.println("payload: " + payload);

                newsRepo.save(Data, payload);
            } catch (Exception ex) {
                System.err.printf("Error: %s\n", ex.getMessage());
                return Collections.emptyList();
            }
        } else {
            
            payload = opt.get();
            System.out.printf(">>>> cache: %s\n", payload);
        }

        // Convert payload to JsonObject
        // Convert the String to a Reader
        Reader strReader = new StringReader(payload);
        // Create a JsonReader from Reader
        JsonReader jsonReader = Json.createReader(strReader);
        // Read the payload as Json object
        JsonObject weatherResult = jsonReader.readObject();
        JsonArray cities = weatherResult.getJsonArray("weather");
        List<Articles> list = new LinkedList<>();
        for (int i = 0; i < cities.size(); i++) {
            // weather[0]
            JsonObject jo = cities.getJsonObject(i);
            list.add(Articles.create(jo));
        }
        return list;
    }

}
