package ru.tomtrix.dm.boosting

/**
 * Abstract input vector
 * @author tom-trix
 */
trait X {
    /** Elements of vector*/
    val attributes: Seq[Any]
    /** Functions that transform each element into 0-1 double equivalent*/
    val f: Seq[Any => Double]
    /** Calculated 0-1 double equivalent of the vector*/
    val vector: Seq[Double]
}