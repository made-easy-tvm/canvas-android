/*
 * Copyright (C) 2017 - present Instructure, Inc.
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 */
package com.instructure.canvasapi2.apis

import com.instructure.canvasapi2.StatusCallback
import com.instructure.canvasapi2.builders.RestBuilder
import com.instructure.canvasapi2.builders.RestParams
import com.instructure.canvasapi2.models.AuthenticatedSession
import com.instructure.canvasapi2.models.OAuthToken
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.io.IOException


object OAuthAPI {

    internal interface OAuthInterface {
        @DELETE("/login/oauth2/token")
        fun deleteToken(): Call<Void>

        @POST("/login/oauth2/token")
        fun getToken(@Query("client_id") clientId: String, @Query("client_secret") clientSecret: String, @Query("code") oAuthRequest: String, @Query(value = "redirect_uri", encoded = true) redirectURI: String): Call<OAuthToken>

        @GET("/login/session_token")
        fun getAuthenticatedSession(@Query("return_to") targetUrl: String): Call<AuthenticatedSession>
    }

    fun deleteToken(adapter: RestBuilder, params: RestParams, callback: StatusCallback<Void>) {
        callback.addCall(adapter.build(OAuthInterface::class.java, params).deleteToken()).enqueue(callback)
    }

    fun getToken(adapter: RestBuilder, params: RestParams, clientID: String, clientSecret: String, oAuthRequest: String, callback: StatusCallback<OAuthToken>) {
        callback.addCall(adapter.build(OAuthInterface::class.java, params).getToken(clientID, clientSecret, oAuthRequest, "https://app.madeeasytvm.in/login/oauth2/auth")).enqueue(callback)
    }

    fun getAuthenticatedSession(targetUrl: String, params: RestParams, adapter: RestBuilder, callback: StatusCallback<AuthenticatedSession>) {
        callback.addCall(adapter.build(OAuthInterface::class.java, params).getAuthenticatedSession(targetUrl)).enqueue(callback)
    }

    fun getAuthenticatedSessionSynchronous(targetUrl: String, params: RestParams, adapter: RestBuilder): String? {
        return try {
            adapter.build(OAuthInterface::class.java, params).getAuthenticatedSession(targetUrl).execute().body()?.sessionUrl
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }

    }
}
