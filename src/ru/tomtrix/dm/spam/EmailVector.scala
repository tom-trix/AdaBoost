package ru.tomtrix.dm.spam

import java.util.regex._
import ru.tomtrix.dm.boosting._

/**
 * @author tom-trix
 *
 */

case class EmailVector(val items: Seq[Double]) extends X

object EmailVector {
    val sr = "[\\. \n\r\t)(;,:\"–—?!\\-«»]"
    val nosr = "[^" + sr.substring(1)

    def getCount(s: String, word: String) = {
        var result = 0
        val m = Pattern.compile(word, Pattern.CASE_INSENSITIVE) matcher (s)
        while (m find) result += 1
        result
    }

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
        EmailVector(Seq(hyperlinks/words, bigWords/words, numbers/words, singleNumbers/words, 
                shortSms/words, exclamations/words, exclamationsGroup/words, money/words,
                brackets/words, mistakes/words))
    }
}