# dot2img

[![Build Status](https://travis-ci.org/bmiller1009/dot2img.svg?branch=master)](https://travis-ci.org/bmiller1009/dot2img)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.bradfordmiller/dot2img_2.11/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.bradfordmiller/dot2img_2.11)
[![github: bmiller1009/dot2img](https://img.shields.io/badge/github%3A-issues-blue.svg?style=flat-square)](https://github.com/bmiller1009/dot2img/issues)

Small library for converting DOT (graph description language) to various image formats (svg, jpeg, png)

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software and how to install them

* sbt 0.13 or greater if you want to build from source
* JVM 8 for the Scala 2.12 build, JVM 6+ for the 2.11 build

### Installing

If you're using [Maven](maven.apache.org) simply specify the GAV coordinate below and Maven will do the rest

```xml
<dependency>
  <groupId>org.bradfordmiller</groupId>
  <artifactId>dot2img_2.11</artifactId>
  <version>0.0.21</version>
</dependency>
```

#### Running with SBT

Add this GAV coordinate to your SBT dependency list

```sbt
libraryDependencies += "org.bradfordmiller" %% "dot2img" % "0.0.21"
```

## Building from source

Building the library requires SBT 0.13+ and a 1.8 version of the JDK for the Scala 2.12 version, or a 1.6+ version of
the JDK for the Scala 2.11 version

You can build the library with this command
```sbt
sbt publishLocal
```

The resulting jar will be published to ~/.ivy2/local/org/bradfordmiller/dot2img_{SCALA_VERSION}


## Using the library

The library is quite simple.  Feed in your dot grammar as a string along with an output path as seen below

```scala
import org.bradfordmiller.dot2img
...

//Set up your dot data
val data = "digraph G {\n  \"Welcome\" -> \"To\"\n  \"To\" -> \"Web\"\n  \"To\" -> \"GraphViz!\"\n}"

val p = Dot2Img.save(data, "/tmp/testformat.jpg")

println(p)

```

The saved image will be at the path in the appropriately specified format.  Currently svg, jpeg and png are supported.

The above code will produce the following image:

![Test Graph](https://github.com/bmiller1009/dot2img/blob/master/src/test/resources/testfiles/test.jpeg?raw=true)

## Built With

* [scala](https://www.scala-lang.org/) - The programming language
* [Apache Batik](https://xmlgraphics.apache.org/batik/) - SVG image conversion
* [viz.js](http://www.webgraphviz.com/viz.js) - Javascript rendering engine for dot grammar

## Versioning

For the versions available, see the [tags on this repository](https://github.com/bmiller1009/dot2img/tags). 

## Authors

* **Bradford Miller** - *Initial work* - [bfm](https://github.com/bmiller1009)

See also the list of [contributors](https://github.com/bmiller1009/dot2img/contributors) who participated in this project.

## License

This project is licensed under the Apache 2.0 License - see the [LICENSE](LICENSE) file for details

## Acknowledgments

* This library wraps viz.js to do the heavy lifting with the graphics rendering as seen [here](http://www.webgraphviz.com/viz.js)
* Thanks to [PurpleBooth](https://gist.github.com/PurpleBooth) for the README template as seen [here](https://gist.github.com/PurpleBooth/109311bb0361f32d87a2)
