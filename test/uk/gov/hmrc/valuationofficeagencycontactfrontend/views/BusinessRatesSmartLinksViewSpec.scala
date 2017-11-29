/*
 * Copyright 2017 HM Revenue & Customs
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

import uk.gov.hmrc.valuationofficeagencycontactfrontend.models.NormalMode
import uk.gov.hmrc.valuationofficeagencycontactfrontend.views.behaviours.ViewBehaviours
import uk.gov.hmrc.valuationofficeagencycontactfrontend.views.html.businessRatesSmartLinks

class BusinessRatesSmartLinksViewSpec extends ViewBehaviours {

  def view = () => businessRatesSmartLinks(frontendAppConfig)(fakeRequest, messages)

  "Business Rates Smart Links View" must {

    behave like normalPage(view, "checkBeforeYouStartBusinessRates", "paragraph", "subheading", "para1",
      "para2", "para3", "para4", "para5", "para6", "para7", "para8", "para9", "para10", "para11",
      "url1", "url2", "url3", "url4", "url5")
  }

  "The Continue button links to the goToBusinessRatesSubcategoryPage method" in {
    val doc = asDocument(view())
    val href = doc.getElementById("continue").attr("href")
    assert(href == uk.gov.hmrc.valuationofficeagencycontactfrontend.controllers.routes.BusinessRatesSmartLinksController.goToBusinessRatesSubcategoryPage().url.toString)
  }

  "The Continue button uses the button--get-started class" in {
    val doc = asDocument(view())
    val button = doc.select("a[class~=button--get-started]")
    assert(button.size() == 1)
  }

  "has a link marked with site.back leading to the Business Rates Smart Links Page" in {
    val doc = asDocument(view())
    val backlinkText = doc.select("a[class=link-back]").text()
    backlinkText mustBe messages("site.back")
    val backlinkUrl = doc.select("a[class=link-back]").attr("href")
    backlinkUrl mustBe uk.gov.hmrc.valuationofficeagencycontactfrontend.controllers.routes.EnquiryCategoryController.onPageLoad(NormalMode).url
  }

}
