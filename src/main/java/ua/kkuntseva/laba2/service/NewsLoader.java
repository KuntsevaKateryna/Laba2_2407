package ua.kkuntseva.laba2.service;


import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestParam;

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
                            @RequestParam String from,
                            @RequestParam String to,
                            @RequestParam(name = "category", required = false, defaultValue = "") String category,
                            @RequestParam String country,
                            String site_address,
                            String apikey_value,
                            int page_size);
}
