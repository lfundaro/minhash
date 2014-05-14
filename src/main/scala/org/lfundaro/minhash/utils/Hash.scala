package org.lfundaro.minhash.utils

import scala.util.Random

/**
 * Contains all the utilities to hash an integer.
 * Hash functions are calculated using the following function:
 * h = (a_i*x + b_i) % p , where:
 * 	{a_i,b_i}: a set of coefficients.
 *  p: a large prime number.
 *  x: the input integer that we want to hash.
 * @param n the amount of hash functions to generate.
 */
class Hash(private val n: Int) {

  /** Best results with the smaller prime number **/
  private val largePrime = 2147483647
  //  private val largePrime = 503

  def getHashFuncNum = n

  /**
   * Generates a list of hash functions according
   * to the algorithm stated in the this class header.
   */
  def hashList(): List[(Int => Int)] = {
    coefs map ({ case (a, b) => ((x: Int) => (x * a + b) % largePrime) })
  }
  /** Alternative implementation for the hashing function **/
  //    def hashList(): List[(Int=>Int)] = {
  //    val rand = new Random(System.currentTimeMillis())
  //    coefs map ({case (a,b) => ((x:Int) => ((x>>4)*a+b*x+rand.nextInt())& 131071)})
  //  }

  /**
   * Returns a list of coefficients {a_i,b_i}.
   */
  private def coefs(): List[(Int, Int)] = {
    val rand = new Random(System.currentTimeMillis())
    auxCoefs(n, rand, List())
  }

  /**
   * Helper function for coefs function.
   *
   * @param n the amount of coefficients needed to be generated.
   * @param rand random generator.
   * @param acc accumulator list of coefficients.
   */
  private def auxCoefs(n: Int, rand: Random, acc: List[(Int, Int)]): List[(Int, Int)] = {
    if (n > 0) {
      auxCoefs(n - 1, rand, (rand.nextInt(largePrime), rand.nextInt(largePrime)) :: acc)
    } else {
      acc
    }
  }

}