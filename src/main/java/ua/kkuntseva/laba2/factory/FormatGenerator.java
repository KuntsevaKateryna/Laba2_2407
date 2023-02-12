package ua.kkuntseva.laba2.factory;

import org.apache.poi.xwpf.usermodel.Document;
import ua.kkuntseva.laba2.model.Article;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public interface FormatGenerator {
    public Document generateDocumentStructure(List<Article> articles);
    //public void saveFile (Document document, String file_path) ;
    public ByteArrayOutputStream convertDocumentToBytes (Document  document) throws IOException;
}
