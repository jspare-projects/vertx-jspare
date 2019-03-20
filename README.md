# Vertx Jspare

## Status

[![Build Status](https://travis-ci.org/jspare-projects/vertx-jspare.svg?branch=master)](https://travis-ci.org/jspare-projects/vertx-jspare)
[![Javadocs](http://www.javadoc.io/badge/org.jspare.vertx/vertx-jspare.svg)](http://www.javadoc.io/doc/org.jspare.vertx/vertx-jspare)

## Description

If you stay here, that is because you know about of the incredible thinks that the Vert.x can do. I have no doubt that it is amazing and extremely versatile for their tasks. But, what if I say that we can extract more! And we can! The Jspare Vert.x will improve your experience with your Vert.x applications. We persue conventions and we seek to make bright ideas of Vert.x are used and improved. Try it, I'm sure your team and you will not regret.

* Reactive and Scallable applications.
* Develop Web applications with agility and intuitively.
* Lightweight IoC and DI (JSR-330 support).
* Easy way to create components and resources, uncoupling your architecture.
* Components based.
* Spring Data Jpa support (NEW);
* Simple conventions, for improve your experience.
* Minimalist api to mock unit tests with vertx-jspare-uni. 

## Installation and Getting Started

The reference documentation includes detailed installation instructions as well as a comprehensive getting started guide.

Here is a quick sample of a simple usage of Vertx Jspare

For maven:

```xml
<parent>
  <groupId>org.jspare.vertx</groupId>
  <artifactId>vertx-jspare</artifactId>
  <version>${vertx-jspare.version}</version>
</parent>
```

For gradle:

```
sourceCompatibility = 1.8
targetCompatibility = 1.8

dependencies {
    compile "org.jspare.vertx:vertx-jspare:${vertx-jspare.version}"
}
```

We can play wiyh one simple http api example (kotlin is fun too:

```kotlin

import org.jspare.vertx.JspareVerticle
import org.jspare.vertx.annotation.Module
import org.jspare.vertx.annotation.Modules
import org.jspare.vertx.web.annotation.module.Routes
import org.jspare.vertx.web.module.HttpServerModule

@Routes(scanClasspath = true)
@Modules(Module(HttpServerModule::class))
class RestModule : JspareVerticle(){

  @Get
  @Handler
  fun getAccounts(ctx : RoutingContext) {
    ctx?.response().end(JsonArray().add(
      JsonObject().put("name", "Paulo").encode()
    ))
  } 
}

```

ok... in java:

```java

import org.jspare.vertx.JspareVerticle;
import org.jspare.vertx.annotation.Module;
import org.jspare.vertx.annotation.Modules;
import org.jspare.vertx.web.annotation.module.Routes;
import org.jspare.vertx.web.module.HttpServerModule;

@Routes(scanPackages = true)
@Modules({
  @Module(HttpServerModule.class)
})
class RestModule extends JspareVerticle{

  @Get
  @Handler
  void getAccounts(RoutingContext ctx) {
    ctx.response().end(new JsonArray().add(
      new JsonObject().put("name", "Paulo").encode()
    ));
  } 
}

```

This is a very simple example, use your imagination to create awesome api's and services, this project is designed to improve your vert.x experience.

## Documentation

You can [find the vertx-jspare documentation here](https://github.com/jspare-projects/vertx-jspare/wiki) which has extended usage instructions and other useful information. Substantial usage information can be found in the API documentation.

We are writting one complete documentation, but this consumes long time, just we ask patience and your help. I'll be a commiter. Please enjoy with us. =)


## Getting help and Reporting Issues

Having trouble with Jspare Container? We’d like to help!

* Check the reference documentation at Wiki — they provide solutions to the most common questions.
* Report bugs and issues at https://github.com/jspare-projects/vertx-jspare/issues

## Other Projects

Be sure to visit our other projects, jspare-container is the basis of all our frameworks and solutions. See it at: http://jspare.org/ or here on github: https://github.com/jspare-projects/

## License

All Jspare projects are Open Source software released under the Apache 2.0 license.
