package org.jetbrains.kotlinx.dataframe.impl.columns

import org.jetbrains.kotlinx.dataframe.ColumnPath
import org.jetbrains.kotlinx.dataframe.ColumnResolutionContext
import org.jetbrains.kotlinx.dataframe.AnyCol
import org.jetbrains.kotlinx.dataframe.columns.ColumnAccessor
import org.jetbrains.kotlinx.dataframe.columns.ColumnReference
import org.jetbrains.kotlinx.dataframe.columns.ColumnWithPath
import org.jetbrains.kotlinx.dataframe.isGroup
import org.jetbrains.kotlinx.dataframe.toColumnPath

internal class ColumnAccessorImpl<T>(val path: ColumnPath) : ColumnAccessor<T> {

    override fun name() = path.last()

    override fun path() = path

    constructor(vararg path: String) : this(path.toColumnPath())

    override fun resolveSingle(context: ColumnResolutionContext): ColumnWithPath<T>? {
        var df = context.df
        var col: AnyCol? = null
        for (colName in path) {
            col = df.getColumn<Any?>(colName, context.unresolvedColumnsPolicy) ?: return null
            if (col.isGroup()) {
                df = col.asGroup().df
            }
        }
        return col?.typed<T>()?.addPath(path, context.df)
    }

    override fun rename(newName: String) = ColumnAccessorImpl<T>(path.dropLast(1) + newName)

    override fun <C> get(column: ColumnReference<C>) = ColumnAccessorImpl<C>(path + column.path())
}