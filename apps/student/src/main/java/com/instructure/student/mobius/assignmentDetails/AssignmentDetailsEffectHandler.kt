/*
 * Copyright (C) 2019 - present Instructure, Inc.
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, version 3 of the License.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */
package com.instructure.student.mobius.assignmentDetails

import com.instructure.canvasapi2.managers.AssignmentManager
import com.instructure.canvasapi2.models.Assignment
import com.instructure.canvasapi2.models.DiscussionTopic
import com.instructure.canvasapi2.utils.*
import com.instructure.canvasapi2.utils.weave.StatusCallbackError
import com.instructure.canvasapi2.utils.weave.awaitApiResponse
import com.instructure.student.mobius.assignmentDetails.ui.AssignmentDetailsView
import com.instructure.student.mobius.common.ui.EffectHandler
import kotlinx.coroutines.launch

class AssignmentDetailsEffectHandler : EffectHandler<AssignmentDetailsView, AssignmentDetailsEvent, AssignmentDetailsEffect>() {
    override fun accept(effect: AssignmentDetailsEffect) {
        when (effect) {
            is AssignmentDetailsEffect.ShowSubmitDialogView -> view?.showSubmitDialogView(effect.assignmentId, effect.course)
            is AssignmentDetailsEffect.ShowSubmissionView -> view?.showSubmissionView(effect.assignmentId, effect.course)
            is AssignmentDetailsEffect.ShowUploadStatusView -> view?.showUploadStatusView(effect.assignmentId, effect.course)
            is AssignmentDetailsEffect.LoadData -> {
                loadData(effect)
            }
            is AssignmentDetailsEffect.ObserveSubmissionStatus -> {
                // TODO: Get status from upload manager
                consumer.accept(AssignmentDetailsEvent.SubmissionStatusUpdated(SubmissionUploadStatus.Empty))
            }
            is AssignmentDetailsEffect.ShowCreateSubmissionView -> {
                when(effect.submissionType) {
                    Assignment.SubmissionType.ONLINE_QUIZ -> {
                        val url = APIHelper.getQuizURL(effect.courseId, effect.assignment.quizId)
                        view?.showQuizOrDiscussionView(url)
                    }
                    Assignment.SubmissionType.DISCUSSION_TOPIC -> {
                        val url = DiscussionTopic.getDiscussionURL(ApiPrefs.protocol, ApiPrefs.domain, effect.assignment.courseId, effect.assignment.discussionTopicHeader!!.id)
                        view?.showQuizOrDiscussionView(url)
                    }
                    Assignment.SubmissionType.ONLINE_UPLOAD -> {
                        view?.showFileUploadView(effect.assignment, effect.courseId)
                    }
                    Assignment.SubmissionType.ONLINE_TEXT_ENTRY -> {
                        view?.showOnlineTextEntryView(effect.assignment.id, effect.courseId)
                    }
                    Assignment.SubmissionType.ONLINE_URL -> {
                        view?.showOnlineUrlEntryView(effect.assignment.id, effect.courseId)
                    }
                    else -> { // Assignment.SubmissionType.MEDIA_RECORDING
                        view?.showMediaRecordingView(effect.assignment, effect.courseId)
                    }
                }
            }
        }.exhaustive
    }

    private fun loadData(effect: AssignmentDetailsEffect.LoadData) {
        launch {
            val result = try {
                val assignmentResponse = awaitApiResponse<Assignment> {
                    AssignmentManager.getAssignment(effect.assignmentId, effect.courseId, effect.forceNetwork, it)
                }
                DataResult.Success(assignmentResponse.body()!!)
            } catch (e: StatusCallbackError) {
                if (e.response?.code() == 401) {
                    DataResult.Fail(Failure.Authorization(e.response?.message()))
                } else {
                    DataResult.Fail(Failure.Network(e.response?.message()))
                }
            }
            consumer.accept(AssignmentDetailsEvent.DataLoaded(result))
        }
    }

}
