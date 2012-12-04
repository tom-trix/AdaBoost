package ru.tomtrix.dm.spam

import ru.tomtrix.dm.boosting._

/**
 * Ada-Boosting example
 * @author tom-trix
 */
object Starter {
    /** Input vector representing an e-mail message*/
    case class Email(recipients: Int, hyperlinks: Int, pictures: Int, shortSMS: Int, upperCase: Int, spamWords: Int) extends X {
        val attributes: Seq[Any] = Seq(recipients, hyperlinks, pictures, shortSMS, upperCase, spamWords)
        val f: Seq[Any => Double] = Seq(
            { t => val x = t.asInstanceOf[Int]; if (x > 4) 1 else x / 4 },
            { t => val x = t.asInstanceOf[Int]; if (x == 1) 0.8 else if (x > 1) 0.4 else 0.12 },
            { t => val x = t.asInstanceOf[Int]; if (x == 1) 0.6 else if (x > 1) 0.6 else 0.1 },
            { t => val x = t.asInstanceOf[Int]; if (x > 4) 1 else x / 4 },
            { t => val x = t.asInstanceOf[Int]; if (x > 4) 1 else x / 4 },
            { t => val x = t.asInstanceOf[Int]; if (x > 3) 1 else x / 3 })
        val vector = attributes zip f map {t => t._2(t._1)}
    }

    /** Entry point */
    def main(args: Array[String]): Unit =
        {
            //training set
            val trainingSet: Map[X, Int] = Map(
                Email(1, 2, 0, 4, 1, 5) -> 1,
                Email(12, 7, 1, 0, 1, 5) -> 1,
                Email(2, 0, 1, 0, 1, 0) -> -1,
                Email(2, 1, 5, 4, 6, 7) -> 1,
                Email(1, 7, 1, 5, 0, 3) -> 1,
                Email(2, 0, 1, 0, 1, 1) -> -1,
                Email(2, 2, 1, 0, 1, 1) -> -1,
                Email(1, 1, 1, 5, 0, 2) -> 1,
                Email(8, 1, 1, 7, 0, 5) -> 1,
                Email(1, 1, 6, 0, 1, 1) -> -1,
                Email(2, 1, 3, 1, 5, 5) -> 1,
                Email(2, 4, 0, 1, 0, 1) -> -1,
                Email(2, 1, 2, 1, 1, 4) -> -1,
                Email(1, 1, 0, 0, 6, 2) -> 1,
                Email(9, 7, 6, 1, 1, 2) -> 1,
                Email(2, 0, 1, 1, 0, 0) -> -1,
                Email(2, 1, 0, 1, 1, 1) -> -1,
                Email(1, 3, 1, 4, 5, 6) -> 1,
                Email(1, 0, 1, 1, 6, 0) -> -1,
                Email(2, 3, 1, 1, 0, 0) -> -1,
                Email(2, 0, 1, 0, 1, 1) -> -1,
                Email(2, 1, 1, 3, 0, 1) -> -1,
                Email(1, 6, 1, 1, 0, 7) -> 1,
                Email(11, 0, 0, 6, 1, 3) -> 1,
                Email(2, 5, 0, 0, 0, 4) -> 1,
                Email(1, 1, 3, 7, 0, 1) -> -1,
                Email(2, 2, 1, 0, 0, 0) -> -1,
                Email(1, 0, 4, 0, 0, 0) -> -1,
                Email(2, 6, 7, 2, 3, 7) -> 1,
                Email(2, 1, 0, 1, 4, 1) -> 1,
                Email(7, 3, 1, 6, 2, 1) -> 1,
                Email(2, 0, 0, 0, 1, 7) -> 1,
                Email(8, 6, 7, 8, 6, 7) -> 1,
                Email(1, 1, 4, 0, 0, 1) -> -1,
                Email(1, 0, 0, 0, 0, 0) -> -1,
                Email(1, 5, 0, 0, 0, 0) -> -1,
                Email(1, 1, 1, 0, 1, 0) -> -1,
                Email(1, 0, 1, 4, 1, 1) -> 1,
                Email(2, 0, 5, 5, 6, 3) -> 1,
                Email(8, 2, 6, 4, 3, 5) -> 1,
                Email(2, 0, 0, 0, 2, 0) -> -1,
                Email(2, 7, 7, 0, 0, 0) -> 1,
                Email(1, 3, 2, 3, 1, 1) -> 1,
                Email(2, 2, 1, 6, 0, 6) -> 1,
                Email(1, 1, 0, 1, 1, 7) -> 1,
                Email(1, 0, 2, 2, 0, 5) -> 1,
                Email(1, 4, 1, 0, 1, 1) -> -1,
                Email(1, 1, 1, 0, 1, 5) -> -1,
                Email(2, 0, 0, 1, 3, 0) -> -1,
                Email(1, 1, 0, 0, 1, 4) -> -1,
                Email(1, 1, 1, 0, 0, 6) -> 1)

            //add a batch of weak classificators
            h.addClassificators(List(
                new h { val k = 0; val Θ = 0.6 },
                new h { val k = 0; val Θ = 0.4 },
                new h { val k = 1; val Θ = 0.6 },
                new h { val k = 1; val Θ = 0.4 },
                new h { val k = 2; val Θ = 0.6 },
                new h { val k = 2; val Θ = 0.4 },
                new h { val k = 3; val Θ = 0.6 },
                new h { val k = 3; val Θ = 0.4 },
                new h { val k = 4; val Θ = 0.6 },
                new h { val k = 4; val Θ = 0.4 },
                new h { val k = 5; val Θ = 0.6 },
                new h { val k = 5; val Θ = 0.4 }))

            //get the strong classificator
            val w = new H(trainingSet)

            //print the results
            println("\n======= T E S T =======")
            println(w(Email(2, 7, 2, 0, 1, 2)))
            println(w(Email(1, 1, 3, 6, 1, 6)))
            println(w(Email(1, 3, 4, 0, 1, 0)))
            println(w(Email(4, 1, 3, 6, 1, 3)))
        }
}