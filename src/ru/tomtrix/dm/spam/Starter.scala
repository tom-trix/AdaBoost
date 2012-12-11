package ru.tomtrix.dm.spam

import scala.io.Source
import ru.tomtrix.dm.boosting._
import ru.tomtrix.dm.spam.EmailVector._

/**
 * Entry point
 */
object Starter {
    // add 1000 weak classificators
    h.addClassificators(Weak.classificators)
    println("there are " + Weak.classificators.size + " weak classificators found")

    // load spam messages
    val spam = Source.fromFile("spam.txt").getLines.foldLeft("") { (a, b) => a + b } split ("##")
    println("there are " + spam.size + " spam messages loaded")

    // load no-spam messages
    val noSpam = Source.fromFile("nospam.txt").getLines.foldLeft("") { (a, b) => a + b } split ("##")
    println("there are " + noSpam.size + " no-spam messages loaded")

    // obtain a training set
    val trainingSet: Map[X, Int] = (spam map { t => getVector(t) -> 1 } toMap) ++ (noSpam map { t => getVector(t) -> -1 } toMap)
    println("training set contains " + trainingSet.size + " samples")
    // AdaBoost!!!
    val adaBoost = new H(trainingSet)

    def main(args: Array[String]) = {
        /*println("input message:")
        while (true)
            println(adaBoost(getVector(readLine)));*/
        println(adaBoost(getVector(args(0))));
    }
}