package com.lcarvalho.sparkddb.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class JobConfiguration {

    public static JavaSparkContext buildSparkContext(String application, String tableName) throws ClassNotFoundException {
        SparkConf conf = new SparkConf()
                .setAppName(application + "-" + tableName);
        return new JavaSparkContext(conf);
    }

    public static JavaSparkContext buildLocalSparkContext(String application, String tableName) throws ClassNotFoundException {
        SparkConf conf = new SparkConf()
                .setMaster("local[4]")
                .setAppName(application + "-" + tableName);
        return new JavaSparkContext(conf);
    }
}
