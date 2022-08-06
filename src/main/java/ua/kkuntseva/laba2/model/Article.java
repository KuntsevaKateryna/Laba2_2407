package ua.kkuntseva.laba2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Article {

 public List<Source> source;

    String author,
        title,
        description,
        url,
        urlToImage,
        publishedAt,
        content;
}
