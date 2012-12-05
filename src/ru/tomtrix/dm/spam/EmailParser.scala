package ru.tomtrix.dm.spam
import java.util.regex._
import ru.tomtrix.dm.spam.Analyzer._

/**
 * @author tom-trix
 *
 */
object EmailParser {
    def getCount(s: String, word: String) = {
        var result = 0
        val p = Pattern.compile(word, Pattern.CASE_INSENSITIVE)
        val m = p matcher (s)
        while (m find) {
            result += 1
            println(m.group)
        }
        result
    }

    def parse(email: String) =
        {
            val sr = splitRegex
            val nosr = "[^" + sr.substring(1)
            println("words = " + getCount(email, nosr + "+"));
            println("hyperlinks = " + getCount(email, "\\.ru|\\.com|\\.net|\\.org|\\.ua|\\.de"));
            println("bigWords = " + getCount(email, sr + "\\p{javaUpperCase}+" + sr));
            println("numbers = " + getCount(email, "\\d+"));
            println("single numbers = " + getCount(email, sr + "\\d+" + sr));
            println("short sms = " + getCount(email, sr + "\\d{4}" + sr));
            println("exclamations = " + getCount(email, "!"));
            println("exclamations group = " + getCount(email, "!{3,}"));
            println("money = " + getCount(email, "\\d\\s*руб|\\d\\s*грн|\\d\\s*$|\\d\\s*дол|\\d\\s*бакс|\\d\\s*%"));
            println("brackets = " + getCount(email, "\\){3,}"));
            println("mistakes = " + getCount(email, "[\\.,!\\?]" + nosr));
        }

    def main(args: Array[String]): Unit = {
        parse("""foseifenji.rujasHUIFEBSIjfnoa.comfh55sif!hs.rujso.comfhsi.com HIHI fseuGVUbu UYGBUY hyu
                esiРИШГШ овсфorgышщ РШГРИШ!!! ошщ  7897 fhsui 4328 yfi!!!!!1 79379 fsi67 fseu7y8== 
                fsehi 3798 77    !!!!!!4!!!!   fneon)))    ))))
                fe50руб. ыкп 34 рубля оыа 56$ jioj рубтвшруб 
                ошмд))оллар 56долларов )) мшв 4  % 57  рублей 
                fjri.nfis.ощш,аоыш!ршкш.HBIu""")
    }

}