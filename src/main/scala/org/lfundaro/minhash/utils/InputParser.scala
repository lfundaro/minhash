package org.lfundaro.minhash.utils

/**
 * Helper object to read input.
 */
object InputParser {

  /**
   * Extracts userId and productId from input 
   * and gives it back in the form of (Int,Int).
   * 
   * @param str current line of input.
   */
  private def extract(str: String): (Int, Int) = {
    val dateP2 = new scala.util.matching.Regex("""(\d+);(\d+)""")
    val dateP2(userId, productId) = str
    (Integer.valueOf(userId), Integer.valueOf(productId))
  }

  /**
   * Opens the file and reads line by line. It returns 
   * a list of tuples (userId, productId).
   * 
   * @param path the path to the sample input.
   */
  //TODO you may want to make use of comprehension lists
  def readFromFile(path: String): List[(Int, Int)] = {
    val lines = io.Source.fromFile(path).getLines
    lines.toList map { x => extract(x) }
  }

  /**
   * Builds a default characteristic matrix with all elements
   * initialized to infinity.
   * 
   * @param pairs a list of tuples (userId,productId).
   */
  def buildCharacteristicMatrix(pairs: List[(Int, Int)],
    numHashFunc: Int): Map[Int, List[Int]] = {
    pairs.foldLeft(Map[Int, List[Int]]()) {
      case (z, (userId, _)) =>
        if (z.contains(userId)) z
        else z ++ (Map(userId -> List.fill(numHashFunc)(Integer.MAX_VALUE)))
    }
  }
  
  /**
   * Builds a map of user to products.
   * 
   * @param pairs a list of tuples (userId,productId).
   */
  def buildUserProductMap(pairs: List[(Int, Int)]): Map[Int, List[Int]] = {
    pairs.foldLeft(Map[Int,List[Int]]()) {
      case (z, (userId,prodId)) =>
        if (z.contains(userId)) {
          z ++ Map(userId -> (prodId :: z.get(userId).get))
        } else {
          z ++ Map(userId -> List(prodId))
        }
    }
  }

  /**
   * Builds a map of product to users.
   * 
   * @param pairs a list of tuples (userId,productId).
   */
  def buildProductUserMap(pairs: List[(Int, Int)]): Map[Int, List[Int]] = {
    pairs.foldLeft(Map[Int, List[Int]]()) {
      case (z, (userId, prodId)) =>
        if (z.contains(prodId)) {
          z ++ Map(prodId -> (userId :: z.get(prodId).get))
        } else {
          z ++ Map(prodId -> List(userId))
        }
    }
  }

}