package ua.kkuntseva.laba2.controller;

import org.apache.poi.xwpf.usermodel.Document;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kkuntseva.laba2.factory.FormatGenerator;
import ua.kkuntseva.laba2.factory.MyFactory;
import ua.kkuntseva.laba2.model.Article;
import ua.kkuntseva.laba2.service.NewsLoader;
import ua.kkuntseva.laba2.service.NewsLoaderImpl;
import ua.kkuntseva.laba2.service.NewsParser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

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
    @Value("${page_size}")
    private int page_size;

    @Autowired
    private NewsLoader newsLoader;
   /* @Autowired
    private NewsParser newsParser;*/


    @GetMapping("/home")
    public String defaultPage(Model model) {
        return "index";
    }

    @PostMapping("/home")
    public String searchArticle(ModelMap model,
                                // @RequestParam String q,
                                @RequestParam String from,
                                @RequestParam String to,
                                @RequestParam(name = "category", required = false, defaultValue = "") String category,
                                @RequestParam String country) {

        newsLoader.loadArticle(model,
                from,
                to,
                category,
                country,
                site_address,
                apikey_value,
                page_size);
   /*     String rez = null;
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
                           newsLoader.findArticle
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
        */

        return "index";
    }

    // @PostMapping("/save")
 /*   public String saveInfo() {
        MyFactory f = new MyFactory();
        FormatGenerator msWordStructure = f.create_format("doc");
        logger.info("this.articles.size() :" + articles.size());
        XWPFDocument doc1 = (XWPFDocument) msWordStructure.generateDocumentStructure(articles);
        msWordStructure.saveFile(doc1, "1.docx");
        articles_info.clear();

        return "index";
    }
*/
    @PostMapping("/save")
    public ResponseEntity<?> saveArticle() throws IOException, InterruptedException {
        MyFactory f = new MyFactory();
        FormatGenerator msWordStructure = f.create_format("doc");
        XWPFDocument document = (XWPFDocument) msWordStructure.generateDocumentStructure(NewsLoaderImpl.articles);
        byte[] articles = msWordStructure.convertDocumentToBytes(document).toByteArray();

        HttpHeaders header = new HttpHeaders();
        header.setContentDispositionFormData("attachment", "11.docx");
        header.setContentType(new MediaType("application", "vnd.openxmlformats-officedocument.wordprocessingml.document"));
        header.setContentLength(articles.length);

        InputStreamResource inputStreamResource = new InputStreamResource(new ByteArrayInputStream(articles));
        NewsLoaderImpl.articles_info.clear();
        return ResponseEntity.ok().headers(header).body(inputStreamResource);
    }


    // not used, just only for testing async effectiveness
    @Async("processExecutor")
    void show_result_in_textarea(CompletableFuture<String> result, String rez, ModelMap model) throws ExecutionException, InterruptedException {
        //articles_info - a collection of received info about articles, shown in textarea
        NewsLoaderImpl.articles_info.add(result.get());
        rez = String.join("\n\n", NewsLoaderImpl.articles_info);
        model.addAttribute("description", rez);
    }
}
