package ua.kkuntseva.laba2.service;

import org.springframework.core.convert.converter.Converter;
import ua.kkuntseva.laba2.model.Article;

import java.util.List;


public interface NewsParser extends Converter<String, List<Article>> {

    public List<Article> parseJSON(String jsonString);
}
