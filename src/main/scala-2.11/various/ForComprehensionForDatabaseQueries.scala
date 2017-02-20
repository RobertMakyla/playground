package various

case class Book(title: String, authors: List[String])


object ForComprehensionForDatabaseQueries extends App {

  val books = List(
    Book("Basnie 100 i jednej nocy", List("Verne", "Mickiewicz")),
    Book("101 dalmatenczykow", List("Dalmatenczyk")),
    Book("21k mil podmorskiej zeglugi", List("Slowacki", "Verne"))
  )

  print("Find the titles which contain '10': ")
  println {
    for {
      book <- books if book.title.contains("10")
    } yield book.title
  }

  print("Find the titles of books whose author is Verne: ")
  println {
    for {
      book <- books
      a <- book.authors if a == "Verne"
    } yield book.title
  }

  print("Find the authors which have written at least 2 books in the DataBase")
  println {
    for {
      b1 <- books
      b2 <- books
      if b1.title != b2.title
      a1 <- b1.authors
      a2 <- b2.authors
      if a1 == a2
    } yield a1
  }

  print("(To Make Verne appear just once, use .distinct on the result or change books to Set() just like in Database - no duplications and order's not important)")
}
