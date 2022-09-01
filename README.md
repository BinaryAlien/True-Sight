# True Sight

📝 [Burp Suite](https://portswigger.net/burp) extension providing auditors with collaborative documentation capabilities.

## Credits

This project was initiated as part of an intership I had at the [EDF](https://www.edf.fr/en) CERT.

It would not have been possible without [koromodako](https://github.com/koromodako) and [meskal](https://github.com/meskal)!

## Features

This extension provides a way for auditors to **document HTTP messages** as well as quality-of-life enhancements to facilitate the reverse-engineering process such as **workflows** and **variable name substitutions**.

When running on a non-temporary project, the extension saves its data as JSON text files. This lets version control systems such as Git easily keep track of changes and thus provides a way for auditors to **share and collaborate on projects**.

## Documentation

In order to understand how the extension works and get started, please refer to the [manual](https://github.com/BinaryAlien/True-Sight/blob/main/manual/manual.pdf).

## Dependencies

This extension depends on the [Burp Extender API](https://mvnrepository.com/artifact/net.portswigger.burp.extender/burp-extender-api), [JSON In Java](https://mvnrepository.com/artifact/org.json/json) and the [Kotlin Stdlib Jdk8](https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib-jdk8).

## Build

This project uses [Gradle](https://gradle.org) as its build system.

Executing the following command will produce a JAR file of the extension under `build/libs/truesight-all.jar`.
```sh
$ ./gradlew shadowJar
```

As an alternative, you can download the extension directly from the [Releases](https://github.com/BinaryAlien/True-Sight/releases) page.
