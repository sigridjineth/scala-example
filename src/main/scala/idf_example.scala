import scala.collection.parallel.CollectionConverters._

object IdfCalculationExample {
  def main(args: Array[String]): Unit = {
    // 샘플 문서 데이터
    val documents = Seq(
      "The quick brown fox jumps over the lazy dog",
      "Quick brown foxes leap over lazy dogs in summer",
      "The lazy dog sleeps in the sun",
      "The quick brown fox jumps over the lazy cat"
    )

    // 문서를 토큰화하고 중복 제거
    val tokenizedDocs = documents.map(_.toLowerCase.split("\\s+").toSet)

    // 전체 고유 단어 목록
    val allWords = tokenizedDocs.flatten.toSet

    // 문서 빈도(df) 계산
    val df = allWords.map { word =>
      val docFreq = tokenizedDocs.count(_.contains(word))
      (word, docFreq)
    }.toSeq.par  // ParSeq로 변환

    // 문서의 총 수
    val DOCUMENT_SENTENCE_LENGTH = documents.length.toLong

    // idf 계산
    val idf = df.map { x =>
      val word = x._1
      val num = x._2.toDouble
      val w = Math.log((DOCUMENT_SENTENCE_LENGTH - num + 0.5) / (num + 0.5))
      (word, w)
    }

    // 결과 출력
    println("IDF 값:")
    idf.toList.sortBy(-_._2).foreach { case (word, idfValue) =>
      println(f"$word%-15s IDF: $idfValue%.4f")
    }
  }
}