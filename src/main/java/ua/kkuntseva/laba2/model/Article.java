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

    String author;
    String    title;
    String    description;
    String    url;
    String    urlToImage;
    String    publishedAt;
    String    content;
}
