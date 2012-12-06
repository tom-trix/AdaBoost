package ru.tomtrix.dm.spam

import scala.io.Source
import java.util.regex._
import ru.tomtrix.dm.boosting._

/**
 * @author tom-trix
 * EmailVector - input vector
 */
case class Tester(val items: Seq[Double]) extends X

/**
 * @author tom-trix
 * Contains methods to extract EmailVector from a message
 */
object Tester {
    /** split regex */
    val sr = "[\\. \n\r\t)(;,:\"–—?!\\-«»]"
    /** non-split regex */
    val nosr = "[^" + sr.substring(1)

    /**
     * @param s - input string
     * @param word - searching word
     * @return count of occurences in the input string
     */
    def getCount(s: String, word: String) = {
        var result = 0
        val m = Pattern.compile(word, Pattern.CASE_INSENSITIVE) matcher (s)
        while (m find) {
            println(m.group)
            result += 1
        }
        println(result)
        result
    }

    /**
     * @param email - input email message
     * @return input vector by the email text
     */
    def getVector(email: String): EmailVector = {
        //
        val words: Double = getCount(email, nosr + "+")
        //
        val hyperlinks = getCount(email, "\\.ru|\\.com|\\.net|\\.org|\\.ua|\\.de")
        val bigWords = getCount(email, sr + "\\p{javaUpperCase}+" + sr)
        val numbers = getCount(email, "\\d+")
        val singleNumbers = getCount(email, sr + "\\d+" + sr)
        val shortSms = getCount(email, sr + "\\d{4}" + sr)
        val exclamations = getCount(email, "!")
        val exclamationsGroup = getCount(email, "!{3,}")
        val money = getCount(email, "\\d\\s*руб|\\d\\s*грн|\\d\\s*$|\\d\\s*дол|\\d\\s*бакс|\\d\\s*%")
        val brackets = getCount(email, "\\){3,}")
        val mistakes = getCount(email, "[\\.,!\\?]" + nosr)
        val friends = getCount(email, "друз|друг")
        EmailVector(Seq(hyperlinks / words, bigWords / words, numbers / words, singleNumbers / words,
            shortSms / words, exclamations / words, exclamationsGroup / words, money / words,
            brackets / words, mistakes / words))
    }
    
    def main(args: Array[String]): Unit = {
        val s = " друзья други оамщыдруг тпмывщтдрузитщмы"
        getVector(s)
    }
}