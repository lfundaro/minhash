package org.lfundaro.minhash

import org.lfundaro.minhash.utils.Hash

/**
 * Encapsulates the behavior of the MinHash Algorithm.
 *
 * @constructor creates a new MinHash. Note that once you call
 * method suggestProducts you cannot reuse this object.
 * @param elements map of product to list of users owning it.
 * @param sigMatrix initialized signature matrix with every value
 * at infinity.
 * @param hashSet the set of hashing functions used by the MinHash
 * algorithm.
 */
class MinHash(elements: Map[Int, List[Int]],
  var sigMatrix: scala.collection.mutable.Map[Int, List[Int]],
  hashSet: Hash) {

  /**
   * This is the MinHash algorithm per se.
   *
   * Generates a final signature matrix with different
   * hash columns per user that are later compared to
   * obtain the Jaccard Similarity of two users.
   */
  private def runMinHash(): Map[Int, List[Int]] = {
    elements foreach {
      case (key, _) =>
        val userSet = elements.get(key).get
        userSet foreach { user =>
          val userSignatures = sigMatrix.get(user).get
          sigMatrix(user) = minSignatureColumn(userSignatures, key)
        }
    }
    sigMatrix.toMap
  }

  /**
   * Compares two signature columns and generates a new
   * one with the minimum values between the two.
   *
   * @param currentSignatures current signatures from a given user.
   * @param key productId
   */
  private def minSignatureColumn(currentSignatures: List[Int], key: Int): List[Int] = {
    currentSignatures zip sigColumn(key) map ({ case (x, y) => x min y })
  }

  /**
   * Hashes a signature column.
   *
   * It applies the set of hash functions to every
   * element in the signature column.
   *
   * @param product element to be hashed.
   */
  private def sigColumn(product: Int): List[Int] = {
    for (fun <- hashSet.hashList) yield fun(product)
  }

  /**
   * Given a userId and a map of userId to products (he/she has bought)
   * returns 5 product suggestions from the user with highest similarity
   * index to the least.
   *
   * @param userId user identification
   * @param userProduct map from user to a list of products he/she has bought.
   */
  def suggestProducts(userId: Int, userProduct: Map[Int, List[Int]]): List[Int] = {
    val reduxMatrix = runMinHash()
    val userColumn = reduxMatrix.get(userId).get
    val reduxMatrixMod = reduxMatrix - userId
    val candidates = reduxMatrixMod.foldLeft(Map[Int, Double]()) {
      case (z, (userId, signatures)) =>
        val k = for ((s1, s2) <- signatures zip userColumn; if (s1 == s2)) yield (s1, s2)
        if (z.contains(userId)) {
          z ++ Map(userId -> (z.get(userId).get + k.length.toDouble / hashSet.getHashFuncNum))
        } else {
          z ++ Map(userId -> (k.length.toDouble / hashSet.getHashFuncNum))
        }
    }
    val userSet = candidates.filter { case (_, d) => d >= 0.4 }
    val orderedList = userSet.toList sortBy (-_._2)
    println("List[(User,SimIndex)] = " + orderedList)
    makeProductSet(orderedList, userProduct, userId)
  }

  /**
   * Helper function for suggestProducts that builds
   * a list of product suggestions.
   *
   * @param ls a list of tuples of the form (Int, Double) with
   * the first element being the userId and the second the index
   * of similarity.
   * @param userProduct map of user to products.
   */
  private def makeProductSet(ls: List[(Int, Double)], userProduct: Map[Int, List[Int]], userId: Int) = {
    makeProductSetAux(ls, userProduct, Set(), userId)
  }

  /**
   * Helper function for makeProductSet.
   *
   */
  private def makeProductSetAux(ls: List[(Int, Double)], userProduct: Map[Int, List[Int]],
    acc: Set[Int], userId: Int): List[Int] = {
    if (acc.size < 5) {
      ls match {
        // subtract userId products from candidate set to avoid suggesting products a 
        // user already bought.
        case x :: xs => makeProductSetAux(xs, userProduct, acc ++ (userProduct.get(x._1).get.toSet).--(userProduct.get(userId).get.toSet), userId) case Nil => acc.toList take 5
      }
    } else {
      acc.toList take 5
    }
  }
}