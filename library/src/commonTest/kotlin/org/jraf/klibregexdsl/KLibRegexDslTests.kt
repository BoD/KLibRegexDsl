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

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class KLibRegexDslTests {
    @Test
    fun `Core`() {
        val characters = Characters("abcd")
        assertToString(characters, "abcd")

        val charactersNeedingEscaping = Characters("abc.d")
        assertToString(charactersNeedingEscaping, "\\Qabc.d\\E")

        val raw = Raw("a|b|(.*)")
        assertToString(raw, "a|b|(.*)")
    }

    @Test
    fun `Operators`() {
        val sequence = Sequence(
            Characters("a"),
            Characters("bc"),
            Characters("d"),
        )
        assertToString(sequence, "abcd")

        val either = Either(
            Characters("a"),
            Characters("bc"),
            Characters("d"),
        )
        assertToString(either, "a|bc|d")

        val group = Group(Characters("abc"))
        assertToString(group, "(abc)")

        val namedGroup = NamedGroup(Characters("abc"), name = "foo")
        assertToString(namedGroup, "(?<foo>abc)")

        val nonCapturingGroup = NonCapturingGroup(Characters("abc"))
        assertToString(nonCapturingGroup, "(?:abc)")
    }

    @Test
    fun `Back references`() {
        val indexedBackReference = IndexedBackReference(3)
        assertToString(indexedBackReference, "\\3")

        val namedBackReference = NamedBackReference("foo")
        assertToString(namedBackReference, "\\k<foo>")
    }


    @Test
    fun `Character classes`() {
        val simple = SimpleCharacterClass('a', 'b', 'c')
        assertToString(simple, "[abc]")

        val range = RangeCharacterClass('a'..'z')
        assertToString(range, "[a-z]")

        val simpleNegation = NegationCharacterClass(simple)
        assertToString(simpleNegation, "[^abc]")

        val rangeNegation = NegationCharacterClass(range)
        assertToString(rangeNegation, "[^a-z]")

        val union = UnionCharacterClass(simple, range)
        assertToString(union, "[abca-z]")

        val intersection = IntersectionCharacterClass(simple, range)
        assertToString(intersection, "[[abc]&&[a-z]]")
    }

    @Test
    fun `Predefined character classes`() {
        assertToString(AnyCharacter, ".")
        assertToString(Digit, "\\d")
        assertToString(NonDigit, "\\D")
        assertToString(HorizontalWhitespaceCharacter, "\\h")
        assertToString(NonHorizontalWhitespaceCharacter, "\\H")
        assertToString(WhitespaceCharacter, "\\s")
        assertToString(NonWhitespaceCharacter, "\\S")
        assertToString(VerticalWhitespaceCharacter, "\\v")
        assertToString(NonVerticalWhitespaceCharacter, "\\V")
        assertToString(WordCharacter, "\\w")
        assertToString(NonWordCharacter, "\\W")
    }

    @Test
    fun `Posix character classes`() {
        assertToString(LowerCaseAlphabeticCharacter, "\\p{Lower}")
        assertToString(UpperCaseAlphabeticCharacter, "\\p{Upper}")
        assertToString(AllAscii, "\\p{ASCII}")
        assertToString(AsciiAlphabeticCharacter, "\\p{Alpha}")
        assertToString(DecimalDigit, "\\p{Digit}")
        assertToString(AlphanumericCharacter, "\\p{Alnum}")
        assertToString(Punctuation, "\\p{Punct}")
        assertToString(VisibleCharacter, "\\p{Graph}")
        assertToString(PrintableCharacter, "\\p{Print}")
        assertToString(SpaceOrTab, "\\p{Blank}")
        assertToString(ControlCharacter, "\\p{Cntrl}")
        assertToString(HexadecimalDigit, "\\p{XDigit}")
    }

    @Test
    fun `Java lang character classes`() {
        assertToString(JavaLowerCase, "\\p{javaLowerCase}")
        assertToString(JavaUpperCase, "\\p{javaUpperCase}")
        assertToString(JavaWhitespace, "\\p{javaWhitespace}")
        assertToString(JavaMirrored, "\\p{javaMirrored}")
    }

    @Test
    fun `Classes for Unicode scripts, blocks, categories and binary properties`() {
        assertToString(LatinScriptCharacter, "\\p{IsLatin}")
        assertToString(CharacterInTheGreekBlock, "\\p{InGreek}")
        assertToString(UppercaseLetter, "\\p{Lu}")
        assertToString(UnicodeAlphabeticCharacter, "\\p{IsAlphabetic}")
        assertToString(CurrencySymbol, "\\p{Sc}")
        assertToString(AnyCharacterExceptOneInTheGreekBlock, "\\P{InGreek}")
        assertToString(AnyLetterExceptAnUppercaseLetter, "[\\p{L}&&[^\\p{Lu}]]")
        assertToString(LineBreak, "\\R")
    }

    @Test
    fun `Boundary matchers`() {
        assertToString(BeginningOfLine, "^")
        assertToString(EndOfLine, "$")
        assertToString(WordBoundary, "\\b")
        assertToString(NonWordBoundary, "\\B")
        assertToString(BeginningOfInput, "\\A")
        assertToString(EndOfPreviousMatch, "\\G")
        assertToString(EndOfInputButForTheFinalTerminatorIfAny, "\\Z")
        assertToString(EndOfInput, "\\z")
    }

    @Test
    fun `Greedy quantifiers`() {
        val simple = SimpleCharacterClass('a', 'b', 'c')

        val onceOrNotAtAll = Quantifier(simple, from = 0, to = 1)
        val onceOrNotAtAllExpected = "[abc]?"
        assertToString(onceOrNotAtAll, onceOrNotAtAllExpected)
        assertToString(simple.onceOrNotAtAll(), onceOrNotAtAllExpected)

        val zeroOrMoreTimes = Quantifier(simple, from = 0, to = null)
        val zeroOrMoreTimesExpected = "[abc]*"
        assertToString(zeroOrMoreTimes, zeroOrMoreTimesExpected)
        assertToString(simple.zeroOrMoreTimes(), zeroOrMoreTimesExpected)

        val oneOrMoreTimes = Quantifier(simple, from = 1, to = null)
        val oneOrMoreTimesExpected = "[abc]+"
        assertToString(oneOrMoreTimes, oneOrMoreTimesExpected)
        assertToString(simple.oneOrMoreTimes(), oneOrMoreTimesExpected)

        val exactlyNTimes = Quantifier(simple, exactly = 5)
        val exactlyNTimesExpected = "[abc]{5}"
        assertToString(exactlyNTimes, exactlyNTimesExpected)
        assertToString(simple.repeatedExactly(5), exactlyNTimesExpected)

        val atLeastNTimes = Quantifier(simple, from = 5, to = null)
        val atLeastNTimesExpected = "[abc]{5,}"
        assertToString(atLeastNTimes, atLeastNTimesExpected)
        assertToString(simple.repeatedAtLeast(5), atLeastNTimesExpected)

        val atLeastNTimesButNotMoreThanMTimes = Quantifier(simple, from = 5, to = 12)
        val atLeastNTimesButNotMoreThanMTimesExpected = "[abc]{5,12}"
        assertToString(atLeastNTimesButNotMoreThanMTimes, atLeastNTimesButNotMoreThanMTimesExpected)
        assertToString(simple.repeated(5..12), atLeastNTimesButNotMoreThanMTimesExpected)
    }

    @Test
    fun `Reluctant quantifiers`() {
        val simple = SimpleCharacterClass('a', 'b', 'c')

        val onceOrNotAtAll = Quantifier(simple, from = 0, to = 1, type = QuantifierType.RELUCTANT)
        val onceOrNotAtAllExpected = "[abc]??"
        assertToString(onceOrNotAtAll, onceOrNotAtAllExpected)
        assertToString(simple.onceOrNotAtAll(type = QuantifierType.RELUCTANT), onceOrNotAtAllExpected)

        val zeroOrMoreTimes = Quantifier(simple, from = 0, to = null, type = QuantifierType.RELUCTANT)
        val zeroOrMoreTimesExpected = "[abc]*?"
        assertToString(zeroOrMoreTimes, zeroOrMoreTimesExpected)
        assertToString(simple.zeroOrMoreTimes(type = QuantifierType.RELUCTANT), zeroOrMoreTimesExpected)

        val oneOrMoreTimes = Quantifier(simple, from = 1, to = null, type = QuantifierType.RELUCTANT)
        val oneOrMoreTimesExpected = "[abc]+?"
        assertToString(oneOrMoreTimes, oneOrMoreTimesExpected)
        assertToString(simple.oneOrMoreTimes(type = QuantifierType.RELUCTANT), oneOrMoreTimesExpected)

        val exactlyNTimes = Quantifier(simple, exactly = 5, type = QuantifierType.RELUCTANT)
        val exactlyNTimesExpected = "[abc]{5}?"
        assertToString(exactlyNTimes, exactlyNTimesExpected)
        assertToString(simple.repeatedExactly(5, type = QuantifierType.RELUCTANT), exactlyNTimesExpected)

        val atLeastNTimes = Quantifier(simple, from = 5, to = null, type = QuantifierType.RELUCTANT)
        val atLeastNTimesExpected = "[abc]{5,}?"
        assertToString(atLeastNTimes, atLeastNTimesExpected)
        assertToString(simple.repeatedAtLeast(5, type = QuantifierType.RELUCTANT), atLeastNTimesExpected)

        val atLeastNTimesButNotMoreThanMTimes = Quantifier(simple, from = 5, to = 12, type = QuantifierType.RELUCTANT)
        val atLeastNTimesButNotMoreThanMTimesExpected = "[abc]{5,12}?"
        assertToString(atLeastNTimesButNotMoreThanMTimes, atLeastNTimesButNotMoreThanMTimesExpected)
        assertToString(
            simple.repeated(5..12, type = QuantifierType.RELUCTANT),
            atLeastNTimesButNotMoreThanMTimesExpected
        )
    }

    @Test
    fun `Possessive quantifiers`() {
        val simple = SimpleCharacterClass('a', 'b', 'c')

        val onceOrNotAtAll = Quantifier(simple, from = 0, to = 1, type = QuantifierType.POSSESSIVE)
        val onceOrNotAtAllExpected = "[abc]?+"
        assertToString(onceOrNotAtAll, onceOrNotAtAllExpected)
        assertToString(simple.onceOrNotAtAll(type = QuantifierType.POSSESSIVE), onceOrNotAtAllExpected)

        val zeroOrMoreTimes = Quantifier(simple, from = 0, to = null, type = QuantifierType.POSSESSIVE)
        val zeroOrMoreTimesExpected = "[abc]*+"
        assertToString(zeroOrMoreTimes, zeroOrMoreTimesExpected)
        assertToString(simple.zeroOrMoreTimes(type = QuantifierType.POSSESSIVE), zeroOrMoreTimesExpected)

        val oneOrMoreTimes = Quantifier(simple, from = 1, to = null, type = QuantifierType.POSSESSIVE)
        val oneOrMoreTimesExpected = "[abc]++"
        assertToString(oneOrMoreTimes, oneOrMoreTimesExpected)
        assertToString(simple.oneOrMoreTimes(type = QuantifierType.POSSESSIVE), oneOrMoreTimesExpected)

        val exactlyNTimes = Quantifier(simple, exactly = 5, type = QuantifierType.POSSESSIVE)
        val exactlyNTimesExpected = "[abc]{5}+"
        assertToString(exactlyNTimes, exactlyNTimesExpected)
        assertToString(simple.repeatedExactly(5, type = QuantifierType.POSSESSIVE), exactlyNTimesExpected)

        val atLeastNTimes = Quantifier(simple, from = 5, to = null, type = QuantifierType.POSSESSIVE)
        val atLeastNTimesExpected = "[abc]{5,}+"
        assertToString(atLeastNTimes, atLeastNTimesExpected)
        assertToString(simple.repeatedAtLeast(5, type = QuantifierType.POSSESSIVE), atLeastNTimesExpected)

        val atLeastNTimesButNotMoreThanMTimes = Quantifier(simple, from = 5, to = 12, type = QuantifierType.POSSESSIVE)
        val atLeastNTimesButNotMoreThanMTimesExpected = "[abc]{5,12}+"
        assertToString(atLeastNTimesButNotMoreThanMTimes, atLeastNTimesButNotMoreThanMTimesExpected)
        assertToString(
            simple.repeated(5..12, type = QuantifierType.POSSESSIVE),
            atLeastNTimesButNotMoreThanMTimesExpected
        )
    }


    @Test
    fun `Email regex`() {
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

        assertToString(
            emailRegexNode,
            "[a-zA-Z0-9.+\\-]{1,255}@[[\\p{Alnum}.\\-]&&[^@]]+(\\Q.com\\E|\\Q.net\\E|\\Q.edu\\E|\\Q.org\\E)"
        )

        val emailRegex = emailRegexNode.toRegex()

        assertTrue(emailRegex.matches("a@a.com"))
        assertTrue(emailRegex.matches("ab@cd.com"))
        assertTrue(emailRegex.matches("te.st-foo+04@cd.com"))
        assertTrue(emailRegex.matches("te.st-foo+04@example.net"))
        assertTrue(emailRegex.matches("te.st-foo+04@testo65.example.net"))
        assertFalse(emailRegex.matches("te.st-foo+04@testo65.ex@mple.net"))
    }
}

private fun assertToString(obj: Any, expected: String) = assertEquals(expected = expected, actual = obj.toString())
