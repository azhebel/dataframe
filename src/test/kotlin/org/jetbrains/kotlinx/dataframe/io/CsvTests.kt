package org.jetbrains.kotlinx.dataframe.io

import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import kotlinx.datetime.LocalDateTime
import org.jetbrains.kotlinx.dataframe.DataFrame
import org.jetbrains.kotlinx.dataframe.api.ParserOptions
import org.jetbrains.kotlinx.dataframe.api.allNulls
import org.jetbrains.kotlinx.dataframe.api.convert
import org.jetbrains.kotlinx.dataframe.api.dataFrameOf
import org.jetbrains.kotlinx.dataframe.api.group
import org.jetbrains.kotlinx.dataframe.api.groupBy
import org.jetbrains.kotlinx.dataframe.api.into
import org.jetbrains.kotlinx.dataframe.api.schema
import org.jetbrains.kotlinx.dataframe.ncol
import org.jetbrains.kotlinx.dataframe.nrow
import org.jetbrains.kotlinx.dataframe.testCsv
import org.junit.Test
import java.io.StringWriter
import java.util.Locale
import kotlin.reflect.KClass
import kotlin.reflect.typeOf

class CsvTests {

    @Test
    fun readNulls() {
        val src = """
            first,second
            2,,
            3,,
        """.trimIndent()
        val df = DataFrame.readDelimStr(src)
        df.nrow shouldBe 2
        df.ncol shouldBe 2
        df["first"].type() shouldBe typeOf<Int>()
        df["second"].allNulls() shouldBe true
        df["second"].type() shouldBe typeOf<String?>()
    }

    @Test
    fun write() {
        val df = dataFrameOf("col1", "col2")(
            1,
            null,
            2,
            null
        ).convert("col2").to<String>()

        val str = StringWriter()
        df.writeCSV(str)

        val res = DataFrame.readDelimStr(str.buffer.toString())

        res shouldBe df
    }

    @Test
    fun readCSV() {
        val df = DataFrame.read(simpleCsv)

        df.ncol shouldBe 11
        df.nrow shouldBe 5
        df.columnNames()[5] shouldBe "duplicate1"
        df.columnNames()[6] shouldBe "duplicate11"
        df["duplicate1"].type() shouldBe typeOf<String?>()
        df["double"].type() shouldBe typeOf<Double?>()
        df["time"].type() shouldBe typeOf<LocalDateTime>()

        println(df)
    }

    @Test
    fun readCSVwithFrenchLocaleAndAlternativeDelimiter() {
        val df = DataFrame.readCSV(csvWithFrenchLocale, delimiter = ';', parserOptions = ParserOptions(locale = Locale.FRENCH))

        df.ncol shouldBe 11
        df.nrow shouldBe 5
        df.columnNames()[5] shouldBe "duplicate1"
        df.columnNames()[6] shouldBe "duplicate11"
        df["duplicate1"].type() shouldBe typeOf<String?>()
        df["double"].type() shouldBe typeOf<Double?>()
        df["number"].type() shouldBe typeOf<Double>()
        df["time"].type() shouldBe typeOf<LocalDateTime>()

        println(df)
    }

    @Test
    fun readCsvWithFloats() {
        val df = DataFrame.readCSV(wineCsv, delimiter = ';')
        val schema = df.schema()
        fun assertColumnType(columnName: String, kClass: KClass<*>) {
            val col = schema.columns[columnName]
            col.shouldNotBeNull()
            col.type.classifier shouldBe kClass
        }

        assertColumnType("citric acid", Double::class)
        assertColumnType("alcohol", Double::class)
        assertColumnType("quality", Int::class)
    }

    @Test
    fun `read with custom header`() {
        val header = ('A'..'K').map { it.toString() }
        val df = DataFrame.readCSV(simpleCsv, headers = header, skipLines = 1)
        df.columnNames() shouldBe header
        df["B"].type() shouldBe typeOf<Int>()

        val headerShort = ('A'..'E').map { it.toString() }
        val dfShort = DataFrame.readCSV(simpleCsv, headers = headerShort, skipLines = 1)
        dfShort.ncol shouldBe 5
        dfShort.columnNames() shouldBe headerShort
    }

    @Test
    fun `read first rows`() {
        val expected =
            listOf("untitled", "user_id", "name", "duplicate", "username", "duplicate1", "duplicate11", "double", "number", "time", "empty")
        val dfHeader = DataFrame.readCSV(simpleCsv, readLines = 0)
        dfHeader.nrow shouldBe 0
        dfHeader.columnNames() shouldBe expected

        val dfThree = DataFrame.readCSV(simpleCsv, readLines = 3)
        dfThree.nrow shouldBe 3

        val dfFull = DataFrame.readCSV(simpleCsv, readLines = 10)
        dfFull.nrow shouldBe 5
    }

    @Test
    fun `if string starts with a number, it should be parsed as a string anyway`() {
        val df = DataFrame.readCSV(durationCsv)
        df["duration"].type() shouldBe typeOf<String>()
        df["floatDuration"].type() shouldBe typeOf<String>()
    }

    @Test
    fun `write and read frame column`() {
        val df = dataFrameOf("a", "b", "c")(
            1, 2, 3,
            1, 3, 2,
            2, 1, 3
        )
        val grouped = df.groupBy("a").into("g")
        val str = grouped.toCsv()
        val res = DataFrame.readDelimStr(str)
        res shouldBe grouped
    }

    @Test
    fun `write and read column group`() {
        val df = dataFrameOf("a", "b", "c")(
            1, 2, 3,
            1, 3, 2
        )
        val grouped = df.group("b", "c").into("d")
        val str = grouped.toCsv()
        val res = DataFrame.readDelimStr(str)
        res shouldBe grouped
    }

    companion object {
        private val simpleCsv = testCsv("testCSV")
        private val csvWithFrenchLocale = testCsv("testCSVwithFrenchLocale")
        private val wineCsv = testCsv("wine")
        private val durationCsv = testCsv("duration")
    }
}
