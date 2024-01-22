package org.jetbrains.kotlinx.dataframe.api

import org.jetbrains.kotlinx.dataframe.ColumnsSelector
import org.jetbrains.kotlinx.dataframe.DataColumn
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.DataRow
import org.jetbrains.kotlinx.dataframe.columns.ColumnPath
import org.jetbrains.kotlinx.dataframe.columns.ColumnSet
import org.jetbrains.kotlinx.dataframe.columns.ColumnsResolver
import org.jetbrains.kotlinx.dataframe.columns.SingleColumn
import org.jetbrains.kotlinx.dataframe.documentation.Indent
import org.jetbrains.kotlinx.dataframe.documentation.LineBreak
import org.jetbrains.kotlinx.dataframe.documentation.SelectingColumns
import org.jetbrains.kotlinx.dataframe.documentation.UsageTemplateColumnsSelectionDsl
import org.jetbrains.kotlinx.dataframe.documentation.UsageTemplateColumnsSelectionDsl.UsageTemplate
import org.jetbrains.kotlinx.dataframe.impl.columns.ColumnsList
import org.jetbrains.kotlinx.dataframe.util.COL_SELECT_DSL_LIST_DATACOLUMN_GET
import org.jetbrains.kotlinx.dataframe.util.COL_SELECT_DSL_LIST_DATACOLUMN_GET_REPLACE
import kotlin.experimental.ExperimentalTypeInference
import kotlin.reflect.KProperty

/** [Columns Selection DSL][ColumnsSelectionDsl] */
internal interface ColumnsSelectionDslLink

@Suppress("UNCHECKED_CAST")
@PublishedApi
internal fun <T> ColumnsSelectionDsl<T>.asSingleColumn(): SingleColumn<DataRow<T>> = this as SingleColumn<DataRow<T>>

/**
 * [DslMarker] for [ColumnsSelectionDsl] to prevent accessors being used across scopes for nested
 * [ColumnsSelectionDsl.select] calls.
 */
@DslMarker
@Target(AnnotationTarget.CLASS, AnnotationTarget.TYPEALIAS, AnnotationTarget.TYPE, AnnotationTarget.FUNCTION)
public annotation class ColumnsSelectionDslMarker

/**
 * ## Columns Selection DSL
 * {@include [SelectingColumns.Dsl.WithExample]}
 * {@setArg [SelectingColumns.OperationArg] [select][DataFrame.select]}
 *
 * @comment This interface be safely cast to [SingleColumn] across the library. It does not directly
 * implement it for DSL purposes.
 */
