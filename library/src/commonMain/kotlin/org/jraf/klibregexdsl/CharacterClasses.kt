/*
 * This source is part of the
 *      _____  ___   ____
 *  __ / / _ \/ _ | / __/___  _______ _
 * / // / , _/ __ |/ _/_/ _ \/ __/ _ `/
 * \___/_/|_/_/ |_/_/ (_)___/_/  \_, /
 *                              /___/
 * repository.
 *
 * Copyright (C) 2020-present Benoit 'BoD' Lubek (BoD@JRAF.org)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jraf.klibregexdsl


// region Character classes

abstract class CharacterClass : RegexNode() {
    abstract fun rangeString(): String
    override fun toString() = "[${rangeString()}]"
}

class SimpleCharacterClass(private vararg val allowedCharacters: Char) : CharacterClass() {
    override fun rangeString() =
        allowedCharacters.joinToString(separator = "") { if (it == '-') "\\-" else it.toString() }
}

class NegationCharacterClass(private val characterClass: CharacterClass) : CharacterClass() {
    override fun rangeString() = characterClass.rangeString()
    override fun toString() = "[^${rangeString()}]"
}

class RangeCharacterClass(private val range: CharRange) : CharacterClass() {
    override fun rangeString() = "${range.first}-${range.last}"
}

class UnionCharacterClass(private vararg val characterClasses: CharacterClass) : CharacterClass() {
    override fun rangeString() = characterClasses.joinToString(separator = "") { it.rangeString() }
}

class IntersectionCharacterClass(private vararg val characterClasses: CharacterClass) : CharacterClass() {
    override fun rangeString() = characterClasses.joinToString(separator = "&&")
}

// endregion


// region Predefined character classes

val AnyCharacter: CharacterClass = hardCodedCharacterClass(".")

val Digit: CharacterClass = hardCodedCharacterClass("\\d")

val NonDigit: CharacterClass = hardCodedCharacterClass("\\D")

val HorizontalWhitespaceCharacter: CharacterClass = hardCodedCharacterClass("\\h")

val NonHorizontalWhitespaceCharacter: CharacterClass = hardCodedCharacterClass("\\H")

val WhitespaceCharacter: CharacterClass = hardCodedCharacterClass("\\s")

val NonWhitespaceCharacter: CharacterClass = hardCodedCharacterClass("\\S")

val VerticalWhitespaceCharacter: CharacterClass = hardCodedCharacterClass("\\v")

val NonVerticalWhitespaceCharacter: CharacterClass = hardCodedCharacterClass("\\V")

val WordCharacter: CharacterClass = hardCodedCharacterClass("\\w")

val NonWordCharacter: CharacterClass = hardCodedCharacterClass("\\W")

// endregion


// region POSIX character classes (US-ASCII only)

val LowerCaseAlphabeticCharacter: CharacterClass = hardCodedCharacterClass("\\p{Lower}")

val UpperCaseAlphabeticCharacter: CharacterClass = hardCodedCharacterClass("\\p{Upper}")

val AllAscii: CharacterClass = hardCodedCharacterClass("\\p{ASCII}")

val AsciiAlphabeticCharacter: CharacterClass = hardCodedCharacterClass("\\p{Alpha}")

val DecimalDigit: CharacterClass = hardCodedCharacterClass("\\p{Digit}")

val AlphanumericCharacter: CharacterClass = hardCodedCharacterClass("\\p{Alnum}")

val Punctuation: CharacterClass = hardCodedCharacterClass("\\p{Punct}")

val VisibleCharacter: CharacterClass = hardCodedCharacterClass("\\p{Graph}")

val PrintableCharacter: CharacterClass = hardCodedCharacterClass("\\p{Print}")

val SpaceOrTab: CharacterClass = hardCodedCharacterClass("\\p{Blank}")

val ControlCharacter: CharacterClass = hardCodedCharacterClass("\\p{Cntrl}")

val HexadecimalDigit: CharacterClass = hardCodedCharacterClass("\\p{XDigit}")

// endregion


// region java.lang.Character classes

val JavaLowerCase: CharacterClass = hardCodedCharacterClass("\\p{javaLowerCase}")

val JavaUpperCase: CharacterClass = hardCodedCharacterClass("\\p{javaUpperCase}")

val JavaWhitespace: CharacterClass = hardCodedCharacterClass("\\p{javaWhitespace}")

val JavaMirrored: CharacterClass = hardCodedCharacterClass("\\p{javaMirrored}")

// endregion


// region Classes for Unicode scripts, blocks, categories and binary properties

val LatinScriptCharacter: CharacterClass = hardCodedCharacterClass("\\p{IsLatin}")

val CharacterInTheGreekBlock: CharacterClass = hardCodedCharacterClass("\\p{InGreek}")

val UppercaseLetter: CharacterClass = hardCodedCharacterClass("\\p{Lu}")

val UnicodeAlphabeticCharacter: CharacterClass = hardCodedCharacterClass("\\p{IsAlphabetic}")

val CurrencySymbol: CharacterClass = hardCodedCharacterClass("\\p{Sc}")

val AnyCharacterExceptOneInTheGreekBlock: CharacterClass = hardCodedCharacterClass("\\P{InGreek}")

val AnyLetterExceptAnUppercaseLetter: CharacterClass = hardCodedCharacterClass("[\\p{L}&&[^\\p{Lu}]]")

val LineBreak: CharacterClass = hardCodedCharacterClass("\\R")

// endregion


internal fun hardCodedCharacterClass(hardcoded: String): CharacterClass = object : CharacterClass() {
    override fun rangeString() = hardcoded
    override fun toString() = hardcoded
}
