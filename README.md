# Spring Data Generator [![Build Status](https://travis-ci.org/cmeza20/spring-data-generator.svg?branch=master)](https://travis-ci.org/cmeza20/spring-data-generator) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.cmeza/spring-data-generator/badge.svg)](https://maven-badges.herokuapp.com/maven-central/com.cmeza/spring-data-generator)

Spring Data Generator for JPA repositories and managers.

## Features ##
* Generate in Runtime
* Generate by Plugin
* Generate repositories for JPA Entities
* Generate managers for JPA Entities
* EntityScan wrapped annotation
* Overwrite option
* Support JpaSpecificationExcecutor<?>
* Support @EmbeddedId annotation
* @Id or @EmbeddedId primitive support
* Package structure support
* Additional extends support

## Dependencies ##

* **Java 8**
* **Spring data JPA**

## Generate in Runtime ##
Download the jar through Maven:

```xml
<dependency>
  <groupId>com.cmeza</groupId>
  <artifactId>spring-data-generator</artifactId>
  <version>1.1.8</version>
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
        onlyAnnotations = false,
        debug = false,
        overwrite = false,
        additionalExtends = {
                QuerydslPredicateExecutor.class
        }
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
| onlyAnnotations | No | false | Scan only classes annotated with @SDGenerate or @SDNoGenerate |
| debug | No | false | Enable debug log |
| overwrite | No | false | Overwrite existing files |
| additionalExtends | No | [] | Extension of additional interfaces |

## Generate by Plugin ##
Download the jar through Maven:
```xml
<build>
	<plugins>
		<plugin>
			<groupId>com.cmeza</groupId>
			<artifactId>spring-data-generator</artifactId>
			<version>1.1.8</version>
			<configuration>
				<entity-package>
				    <param>com.acme.model</param>
				</entity-package>
				<repository-package>com.acme.repository</repository-package>
				<repository-postfix>Repository</repository-postfix>
				<manager-package>com.acme.managers</manager-package>
				<manager-postfix>Manager</manager-postfix>
				<only-annotations>false</only-annotations>
				<overwrite>false</overwrite>
				<additional-extends>
                    <param>org.springframework.data.querydsl.QuerydslPredicateExecutor</param>
                </additional-extends>
			</configuration>
		</plugin>
	</plugins>
</build>
```

| Attribute | Required | Default | Description |
|----------|:-------------:|:------:|------------|
| entity-package |  Yes | [] | Entity scan package |
| repository-package | Yes | "" | Package where the repositories will be generated |
| manager-package | Yes | "" | Package where the managers will be generated | 
| repository-postfix | No | "Repository" | Postfix for repositories. example: Account**Repository** |
| manager-postfix | No | "Manager" | Postfix for managers. example: Account**Manager** |
| onlyAnnotations | No | false | Scan only classes annotated with @SDGenerate or @SDNoGenerate |
| overwrite | No | false | Overwrite existing files |
| additional-extends | No | [] | Extension of additional interfaces |

#### Generate repositories (terminal)
```
$ mvn spring-data-generator:repositories
```
#### Generate managers (terminal)
```
$ mvn spring-data-generator:managers
```

## Example ##

Sample entity in `com.acme.model`

```java
@Entity
//@SDGenerate   ->  Optional: Include to classes scan
//@SDNoGenerate   ->  Optional: Exclude to classes scan
public class Account {

    @Id
    @GeneratedValue
    private Long id;
    private String firstname;
    private String lastname;
       
    // Getters and setters
    // (Firstname, Lastname)-constructor and noargs-constructor
    // equals / hashcode
}
```

Generate a repository interface example in `com.acme.repositories`:

```java
@Repository
public interface AccountRepository extends JpaRepository<Account, Long>, JpaSpecificationExecutor<Account> {
}
```

Generate a manager class example in `com.acme.managers`:

```java
@Component
public class AccountManager {
    
    private AccountRepository accountRepository;
    
    @Autowired
    public AccountManager(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }   
}
```

## Notes ##

* The overwrite option delete the existing file to regenerate
* The generation of repositories and managers inherits the entity package. Example:

![alt tag](https://user-images.githubusercontent.com/9298942/39490821-bf4b53b6-4d4f-11e8-9853-fb4ece43346e.png "Package structure support")

License
----

MIT
