package ua.kkuntseva.laba2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kkuntseva.laba2.model.Article;
import ua.kkuntseva.laba2.service.NewsLoaderImpl;
import ua.kkuntseva.laba2.service.NewsParserImpl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@org.springframework.stereotype.Controller
@RequestMapping("/laba2")
public class Controller {

    Logger logger = LoggerFactory.getLogger(Controller.class);

    @Value("${apikey_value}")
    private String apikey_value;
    @Value("${file_path}")
    private String file_path;
    @Value("${site_address}")
    private String site_address;


    @Autowired
    private NewsLoaderImpl newsLoaderImpl;
    @Autowired
    private NewsParserImpl newsParserImpl;

    List<String> articles_info = new ArrayList<String>();
    List<Article> articles = new ArrayList<Article>();

    @GetMapping("/home")
    public String defaultPage(Model model) {
        return "index";
    }

    @PostMapping("/home")
    public String searchFilm(ModelMap model,
                             @RequestParam String q,
                             @RequestParam String from,
                             @RequestParam String to,
                             @RequestParam String category,
                             @RequestParam String country)  {
        String rez = null;
        logger.info("start to send request");
        try {
            CompletableFuture<String> result = newsLoaderImpl.findArticle(   site_address,
                                                                                 apikey_value,
                                                                                 q,
                                                                                 from,
                                                                                 to,
                                                                                 category,
                                                                                 country);
            result.thenAcceptAsync(ii -> {
                    logger.warn("finished name result = " + result);
                });
                articles_info.add(result.get());
                articles.add(newsParserImpl.parseJSON(result.get()));


            rez = String.join("\n\n", articles_info);
            model.addAttribute("description", rez);
            logger.info("rez: " + rez);

        } catch (InterruptedException  e) {
            logger.error(e.getMessage());
        } catch (ExecutionException e) {
            logger.error(e.getMessage());
        }
        return "index";
    }

}
