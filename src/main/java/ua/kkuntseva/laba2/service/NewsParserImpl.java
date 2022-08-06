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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class NewsParserImpl implements NewsParser{
    Logger logger = LoggerFactory.getLogger(NewsParserImpl.class);

    public static String[] fieldsNameArray = {
            "author",
            "title",
            "description",
            "url",
            "urlToImage",
            "publishedAt",
            "content"};

    @Override
    public Article parseJSON(String jsonString) {
        Article article = new Article();
        List<Source> source = new ArrayList<Source>();
        article.setSource(source);
        try {
            Object obj = new JSONParser().parse(jsonString);
            JSONObject jo = (JSONObject) obj;

            Class cls = article.getClass();
            String currentFieldName = null;
            for (int i = 0; i < fieldsNameArray.length; i++) {
                currentFieldName = fieldsNameArray[i];
                Method[] methods = cls.getDeclaredMethods();
                for (Method m : methods) {
                    if (m.getName().equalsIgnoreCase("set" + currentFieldName)) {
                        m.invoke(article, (String) jo.get(currentFieldName));
                    }
                }
            }
            // to get array getSource []
            JSONArray getSource = (JSONArray) jo.get("Source");
            Iterator iter = getSource.iterator();
            // to output array items
            while (iter.hasNext()) {
                JSONObject sourceElem = (JSONObject) iter.next();
                source.add(new Source(

                        sourceElem.get("id").toString(),
                        sourceElem.get("name").toString()
                        )
                );
                article.setSource(source);
            }

        } catch (ClassCastException | ParseException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
            logger.error(e.getMessage());
        }
        return article;
    }
}
