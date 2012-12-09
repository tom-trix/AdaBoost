package ru.tomtrix.dm.spam

import scala.io.Source
import ru.tomtrix.dm.boosting._
import ru.tomtrix.dm.spam.EmailVector._
import org.tartarus.snowball.ext.russianStemmer
import scala.collection.mutable.Buffer
import scala.util.Random
import scala.math.BigDecimal

case class W(val components: Set[Int], val tresholds: Seq[Double], val m: Int) extends multiStump

/**
 * @author tom-trix
 *
 */
object Analyzer {
    val splitRegex = "[\\. \n\r\t)(;,:\"–—?!\\-«»]"
    val stemmer = new russianStemmer
    val stopWords = Set("а", "без", "более", "бы", "был", "быть", "в", "вам", "весь", "во", "вот", "все", "всё", "всего", "вы", "где", "да", "даже", "для", "до", "его", "ее", "если", "есть", "еще", "же", "за", "здесь", "и", "из", "или", "им", "их", "к", "как", "ко", "когда", "кто", "ли", "либо", "мне", "может", "мы", "на", "надо", "наш", "не", "него", "нее", "нет", "ни", "них", "но", "ну", "о", "об", "однако", "он", "от", "очень", "по", "под", "при", "с", "со", "так", "также", "такой", "там", "те", "тем", "то", "того", "тоже", "той", "только", "том", "ты", "у", "уже", "хотя", "чего", "чей", "чем", "что", "чтобы", "чье", "я", "это", "тебя", "ваш", "который", "будет", "свой", "меня", "мной", "сам", "твой", "себя", "вас") map { stem(_) }
    val rnd = Random
    def stem(word: String) = {
        stemmer setCurrent (word)
        stemmer.stem
        stemmer.getCurrent
    }
    
    def rand = {
        val i = (rnd.nextDouble/50 * 10000).toInt
        i.toDouble / 10000
    }

    def analize = {
        //read the file
        val lines = Source fromFile ("spam.txt") getLines

        //get messages
        var msgs = Buffer("")
        var i = 0
        for (s <- lines) {
            if (s == "##") { msgs + ""; i += 1 }
            else msgs(i) += "\n" + s
        }

        //get words
        val words = (for {
            msg <- msgs
            word <- msg.split(splitRegex)
        } yield stem(word.toLowerCase)) filter { t => !t.isEmpty() && !stopWords.contains(t) }

        //find count
        val counts: scala.collection.mutable.Map[String, Int] = scala.collection.mutable.Map()
        words.foreach(t => {
            if (counts.contains(t)) counts(t) += 1
            else counts += t -> 1
        })
        val sorted = counts.toList sortBy (_._2)

        sorted foreach println
    }

    def loadLetters = {
        val spam = Source.fromFile("spam.txt").getLines.foldLeft("") { (a, b) => a + " " + b } split ("##")
        spam.foreach(t => println(getVector(t)))
        println(spam.size + " писем загружено")
        println(getVector(spam(0)).items.size + "-мерный вектор")
        val noSpam = Source.fromFile("nospam.txt").getLines.foldLeft("") { (a, b) => a + b } split ("##")
        noSpam.foreach(t => println(getVector(t)))
        println(noSpam.size + " писем загружено")
        println(getVector(noSpam(0)).items.size + "-мерный вектор")
    }

    def weakClassificators = {
        val spam = Source.fromFile("spam.txt").getLines.foldLeft("") { (a, b) => a + b } split ("##")
        val trainingSet: Map[X, Int] = (spam map { t => getVector(t) -> 1 } toMap)
        val clsificators: Buffer[W] = Buffer()
        val tetta = 0.02
        for (i <- 0 to 39) {
            clsificators += W(Set(i), Seq(tetta + rand), 1)
            for (j <- i + 1 to 39) {
                clsificators += W(Set(i, j), Seq(tetta + rand, tetta + rand), 1)
                clsificators += W(Set(i, j), Seq(tetta + rand, tetta + rand), 2)
                for (k <- j + 1 to 39) {
                    clsificators += W(Set(i, j, k), Seq(tetta + rand, tetta + rand, tetta + rand), 1)
                    clsificators += W(Set(i, j, k), Seq(tetta + rand, tetta + rand, tetta + rand), 2)
                    clsificators += W(Set(i, j, k), Seq(tetta + rand, tetta + rand, tetta + rand), 3)
                }
            }
        }
        val result = clsificators map { x => x -> trainingSet.filter(t => x(t._1) == t._2).size }
        result.toList.sortBy(_._2) foreach println
    }

    def main(args: Array[String]): Unit = {
        //analize
        loadLetters
        //weakClassificators
    }
}