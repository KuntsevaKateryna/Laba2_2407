package ua.kkuntseva.laba2.converter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import ua.kkuntseva.laba2.model.Article;
import ua.kkuntseva.laba2.model.Source;

import java.util.ArrayList;
import java.util.List;

public class JSONObjectToArticleConverter implements Converter<JSONObject, Article> {

    Logger logger = LoggerFactory.getLogger(JSONObjectToArticleConverter.class);

    @Override
    public Article convert( JSONObject article_obj) {
        Article article = new Article();
        try {
            logger.info("start method convert JSONString into Article: " + article_obj);
            List<Source> source = new ArrayList<Source>();
            JSONObject source_obj = (JSONObject) article_obj.get("source");
                if (!source_obj.isEmpty()) {
                    source.add(new Source(
                                    source_obj.get("id") != null ? source_obj.get("id").toString() : null,
                                    source_obj.get("name") != null ? source_obj.get("name").toString() : null
                            )
                    );
                    article.setSource(source);
                }
                article.setAuthor(article_obj.get("author") != null ? article_obj.get("author").toString() : null);
                article.setTitle(article_obj.get("title") != null ? article_obj.get("title").toString() : null);
                article.setDescription(article_obj.get("description") != null ? article_obj.get("description").toString() : null);
                article.setUrl(article_obj.get("url") != null ? article_obj.get("url").toString() : null);
                article.setUrlToImage(article_obj.get("urlToImage") != null ? article_obj.get("urlToImage").toString() : null);
                article.setPublishedAt(article_obj.get("publishedAt") != null ? article_obj.get("publishedAt").toString() : null);
                article.setContent(article_obj.get("content") != null ? article_obj.get("content").toString() : null);
                source.clear();
      } catch (ClassCastException |
                NullPointerException e) {
           logger.error(e.getMessage());
        }
        return article;
    }
}
