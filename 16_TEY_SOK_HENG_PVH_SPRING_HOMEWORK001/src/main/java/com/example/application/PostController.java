package com.example.application;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {
    private final List<Post> posts = new ArrayList<>();

    public PostController() {
        addStaticPosts();
    }

    private void addStaticPosts() {
        List<String> tags1 = new ArrayList<>();
        tags1.add("Spring Boot");
        tags1.add("Framework");
        Post post1 = new Post("Spring Boot Jwt", "JWT, or JSON Web Token...", "John", tags1);
        posts.add(post1);

        List<String> tags2 = new ArrayList<>();
        tags2.add("Spring Boot");
        tags2.add("Framework");
        tags2.add("Data Access");
        Post post2 = new Post("Spring Boot MyBatis", "MyBatis is ...", "William", tags2);
        posts.add(post2);

        List<String> tags3 = new ArrayList<>();
        tags3.add("Spring Data JPA");
        tags3.add("ORM");
        Post post3 = new Post("Introduction to Spring Data JPA", "Spring Data JPA is ...", "Emily", tags3);
        posts.add(post3);

        List<String> tags4 = new ArrayList<>();
        tags4.add("Spring Security");
        tags4.add("Authentication");
        Post post4 = new Post("Spring Security Basics", "Spring Security provides ...", "Michael", tags4);
        posts.add(post4);

        List<String> tags5 = new ArrayList<>();
        tags5.add("Spring MVC");
        tags5.add("Web Development");
        Post post5 = new Post("Getting Started with Spring MVC", "Spring MVC is ...", "Sophia", tags5);
        posts.add(post5);
    }

    @PostMapping
    @Operation(summary = "Create new post")
    public ResponseEntity<Response<List<Post>>> addPost(@RequestBody PostRequest postRequest) {
        Post post = new Post(postRequest.getTitle(), postRequest.getContent(), postRequest.getAuthor(), postRequest.getTags());
        posts.add(post);

        Response<List<Post>> response = new Response<>();
        response.setMessage("Post created successfully");

        List<Post> payload = new ArrayList<>();
        payload.add(post);
        response.setPayload(payload);

        response.setStatus("201 Created");
        response.setTime(LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Read all posts")
    public ResponseEntity<Response<List<Post>>> getAllPosts(@RequestParam(defaultValue = "1") int page,
                                                            @RequestParam(defaultValue = "2") int size) {
        int pageNo = page - 1;
        int start = pageNo * size;
        int end = Math.min(start + size, posts.size());

        Response<List<Post>> response = new Response<>();
        if (start < posts.size()) {
            List<Post> paginatedPosts = posts.subList(start, end);
            response.setMessage("Retrieved posts successfully");
            response.setStatus("200 OK");
            response.setPayload(paginatedPosts);
            return ResponseEntity.ok(response);
        } else {
            response.setMessage("No posts found");
            response.setStatus("200");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Read post by id")
    public ResponseEntity<Response<Post>> getPostById(@PathVariable int id) {
        for (Post post : posts) {
            if (post.getId() == id) {
                Response<Post> response = new Response<>();
                response.setMessage("Retrieved post by id successfully");
                response.setStatus("200 OK");
                response.setPayload(post);
                return ResponseEntity.ok(response);
            }
        }
        Response<Post> response = new Response<>();
        response.setMessage("Post not found");
        response.setStatus("200");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Search post title")
    public ResponseEntity<Response<List<Post>>> getPostByTitle(@RequestParam String title) {
        List<Post> matchingPosts = new ArrayList<>();
        for (Post post : posts) {
            if (post.getTitle().equalsIgnoreCase(title)) {
                matchingPosts.add(post);
            }
        }
        Response<List<Post>> response = new Response<>();
        if (!matchingPosts.isEmpty()) {
            response.setMessage("Retrieved post by title Successfully");
            response.setStatus("200 OK");
            response.setPayload(matchingPosts);
            return ResponseEntity.ok(response);
        } else {
            response.setMessage("No posts found with title: " + title);
            response.setStatus("200");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/author")
    @Operation(summary = "Filter post by author")
    public ResponseEntity<Response<List<Post>>> getPostByAuthor(@RequestParam String author) {
        List<Post> matchingPosts = new ArrayList<>();
        for (Post post : posts) {
            if (post.getAuthor().equalsIgnoreCase(author)) {
                matchingPosts.add(post);
            }
        }
        Response<List<Post>> response = new Response<>();
        if (!matchingPosts.isEmpty()) {
            response.setMessage("Retrieved post by author Successfully");
            response.setStatus("200 OK");
            response.setPayload(matchingPosts);
            return ResponseEntity.ok(response);
        } else {
            response.setMessage("No posts found by author: " + author);
            response.setStatus("200");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update post by id")
    public ResponseEntity<Response<Post>> updatePost(@PathVariable int id, @RequestBody PostRequest postRequest) {
        for (Post post : posts) {
            if (post.getId() == id) {
                post.setTitle(postRequest.getTitle());
                post.setContent(postRequest.getContent());
                post.setAuthor(postRequest.getAuthor());
                post.setTags(postRequest.getTags());

                Response<Post> response = new Response<>();
                response.setMessage("Post updated successfully");
                response.setStatus("200 OK");
                response.setPayload(post);
                return ResponseEntity.ok(response);
            }
        }

        Response<Post> response = new Response<>();
        response.setMessage("Post not found");
        response.setStatus("200");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete post by id")
    public ResponseEntity<Response<Post>> deletePost(@PathVariable int id) {
        Post deletedPost = null;
        for (Post post : posts) {
            if (post.getId() == id) {
                deletedPost = post;
                posts.remove(post);
                break;
            }
        }
        if (deletedPost != null) {
            Response<Post> response = new Response<>();
            response.setMessage("Post deleted successfully");
            response.setStatus("200 OK");
            response.setPayload(deletedPost);
            return ResponseEntity.ok(response);
        } else {
            Response<Post> response = new Response<>();
            response.setMessage("Post not found");
            response.setStatus("200");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    @GetMapping("/tag")
    @Operation(summary = "Filter posts by multiple tags")
    public ResponseEntity<Response<List<Post>>> getPostsByMultipleTags(@RequestParam List<String> tags) {
        List<Post> matchingPosts = new ArrayList<>();
        for (Post post : posts) {
            if (post.getTags().stream().anyMatch(tags::contains)) {
                matchingPosts.add(post);
            }
        }

        Response<List<Post>> response = new Response<>();
        if (!matchingPosts.isEmpty()) {
            response.setMessage("Retrieved posts by tags successfully");
            response.setStatus("200 OK");
            response.setPayload(matchingPosts);
            return ResponseEntity.ok(response);
        } else {
            response.setMessage("No posts found with the provided tags");
            response.setStatus("200");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
}
