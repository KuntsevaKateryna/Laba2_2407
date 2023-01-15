package ua.kkuntseva.laba2.factory;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.util.Units;
import org.apache.poi.xwpf.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.kkuntseva.laba2.model.Article;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

public class MSWordFormat implements FormatGenerator {
    Logger logger = LoggerFactory.getLogger(MSWordFormat.class);

    @Override
    public Document generateDocumentStructure(List<Article> articles) {
        XWPFDocument document = new XWPFDocument();
        System.out.println(articles);
        try {
            if (articles.size() == 0) throw new NullPointerException("articles list is empty");
            logger.info("generateMsWordStructure article count: " + articles.size());
            Article article = null;
            Iterator<Article> iter = articles.iterator();
            while (iter.hasNext()) {
                article = iter.next();
                logger.info(" article: " + article.getTitle());
                //create title
                XWPFParagraph title = document.createParagraph();
                title.setAlignment(ParagraphAlignment.CENTER);
                XWPFRun titleRun = title.createRun();
                titleRun.setText(article.getTitle());
                titleRun.setColor("009933");
                titleRun.setBold(true);
                titleRun.setFontFamily("Courier");
                titleRun.setFontSize(20);

                //write image
                if (article.getUrlToImage() != null ) {
                    XWPFParagraph image = document.createParagraph();
                    image.setAlignment(ParagraphAlignment.CENTER);
                    XWPFRun imageRun = image.createRun();
                    imageRun.setTextPosition(20);
                    InputStream pic = new URL(article.getUrlToImage()).openStream();
                    imageRun.addPicture(pic, XWPFDocument.PICTURE_TYPE_JPEG, "image file", Units.toEMU(200), Units.toEMU(200));
                    pic.close();
                }

                //write all text
                XWPFParagraph para1 = document.createParagraph();
                para1.setAlignment(ParagraphAlignment.BOTH);
                XWPFRun para1Run = para1.createRun();
                para1Run.setText(article.toString());
            }
        } catch (NullPointerException e) {
           logger.error(e.getMessage());
        } catch (InvalidFormatException | IOException e) {
           logger.error(e.getMessage());
        }
        return document;
    }

    @Override
    public void saveFile (Document document, String file_path) {
        File file = new File(file_path);
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);

                XWPFDocument wordDoc = (XWPFDocument)document;
                wordDoc.write(out);
                out.close();
                wordDoc.close();

        } catch (IOException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
