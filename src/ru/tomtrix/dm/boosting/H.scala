package ru.tomtrix.dm.boosting

import scala.Math._

/**
 * Strong classificator obtained by Ada-Boost algorithm
 * @param trainingSet - training set to learn
 * @param n - number od iterations
 * @author tom-trix
 */
class H(trainingSet: Map[X, Int]) {
    //weights (initially = 1/n)
    private var D = trainingSet.toList map {t => 1.0 / trainingSet.size}
    //calculate a sequence of ("α"; "h(x)") pairs
    private val result = for {
        i <- 1 to 16 map (t => {
            //find the best weak classificator and its an ε-error
            val (ht, εt) = h find (trainingSet, D)
            if (εt < 0.5) {
                //find α(t)
                val αt = 0.5 * log((1 - εt) / εt)
                //recalculate weights (= D(i)*exp(-αyh(x)))
                D = D zip trainingSet.toList map (d => d._1 * exp(-αt * d._2._2 * ht(d._2._1)))
                //normalize them
                val n = D.sum
                D = D map {_ / n}
                //print
                println("%2d): %52s; ε=%.18f; α=%.18f" format (t, ht, εt, αt))
                Some(αt, ht)
            } else None
        })
    } yield i
    
    /**
     * @param x - input vector
     * @return classification result (+1 or -1)
     */
    def apply(x: X) = {
        val s = result.flatten.map { t => t._1 * t._2(x) }.sum
        println ("summa = " + s)
        s
    }
}