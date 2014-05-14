package org.lfundaro.minhash

import org.lfundaro.minhash.utils.InputParser._
import org.lfundaro.minhash.utils.Hash

object Main extends App {
  val pairs = readFromFile(args(0))
  val productUser = buildProductUserMap(pairs)
  val userProduct = buildUserProductMap(pairs)
  val hash = new Hash(Integer.valueOf(args(1)))
  val characteristic = buildCharacteristicMatrix(pairs, hash.getHashFuncNum)
  val minhash = new MinHash(productUser,collection.mutable.Map(characteristic.toSeq: _*) , hash)
  val result = minhash.suggestProducts(Integer.valueOf(args(2)),userProduct)
  println("Suggested products for user "+args(2)+": "+result)
}