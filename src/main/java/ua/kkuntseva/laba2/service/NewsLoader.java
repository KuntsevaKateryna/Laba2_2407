package ua.kkuntseva.laba2.service;


import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;
import ua.kkuntseva.laba2.model.Article;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface NewsLoader {

    public CompletableFuture<String> findArticle(String site_address,
                                                 String apikey_value,
                                                 //String q,
                                                 String from,
                                                 String to,
                                                 String category,
                                                 String language,
                                                 int pageSize,
                                                 int page
    ) throws InterruptedException;

    public void loadArticle(ModelMap model,
                            // @RequestParam String q,
                            String from,
                            String to,
                            String category,
                            String country,
                            String site_address,
                            String apikey_value,
                            int page_size);

    public List<String> getArticles_info();
    public List<Article> getArticles();
}
