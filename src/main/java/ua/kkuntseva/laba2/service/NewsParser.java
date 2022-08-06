package ua.kkuntseva.laba2.service;

import ua.kkuntseva.laba2.model.Article;

public interface NewsParser {

    public Article parseJSON(String jsonString);
}
