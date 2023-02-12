package ua.kkuntseva.laba2.service;


import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ua.kkuntseva.laba2.model.Article;
import ua.kkuntseva.laba2.model.Source;

import java.io.IOException;
import java.util.*;

@Service
public class NewsParserImpl implements NewsParser {
    Logger logger = LoggerFactory.getLogger(NewsParserImpl.class);



 @Override
    public List<Article> parseJSON(String jsonString) {
     List<Article> articles1 = new ArrayList<Article>();
        try {
            logger.info("start method parseJSON jsonString: " + jsonString);

            List<Source> source = new ArrayList<Source>();

            Object obj = new JSONParser().parse(jsonString);
            JSONObject jo = (JSONObject) obj;
            JSONArray articlesArr = (JSONArray) jo.get("articles");
            JSONObject article_obj = null;
            Iterator articlesItr = articlesArr.iterator();
            Long article_count = (Long) jo.get("totalResults");
            logger.info("totalResults: " + article_count);

            JSONObject source_obj = null;
            while (articlesItr.hasNext()) {
                Article article = new Article();
                article_obj = (JSONObject) articlesItr.next();
                source_obj = (JSONObject) article_obj.get("source");
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

                logger.info("--articles1.add(article) with title: " + article.getTitle());
                articles1.add(article);
                logger.info("--articles1.size() " + articles1.size());
                source.clear();
            }
            logger.info("-- Arrays.toString(articles1.toArray()) " + Arrays.toString(articles1.toArray()));
        } catch (ClassCastException | ParseException |
                NullPointerException e) {
            logger.error(e.getMessage());
        }
        return articles1;
    }

@Override
    public List<Article> convert(String source) {
        List<Article> articles2 = new ArrayList<Article>();
        //if (source.startsWith("<?xml "))
        //    articles2 = parseXML(source);
        //else
        //if (source.startsWith("{\""))
        if (source.startsWith("{\"status\"")) {
            articles2 = parseJSON(source);
        }
        return articles2;
    }

    @Override
    public long parse_articles_count(String jsonString) throws ParseException {
     Object obj = new JSONParser().parse(jsonString);
        JSONObject jo = (JSONObject) obj;
        long articles_count = (long) jo.get("totalResults");
        logger.info("-- parse_articles_count " + articles_count);
        return articles_count;
    }
}
