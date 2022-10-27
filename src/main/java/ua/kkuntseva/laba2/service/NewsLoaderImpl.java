package ua.kkuntseva.laba2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Service
public class NewsLoaderImpl implements NewsLoader {

    Logger logger = LoggerFactory.getLogger(NewsLoaderImpl.class);

    @Async("processExecutor")
    @Override
    public CompletableFuture<String> findArticle(String site_address,
                                                 String apikey_value,
                                                 //String q,
                                                 String from,
                                                 String to,
                                                 String category,
                                                 String country) throws InterruptedException {
        StringBuilder sb = new StringBuilder();
        try {
            long startTime = System.currentTimeMillis();
            UriComponentsBuilder builder1 = UriComponentsBuilder.fromUriString(site_address)
                    .queryParam("apikey", apikey_value);
            //builder1.queryParam("q", q);
            builder1.queryParam("from", from);
            builder1.queryParam("to", to);
            builder1.queryParam("category", category);
            builder1.queryParam("country", country);
            URI builder1URI = builder1.build().toUri();
            logger.info("URL is " + builder1URI);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(builder1URI.toURL().openStream()));
            Thread.sleep(1000L);

            if (in != null) {
                //read the result
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    logger.info(" find article: " + inputLine);
                    sb.append(inputLine);
                }
                in.close();
            }

            long endTime = System.currentTimeMillis();
            System.out.println("Process time:" + (endTime - startTime));
        }
        catch (IOException e) {
            logger.error("error in reading URL resource");
        }
        return CompletableFuture.completedFuture(sb.toString());
    }
}
