/*
 * Copyright 2020 HM Revenue & Customs
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

import org.scalatest.mockito.MockitoSugar
import org.mockito.Mockito.when
import org.mockito.Matchers.any
import play.api.data.Form
import play.api.libs.json.JsString
import uk.gov.hmrc.http.cache.client.CacheMap
import uk.gov.hmrc.valuationofficeagencycontactfrontend.FakeNavigator
import uk.gov.hmrc.valuationofficeagencycontactfrontend.connectors.{DataCacheConnector, FakeDataCacheConnector}
import uk.gov.hmrc.valuationofficeagencycontactfrontend.controllers.actions._
import play.api.test.Helpers._
import uk.gov.hmrc.valuationofficeagencycontactfrontend.forms.CouncilTaxSubcategoryForm
import uk.gov.hmrc.valuationofficeagencycontactfrontend.identifiers.CouncilTaxSubcategoryId
import uk.gov.hmrc.valuationofficeagencycontactfrontend.models.NormalMode
import uk.gov.hmrc.valuationofficeagencycontactfrontend.utils.MessageControllerComponentsHelpers
import uk.gov.hmrc.valuationofficeagencycontactfrontend.views.html.councilTaxSubcategory

import scala.concurrent.Future

class CouncilTaxSubcategoryControllerSpec extends ControllerSpecBase with MockitoSugar {

  def onwardRoute = routes.EnquiryCategoryController.onPageLoad(NormalMode)

  def controller(dataRetrievalAction: DataRetrievalAction = getEmptyCacheMap) = {
    val fakeDataCacheConnector = mock[DataCacheConnector]
    when(fakeDataCacheConnector.save(any, any, any)(any))
      .thenReturn(Future.successful(CacheMap("councilTaxSubcategory", Map("councilTaxSubcategory" -> JsString("bar")))))
    new CouncilTaxSubcategoryController(frontendAppConfig, messagesApi, fakeDataCacheConnector, new FakeNavigator(desiredRoute = onwardRoute),
      dataRetrievalAction, new DataRequiredActionImpl(ec), MessageControllerComponentsHelpers.stubMessageControllerComponents)
  }

  def viewAsString(form: Form[String] = CouncilTaxSubcategoryForm()) =
    councilTaxSubcategory(frontendAppConfig, form, NormalMode)(fakeRequest, messages).toString

  "CouncilTaxSubcategory Controller" must {

    "return OK and the correct view for a GET" in {
      val result = controller().onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
      contentAsString(result) mustBe viewAsString()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {
      val validData = Map(CouncilTaxSubcategoryId.toString -> JsString(CouncilTaxSubcategoryForm.options.head.value))
      val getRelevantData = new FakeDataRetrievalAction(Some(CacheMap(cacheMapId, validData)))

      val result = controller(getRelevantData).onPageLoad(NormalMode)(fakeRequest)

      contentAsString(result) mustBe viewAsString(CouncilTaxSubcategoryForm().fill(CouncilTaxSubcategoryForm.options.head.value))
    }

    "redirect to the next page when valid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", CouncilTaxSubcategoryForm.options.head.value))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))
      val boundForm = CouncilTaxSubcategoryForm().bind(Map("value" -> "invalid value"))

      val result = controller().onSubmit(NormalMode)(postRequest)

      status(result) mustBe BAD_REQUEST
      contentAsString(result) mustBe viewAsString(boundForm)
    }

    "redirect to Session Expired for a GET if no existing data is found" in {
      val result = controller(dontGetAnyData).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {
      val postRequest = fakeRequest.withFormUrlEncodedBody(("value", CouncilTaxSubcategoryForm.options.head.value))
      val result = controller(dontGetAnyData).onSubmit(NormalMode)(postRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(routes.SessionExpiredController.onPageLoad().url)
    }
  }
}
