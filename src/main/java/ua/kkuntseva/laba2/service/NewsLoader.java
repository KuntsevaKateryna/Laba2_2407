package ua.kkuntseva.laba2.service;

import java.util.concurrent.CompletableFuture;

public interface NewsLoader {
    public CompletableFuture<String> findArticle(      String site_address,
                                                       String apikey_value,
                                                       String q,
                                                       String from,
                                                       String to,
                                                       String category,
                                                       String language
                                                       ) ;
}
