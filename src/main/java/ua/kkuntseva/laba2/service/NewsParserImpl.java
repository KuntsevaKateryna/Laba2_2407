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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import ua.kkuntseva.laba2.model.Article;
import ua.kkuntseva.laba2.model.Source;

import java.io.IOException;
import java.util.*;

@Service
public class NewsParserImpl implements NewsParser {

    Logger logger = LoggerFactory.getLogger(NewsParserImpl.class);

    @Autowired
    private ConversionService conversionService;

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
                article = conversionService.convert(article_obj, Article.class);
                articles1.add(article);
                logger.info("articles1.size() " + articles1.size());
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
    public long parse_articles_count(String jsonString) throws ParseException {
     Object obj = new JSONParser().parse(jsonString);
        JSONObject jo = (JSONObject) obj;
        long articles_count = (long) jo.get("totalResults");
        logger.info("-- parse_articles_count " + articles_count);
        return articles_count;
    }
}
