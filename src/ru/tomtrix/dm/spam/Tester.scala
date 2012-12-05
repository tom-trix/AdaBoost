package ru.tomtrix.dm.spam

trait GG{
    val item: Int
}

case class gg(val item: Int) extends GG


object Tester {

    
    def main(args: Array[String]): Unit = {
        val lst: List[GG] = List(gg(5), gg(6))
        lst foreach (t => println(t.item))        
    }

}