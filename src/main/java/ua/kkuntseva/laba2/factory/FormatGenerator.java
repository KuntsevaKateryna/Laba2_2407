package ua.kkuntseva.laba2.factory;

import org.apache.poi.xwpf.usermodel.Document;
import ua.kkuntseva.laba2.model.Article;

import java.util.List;

public interface FormatGenerator {
    public Document generateDocumentStructure(List<Article> articles);
    public void saveFile (Document document, String file_path) ;
}
