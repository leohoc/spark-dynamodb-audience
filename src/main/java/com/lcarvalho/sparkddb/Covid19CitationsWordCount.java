package com.lcarvalho.sparkddb;

import com.lcarvalho.sparkddb.config.SparkConfiguration;
import com.lcarvalho.sparkddb.model.WordCount;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Encoders;
import org.apache.spark.sql.SparkSession;
import scala.Tuple2;

import java.util.*;

import static org.apache.spark.sql.functions.col;

public class Covid19CitationsWordCount {

    private static Logger LOGGER = LogManager.getLogger(Covid19CitationsWordCount.class);

    public static void main(String[] args) throws Exception {

        Logger.getLogger("org").setLevel(Level.ERROR);

        // Building the Spark session
        JavaSparkContext sparkContext = SparkConfiguration.buildSparkContext();
        SparkSession sparkSession = SparkSession.builder().sparkContext(sparkContext.sc()).getOrCreate();

        // Building a dataset of the Covid19Citation table
        Dataset citations = sparkSession.read().option("tableName", "Covid19Citation").format("dynamodb").load();
        citations.show(10);
        LOGGER.info("Citations count: " + citations.count());

        // Filtering only the citations published in 2020
        Dataset filteredCitations = citations.filter(col("publishedYear").equalTo("2020"));
        filteredCitations.show(10);
        LOGGER.info("Filtered citations count: " + filteredCitations.count());

        // Selecting only the citation titles and then creating a Pair RDD with its words count
        Dataset citationTitles = filteredCitations.select(col("title"));
        JavaRDD<String> citationTitlesWords = citationTitles.javaRDD().flatMap(citationTitle -> Arrays.asList(citationTitle.toString().split(" ")).iterator());
        JavaPairRDD<String, Integer> citationTitlesWordCount = citationTitlesWords.mapToPair(wordCount -> new Tuple2<>(wordCount, 1));
        LOGGER.info("Citations titles word count: " + citationTitlesWordCount.count());

        // Grouping the words and counting the number of times each one appeared
        JavaPairRDD<String, Integer> groupedCitationTitlesWordCount = citationTitlesWordCount
                .reduceByKey((firstWordCount, secondWordCount) -> firstWordCount + secondWordCount);
        LOGGER.info("Citation titles distinct word count: " + groupedCitationTitlesWordCount.count());

        // Transforming the Pair RDD in a RDD of the model object type
        JavaRDD wordCountRDD = groupedCitationTitlesWordCount.map(wordCountTuple -> new WordCount(wordCountTuple._1, wordCountTuple._2));

        // Converting the RDD in a Dataset and filtering the empty string words
        Dataset<WordCount> wordCountDataset = sparkSession.createDataset(wordCountRDD.rdd(), Encoders.bean(WordCount.class));
        Dataset<WordCount> filteredWordCountDataset = wordCountDataset.filter(col("word").notEqual(""));

        // Writing the result Dataset to a DynamoDB table
        filteredWordCountDataset.write().option("tableName", "Covid19CitationTitlesWordCount").format("dynamodb").save();
    }
}
