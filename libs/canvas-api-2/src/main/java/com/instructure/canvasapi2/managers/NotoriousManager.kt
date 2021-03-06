/*
 * Copyright (C) 2018 - present Instructure, Inc.
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
package com.instructure.canvasapi2.managers

import com.instructure.canvasapi2.StatusCallback
import com.instructure.canvasapi2.apis.NotoriousAPI
import com.instructure.canvasapi2.builders.RestBuilder
import com.instructure.canvasapi2.builders.RestParams
import com.instructure.canvasapi2.models.NotoriousConfig
import com.instructure.canvasapi2.models.NotoriousSession
import com.instructure.canvasapi2.models.notorious.NotoriousResultWrapper
import com.instructure.canvasapi2.utils.ApiPrefs
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import java.io.File

object NotoriousManager {

    @JvmStatic
    fun getConfiguration(callback: StatusCallback<NotoriousConfig>) {
        val adapter = RestBuilder(callback)
        val params = RestParams()
        NotoriousAPI.getConfiguration(adapter, params, callback)
    }

    @JvmStatic
    fun startSession(callback: StatusCallback<NotoriousSession>) {
        val adapter = RestBuilder(callback)
        val params = RestParams()
        NotoriousAPI.startSession(adapter, params, callback)
    }

    @JvmStatic
    fun getUploadToken(callback: StatusCallback<NotoriousResultWrapper>) {
        val adapter = RestBuilder(callback)
        NotoriousAPI.getUploadToken(adapter, callback)
    }

    @JvmStatic
    fun uploadFileSynchronous(uploadToken: String, file: File, contentType: String): Response<Void>? {
        val requestBody = RequestBody.create(MediaType.parse(contentType), file)
        val filePart = MultipartBody.Part.createFormData("fileData", file.name, requestBody)
        val adapter = RestBuilder()
        return NotoriousAPI.uploadFileSynchronous(ApiPrefs.notoriousToken, uploadToken, filePart, adapter)
    }

    @JvmStatic
    fun getMediaIdSynchronous(uploadToken: String, fileName: String, mimeType: String): NotoriousResultWrapper? {
        val adapter = RestBuilder()
        return NotoriousAPI.getMediaIdSynchronous(ApiPrefs.notoriousToken, uploadToken, fileName, mimeType, adapter)
    }

}