@ColumnsSelectionDslMarker
public interface ColumnsSelectionDsl<out T> : /* SingleColumn<DataRow<T>> */
    ColumnSelectionDsl<T>,

    // first {}, firstCol()
    FirstColumnsSelectionDsl,
    // last {}, lastCol()
    LastColumnsSelectionDsl,
    // single {}, singleCol()
    SingleColumnsSelectionDsl,

    // col(name), col(5), [5]
    ColColumnsSelectionDsl,
    // valueCol(name), valueCol(5)
    ValueColColumnsSelectionDsl,
    // frameCol(name), frameCol(5)
    FrameColColumnsSelectionDsl,
    // colGroup(name), colGroup(5)
    ColGroupColumnsSelectionDsl,

    // cols {}, cols(), cols(colA, colB), cols(1, 5), cols(1..5), [{}]
    ColsColumnsSelectionDsl,

    // colA.."colB"
    ColumnRangeColumnsSelectionDsl,

    // valueCols {}, valueCols()
    ValueColsColumnsSelectionDsl,
    // frameCols {}, frameCols()
    FrameColsColumnsSelectionDsl,
    // colGroups {}, colGroups()
    ColGroupsColumnsSelectionDsl,
    // colsOfKind(Value, Frame) {}, colsOfKind(Value, Frame)
    ColsOfKindColumnsSelectionDsl,

    // all(Cols), allAfter(colA), allBefore(colA), allFrom(colA), allUpTo(colA)
    AllColumnsSelectionDsl,
    // colsAtAnyDepth {}, colsAtAnyDepth()
    ColsAtAnyDepthColumnsSelectionDsl,
    // colsInGroups {}, colsInGroups()
    ColsInGroupsColumnsSelectionDsl,
    // take(5), takeLastCols(2), takeLastWhile {}, takeColsWhile {}
    TakeColumnsSelectionDsl,
    // drop(5), dropLastCols(2), dropLastWhile {}, dropColsWhile {}
    DropColumnsSelectionDsl,

    // select {}, TODO due to String.invoke conflict this cannot be moved out of ColumnsSelectionDsl
    SelectColumnsSelectionDsl,
    // except(), allExcept {}, allColsExcept {}
    AllExceptColumnsSelectionDsl,

    // nameContains(""), colsNameContains(""), nameStartsWith(""), childrenNameEndsWith("")
    ColumnNameFiltersColumnsSelectionDsl,
    // withoutNulls(), colsWithoutNulls()
    WithoutNullsColumnsSelectionDsl,
    // distinct()
    DistinctColumnsSelectionDsl,
    // none()
    NoneColumnsSelectionDsl,
    // colsOf<>(), colsOf<> {}
    ColsOfColumnsSelectionDsl,
    // simplify()
    SimplifyColumnsSelectionDsl,
    // filter {}
    FilterColumnsSelectionDsl,
    // colSet and colB
    AndColumnsSelectionDsl,
    // colA named "colB", colA into "colB"
    RenameColumnsSelectionDsl,
    // expr {}
    ExprColumnsSelectionDsl {

    /**
     * ## [ColumnsSelectionDsl] Usage
     *
     * @include [UsageTemplateColumnsSelectionDsl.UsageTemplate]
     *
     * {@setArg [UsageTemplate.DefinitionsArg]
     *  {@include [UsageTemplate.ColumnGroupNoSingleColumnDef]}
     *
     *  {@include [UsageTemplate.ColumnSelectorDef]}
     *
     *  {@include [UsageTemplate.ColumnsSelectorDef]}
     *
     *  {@include [UsageTemplate.ColumnDef]}
     *
     *  {@include [UsageTemplate.ColumnGroupDef]}
     *
     *  {@include [UsageTemplate.ColumnNoAccessorDef]}
     *
     *  {@include [UsageTemplate.ColumnOrColumnSetDef]}
     *
     *  {@include [UsageTemplate.ColumnSetDef]}
     *
     *  {@include [UsageTemplate.ColumnsResolverDef]}
     *
     *  {@include [UsageTemplate.ConditionDef]}
     *
     *  {@include [UsageTemplate.ColumnExpressionDef]}
     *
     *  {@include [UsageTemplate.IgnoreCaseDef]}
     *
     *  {@include [UsageTemplate.IndexDef]}
     *
     *  {@include [UsageTemplate.IndexRangeDef]}
     *
     *  {@include [UsageTemplate.InferDef]}
     *
     *  {@include [UsageTemplate.ColumnKindDef]}
     *
     *  {@include [UsageTemplate.KTypeDef]}
     *
     *  {@include [UsageTemplate.NameDef]}
     *
     *  {@include [UsageTemplate.NumberDef]}
     *
     *  {@include [UsageTemplate.RegexDef]}
     *
     *  {@include [UsageTemplate.SingleColumnDef]}
     *
     *  {@include [UsageTemplate.ColumnTypeDef]}
     *
     *  {@include [UsageTemplate.TextDef]}
     * }
     * {@comment Plain DSL: -------------------------------------------------------------------------------------------- }
     * {@setArg [UsageTemplate.PlainDslFunctionsArg]
     *
     * {@include [UsageTemplate.ColumnRef]} {@include [ColumnRangeColumnsSelectionDsl.Usage.PlainDslName]} {@include [UsageTemplate.ColumnRef]}
     *
     *  `|` **`this`**`/`**`it`**[**`[`**][cols]{@include [UsageTemplate.ColumnRef]}**`,`**` .. `[**`]`**][cols]
     *
     *  `|` **`this`**`/`**`it`**[**`[`**][cols]**`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`**[**`]`**][cols]
     *
     *  `|` {@include [AllColumnsSelectionDsl.Usage.PlainDslName]}**`()`**
     *
     *  `|` **`all`**`(`{@include [AllColumnsSelectionDsl.Usage.Before]}`|`{@include [AllColumnsSelectionDsl.Usage.After]}`|`{@include [AllColumnsSelectionDsl.Usage.From]}`|`{@include [AllColumnsSelectionDsl.Usage.UpTo]}`)` `(` **`(`**{@include [UsageTemplate.ColumnRef]}**`)`** `|` **`{`** {@include [UsageTemplate.ColumnSelectorRef]} **`\\}`** `)`
     *
     *  `|` {@include [AllExceptColumnsSelectionDsl.Usage.PlainDslName]} **`{ `**{@include [UsageTemplate.ColumnsSelectorRef]}**` \\}`**
     *
     *  `|` {@include [AllExceptColumnsSelectionDsl.Usage.PlainDslName]}**`(`**{@include [UsageTemplate.ColumnRef]}**`,`**` ..`**`)`**
     *
     *  `|` {@include [UsageTemplate.ColumnOrColumnSetRef]} {@include [AndColumnsSelectionDsl.Usage.InfixName]}` [ `**`{`**` ] `{@include [UsageTemplate.ColumnOrColumnSetRef]}` [ `**`\\}`**` ] `
     *
     *  `|` {@include [UsageTemplate.ColumnOrColumnSetRef]}{@include [AndColumnsSelectionDsl.Usage.Name]} **`(`**`|`**`{ `**{@include [UsageTemplate.ColumnOrColumnSetRef]}**` \\}`**`|`**`)`**
     *
     *  `|` `(`
     *  {@include [ColColumnsSelectionDsl.Usage.PlainDslName]}
     *  `|` {@include [ValueColColumnsSelectionDsl.Usage.PlainDslName]}
     *  `|` {@include [FrameColColumnsSelectionDsl.Usage.PlainDslName]}
     *  `|` {@include [ColGroupColumnsSelectionDsl.Usage.PlainDslName]}
     *  `)[`**`<`**{@include [UsageTemplate.ColumnTypeRef]}**`>`**`]`**`(`**{@include [UsageTemplate.ColumnRef]}` | `{@include [UsageTemplate.IndexRef]}**`)`**
     *
     * `|` `(`
     *  {@include [ColsColumnsSelectionDsl.Usage.PlainDslName]}
     *  `|` {@include [ValueColsColumnsSelectionDsl.Usage.PlainDslName]}
     *  `|` {@include [FrameColsColumnsSelectionDsl.Usage.PlainDslName]}
     *  `|` {@include [ColGroupsColumnsSelectionDsl.Usage.PlainDslName]}
     *  `) [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  `|` {@include [ColsColumnsSelectionDsl.Usage.PlainDslName]}`[`**`<`**{@include [UsageTemplate.ColumnTypeRef]}**`>`**`]`**`(`**{@include [UsageTemplate.ColumnRef]}**`,`**` .. | `{@include [UsageTemplate.IndexRef]}**`,`**` .. | `{@include [UsageTemplate.IndexRangeRef]}**`)`**
     *
     *  `|` {@include [ColsAtAnyDepthColumnsSelectionDsl.Usage.PlainDslName]}` [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  `|` {@include [ColsInGroupsColumnsSelectionDsl.Usage.PlainDslName]}` [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]
     *
     *  `|` {@include [ColsOfColumnsSelectionDsl.Usage.PlainDslName]}**`<`**{@include [UsageTemplate.ColumnTypeRef]}**`>`**` [` **`(`**{@include [UsageTemplate.KTypeRef]}**`)`** `] [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  `|` {@include [ColsOfKindColumnsSelectionDsl.Usage.PlainDslName]}**`(`**{@include [UsageTemplate.ColumnKindRef]}**`,`**` ..`**`)`**` [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  `|` {@include [DropColumnsSelectionDsl.Usage.PlainDslName]}**`(`**{@include [UsageTemplate.NumberRef]}**`)`**
     *
     *  `|` {@include [DropColumnsSelectionDsl.Usage.PlainDslWhileName]}**` { `**{@include [UsageTemplate.ConditionRef]}**` \\}`**
     *
     *  `|` {@include [ExprColumnsSelectionDsl.Usage.PlainDslName]}**`(`**`[`{@include [UsageTemplate.NameRef]}**`,`**`][`{@include [UsageTemplate.InferRef]}`]`**`)`** **`{ `**{@include [UsageTemplate.ColumnExpressionRef]}**` \\}`**
     *
     *  `|` `(`
     *  {@include [FirstColumnsSelectionDsl.Usage.PlainDslName]}
     *  `|` {@include [LastColumnsSelectionDsl.Usage.PlainDslName]}
     *  `|` {@include [SingleColumnsSelectionDsl.Usage.PlainDslName]}
     *  `) [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  `|` {@include [ColumnNameFiltersColumnsSelectionDsl.Usage.PlainDslNameContains]}**`(`**{@include [UsageTemplate.TextRef]}`[`**`,`** {@include [UsageTemplate.IgnoreCaseRef]}`] | `{@include [UsageTemplate.RegexRef]}**`)`**
     *
     *  `|` {@include [ColumnNameFiltersColumnsSelectionDsl.Usage.PlainDslNameStartsEndsWith]}**`(`**{@include [UsageTemplate.TextRef]}`[`**`,`** {@include [UsageTemplate.IgnoreCaseRef]}`]`**`)`**
     *
     *  `|` {@include [UsageTemplate.ColumnRef]} {@include [RenameColumnsSelectionDsl.Usage.InfixNamedName]}`/`{@include [RenameColumnsSelectionDsl.Usage.InfixIntoName]} {@include [UsageTemplate.ColumnRef]}
     *
     *  `|` {@include [UsageTemplate.ColumnRef]}`(`{@include [RenameColumnsSelectionDsl.Usage.NamedName]}`|`{@include [RenameColumnsSelectionDsl.Usage.IntoName]}`)`**`(`**{@include [UsageTemplate.ColumnRef]}**`)`**
     *
     *  `|` {@include [NoneColumnsSelectionDsl.Usage.PlainDslName]}**`()`**
     *
     *  `|` {@include [TakeColumnsSelectionDsl.Usage.PlainDslName]}**`(`**{@include [UsageTemplate.NumberRef]}**`)`**
     *
     *  `|` {@include [TakeColumnsSelectionDsl.Usage.PlainDslWhileName]}**` { `**{@include [UsageTemplate.ConditionRef]}**` \\}`**
     *
     *  `|` {@include [WithoutNullsColumnsSelectionDsl.Usage.PlainDslName]}**`()`**
     * }
     * {@comment ColumnSet: -------------------------------------------------------------------------------------------- }
     * {@setArg [UsageTemplate.ColumnSetFunctionsArg]
     *
     *  {@include [Indent]}[**`[`**][ColumnsSelectionDsl.col]{@include [UsageTemplate.IndexRef]}[**`]`**][ColumnsSelectionDsl.col]
     *
     *  {@include [Indent]}`|` [**`[`**][cols]{@include [UsageTemplate.IndexRef]}**`,`**` .. | `{@include [UsageTemplate.IndexRangeRef]}[**`]`**][cols]`
     *
     *  {@include [Indent]}`|` [**`[`**][cols]**`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`**[**`]`**][cols]
     *
     *  {@include [Indent]}`|` {@include [AllColumnsSelectionDsl.Usage.ColumnSetName]}**`()`**
     *
     *  {@include [Indent]}`|` .**`all`**`(`{@include [AllColumnsSelectionDsl.Usage.Before]}`|`{@include [AllColumnsSelectionDsl.Usage.After]}`|`{@include [AllColumnsSelectionDsl.Usage.From]}`|`{@include [AllColumnsSelectionDsl.Usage.UpTo]}`)` `(` **`(`**{@include [UsageTemplate.ColumnRef]}**`)`** `|` **`{`** {@include [UsageTemplate.ConditionRef]} **`\\}`** `)`
     *
     *  {@include [Indent]}`|` {@include [AndColumnsSelectionDsl.Usage.Name]} **`(`**`|`**`{ `**{@include [UsageTemplate.ColumnOrColumnSetRef]}**` \\}`**`|`**`)`**
     *
     *  {@include [Indent]}`|` `(`
     *  {@include [ColColumnsSelectionDsl.Usage.ColumnSetName]}
     *  `|` {@include [ValueColColumnsSelectionDsl.Usage.ColumnSetName]}
     *  `|` {@include [FrameColColumnsSelectionDsl.Usage.ColumnSetName]}
     *  `|` {@include [ColGroupColumnsSelectionDsl.Usage.ColumnSetName]}
     *  `)`**`(`**{@include [UsageTemplate.IndexRef]}**`)`**
     *
     *  {@include [Indent]}`|` `(`
     *  {@include [ColsColumnsSelectionDsl.Usage.ColumnSetName]}
     *  `|` {@include [ValueColsColumnsSelectionDsl.Usage.ColumnSetName]}
     *  `|` {@include [FrameColsColumnsSelectionDsl.Usage.ColumnSetName]}
     *  `|` {@include [ColGroupsColumnsSelectionDsl.Usage.ColumnSetName]}
     *  `) [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  {@include [Indent]}`|` {@include [ColsColumnsSelectionDsl.Usage.ColumnSetName]}**`(`**{@include [UsageTemplate.IndexRef]}**`,`**` .. | `{@include [UsageTemplate.IndexRangeRef]}**`)`**
     *
     *  {@include [Indent]}`|` {@include [ColsAtAnyDepthColumnsSelectionDsl.Usage.ColumnSetName]}` [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  {@include [Indent]}`|` {@include [ColsInGroupsColumnsSelectionDsl.Usage.ColumnSetName]}` [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  {@include [Indent]}`|` {@include [ColsOfColumnsSelectionDsl.Usage.ColumnSetName]}**`<`**{@include [UsageTemplate.ColumnTypeRef]}**`>`**` [` **`(`**{@include [UsageTemplate.KTypeRef]}**`)`** `] [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  {@include [Indent]}`|` {@include [ColsOfKindColumnsSelectionDsl.Usage.ColumnSetName]}**`(`**{@include [UsageTemplate.ColumnKindRef]}**`,`**` ..`**`)`**` [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  {@include [Indent]}`|` {@include [DistinctColumnsSelectionDsl.Usage.ColumnSetName]}**`()`**
     *
     *  {@include [Indent]}`|` {@include [DropColumnsSelectionDsl.Usage.ColumnSetName]}**`(`**{@include [UsageTemplate.NumberRef]}**`)`**
     *
     *  {@include [Indent]}`|` {@include [DropColumnsSelectionDsl.Usage.ColumnSetWhileName]}**` { `**{@include [UsageTemplate.ConditionRef]}**` \\}`**
     *
     *  {@include [Indent]}`|` {@include [AllExceptColumnsSelectionDsl.Usage.ColumnSetName]} `[`**` { `**`]` {@include [UsageTemplate.ColumnsResolverRef]} `[`**` \\} `**`]`
     *
     *  {@include [Indent]}`|` {@include [AllExceptColumnsSelectionDsl.Usage.ColumnSetName]} {@include [UsageTemplate.ColumnRef]}
     *
     *  {@include [Indent]}`|` .{@include [AllExceptColumnsSelectionDsl.Usage.ColumnSetName]}**`(`**{@include [UsageTemplate.ColumnRef]}**`,`**` ..`**`)`**
     *
     *  {@include [Indent]}`|` {@include [FilterColumnsSelectionDsl.Usage.ColumnSetName]}**` {`** {@include [UsageTemplate.ConditionRef]} **`\\}`**
     *
     *  {@include [Indent]}`|` `(`
     *  {@include [FirstColumnsSelectionDsl.Usage.ColumnSetName]}
     *  `|` {@include [LastColumnsSelectionDsl.Usage.ColumnSetName]}
     *  `|` {@include [SingleColumnsSelectionDsl.Usage.ColumnSetName]}
     *  `) [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  {@include [Indent]}`|` {@include [ColumnNameFiltersColumnsSelectionDsl.Usage.ColumnSetNameStartsEndsWith]}**`(`**{@include [UsageTemplate.TextRef]}`[`**`,`** {@include [UsageTemplate.IgnoreCaseRef]}`]`**`)`**
     *
     *  {@include [Indent]}`|` {@include [ColumnNameFiltersColumnsSelectionDsl.Usage.ColumnSetNameContains]}**`(`**{@include [UsageTemplate.TextRef]}`[`**`,`** {@include [UsageTemplate.IgnoreCaseRef]}`] | `{@include [UsageTemplate.RegexRef]}**`)`**
     *
     *  {@include [Indent]}`|` {@include [SimplifyColumnsSelectionDsl.Usage.ColumnSetName]}**`()`**
     *
     *  {@include [Indent]}`|` {@include [TakeColumnsSelectionDsl.Usage.ColumnSetName]}**`(`**{@include [UsageTemplate.NumberRef]}**`)`**
     *
     *  {@include [Indent]}`|` {@include [TakeColumnsSelectionDsl.Usage.ColumnSetWhileName]}**` { `**{@include [UsageTemplate.ConditionRef]}**` \\}`**
     *
     *  {@include [Indent]}`|` {@include [WithoutNullsColumnsSelectionDsl.Usage.ColumnSetName]}**`()`**
     * }
     * {@comment ColumnGroup: -------------------------------------------------------------------------------------------- }
     * {@setArg [UsageTemplate.ColumnGroupFunctionsArg]
     *
     *  {@include [Indent]}`|` [**`[`**][cols]{@include [UsageTemplate.ColumnRef]}**`,`**` ..`[**`]`**][cols]
     *
     *  {@include [Indent]}`|` [**`[`**][cols]**`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`**[**`]`**][cols]
     *
     *  {@include [Indent]}`|`[**` {`**][ColumnsSelectionDsl.select] {@include [UsageTemplate.ColumnsSelectorRef]} [**`\\}`**][ColumnsSelectionDsl.select]
     *
     *  {@include [Indent]}`|` {@include [AllColumnsSelectionDsl.Usage.ColumnGroupName]}**`()`**
     *
     *  {@include [Indent]}`|` .**`allCols`**`(`{@include [AllColumnsSelectionDsl.Usage.Before]}`|`{@include [AllColumnsSelectionDsl.Usage.After]}`|`{@include [AllColumnsSelectionDsl.Usage.From]}`|`{@include [AllColumnsSelectionDsl.Usage.UpTo]}`)` `(` **`(`**{@include [UsageTemplate.ColumnRef]}**`)`** `|` **`{`** {@include [UsageTemplate.ColumnSelectorRef]} **`\\}`** `)`
     *
     *  {@include [Indent]}`|` {@include [AllExceptColumnsSelectionDsl.Usage.ColumnGroupName]} **` { `**{@include [UsageTemplate.ColumnsSelectorRef]}**` \\} `**
     *
     *  {@include [Indent]}`|` {@include [AllExceptColumnsSelectionDsl.Usage.ColumnGroupName]}**`(`**{@include [UsageTemplate.ColumnNoAccessorRef]}**`,`**` ..`**`)`**
     *
     *  {@include [Indent]}`|` {@include [AndColumnsSelectionDsl.Usage.Name]} **`(`**`|`**`{ `**{@include [UsageTemplate.ColumnOrColumnSetRef]}**` \\}`**`|`**`)`**
     *
     *  {@include [Indent]}`| (`
     *  {@include [ColColumnsSelectionDsl.Usage.ColumnGroupName]}
     *  `|` {@include [ValueColColumnsSelectionDsl.Usage.ColumnGroupName]}
     *  `|` {@include [FrameColColumnsSelectionDsl.Usage.ColumnGroupName]}
     *  `|` {@include [ColGroupColumnsSelectionDsl.Usage.ColumnGroupName]}
     *  `)[`**`<`**{@include [UsageTemplate.ColumnTypeRef]}**`>`**`]`**`(`**{@include [UsageTemplate.ColumnRef]}` | `{@include [UsageTemplate.IndexRef]}**`)`**
     *
     *  {@include [Indent]}`|` `(`
     *   {@include [ColsColumnsSelectionDsl.Usage.ColumnGroupName]}
     *   `|` {@include [ValueColsColumnsSelectionDsl.Usage.ColumnGroupName]}
     *   `|` {@include [FrameColsColumnsSelectionDsl.Usage.ColumnGroupName]}
     *   `|` {@include [ColGroupsColumnsSelectionDsl.Usage.ColumnGroupName]}
     *   `) [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  {@include [Indent]}`|` {@include [ColsColumnsSelectionDsl.Usage.ColumnGroupName]}`[`**`<`**{@include [UsageTemplate.ColumnTypeRef]}**`>`**`]`**`(`**{@include [UsageTemplate.ColumnRef]}**`,`**` .. | `{@include [UsageTemplate.IndexRef]}**`,`**` .. | `{@include [UsageTemplate.IndexRangeRef]}**`)`**
     *
     *  {@include [Indent]}`|` {@include [ColsAtAnyDepthColumnsSelectionDsl.Usage.ColumnGroupName]}` [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  {@include [Indent]}`|` {@include [ColsInGroupsColumnsSelectionDsl.Usage.ColumnGroupName]}` [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  {@include [Indent]}`|` {@include [ColumnNameFiltersColumnsSelectionDsl.Usage.ColumnGroupNameStartsWith]}**`(`**{@include [UsageTemplate.TextRef]}`[`**`,`** {@include [UsageTemplate.IgnoreCaseRef]}`]`**`)`**
     *
     *  {@include [Indent]}`|` {@include [ColumnNameFiltersColumnsSelectionDsl.Usage.ColumnGroupNameContains]}**`(`**{@include [UsageTemplate.TextRef]}`[`**`,`** {@include [UsageTemplate.IgnoreCaseRef]}`] | `{@include [UsageTemplate.RegexRef]}**`)`**
     *
     *  {@include [Indent]}`|` {@include [ColsOfKindColumnsSelectionDsl.Usage.ColumnGroupName]}**`(`**{@include [UsageTemplate.ColumnKindRef]}**`,`**` ..`**`)`**` [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  {@include [Indent]}`|` {@include [WithoutNullsColumnsSelectionDsl.Usage.ColumnGroupName]}**`()`**
     *
     *  {@include [Indent]}`|` {@include [DropColumnsSelectionDsl.Usage.ColumnGroupName]}**`(`**{@include [UsageTemplate.NumberRef]}**`)`**
     *
     *  {@include [Indent]}`|` {@include [DropColumnsSelectionDsl.Usage.ColumnGroupWhileName]}**` { `**{@include [UsageTemplate.ConditionRef]}**` \\}`**
     *
     *  {@include [Indent]}`|` {@include [AllExceptColumnsSelectionDsl.Usage.ColumnGroupExperimentalName]} **` { `**{@include [UsageTemplate.ColumnsSelectorRef]}**` \\} EXPERIMENTAL!`**
     *
     *  {@include [Indent]}`|` {@include [AllExceptColumnsSelectionDsl.Usage.ColumnGroupExperimentalName]}**`(`**{@include [UsageTemplate.ColumnNoAccessorRef]}**`,`**` ..`**`) EXPERIMENTAL!`**
     *
     *  {@include [Indent]}`|` `(`
     *  {@include [FirstColumnsSelectionDsl.Usage.ColumnGroupName]}
     *  `|` {@include [LastColumnsSelectionDsl.Usage.ColumnGroupName]}
     *  `|` {@include [SingleColumnsSelectionDsl.Usage.ColumnGroupName]}
     *  `) [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  {@include [Indent]}`|` {@include [SelectColumnsSelectionDsl.Usage.ColumnGroupName]}**` {`** {@include [UsageTemplate.ColumnsSelectorRef]} **`\\}`**
     *
     *  {@include [Indent]}`|` {@include [TakeColumnsSelectionDsl.Usage.ColumnGroupName]}**`(`**{@include [UsageTemplate.NumberRef]}**`)`**
     *
     *  {@include [Indent]}`|` {@include [TakeColumnsSelectionDsl.Usage.ColumnGroupWhileName]}**` { `**{@include [UsageTemplate.ConditionRef]}**` \\}`**
     *
     *  {@include [LineBreak]}
     *
     *  {@include [UsageTemplate.SingleColumnRef]}
     *
     *  {@include [Indent]}{@include [ColsOfColumnsSelectionDsl.Usage.ColumnGroupName]}**`<`**{@include [UsageTemplate.ColumnTypeRef]}**`>`**` [` **`(`**{@include [UsageTemplate.KTypeRef]}**`)`** `] [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     *
     *  {@include [LineBreak]}
     *
     *  {@include [UsageTemplate.ColumnGroupNoSingleColumnRef]}
     *
     *  {@include [Indent]}{@include [ColsOfColumnsSelectionDsl.Usage.ColumnGroupName]}**`<`**{@include [UsageTemplate.ColumnTypeRef]}**`>(`**{@include [UsageTemplate.KTypeRef]}**`)`** ` [` **`{ `**{@include [UsageTemplate.ConditionRef]}**` \\}`** `]`
     * }
     */
    public interface Usage

    /**
     * Invokes the given [ColumnsSelector] using this [ColumnsSelectionDsl].
     */
    public operator fun <C> ColumnsSelector<T, C>.invoke(): ColumnsResolver<C> =
        this@invoke(this@ColumnsSelectionDsl, this@ColumnsSelectionDsl)

    /**
     * ## Deprecated: Columns by Index Range from List of Columns
     * Helper function to create a [ColumnSet] from a list of columns by specifying a range of indices.
     *
     * ### Deprecated
     *
     * Deprecated because it's too niche. Let us know if you have a good use for it!
     */
    @Deprecated(
        message = COL_SELECT_DSL_LIST_DATACOLUMN_GET,
        replaceWith = ReplaceWith(COL_SELECT_DSL_LIST_DATACOLUMN_GET_REPLACE),
        level = DeprecationLevel.WARNING,
    )
    public operator fun <C> List<DataColumn<C>>.get(range: IntRange): ColumnSet<C> =
        ColumnsList(subList(range.first, range.last + 1))

    // region select
    // NOTE: due to invoke conflicts these cannot be moved out of the interface

    /**
     * @include [SelectColumnsSelectionDsl.CommonSelectDocs]
     * @setArg [SelectColumnsSelectionDsl.CommonSelectDocs.ExampleArg]
     *
     * `df.`[select][DataFrame.select]` { myColGroup.`[select][SingleColumn.select]` { someCol `[and][ColumnsSelectionDsl.and]` `[colsOf][SingleColumn.colsOf]`<`[String][String]`>() } }`
     *
     * `df.`[select][DataFrame.select]` { myColGroup `[{][SingleColumn.select]` colA `[and][ColumnsSelectionDsl.and]` colB `[}][SingleColumn.select]` }`
     */
    public operator fun <C, R> SingleColumn<DataRow<C>>.invoke(selector: ColumnsSelector<C, R>): ColumnSet<R> =
        select(selector)

    /**
     * @include [SelectColumnsSelectionDsl.CommonSelectDocs]
     * @setArg [SelectColumnsSelectionDsl.CommonSelectDocs.ExampleArg]
     *
     * `df.`[select][DataFrame.select]` { Type::myColGroup.`[select][KProperty.select]` { someCol `[and][ColumnsSelectionDsl.and]` `[colsOf][SingleColumn.colsOf]`<`[String][String]`>() } }`
     *
     * `df.`[select][DataFrame.select]` { DataSchemaType::myColGroup `[`{`][KProperty.select]` colA `[and][ColumnsSelectionDsl.and]` colB `[`}`][KProperty.select]` }`
     *
     * ## NOTE: {@comment TODO fix warning}
     * If you get a warning `CANDIDATE_CHOSEN_USING_OVERLOAD_RESOLUTION_BY_LAMBDA_ANNOTATION`, you
     * can safely ignore this. It is caused by a workaround for a bug in the Kotlin compiler
     * ([KT-64092](https://youtrack.jetbrains.com/issue/KT-64092/OVERLOADRESOLUTIONAMBIGUITY-caused-by-lambda-argument)).
     */
    @Suppress("INAPPLICABLE_JVM_NAME")
    @JvmName("KPropertyDataRowInvoke")
    public operator fun <C, R> KProperty<DataRow<C>>.invoke(selector: ColumnsSelector<C, R>): ColumnSet<R> =
        select(selector)

    /**
     * @include [SelectColumnsSelectionDsl.CommonSelectDocs]
     * @setArg [SelectColumnsSelectionDsl.CommonSelectDocs.ExampleArg]
     *
     * `df.`[select][DataFrame.select]` { Type::myColGroup.`[select][KProperty.select]` { someCol `[and][ColumnsSelectionDsl.and]` `[colsOf][SingleColumn.colsOf]`<`[String][String]`>() } }`
     *
     * `df.`[select][DataFrame.select]` { DataSchemaType::myColGroup `[`{`][KProperty.select]` colA `[and][ColumnsSelectionDsl.and]` colB `[`}`][KProperty.select]` }`
     */
    @OptIn(ExperimentalTypeInference::class)
    @OverloadResolutionByLambdaReturnType
    public operator fun <C, R> KProperty<C>.invoke(selector: ColumnsSelector<C, R>): ColumnSet<R> =
        columnGroup(this).select(selector)

    /**
     * @include [SelectColumnsSelectionDsl.CommonSelectDocs]
     * @setArg [SelectColumnsSelectionDsl.CommonSelectDocs.ExampleArg]
     *
     * `df.`[select][DataFrame.select]` { "myColGroup".`[select][String.select]` { someCol `[and][ColumnsSelectionDsl.and]` `[colsOf][SingleColumn.colsOf]`<`[String][String]`>() } }`
     *
     * `df.`[select][DataFrame.select]` { "myColGroup" `[{][String.select]` colA `[and][ColumnsSelectionDsl.and]` colB `[}][String.select]` }`
     */
    public operator fun <R> String.invoke(selector: ColumnsSelector<*, R>): ColumnSet<R> =
        select(selector)

    /**
     * @include [SelectColumnsSelectionDsl.CommonSelectDocs]
     * @setArg [SelectColumnsSelectionDsl.CommonSelectDocs.ExampleArg]
     *
     * `df.`[select][DataFrame.select]` { "pathTo"["myColGroup"].`[select][ColumnPath.select]` { someCol `[and][ColumnsSelectionDsl.and]` `[colsOf][SingleColumn.colsOf]`<`[String][String]`>() } }`
     *
     * `df.`[select][DataFrame.select]` { "pathTo"["myColGroup"] `[{][ColumnPath.select]` colA `[and][ColumnsSelectionDsl.and]` colB `[}][ColumnPath.select]` }`
     *
     * `df.`[select][DataFrame.select]` { `[pathOf][pathOf]`("pathTo", "myColGroup").`[select][ColumnPath.select]` { someCol `[and][ColumnsSelectionDsl.and]` `[colsOf][SingleColumn.colsOf]`<`[String][String]`>() } }`
     *
     * `df.`[select][DataFrame.select]` { `[pathOf][pathOf]`("pathTo", "myColGroup")`[() {][ColumnPath.select]` someCol `[and][ColumnsSelectionDsl.and]` `[colsOf][SingleColumn.colsOf]`<`[String][String]`>() `[}][ColumnPath.select]` }`
     */
    public operator fun <R> ColumnPath.invoke(selector: ColumnsSelector<*, R>): ColumnSet<R> =
        select(selector)

    // endregion
}
