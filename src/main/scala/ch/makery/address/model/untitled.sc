import scalafx.beans.property.{IntegerProperty, StringProperty}
import scalafx.collections.ObservableBuffer
import scalikejdbc._

class Testing (a: Int, b: String) {
  val aS = IntegerProperty(a)
  val bS = new StringProperty(b)
}
val u = new ObservableBuffer[Testing]

u += new Testing ( 1, "HI")
u += new Testing ( 2, "HI")



println("re")

val abc = u.result()
print(abc)


for (b <- u)
println(b)
val firstName = StringProperty("name")
val lastName = StringProperty("name")
sql"""
				delete from student where
					firstName = ${firstName.value} and lastName = ${lastName.value}
				""".update.apply()