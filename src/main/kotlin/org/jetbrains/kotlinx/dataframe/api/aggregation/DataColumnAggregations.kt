package org.jetbrains.dataframe

import org.jetbrains.kotlinx.dataframe.Predicate
import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.columns.values
import org.jetbrains.kotlinx.dataframe.impl.aggregation.modes.aggregateOf
import org.jetbrains.kotlinx.dataframe.impl.aggregation.modes.of

public fun <T> DataColumn<T>.count(predicate: Predicate<T>? = null): Int = if (predicate == null) size() else values().count(predicate)

// region min

public fun <T : Comparable<T>> DataColumn<T?>.min(): T = minOrNull()!!
public fun <T : Comparable<T>> DataColumn<T?>.minOrNull(): T? = asSequence().filterNotNull().minOrNull()

public fun <T, R : Comparable<R>> DataColumn<T>.minBy(selector: (T) -> R): T = minByOrNull(selector)!!
public fun <T, R : Comparable<R>> DataColumn<T>.minByOrNull(selector: (T) -> R): T? = values.minByOrNull(selector)

public fun <T, R : Comparable<R>> DataColumn<T>.minOf(selector: (T) -> R): R = minOfOrNull(selector)!!
public fun <T, R : Comparable<R>> DataColumn<T>.minOfOrNull(selector: (T) -> R): R? = values.minOfOrNull(selector)

// endregion

// region max

public fun <T : Comparable<T>> DataColumn<T?>.max(): T = maxOrNull()!!
public fun <T : Comparable<T>> DataColumn<T?>.maxOrNull(): T? = asSequence().filterNotNull().maxOrNull()

public fun <T, R : Comparable<R>> DataColumn<T>.maxBy(selector: (T) -> R): T = maxByOrNull(selector)!!
public fun <T, R : Comparable<R>> DataColumn<T>.maxByOrNull(selector: (T) -> R): T? = values.maxByOrNull(selector)

public fun <T, R : Comparable<R>> DataColumn<T>.maxOf(selector: (T) -> R): R = maxOfOrNull(selector)!!
public fun <T, R : Comparable<R>> DataColumn<T>.maxOfOrNull(selector: (T) -> R): R? = values.maxOfOrNull(selector)

// endregion

// region sum

@JvmName("sumT")
public fun <T : Number> DataColumn<T>.sum(): T = values.sum(type())

@JvmName("sumT?")
public fun <T : Number> DataColumn<T?>.sum(): T = values.sum(type())

public inline fun <T, reified R : Number> DataColumn<T>.sumOf(crossinline expression: (T) -> R): R? =
    org.jetbrains.kotlinx.dataframe.impl.aggregation.aggregators.Aggregators.sum.cast<R>().of(this, expression)

// endregion

// region mean

public fun <T : Number> DataColumn<T?>.mean(skipNa: Boolean = false): Double = meanOrNull(skipNa)!!
public fun <T : Number> DataColumn<T?>.meanOrNull(skipNa: Boolean = false): Double? = org.jetbrains.kotlinx.dataframe.impl.aggregation.aggregators.Aggregators.mean(skipNa).aggregate(this)

public inline fun <T, reified R : Number> DataColumn<T>.meanOf(skipNa: Boolean = false, noinline expression: (T) -> R?): Double = org.jetbrains.kotlinx.dataframe.impl.aggregation.aggregators.Aggregators.mean(skipNa).cast2<R?, Double>().aggregateOf(this, expression) ?: Double.NaN

// endregion

// region median

public fun <T : Comparable<T>> DataColumn<T?>.median(): T = medianOrNull()!!
public fun <T : Comparable<T>> DataColumn<T?>.medianOrNull(): T? = org.jetbrains.kotlinx.dataframe.impl.aggregation.aggregators.Aggregators.median.cast<T>().aggregate(this)

public inline fun <T, reified R : Comparable<R>> DataColumn<T>.medianOfOrNull(noinline expression: (T) -> R?): R? = org.jetbrains.kotlinx.dataframe.impl.aggregation.aggregators.Aggregators.median.cast<R?>().aggregateOf(this, expression)
public inline fun <T, reified R : Comparable<R>> DataColumn<T>.medianOf(noinline expression: (T) -> R?): R = medianOfOrNull(expression)!!

// endregion

// region std

public fun <T : Number> DataColumn<T?>.std(): Double = org.jetbrains.kotlinx.dataframe.impl.aggregation.aggregators.Aggregators.std.aggregate(this) ?: .0

public inline fun <T, reified R : Number> DataColumn<T>.stdOf(noinline expression: (T) -> R?): Double = org.jetbrains.kotlinx.dataframe.impl.aggregation.aggregators.Aggregators.std.aggregateOf(this, expression) ?: .0

// endregion