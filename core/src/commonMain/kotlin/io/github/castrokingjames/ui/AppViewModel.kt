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
package io.github.castrokingjames.ui

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import io.github.castrokingjames.feature.dashboard.DashboardComponent
import kotlinx.serialization.Serializable

class AppViewModel internal constructor(
  private val dashboardComponentFactory: DashboardComponent.Factory,
  private val componentContext: ComponentContext,
) : AppComponent, ComponentContext by componentContext {

  private val nav = StackNavigation<Config>()

  override val stack: Value<ChildStack<*, AppComponent.Child>> =
    childStack(
      source = nav,
      initialConfiguration = Config.Dashboard,
      serializer = Config.serializer(),
      handleBackButton = true,
      childFactory = ::childFactory,
    )

  private fun childFactory(config: Config, context: ComponentContext): AppComponent.Child {
    return when (config) {
      is Config.Dashboard -> dashboardComponent(context)
    }
  }

  private fun dashboardComponent(context: ComponentContext): AppComponent.Child {
    val component = dashboardComponentFactory(context)
    return AppComponent.Child.Dashboard(component)
  }

  @Serializable
  private sealed interface Config {

    @Serializable
    data object Dashboard : Config
  }
}
