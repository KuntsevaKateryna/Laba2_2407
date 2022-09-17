package ua.kkuntseva.laba2.service;

import org.springframework.core.convert.converter.Converter;
import ua.kkuntseva.laba2.model.Article;

public interface NewsParser extends Converter<String, Article> {

    public Article parseJSON(String jsonString);
}
