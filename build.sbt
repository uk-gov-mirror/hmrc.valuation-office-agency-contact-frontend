import Dependencies.{Test, _}
import sbt.Keys.parallelExecution

lazy val appName = "valuation-office-agency-contact-frontend"

import sbt.Keys._
import sbt.Tests.{Group, SubProcess}
import sbt._
import scoverage.ScoverageKeys
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import com.typesafe.sbt.web.Import._
import net.ground5hark.sbt.concat.Import._
import com.typesafe.sbt.uglify.Import._
import com.typesafe.sbt.digest.Import._
import play.sbt.routes.RoutesKeys
import uk.gov.hmrc._
import DefaultBuildSettings._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning
import uk.gov.hmrc.SbtArtifactory

lazy val plugins : Seq[Plugins] = Seq.empty
lazy val playSettings : Seq[Setting[_]] = Seq.empty

lazy val root = Project(appName, file("."))
  .enablePlugins(Seq(play.sbt.PlayScala, SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory) ++ plugins : _*)
  .disablePlugins(JUnitXmlReportPlugin)
  .settings(majorVersion := 1)
  .settings(playSettings : _*)
  .settings(RoutesKeys.routesImport ++= Seq("uk.gov.hmrc.valuationofficeagencycontactfrontend.models._"))
  .settings(
    ScoverageKeys.coverageExcludedFiles := "<empty>;Reverse.*;.*filters.*;.*handlers.*;.*components.*;.*models.*;" +
      ".*BuildInfo.*;.*javascript.*;.*FrontendAuditConnector.*;.*Routes.*;.*GuiceInjector;.*DataCacheConnector;" +
      ".*ControllerConfiguration;.*LanguageSwitchController;.*MongoCleanupActor",
    ScoverageKeys.coverageMinimum := 90,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    PlayKeys.playDefaultPort := 7311
  )
  .settings(defaultSettings())
  .settings(publishingSettings: _*)
  .settings(
    scalacOptions ++= Seq("-feature"),
    libraryDependencies ++= appDependencies,
    dependencyOverrides ++= appDependencyOverrides,
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false),
    Keys.fork in sbt.Test := true
  )
  .configs(IntegrationTest)
  .settings(integrationTestSettings())
  .settings(resolvers ++= Seq(
    Resolver.bintrayRepo("hmrc", "releases"),
    Resolver.jcenterRepo,
    Resolver.bintrayRepo("emueller", "maven")
  ))
  .settings(
    // concatenate js
    Concat.groups := Seq(
      "javascripts/valuationofficeagencycontactfrontend-app.js" -> group(Seq("javascripts/show-hide-content.js", "javascripts/valuationofficeagencycontactfrontend.js"))
    ),
    // prevent removal of unused code which generates warning errors due to use of third-party libs
    uglifyCompressOptions := Seq("unused=false", "dead_code=false"),
    pipelineStages := Seq(digest),
    // below line required to force asset pipeline to operate in dev rather than only prod
    pipelineStages in Assets := Seq(concat,uglify),
    // only compress files generated by concat
    includeFilter in uglify := GlobFilter("valuationofficeagencycontactfrontend-*.js")
  )
