package com.example.application;

import java.time.LocalDate;
import java.util.List;

public class Post {
    private static int nextId = 1;
    private int id;
    private String title;
    private String content;
    private String author;
    private LocalDate creationDate;
    private List<String> tags;

    public Post(String title, String content, String author, List<String> tags) {
        this.id = nextId++;
        this.title = title;
        this.content = content;
        this.author = author;
        this.creationDate = LocalDate.now();
        this.tags = tags;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
