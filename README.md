## Sparrow HTTP Client

An modular, easy-to-use HTTP client inspired by Openfeign/feign.

## Quick Start

### Spring Boot Starter
1. Add Spring Boot Starter dependency
```xml
<dependencies>
    <dependency>
        <groupId>win.minaandyyh.sparrow</groupId>
        <artifactId>sparrow-spring-boot-starter</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```
2. Add `@EnableSparrow` annotation on main class
```java
@SpringBootApplication
@EnableSparrow
public class AwesomeProjectApplication {
    public static void main(String[] args) {
        SpringApplication.run(AwesomeProjectApplication.class, args);
    }
}
```
3. Define data model
```java
@Data  // Lombok
public class Post {
    private Integer userId;
    
    private Integer id;
    
    private String title;
    
    private String body;
}
```
4. Define your client interface
```java
@SparrowClient(url = "https://jsonplaceholder.typicode.com")
public interface PostAPI {
    @Get(path = "/posts", produces = MediaType.APPLICATION_JSON)
    List<Post> all();
    
    @Get(path = "/post/{id}", produces = MediaType.APPLICATION_JSON)
    Post byId(@UrlVariable(name = "id") Integer id);
}
```
5. Use this interface just like other Spring beans
```java
@Service
public class SomeService {
    private final PostAPI postApi;
    
    @Autowired
    public SomeService(PostAPI postApi) {
        this.postApi = postApi;
    }
    
    // ...
    // Currently no java.util.Optional support, but it will be added later
    // Just for demonstration so no null-check present
    public Post getPost(Integer id) {
        return postApi.byId(id);
    }
}
```
### Non-Spring project
1. Add dependencies
```xml
<dependencies>
    <dependency>
        <groupId>win.minaandyyh</groupId>
        <artifactId>sparrow-core</artifactId>
        <version>1.0</version>
    </dependency>
    <dependency>
        <groupId>win.minaandyyh</groupId>
        <artifactId>sparrow-jackson-codec</artifactId>
        <version>1.0</version>
    </dependency>
</dependencies>
```
2. Define data model
```java
@Data  // Lombok
public class Post {
    private Integer userId;
    
    private Integer id;
    
    private String title;
    
    private String body;
}
```
3. Define your client interface
```java
@SparrowClient(url = "https://jsonplaceholder.typicode.com")
public interface PostAPI {
    @Get(path = "/posts", produces = MediaType.APPLICATION_JSON)
    List<Post> all();
    
    @Get(path = "/post/{id}", produces = MediaType.APPLICATION_JSON)
    Post byId(@UrlVariable(name = "id") Integer id);
}
```
4. Build client using `Sparrow` builder and use it somewhere
```java
public class SomeAwesomeClass {
    // ...
    private final PostAPI postApi = Sparrow.<PostAPI>newClient()
            .encoder(new JacksonJsonEncoder())
            .decoder(new JacksonJsonDecoder())
            .client(PostAPI.class)
            .handler(JDKHttpClientRequestHandler.defaultHandler())
            .build();
    
    public Post getPost(Integer id) {
        return postApi.byId(id);
    }
}
```

## License

This project is licensed under the [Apache License, Version 2.0](https://raw.githubusercontent.com/masteryyh/sparrow/main/LICENSE)