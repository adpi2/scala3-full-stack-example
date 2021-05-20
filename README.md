# Example full-stack Scala 3 project

These days, full-stack development in Scala is a widespread reality.

But Scala 3 makes it exciting again, and simpler, without disrupting established practices.

This project is a full-stack application in Scala 3.
It uses both libraries that have already been published for Scala 3, like circe, and libraries that have not, like akka-http and scalajs-dom, through Scala 3's interoperability with Scala 2.13.

## Structure

The `webpage` project is a Scala.js project that uses the scalajs-dom facade to manipulate the HTML document.
It is served by an akka-http server in the `webserver` project.

The data types are shared across the client and server in the `core` project.
They are sent across the network in the json format using circe.

## Usage

You can use sbt to run the application.

```text
sbt:scala3-full-stack-example> webserver / run
[info] running (fork) example.start 
[info] Server online at http://localhost:8080/
```

Then open the http://localhost:8080 page in your favorite browser.
