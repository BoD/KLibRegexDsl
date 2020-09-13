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

/**
 * @param from The lower limit (inclusive).
 * @param to The upper limit (inclusive), or `null` for no upper limit.
 */
class Quantifier(
    private val node: RegexNode,
    private val from: Int,
    private val to: Int?,
    private val type: QuantifierType = QuantifierType.GREEDY
) : RegexNode() {

    constructor(
        node: RegexNode,
        exactly: Int,
        type: QuantifierType = QuantifierType.GREEDY
    ) : this(node = node, from = exactly, to = exactly, type = type)

    override fun toString(): String {
        val suffix = when {
            from == 0 && to == 1 -> "?"
            from == 0 && to == null -> "*"
            from == 1 && to == null -> "+"
            from == to -> "{$from}"
            to == null -> "{$from,}"
            else -> "{$from,$to}"
        }
        return "$node$suffix${type.notation}"
    }
}

enum class QuantifierType(internal val notation: String) {
    GREEDY(""),
    RELUCTANT("?"),
    POSSESSIVE("+"),
}

fun RegexNode.onceOrNotAtAll(type: QuantifierType = QuantifierType.GREEDY) = Quantifier(
    node = this,
    from = 0,
    to = 1,
    type = type,
)

fun RegexNode.zeroOrMoreTimes(type: QuantifierType = QuantifierType.GREEDY) = Quantifier(
    node = this,
    from = 0,
    to = null,
    type = type,
)

fun RegexNode.oneOrMoreTimes(type: QuantifierType = QuantifierType.GREEDY) = Quantifier(
    node = this,
    from = 1,
    to = null,
    type = type,
)

fun RegexNode.repeated(fromTo: IntRange, type: QuantifierType = QuantifierType.GREEDY) = Quantifier(
    node = this,
    from = fromTo.first,
    to = fromTo.last,
    type = type,
)

fun RegexNode.repeatedExactly(times: Int, type: QuantifierType = QuantifierType.GREEDY) = Quantifier(
    node = this,
    exactly = times,
    type = type,
)

fun RegexNode.repeatedAtLeast(times: Int, type: QuantifierType = QuantifierType.GREEDY) = Quantifier(
    node = this,
    from = times,
    to = null,
    type = type,
)

