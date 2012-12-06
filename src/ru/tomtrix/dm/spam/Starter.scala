package ru.tomtrix.dm.spam

import scala.io.Source
import ru.tomtrix.dm.boosting._
import ru.tomtrix.dm.spam.EmailVector._

/**
 * @author tom-trix
 * Weak classificator based on the MultiStump-function
 */
case class Weak(val components: Set[Int], val tresholds: Seq[Double], val existsOrForall: Boolean) extends multiStump

/**
 * Entry point
 */
object Starter {
    // add a bunch of weak classificators
    h.addClassificators(List(
        Weak(Set(0, 1, 5), Seq(1, 0.8, 0.7), true),
        Weak(Set(0, 1, 5), Seq(1, 0.8, 0.7), true)))
    // load spam messages
    val spam = Source.fromFile("spam.txt").getLines.foldLeft("") {(a, b) => a+b} split("##")
    // load no-spam messages
    val noSpam = Source.fromFile("noSpam.txt").getLines.foldLeft("") {(a, b) => a+b} split("##")
    // obtain a training set
    val trainingSet: Map[X, Int] = (spam map {t => getVector(t) -> 1} toMap) ++ (noSpam map {t => getVector(t) -> -1} toMap)
    // AdaBoost!!!
    val adaBoost = new H(trainingSet)
    
    def main(args: Array[String]): Unit = println(adaBoost(getVector(args(0))));
}