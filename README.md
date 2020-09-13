# KLibRegexDsl

This is a tiny Regex DSL library for Kotlin Multiplatform.

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

val localPart = UnionCharacterClass(
    RangeCharacterClass('a'..'z'),
    RangeCharacterClass('A'..'Z'),
    RangeCharacterClass('0'..'9'),
    SimpleCharacterClass('.', '+', '-'),
).repeated(1..255)

val at = Characters("@")

val domain = IntersectionCharacterClass(
    UnionCharacterClass(
        AlphanumericCharacter,
        SimpleCharacterClass('.', '-'),
    ),
    NegationCharacterClass(SimpleCharacterClass('@'))
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

val emailRegex: Regeex = emailRegexNode.toRegex()

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
