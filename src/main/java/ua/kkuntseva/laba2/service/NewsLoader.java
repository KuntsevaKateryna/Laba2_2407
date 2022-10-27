package ua.kkuntseva.laba2.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.concurrent.CompletableFuture;

public interface NewsLoader {

    public CompletableFuture<String> findArticle(      String site_address,
                                                       String apikey_value,
                                                       //String q,
                                                       String from,
                                                       String to,
                                                       String category,
                                                       String language
                                                       ) throws InterruptedException ;
}
