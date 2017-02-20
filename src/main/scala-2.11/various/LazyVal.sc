
/////////////////////////////////
// VAL - evaluated immediately //
/////////////////////////////////
val a = {println("eval"); 1} // EVALUATING + returning 1
a // not printing anything, just returning 1

///////////////////////////////////////
// Lazy VAL - evaluated at 1st usage //
///////////////////////////////////////
lazy val b = {println("lazy eval"); 2} // not printing anything
b // EVALUATING + returning 2
b // returning 2
b // returning 2