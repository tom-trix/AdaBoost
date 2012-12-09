package ru.tomtrix.dm.boosting

/**
 * Weak classificator that uses simple stump-function: IF (X(k) > Θ) => 1 ELSE -1
 * @author tom-trix
 */
trait h {
    /** Which component of vector to check */
    val k: Int
    /** Treshold (this is Tetta! Not Zero!) */
    val Θ: Double
    /**
     * @param t - input vector
     * @return classification result (+1 or -1)
     */
    def apply(t: X) = if (t.items(k) > Θ) 1 else -1
}

/**
 * Weak classificator that uses modifyed stump-function: IF "n" components > Θ => 1 ELSE -1
 * @author tom-trix
 */
trait multiStump extends h {
    override val k = 0;
    override val Θ = 0d;
    /** Which components of vector to check */
    val components: Set[Int]
    /** List of corresponding tresholds */
    val tresholds: Seq[Double]
    /** Function to be applied (exists or forall) */
    val m: Int
    override def apply(t: X) = {
        val lst = for {
            c <- components toList
        } yield t.items(c)
        if (lst.zip(tresholds).toList.filter(p => p._1 > p._2).size >= m) 1 else -1
    }
}

/**
 * Object to deal with weak classificators
 * @author tom-trix
 */
object h {
    /** List of weak classificators */
    private var allClassificators: List[h] = List()

    /**
     * Adds new weak classificator
     * @param t
     */
    def addClassificators(t: List[h]) = allClassificators = t ::: allClassificators

    /**
     * @param w - weak classificator
     * @param trainingSet - training set to learn
     * @param D - current weights
     * @return ε-error (= summa of such D(i) where y(i) != h(x(i)))
     */
    private def ε(w: h, trainingSet: Map[X, Int], D: Seq[Double]) =
        trainingSet.zip(D).map { p => if (p._1._2 != w(p._1._1)) p._2 else 0 }.toList sum;

    /**
     * @param trainingSet - training set to learn
     * @param D - current weights
     * @return (ε, h)-pair where "h" is the most appropriate weak classificator that corresponds to the current weights; "ε" is the error
     */
    def find(trainingSet: Map[X, Int], D: Seq[Double]) = {
        val min_h = h.allClassificators.minBy(t => ε(t, trainingSet, D))
        (min_h, ε(min_h, trainingSet, D))
    }
}