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
package io.github.castrokingjames.datasource.remote

const val RESPONSE_WEATHER_200 = """
    {"coord":{"lon":-0.1257,"lat":51.5085},"weather":[{"id":804,"main":"Clouds","description":"overcast clouds","icon":"04d"}],"base":"stations","main":{"temp":278.14,"feels_like":276.27,"temp_min":276.64,"temp_max":279.31,"pressure":1018,"humidity":87,"sea_level":1018,"grnd_level":1015},"visibility":10000,"wind":{"speed":2.24,"deg":308,"gust":5.36},"clouds":{"all":100},"dt":1728803534,"sys":{"type":2,"id":2075535,"country":"GB","sunrise":1728800484,"sunset":1728839496},"timezone":3600,"id":2643743,"name":"London","cod":200}
"""

const val RESPONSE_WEATHER_401 = """
    {"cod":401, "message": "Invalid API key. Please see https://openweathermap.org/faq#error401 for more info."}
?"""

const val RESPONSE_LOCATION_200 = """
{"About Us":"https://ipwhois.io","ip":"131.226.100.170","success":true,"type":"IPv4","continent":"Asia","continent_code":"AS","country":"Philippines","country_code":"PH","region":"Metro Manila","region_code":"NCR","city":"Manila","latitude":14.5995124,"longitude":120.9842195,"is_eu":false,"postal":"1000","calling_code":"63","capital":"Manila","borders":"","flag":{"img":"https://cdn.ipwhois.io/flags/ph.svg","emoji":"🇵🇭","emoji_unicode":"U+1F1F5 U+1F1ED"},"connection":{"asn":139831,"org":"DITO TELECOMMUNITY CORP.","isp":"DITO TELECOMMUNITY CORP.","domain":"dito.ph"},"timezone":{"id":"Asia/Manila","abbr":"PST","is_dst":false,"offset":28800,"utc":"+08:00","current_time":"2024-10-14T07:24:17+08:00"}}
"""
