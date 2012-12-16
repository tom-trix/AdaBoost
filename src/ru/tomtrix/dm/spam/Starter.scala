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
    
    def getQuality = {
        val spamResult = spam.map { t => Strong(getVector(t)) } toList
        val noSpamResult = noSpam.map { t => Strong(getVector(t)) } toList

        val goodSpam = 100.0 * spamResult.filter { _ > 0 }.size / spamResult.size
        val excellentSpam = 100.0 * spamResult.filter { _ > 0.5 }.size / spamResult.size
        val badSpam = 100.0 * spamResult.filter { _ <= 0 }.size / spamResult.size

        val goodNospam = 100.0 * noSpamResult.filter { _ < 0 }.size / noSpamResult.size
        val excellentNospam = 100.0 * noSpamResult.filter { _ < 0.5 }.size / noSpamResult.size
        val badNospam = 100.0 * noSpamResult.filter { _ >= 0 }.size / noSpamResult.size

        "  Spam analysis: good - %4.1f%% (excellent - %4.1f%%),  I-type error - %4.1f%%\nNoSpam analysis: good - %4.1f%% (excellent - %4.1f%%), II-type error - %4.1f%%" format (goodSpam, excellentSpam, badSpam, goodNospam, excellentNospam, badNospam)
    }

    /**
     * Entry point
     * @param args
     */
    def main(args: Array[String]) = {
        System.setProperty("java.net.preferIPv4Stack", "true")
        var channel = new JChannel("UDP(bind_addr=127.0.0.1)")
        channel setReceiver(new ReceiverAdapter {
            override def receive(m: Message) = if (channel.getAddress != m.getSrc) {
                var s = m.getObject.toString
                println("  >> received message: " + s)
                val d = if (s.toLowerCase == "getquality") getQuality else Strong(getVector(s))
                channel send(new Message(null, null, d))
            }
        })
        channel connect("AdaBoost")
        println("Press Enter to quit the AdaBoost server...")
        readLine
        channel close
    }
}