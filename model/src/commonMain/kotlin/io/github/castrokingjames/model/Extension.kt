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
package io.github.castrokingjames.model

import java.text.SimpleDateFormat
import java.util.Calendar

fun Weather.getIcon(): String {
  val calendar = Calendar.getInstance()
  val hour = calendar.get(Calendar.HOUR_OF_DAY)
  val slag = if (hour >= 18) {
    icon.replace("d", "n")
  } else {
    icon.replace("n", "d")
  }
  return "https://openweathermap.org/img/wn/$slag@4x.png"
}

fun Weather.getDate(): String {
  val sdf = SimpleDateFormat("MMMM dd, hh:mm")
  val calendar = Calendar.getInstance()
  calendar.timeInMillis = id
  val date = calendar.time
  return sdf.format(date)
}
