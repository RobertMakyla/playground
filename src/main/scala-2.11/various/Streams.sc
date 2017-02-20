
println("Stream is immutable List in which the tail is computed lazily.")

val myTmpStream = 1 #:: 2 #:: 3 #:: 40 #:: 500 #:: Stream.empty

def createStream(n: BigInt): Stream[BigInt] = if(n < 0) Stream.empty else n #:: createStream(n - 1)

val myStream = createStream(10)
val myNewStream = createStream(10)

myStream               // Stream(10, ?) - only the 1st elem computed
myStream.take(4)       // Stream(10, ?) - still nothing calculated
myStream.take(4).force // Stream(10, 9, 8, 7)

myStream               // Stream(10, 9, 8, 7, ?) - only first 4 are computed
myStream force         // Stream(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)

myNewStream tail        // Stream(9, ?)  tail not computed yet

myNewStream.tail.force  // Stream(9, 8, 7, 6, 5, 4, 3, 2, 1, 0)  computing the tail
myNewStream.tail.toList // another way of computing

myNewStream             // Stream(10, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0)  now it's all computed

Stream(1,2,3)                      // Stream(1, ?)
Stream(1,2,3).filter(_ > 2)        // Stream(1, ?)
Stream(1,2,3).filter(_ > 2).force  // Stream(3)
Stream(1,2,3).filter(_ > 2).toList //   List(3)


/////////////////////////////
//                         //
//    Infinite Sequence    //
//                         //
/////////////////////////////

def from(n: Int): Stream[Int] = n #:: from(n + 1)
val naturals = from(0)             //Stream(0, ?)
val odds = naturals.filter(_%2!=0) //Stream(1, ?)
(odds take 5) force                //Stream(1, 3, 5, 7, 9)

/**
  * Bierzemy pierwszy element (najlepiej od 2) i w pozostałości skreślamy wszystkie podzielne przez 2
  * potem bierzemy kolejny nieskreślony element (3) i skreślamy w pozostałości wszystkie podzielne przez 3
  * itd... i tak dostajemy liczby pierwsze
  *
  * Jakbyśmy zaczynali od 1 to wszystkie byłyby skreślone na początku
  */
def getPrimesFrom( s: Stream[Int]): Stream[Int] =
s.head #:: getPrimesFrom(s.tail.filter(_ % s.head!=0))

(getPrimesFrom(from(2)) take 5) toList // List(2, 3, 5, 7, 11)
