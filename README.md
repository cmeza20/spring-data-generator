# Spring Data Generator [![Build Status](https://travis-ci.org/cmeza20/spring-data-generator.svg?branch=master)](https://travis-ci.org/cmeza20/spring-data-generator)
Spring Data Generator for JPA repositories and managers.

## Features ##

* Generated repositories for JPA Entities
* Generated managers for JPA Entities
* EntityScan wrapped annotation

## Quick Start ##

* **Dependency Java 8**

Download the jar through Maven:

```xml
<dependency>
  <groupId>com.cmeza</groupId>
  <artifactId>spring-data-generator</artifactId>
  <version>1.0.0</version>
</dependency>
```

The simple Spring Data JPA configuration with Java-Config looks like this: 
```java
@SDGenerator(
        entityPackage = "com.acme.model",
        repositoryPackage = "com.acme.repositories",
        managerPackage = "com.acme.managers",
        repositoryPostfix = "Repository",
        managerPostfix = "Manager",
        debug = false
)
@SpringBootApplication
public class AppConfig {
    
    public static void main(String[] args) {
        SpringApplication.run(WebApplication.class, args);
    }
    
}
```

| Attribute | Required | Default | Description |
|----------|:-------------:|:------:|------------|
| entityPackage |  No | [] | Entity scan package |
| repositoryPackage | No | "" | Package where the repositories will be generated |
| managerPackage | No | "" | Package where the managers will be generated | 
| repositoryPostfix | No | "Repository" | Postfix for repositories. example: Account**Repository** |
| managerPostfix | No | "Manager" | Postfix for managers. example: Account**Manager** |
| debug | No | false | Enable debug log |


Sample entity in `com.acme.model`

```java
@Entity
public class Account {

    @Id
    @GeneratedValue
    private Integer id;
    private String firstname;
    private String lastname;
       
    // Getters and setters
    // (Firstname, Lastname)-constructor and noargs-constructor
    // equals / hashcode
}
```

Generated a repository interface example in `com.acme.repositories`:

```java
public interface AccountRepository extends JpaRepository<Account, Long> {
    
}
```

Generated a manager class example in `com.acme.managers`:

```java
@Component
public class AccountManager {
    
    @Autowired
    public AccountManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }
   
    private AccountRepository accountRepository;
}
```


License
----

MIT