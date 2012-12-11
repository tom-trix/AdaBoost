package ru.tomtrix.dm.spam

import scala.io.Source
import ru.tomtrix.dm.boosting._
import ru.tomtrix.dm.spam.EmailVector._
import org.jgroups.JChannel
import org.jgroups.ReceiverAdapter
import org.jgroups.Message

/** @author tom-trix
 * This is AdaBoost Server (that uses JGroups to interconnect)
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
    val Strong = new H(trainingSet)

    /**
     * Entry point
     * @param args
     */
    def main(args: Array[String]) = {
        System.setProperty("java.net.preferIPv4Stack", "true")
        var channel = new JChannel("UDP(bind_addr=127.0.0.1)")
        channel setReceiver(new ReceiverAdapter {
            override def receive(m: Message) = if (channel.getAddress != m.getSrc) {
                println("  >> received message: " + m.getObject)
                val d = Strong(getVector(m.getObject toString))
                channel send(new Message(null, null, d))
            }
        })
        channel connect("AdaBoost")
        println("Press Enter to quit the AdaBoost server...")
        readLine
        channel close
    }
}