package ua.kkuntseva.laba2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Service
public class NewsLoaderImpl implements NewsLoader{

    Logger logger = LoggerFactory.getLogger(NewsLoaderImpl.class);

    @Override
    public CompletableFuture<String> findArticle(String site_address,
                                                       String apikey_value,
                                                       String q,
                                                       String from,
                                                       String to,
                                                       String category,
                                                       String country) {
        StringBuilder sb = new StringBuilder();
        try {
            UriComponentsBuilder builder1 = UriComponentsBuilder.fromUriString(site_address)
                    .queryParam("apikey", apikey_value);

            builder1.queryParam("q", q);
            builder1.queryParam("from", from);
            builder1.queryParam("to", to);
            builder1.queryParam("category", category);
            builder1.queryParam("country", country);
            URI builder1URI = builder1.build().toUri();
            logger.info("URL is " + builder1URI);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(builder1URI.toURL().openStream()));

            if (in != null) {
                //read the result
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    logger.info(" find article: "+inputLine);
                    sb.append(inputLine);
                }
                in.close();
            }
            Thread.sleep(1000L);
        }
        catch (  InterruptedIOException e) {
            logger.error("error");
        }
        catch (  IOException e) {
            logger.error("error");
        } catch (InterruptedException e) {
            logger.error("Error in InfoLoader.find article: {}", e.getMessage());
        }
        return CompletableFuture.completedFuture(sb.toString());
    }
}
