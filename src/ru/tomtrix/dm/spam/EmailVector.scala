package ru.tomtrix.dm.spam

import java.util.regex._
import ru.tomtrix.dm.boosting._

/**
 * @author tom-trix
 * EmailVector - input vector
 */
case class EmailVector(val items: Seq[Double]) extends X

/**
 * @author tom-trix
 * Contains methods to extract EmailVector from a message
 */
object EmailVector {
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
        while (m find) result += 1
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
        val mail1 = getCount(email, "mail|сообще")
        val mail2 = getCount(email, "письм|спам")
        val href = getCount(email, "ссылк|сайт")
        val people = getCount(email, "люд|человек")
        val send = getCount(email, "отправ|получ")
        val hello = getCount(email, "привет|пожалуй")
        val break = getCount(email, "взлом|парол")
        val networks = getCount(email, "контакт|однокласс")
        val free = getCount(email, "деньг|бесплатн")
        val lots = getCount(email, "больш|мног")
        val rating = getCount(email, "голос|рейтинг")
        val admin = getCount(email, "администр|автомат")
        val request = getCount(email, "слуш|просьб")
        val photo = getCount(email, "смотр|фотк")
        val sex = getCount(email, "знаком|секс")
        val all = getCount(email, "все|люб")
        val way = getCount(email, "наш|способ")
        val bill = getCount(email, "заработ|счет|счёт")
        val needhelp = getCount(email, "нужн|помо")
        val triple0 = getCount(email, "год|прош|игр")
        val triple1 = getCount(email, "раз|програм|течен")
        val triple2 = getCount(email, "можн|номер|страниц")
        val triple3 = getCount(email, "прост|списк|перешл")
        val triple4 = getCount(email, "спасиб|переда|чита")
        val triple5 = getCount(email, "телеф|комп|узна")
        val triple6 = getCount(email, "интерн|любов|онлайн")
        val triple7 = getCount(email, "прода|заран|разошл")
        val triple8 = getCount(email, "вирус|почт|знаеш")
        val triple9 = getCount(email, "действит|кача|дает")
        EmailVector(Seq(hyperlinks / words, bigWords / words, numbers / words, singleNumbers / words,
            shortSms / words, exclamations / words, exclamationsGroup / words, money / words,
            brackets / words, mistakes / words, friends / words, mail1 / words, mail2 / words, 
            href / words, people / words, send / words, hello / words, break / words, 
            networks / words, free / words, lots / words, rating / words, admin / words, 
            request / words, photo / words, sex / words, all / words, way / words, bill / words, 
            needhelp / words, triple0 / words, triple1 / words, triple2 / words, triple3 / words, 
            triple4 / words, triple5 / words, triple6 / words, triple7 / words, triple8 / words, 
            triple9 / words))
    }
}