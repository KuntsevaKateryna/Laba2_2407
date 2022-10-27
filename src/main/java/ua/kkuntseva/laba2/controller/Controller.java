package ua.kkuntseva.laba2.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kkuntseva.laba2.model.Article;
import ua.kkuntseva.laba2.service.NewsLoader;
import ua.kkuntseva.laba2.service.NewsParserImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

@org.springframework.stereotype.Controller
@RequestMapping("/laba2")
//@EnableAsync
public class Controller {

    Logger logger = LoggerFactory.getLogger(Controller.class);

    @Value("${apikey_value}")
    private String apikey_value;
    @Value("${file_path}")
    private String file_path;
    @Value("${site_address}")
    private String site_address;

    @Autowired
    private NewsLoader newsLoader;
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
                             // @RequestParam String q,
                             @RequestParam String from,
                             @RequestParam String to,
                             @RequestParam(name = "category", required = false, defaultValue = "") String category,
                             @RequestParam String country) {
        String rez = null;
        logger.info("start to send request");
        try {

            String[] category_array = category.split(Pattern.quote(","));
            System.out.println("category: " + category);
            for (int i = 0; i < category_array.length; i++) {
                System.out.println("category_array[i]: " + category_array[i]);
                CompletableFuture<String> result = //async_findArticle
                        newsLoader.findArticle
                                (site_address,
                                        apikey_value,
                                        //   q,
                                        from,
                                        to,
                                        category_array[i],
                                        country);
                result.thenAcceptAsync(ii -> {
                    logger.warn("finished name result = " + result);
                });
                articles_info.add(result.get());
                articles.add(newsParserImpl.parseJSON(result.get()));


                rez = String.join("\n\n", articles_info);
                model.addAttribute("description", rez);
            }
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
        } catch (ExecutionException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return "index";
    }
}
