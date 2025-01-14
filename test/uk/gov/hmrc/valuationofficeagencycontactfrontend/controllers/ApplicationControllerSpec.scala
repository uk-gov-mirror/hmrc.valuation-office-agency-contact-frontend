/*
 * Copyright 2021 HM Revenue & Customs
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

package uk.gov.hmrc.valuationofficeagencycontactfrontend.controllers

import play.api.data.Form
import play.api.test.Helpers._
import uk.gov.hmrc.valuationofficeagencycontactfrontend.controllers.actions._
import uk.gov.hmrc.valuationofficeagencycontactfrontend.forms.EnquiryCategoryForm
import uk.gov.hmrc.valuationofficeagencycontactfrontend.models.NormalMode
import uk.gov.hmrc.valuationofficeagencycontactfrontend.utils.MessageControllerComponentsHelpers
import uk.gov.hmrc.valuationofficeagencycontactfrontend.views.html.{contactReason => contact_reason}

class ApplicationControllerSpec extends ControllerSpecBase {

  def contactReason = app.injector.instanceOf[contact_reason]

  def languageSwitchController = app.injector.instanceOf[LanguageSwitchController]

  def onwardRoute = routes.EnquiryCategoryController.onPageLoad(NormalMode)

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) = new Application(
    messagesApi, frontendAppConfig, contactReason, languageSwitchController, MessageControllerComponentsHelpers.stubMessageControllerComponents)

  def viewAsString(form: Form[String] = EnquiryCategoryForm()) = contactReason(form, NormalMode)(fakeRequest, messages).toString

  "Application Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().start(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "return OK and the correct welsh view for a GET" in {
      val result = controller().startWelsh()(fakeRequest)

      status(result) mustBe SEE_OTHER
    }

    "return the contact reason controller url" in {
      controller().createRefererURL() mustBe
        uk.gov.hmrc.valuationofficeagencycontactfrontend.controllers.routes.ContactReasonController.onPageLoad().url
    }
  }
}
