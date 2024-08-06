import scala.collection.parallel.CollectionConverters._
import scala.collection.parallel.immutable.ParSeq

object Main extends App {
  // TOKENS를 모방한 간단한 데이터 구조
  val TOKENS = Seq(
    Seq(
      (List("cat", "dog", "fish"), 1L, "Document 1"),
      (List("cat", "bird"), 1L, "Document 1")
    ),
    Seq(
      (List("dog", "fish", "bird"), 2L, "Document 2"),
      (List("cat", "dog"), 2L, "Document 2")
    )
  ).par // ParSeq로 변환

  // words 계산
  val words: ParSeq[(String, Int)] = TOKENS.flatMap { file =>
    file.flatMap(x => x._1.map(_ -> 1)).toMap
  }

  println("Words with counts:")
  words.seq.foreach(println) // 결과 출력을 위해 다시 순차적 컬렉션으로 변환

  // Document Frequency (df) 계산
  val df: ParSeq[(String, Int)] = words.groupBy(_._1).toSeq.map(x => (x._1, x._2.map(_._2).sum)).par

  println("\nDocument Frequencies:")
  df.seq.foreach(println) // 결과 출력
}