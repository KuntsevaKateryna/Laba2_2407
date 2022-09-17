package ua.kkuntseva.laba2.service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.kkuntseva.laba2.model.Article;
import ua.kkuntseva.laba2.model.Source;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class NewsParserImpl implements NewsParser {
    Logger logger = LoggerFactory.getLogger(NewsParserImpl.class);


    @Override
    public Article parseJSON(String jsonString) {
        Article article = new Article();
        List<Source> source = new ArrayList<Source>();
        article.setSource(source);
        try {
            Object obj = new JSONParser().parse(jsonString);
            JSONObject jo = (JSONObject) obj;
            Long totalResults = (Long) jo.get("totalResults");
            System.out.println("articles_count: " + totalResults);
            JSONArray articlesArr = (JSONArray) jo.get("articles");
            Iterator articlesItr = articlesArr.iterator();
            JSONObject article_obj = null;
            while (articlesItr.hasNext()) {
                article_obj = (JSONObject) articlesItr.next();
                article.setAuthor(article_obj.get("author") != null ? article_obj.get("author").toString() : null);
                article.setTitle(article_obj.get("title") != null ? article_obj.get("title").toString() : null);
                article.setDescription(article_obj.get("description") != null ? article_obj.get("description").toString() : null);
                article.setUrl(article_obj.get("url") != null ? article_obj.get("url").toString() : null);
                article.setUrlToImage(article_obj.get("urlToImage") != null ? article_obj.get("urlToImage").toString() : null);
                article.setPublishedAt(article_obj.get("publishedAt") != null ? article_obj.get("publishedAt").toString() : null);
                article.setContent(article_obj.get("content") != null ? article_obj.get("content").toString() : null);
                System.out.println("article.getAuthor and title " + article.getAuthor() + ", " + article.getTitle());
                JSONObject source_obj = (JSONObject) article_obj.get("source");
                if (!source_obj.isEmpty()) {
                    source.add(new Source(
                                    source_obj.get("id") != null ? source_obj.get("id").toString() : null,
                                    source_obj.get("name") != null ? source_obj.get("name").toString() : null
                            )
                    );
                    article.setSource(source);
                }
                System.out.println("- article.toString() : " + article.toString());
                source.clear();
            }
        } catch (ClassCastException | ParseException | NullPointerException e) {
            logger.error(e.getMessage());
        }
        return article;
    }

    @Override
    public Article convert(String source) {
        Article article = null;
        //if (source.startsWith("<?xml "))
        //    film = parseXML(source);
        //else
        if (source.startsWith("{\""))
            article = parseJSON(source);
        return article;
    }
}
