package ru.netology.repository;

import ru.netology.exception.NotFoundException;
import ru.netology.model.Post;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PostRepository {
  private final ConcurrentHashMap<Long, Post> posts = new ConcurrentHashMap<>();
  private final AtomicLong idGenerator = new AtomicLong(1);

  public List<Post> all() {
    return new ArrayList<>(posts.values());
  }

  public Optional<Post> getById(long id) {
    return Optional.ofNullable(posts.get(id));
  }

  public Post save(Post post) {
    if (post.getId() == 0) {
      long newId = idGenerator.getAndIncrement();
      Post newPost = new Post(newId, post.getContent());
      posts.put(newId, newPost);
      return newPost;
    } else {
      long id = post.getId();
      if (!posts.containsKey(id)) {
        throw new NotFoundException("Post not found with id: " + id);
      }
      Post updatedPost = new Post(id, post.getContent());
      posts.put(id, updatedPost);
      return updatedPost;
    }
  }

  public void removeById(long id) {
    if (!posts.containsKey(id)) {
      throw new NotFoundException("Post not found with id: " + id);
    }
    posts.remove(id);
  }
}