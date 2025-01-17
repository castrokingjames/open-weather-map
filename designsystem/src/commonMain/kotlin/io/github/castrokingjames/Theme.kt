/*
 * Copyright 2024, King James Castro and project contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.castrokingjames

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
  primary = md_theme_light_primary,
  onPrimary = md_theme_light_onPrimary,

  secondary = md_theme_light_secondary,
  onSecondary = md_theme_light_onSecondary,

  secondaryContainer = md_theme_light_secondaryContainer,
  onSecondaryContainer = md_theme_light_onSecondaryContainer,

  background = md_theme_light_background,
)

private val LightColorScheme = lightColorScheme(
  primary = md_theme_light_primary,
  onPrimary = md_theme_light_onPrimary,

  secondary = md_theme_light_secondary,
  onSecondary = md_theme_light_onSecondary,

  secondaryContainer = md_theme_light_secondaryContainer,
  onSecondaryContainer = md_theme_light_onSecondaryContainer,

  background = md_theme_light_background,
)

@Composable
fun AppTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = when {
    darkTheme -> DarkColorScheme
    else -> LightColorScheme
  }
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content,
  )
}
