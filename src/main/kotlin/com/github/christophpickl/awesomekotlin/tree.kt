package com.github.christophpickl.awesomekotlin

import java.util.LinkedList
import java.util.Random
import kotlin.streams.toList

private val stammBaseLength = 60
private val random = Random()

fun main(args: Array<String>) {
    println(randTree().toNiceString())
}

fun randTree(): Tree {
    val stamm = Stamm(length = randLength(0))
    val tree = Tree(stamm)
    insertBranches(stamm)
    return tree
}

fun insertBranches(parent: Line) {
    val countBranches = randBranchCount(parent.stammDistance)
    val randAnguluse = randAnguluse(countBranches, parent.stammDistance)
    parent.branches.addAll(IntRange(1, countBranches).map {
        Branch(
                parent = parent,
                length = randLength(parent.stammDistance),
                stammDistance = parent.stammDistance + 1,
                angulus = randAnguluse[it - 1]
        )
    })
    parent.branches.forEach(::insertBranches)
}

fun randAnguluse(countBranches: Int, stammDistance: Int): List<Int> {
    val angulusRange = distributed(angulusDistribution)
    return random.ints(countBranches.toLong(), angulusRange.first, angulusRange.second).toList()

//    return random.ints(countBranches.toLong(), 0, 360).toList()
//    return IntRange(1, countBranches).map { Math.min(it * 10, 360) }
}

fun randLength(stammDistance: Int): Int {
    val x = random.nextDouble() + 0.5
    val stammDistanceStammSurplus = if (stammDistance == 0) 80 else 0
    val baseLength = Math.max(stammBaseLength - (stammDistance * 10), 1) + stammDistanceStammSurplus
    return Math.max((baseLength * x).toInt(), 1)
}

private val branchCountDistributions = mapOf(
        0 to Distribution(listOf(
                DistributionItem(1, 0),
                DistributionItem(20, 1),
                DistributionItem(60, 2),
                DistributionItem(15, 3),
                DistributionItem(3, 4),
                DistributionItem(1, 5)
        )),
        1 to Distribution(listOf(
                DistributionItem(16, 0),
                DistributionItem(30, 1),
                DistributionItem(40, 2),
                DistributionItem(10, 3),
                DistributionItem(3, 4),
                DistributionItem(1, 5)
        )),
        2 to Distribution(listOf(
                DistributionItem(30, 0),
                DistributionItem(40, 1),
                DistributionItem(25, 2),
                DistributionItem(4, 3),
                DistributionItem(1, 4)
        ))
)
private val looongDistribution = Distribution(listOf(
        DistributionItem(70, 0),
        DistributionItem(20, 1),
        DistributionItem(8, 2),
        DistributionItem(2, 3)
))
private val angulusDistribution = Distribution(listOf(
        DistributionItem(3, Pair(0, 90)),
        DistributionItem(15, Pair(90, 135)),
        DistributionItem(64, Pair(135, 225)),
        DistributionItem(15, Pair(225, 270)),
        DistributionItem(3, Pair(270, 360))
))

fun randBranchCount(stammDistance: Int): Int {
    val distribution = if (stammDistance >= branchCountDistributions.size) {
        looongDistribution
    } else {
        branchCountDistributions[stammDistance]!!
    }
    return distributed(distribution)
}


interface Line {
    val length: Int
    val branches: MutableList<Branch>
    val stammDistance: Int
    val angulus: Int
}

data class Branch(
        val parent: Line,
        override val length: Int,
        override val stammDistance: Int,
        override val angulus: Int,
        override val branches: MutableList<Branch> = LinkedList()
) : Line {
    val isLeaf = branches.isEmpty()

    fun toNiceString(sb: StringBuilder) {
        val prefix = IntRange(1, stammDistance).fold("", { acc, _ -> acc + "\t" })
        sb.append(prefix).appendln("Branch [${length}cm] ${angulus}ang")
        branches.forEach { it.toNiceString(sb) }
    }
}

data class Stamm(
        override val length: Int,
        override val branches: MutableList<Branch> = LinkedList()
) : Line {
    override val stammDistance = 0
    override val angulus: Int get() {
        throw UnsupportedOperationException()
    }

    fun toNiceString(sb: StringBuilder) {
        sb.appendln("Stamm [${length}cm]")
        branches.forEach {
            it.toNiceString(sb)
        }
    }
}

data class Tree(
        val stamm: Stamm
) {
    fun toNiceString(): String {
        val sb = StringBuilder()
        stamm.toNiceString(sb)
        return sb.toString()
    }
}


data class DistributionItem<T>(val percent: Int, val value: T)
data class Distribution<T>(val items: List<DistributionItem<T>>) {
    init {
        if (items.isEmpty()) throw IllegalArgumentException("Distribution must not be empty!")
    }
}

fun <T> distributed(distribution: Distribution<T>): T {
    val rand = randomBetween(0, 100)
    return _distributed(distribution, rand)
}

private fun <T> Distribution<T>.validatePercentSum() {
    if (sumOfPercents() != 100)
        throw IllegalArgumentException("Distribution percent must be sum of 100: $this")
}

private fun <T> Distribution<T>.sumOfPercents() = this.items.sumBy { it.percent }
fun <T> _distributed(distribution: Distribution<T>, rand: Int): T {
    distribution.validatePercentSum()

    var currentPercent = 0
    for (item in distribution.items) {
        currentPercent += item.percent
        if (rand <= currentPercent) {
            return item.value
        }
    }
    throw IllegalStateException("Distribution algorithm failed! rand=$rand, distribution=$distribution (currentPercent=$currentPercent)")
}

fun randomBetween(from: Int, to: Int, except: Int? = null): Int {
    val diff = to - from
    if (except == null) {
        return rand(diff) + from
    }
    if (diff <= 1) throw IllegalArgumentException("difference between from and to must be at least 2 big: from=$from, to=$to")
    var randPos: Int?
    do {
        randPos = rand(diff) + from
    } while (randPos == except)
    return randPos!!
}

fun rand(diff: Int) = Math.round(Math.random() * diff).toInt()
