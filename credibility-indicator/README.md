# Credibility Indicator

A tool for assessing the credibility of a news article by analyzing the relevance of linked articles.

According to this metric, an article is "credible", if it links to 1 or 2 related articles that also
cover the same topic. The rationale is that if there are related articles like that, it indicates
a deeper knowledge of the topic being discussed and a more "in depth" coverage of it.

## Usage
You are going to need [Maven](https://maven.apache.org) Maven and Java installed in order to run the web application.
Or you simply run it using [IntelliJ](https://www.jetbrains.com/idea/)

## Team

Gruner & Jahr Team

Members:
* Andy Herzberg <herzberg.andy@guj.de>
* Martin Ocker <ocker.martin@guj.de>
* Torsten Huber <torsten.huber@neofonie.de>

### Running with IntelliJ
Simply open the `pom.xml` file in IntelliJ and once the project was loaded, run the `WebApp` class (Simply right-click
on it and hit click on 'run' in the context menu).

The web application can then be accessed in your browser at
```
http://localhost:8000
```

### Maven Build
Run the following commands

```
mvn clean package
java -jar target/credibility.jar
```

The web application can then be accessed in your browser at
```
localhost:8000
```

## REST API Endpoint
The REST API is available at `http://localhost:8000/credibility`.

Example:
```
http://localhost:8000/credibility?url=https://www.theguardian.com/politics/2016/nov/30/labour-split-expected-motion-tony-blair-role-iraq-war
```

This will return all results of the analysis in JSON format.