/**
 *
 */
package ru.tomtrix.dm.spam

import ru.tomtrix.dm.boosting._
import ru.tomtrix.dm.spam.EmailVector._
import scala.io.Source

/**
 * @author tom-trix
 *
 */
case class Weak(val components: Set[Int], val tresholds: Seq[Double], val existsOrForall: Boolean) extends multiStump

object LetsGo {
    h.addClassificators(List(
        Weak(Set(0, 1, 5), Seq(1, 0.8, 0.7), true),
        Weak(Set(0, 1, 5), Seq(1, 0.8, 0.7), true)))
    val spam = Source.fromFile("spam.txt").getLines.foldLeft("") {(a, b) => a+b} split("##")
    val noSpam = Source.fromFile("noSpam.txt").getLines.foldLeft("") {(a, b) => a+b} split("##")
    val trainingSet: Map[X, Int] = (spam map {t => getVector(t) -> 1} toMap) ++ (noSpam map {t => getVector(t) -> -1} toMap)
    val w = new H(trainingSet)
}