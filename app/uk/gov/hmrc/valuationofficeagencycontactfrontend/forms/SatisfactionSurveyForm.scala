/*
 * Copyright 2019 HM Revenue & Customs
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

package uk.gov.hmrc.valuationofficeagencycontactfrontend.forms

import play.api.Logger
import play.api.data.Form
import play.api.data.Forms.{mapping, nonEmptyText}
import play.api.data.format.Formatter
import uk.gov.hmrc.valuationofficeagencycontactfrontend.utils.RadioOption

object SatisfactionSurveyForm extends FormErrorHelper {

  private val antiXSSMessageRegex = """^['`A-Za-z0-9\s\-&,\.£\(\)%;:\?\!]+$"""

  def apply(): Form[SatisfactionSurvey] = Form(
    mapping(
      "satisfaction" -> nonEmptyText,
//        .verifying("error.required", _.length <= 1200),
      "details" -> nonEmptyText
        .verifying("error.message.max_length", _.length <= 1200)
        .verifying("error.message.xss-invalid", _.matches(antiXSSMessageRegex))
    )(SatisfactionSurvey.apply)(SatisfactionSurvey.unapply)//.verifying(
//      "Failed for Constraints!",
//      fields =>
//        fields match {
//          case userData => {
//            Logger.warn(s"****** Satisfaction: ${userData.satisfaction} : Details: ${userData.details} .... ******")
//            false
//          }
//        }
//    )
  )

  def options = Seq(
    RadioOption("satisfaction", "verySatisfied"),
    RadioOption("satisfaction", "satisfied"),
    RadioOption("satisfaction", "neither"),
    RadioOption("satisfaction", "dissatisfied"),
    RadioOption("satisfaction", "veryDissatisfied")
  )

  def optionIsValid(value: String) = {
    Logger.warn(s"****** ${value} : .... ******")
    options.exists(o => o.value == value)
  }
}

case class SatisfactionSurvey (satisfaction: String, details: String)
