# KLibRegexDsl

A tiny Regex DSL library for Kotlin Multiplatform.

## Why

[Regular expressions](https://en.wikipedia.org/wiki/Regular_expression) are a powerful tool, but a bit of a nightmare to read and maintain.  In fact, they aren't ideal to write either: the syntax is not always intuitive or simple to remember, and it's quite easy to make mistakes - which will typically be detected at run-time only.

Using a DSL instead fixes these issues (albeit at the cost of verbosity - but as Kotlin developers, we like verbose, don't we?)

As an illustration, compare these two ways to express _"Time Format HH:MM 12-hour, optional leading 0"_:

```regexp
(0?[1-9]|1[0-2]]):[0-5][0-9]
```
vs
```kotlin
val hours = Group(
    Either(
        Sequence(
            Characters("0").onceOrNotAtAll(),
            characterClass('1'..'9'),
        ),
        Sequence(
            Characters("1"),
            characterClass('0'..'2'),
        )
    )
)
val separator = Characters(":")
val minutes = Sequence(
    characterClass('0'..'5'),
    characterClass('0'..'9'),
)

val timeRegex = Sequence(
    hours,
    separator,
    minutes
)
```
Which one is easier to read?

Fooled you - the first one isn't even valid (duplicated bracket)! You may have missed it, but I don't blame you: it _is_ easy to miss.

If that convinced you, continue reading.

## Usage
### 1/ Add the dependencies to your project

**TODO!  This is not actually published yet!**

```groovy
dependencies {
    /* ... */
    implementation 'org.jraf:klibregexdsl:1.0.0'
}
```
_(The artifact is hosted on jcenter)_



### 2/ Use it

The API is pretty self-explanatory so here's an example:

```kotlin

// Note: this is of course a very naive implementation of an email regex

val localPart = union(
    characterClass('a'..'z'),
    characterClass('A'..'Z'),
    characterClass('0'..'9'),
    characterClass('.', '+', '-'),
).repeated(1..255)

val at = Characters("@")

val domain = intersection(
    union(
        AlphanumericCharacter,
        characterClass('.', '-'),
    ),
    negation(characterClass('@'))
).oneOrMoreTimes()

val tld = Group(
    Either(
        Characters(".com"),
        Characters(".net"),
        Characters(".edu"),
        Characters(".org"),
    )
)
val emailRegexNode = Sequence(
    localPart,
    at,
    domain,
    tld
)

// emailRegexNode.toString() = "[a-zA-Z0-9.+\\-]{1,255}@[[\\p{Alnum}.\\-]&&[^@]]+(\\Q.com\\E|\\Q.net\\E|\\Q.edu\\E|\\Q.org\\E)"

val emailRegex: Regex = emailRegexNode.toRegex()

```

You can also have a look at the [sample](sample/).


## License

```
Copyright (C) 2020-present Benoit 'BoD' Lubek (BoD@JRAF.org)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
