# SPARK-DYNAMODB-AUDIENCE

> Development in progress

### Description

Implementation of a WordCount Apache Spark application using input data stored in a DynamoDB table.

This project connects to DynamoDB using the [Audience Project data source](https://github.com/audienceproject/spark-dynamodb). For examples using the [AWS emr-dynamodb-connector](https://github.com/awslabs/emr-dynamodb-connector), see [spark-dynamodb-example](https://github.com/leohoc/spark-dynamodb-example).

#### Install requirements

- An AWS account;
- awscli >= 2 with AWS account credentials configured;
- An installed JDK 8;

#### Providing the infrastructure

Follow the instructions of the [spark-dynamodb-infrastructure](https://github.com/leohoc/spark-dynamodb-infrastructure) project.

#### Generating the Input Data

The DynamoDB input data used in this project was generated using the instructions contained in [spark-dynamodb-example](https://github.com/leohoc/spark-dynamodb-example).

#### Spark App #1: Counting the words in the COVID-19 citations titles 

The Covid19CitationsWordCount application will count the number of times each word was used in the COVID-19 citations titles and store the result in a DynamoDB table.
Instructions:

1. Generate the application jar file:

```bash

./gradlew clean fatJar

```

2. the application file will be generated in build/libs/Covid19CitationsWordCount-1.0-SNAPSHOT.jar.

#### Running in the AWS EMR cluster

1. Upload the generated application file to the 'spark-dynamodb-example' bucket;

2. Connect to the EMR cluster master node with SSH (click the SSH link in the cluster summary panel and follow the instructions);

3. Download the application jar file to the master node:

```bash

aws s3 cp s3://spark-dynamodb-example/Covid19CitationsWordCount-1.0-SNAPSHOT.jar .

```

4. Execute the application:
 
```bash

spark-submit --packages com.audienceproject:spark-dynamodb_2.11:1.0.2 Covid19CitationsWordCount-1.0-SNAPSHOT.jar

```  