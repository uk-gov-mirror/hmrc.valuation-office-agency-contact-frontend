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

package uk.gov.hmrc.valuationofficeagencycontactfrontend.views

import uk.gov.hmrc.valuationofficeagencycontactfrontend.views.behaviours.ViewBehaviours
import uk.gov.hmrc.valuationofficeagencycontactfrontend.views.html.{propertyWindWatertight => property_wind}

class PropertyWindWatertightViewSpec extends ViewBehaviours {

  def propertyWindWatertight = app.injector.instanceOf[property_wind]

  def view = () => propertyWindWatertight(frontendAppConfig)(fakeRequest, messages)

  "The Council Tax band cannot be reduced or removed view" must {
    behave like normalPage(view, "propertyWindWater", "title", "heading",
      "p1", "subheading", "p2.url", "p2")

    "has a link marked with site.back leading to the Council Tax band cannot be reduced or removed" in {
      val doc = asDocument(view())
      val backlinkText = doc.select("a[class=govuk-back-link]").text()
      backlinkText mustBe messages("site.back")
      val backlinkUrl = doc.select("a[class=govuk-back-link]").attr("href")
      backlinkUrl mustBe uk.gov.hmrc.valuationofficeagencycontactfrontend.controllers.routes.PropertyWindWaterController.onEnquiryLoad().url
    }
  }
}

