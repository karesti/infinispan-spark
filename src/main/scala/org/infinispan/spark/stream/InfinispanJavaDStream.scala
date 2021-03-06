package org.infinispan.spark.stream

import java.util.Properties

import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming.api.java.{JavaDStream, JavaInputDStream, JavaPairDStream, JavaStreamingContext}
import org.infinispan.client.hotrod.event.ClientEvent
import org.infinispan.spark._

/**
  * @author gustavonalle
  */
object InfinispanJavaDStream {

   def writeToInfinispan[K, V](javaDStream: JavaPairDStream[K, V], configuration: Properties) = {
      javaDStream.dstream.foreachRDD(rdd => rdd.writeToInfinispan(configuration))
   }

   def writeToInfinispan[K, V](javaDStream: JavaDStream[(K, V)], configuration: Properties) = {
      javaDStream.dstream.foreachRDD(rdd => rdd.writeToInfinispan(configuration))
   }

   def createInfinispanInputDStream[K, V](javaStreamingContext: JavaStreamingContext,
                                          storageLevel: StorageLevel, configuration: Properties, includeState: Boolean) = {
      val infinispanDStream: InfinispanInputDStream[K, V] = new InfinispanInputDStream[K, V](javaStreamingContext.ssc, storageLevel, configuration, includeState)
      JavaInputDStream.fromInputDStream(infinispanDStream)
   }

   def createInfinispanInputDStream[K, V](javaStreamingContext: JavaStreamingContext,
                                          storageLevel: StorageLevel, configuration: Properties): JavaInputDStream[(K, V, ClientEvent.Type)] =
      createInfinispanInputDStream(javaStreamingContext, storageLevel, configuration, includeState = false)
}
