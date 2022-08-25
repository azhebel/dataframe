

package org.jetbrains.kotlinx.dataframe.runners;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link GenerateNewCompilerTests.kt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("testData/diagnostics")
@TestDataPath("$PROJECT_ROOT")
public class DiagnosticTestGenerated extends AbstractDiagnosticTest {
    @Test
    public void testAllFilesPresentInDiagnostics() throws Exception {
        KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("testData/diagnostics"), Pattern.compile("^(.+)\\.kt$"), null, true);
    }

    @Test
    @TestMetadata("simple.kt")
    public void testSimple() throws Exception {
        runTest("testData/diagnostics/simple.kt");
    }
}
