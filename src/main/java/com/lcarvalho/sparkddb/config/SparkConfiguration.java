package com.lcarvalho.sparkddb.config;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaSparkContext;

public class SparkConfiguration {

    private static final String APP_NAME = "Covid19CitationsWordCount-AudienceProject";

    public static JavaSparkContext buildSparkContext() throws ClassNotFoundException {
        SparkConf conf = new SparkConf()
                .setAppName(APP_NAME);
        return new JavaSparkContext(conf);
    }

    public static JavaSparkContext buildLocalSparkContext() throws ClassNotFoundException {
        SparkConf conf = new SparkConf()
                .setMaster("local[2]")
                .setAppName(APP_NAME);
        return new JavaSparkContext(conf);
    }
}
