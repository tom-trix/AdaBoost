package ru.tomtrix.dm.spam
import scala.io.Source
import scala.collection.mutable._
import org.tartarus.snowball.ext.russianStemmer

/**
 * @author tom-trix
 *
 */
object Analyzer {
    val splitRegex = "[\\. \n\r\t)(;,:\"–—?!\\-«»]"
    val stemmer = new russianStemmer
    val stopWords = Set("а", "без", "более", "бы", "был", "быть", "в", "вам", "весь", "во", "вот", "все", "всё", "всего", "вы", "где", "да", "даже", "для", "до", "его", "ее", "если", "есть", "еще", "же", "за", "здесь", "и", "из", "или", "им", "их", "к", "как", "ко", "когда", "кто", "ли", "либо", "мне", "может", "мы", "на", "надо", "наш", "не", "него", "нее", "нет", "ни", "них", "но", "ну", "о", "об", "однако", "он", "от", "очень", "по", "под", "при", "с", "со", "так", "также", "такой", "там", "те", "тем", "то", "того", "тоже", "той", "только", "том", "ты", "у", "уже", "хотя", "чего", "чей", "чем", "что", "чтобы", "чье", "я", "это", "тебя", "ваш", "который", "будет", "свой", "меня", "мной", "сам", "твой", "себя", "вас") map {stem(_)}
    def stem(word: String) = {
        stemmer setCurrent (word)
        stemmer.stem
        stemmer.getCurrent
    }

    def main(args: Array[String]): Unit = {
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
        } yield stem(word.toLowerCase)) filter {t => !t.isEmpty() && !stopWords.contains(t)}
        
        //find count
        val counts: Map[String, Int] = Map()
        words.foreach(t => {
            if (counts.contains(t)) counts(t)+=1
            else counts += t -> 1
        })
        val sorted = counts.toList sortBy(_._2)
        
        sorted foreach println
    }
}