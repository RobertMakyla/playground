package various

object ForComprehensionOnLists extends App {

  val myMap = Map(1 -> "a", 2 -> "b", 3 -> "c")
  val ints = List(1, 2, 3)
  val chars = Seq("a", "b")

  ////////////////////////////////////
  // Dealing with operations on MAP //
  ////////////////////////////////////

  println {
    // alternative to ugly .map(e => e._1 * 1000 -> e._2.toUpperCase )
    myMap.map {
      case (k, v) => k * 1000 -> v.toUpperCase
    }
  }

  //////////////////////////////////
  // Desugaring For-Comprehension //
  //                              //
  // 1 iterator = 1 .map()       //
  //                              //
  //////////////////////////////////

  println {
    // the same as: ints.map(_ * 1000)
    for {
      i <- ints
    } yield i * 1000
  }

  //////////////////////////////////
  // Desugaring For-Comprehension //
  //                              //
  // 2 iterators = .flatMap()     //
  //               .map()         //
  //////////////////////////////////

  println {
    for {
      i <- ints // each iterator above the last one, gets flatMap[E, I](e:E => Iterable[I])
      c <- chars // ------ the last iterator - get a map assigned
    } yield i -> c // ---- and that's the mapping function
  }

  println {
    // de-sugared - step 1
    ints.flatMap {
      i => for (c <- chars) yield i -> c
    }
  }

  println {
    // de-sugared - step 2
    ints.flatMap {
      i => chars.map(c => i -> c)
    }
  }

  println {
    // de-sugared - more meaningful
    def assignIntToAllChars(i: Int): Seq[_] = chars.map(c => i -> c)
    ints.flatMap(i => assignIntToAllChars(i))
  }

  //////////////////////////////////
  // With filter                  //
  //////////////////////////////////

  println {
    for {
      i <- ints if i < 3 // .withFilter()
      c <- chars
    } yield i -> c
  }

  println {
    // de-sugared
    def assignIntToAllChars(i: Int): Seq[_] = chars.map(c => i -> c)
    ints.withFilter(_ < 3).flatMap(i => assignIntToAllChars(i))
  }

  /**
    * filter() - will take the original collection and produce a new collection
    */

  /**
    * withFilter() - will non-strictly (ie. lazily) pass unfiltered values through to later map/flatMap calls,
    * saving a second pass through the (filtered) collection, hence it's more efficient.
    *
    * withFilter() -  is specifically designed for working with chains of these methods (map/flatMap),
    * which is what a for comprehension is de-sugared into.
    */
}
