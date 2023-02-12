package ua.kkuntseva.laba2.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import ua.kkuntseva.laba2.controller.Controller;
import ua.kkuntseva.laba2.model.Article;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@Service
public class NewsLoaderImpl implements NewsLoader {

    Logger logger = LoggerFactory.getLogger(NewsLoaderImpl.class);
    public static List<String> articles_info = new ArrayList<String>();
    public static List<Article> articles = new ArrayList<Article>();

    @Autowired
    private NewsParser newsParser;

    @Async("processExecutor")
    @Override
    public CompletableFuture<String> findArticle(String site_address,
                                                 String apikey_value,
                                                 //String q,
                                                 String from,
                                                 String to,
                                                 String category,
                                                 String country,
                                                 int pageSize,
                                                 int page) throws InterruptedException {
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
            builder1.queryParam("pageSize", pageSize);
            builder1.queryParam("page", page);
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
            logger.info("Process time:" + (endTime - startTime));
        }
        catch (IOException e) {
           logger.error("error in reading URL resource");
        }
        return CompletableFuture.completedFuture(sb.toString());
    }


    @Override
    public void loadArticle(ModelMap model,
                            // @RequestParam String q,
                            @RequestParam String from,
                            @RequestParam String to,
                            @RequestParam(name = "category", required = false, defaultValue = "") String category,
                            @RequestParam String country,
                            String site_address,
                            String apikey_value,
                            int page_size
                            ) {
        String rez = null;
        logger.info("start to send request");
        long startTime = System.currentTimeMillis();
        try {
            String[] category_array = category.split(Pattern.quote(","));
            logger.info("category: " + category);

            for (int i = 0; i < category_array.length; i++) {
                logger.info("category_array[i]: " + category_array[i]);

                int page_number = 1;
                Long articles_count = null;
                long cycles_to_read = 0;

                do {
                    //load articles:
                    CompletableFuture<String> result =
                            findArticle
                                    (site_address,
                                            apikey_value,
                                            //   q,
                                            from,
                                            to,
                                            category_array[i],
                                            country,
                                            page_size,
                                            page_number);
                    result.thenAcceptAsync(ii -> {
                        logger.warn("finished name result = " + result);
                    });

                    if ( articles_count == null) {
                        //find count of articles:
                        articles_count = newsParser.parse_articles_count(result.get());
                        logger.info("++articles_count :" + articles_count);
                    }
                    cycles_to_read = articles_count - page_size;
                    page_number++;

                    //articles_info - a collection of received info about articles, shown in textare
                    articles_info.add(result.get());
                    rez = String.join("\n\n", articles_info);
                    model.addAttribute("description", rez);
                    //articles - a collection of Articles, ready to be written to Word document
                    articles = newsParser.parseJSON(result.get());
                    logger.info("++result.get() :" + result.get());
                }

                while (cycles_to_read > 0);
            }
            long endTime = System.currentTimeMillis();
            logger.info("Common Process time :" + (endTime - startTime));
        } catch (InterruptedException e) {
            logger.info("InterruptedException " + e.getMessage());
        } catch (ExecutionException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

        ;
    }

}
