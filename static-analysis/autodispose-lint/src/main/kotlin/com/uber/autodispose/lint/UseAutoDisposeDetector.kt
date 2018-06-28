/*
 * Copyright (C) 2018. Uber Technologies
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

package com.uber.autodispose.lint

import com.android.tools.lint.client.api.UElementHandler
import com.android.tools.lint.detector.api.Category
import com.android.tools.lint.detector.api.Detector
import com.android.tools.lint.detector.api.Implementation
import com.android.tools.lint.detector.api.Issue
import com.android.tools.lint.detector.api.JavaContext
import com.android.tools.lint.detector.api.LintUtils
import com.android.tools.lint.detector.api.Scope.JAVA_FILE
import com.android.tools.lint.detector.api.Scope.TEST_SOURCES
import com.android.tools.lint.detector.api.Severity
import com.google.auto.service.AutoService
import org.jetbrains.uast.UCallExpression
import org.jetbrains.uast.getContainingUMethod
import java.util.EnumSet

@AutoService(Detector::class)
class UseAutoDisposeDetector : Detector(), Detector.UastScanner {

  companion object {
    @JvmField
    val ISSUE: Issue = Issue.create(
        "UseAutoDispose",
        "Always apply an AutoDispose scope before subscribing within defined scoped elements.",
        "When providing arguments to query you need to provide the same amount of " +
            "arguments that is specified in query.",
        Category.PERFORMANCE,
        9,
        Severity.ERROR,
        Implementation(UseAutoDisposeDetector::class.java, EnumSet.of(JAVA_FILE, TEST_SOURCES)))

    private const val AS = "as"
  }

  override fun getApplicableMethodNames() = listOf("subscribe")

  override fun createUastHandler(context: JavaContext): UElementHandler? {
    return object : UElementHandler() {
      override fun visitCallExpression(node: UCallExpression) {
        super.visitCallExpression(node)
      }
    }
  }
}
