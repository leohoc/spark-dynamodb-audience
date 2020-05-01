# SPARK-DYNAMODB-AUDIENCE

> Development in progress

### Description

Implementation of a WordCount Apache Spark application using input data stored in a DynamoDB table.

This project connect to DynamoDB using the [Audience Project data source](https://github.com/audienceproject/spark-dynamodb). For examples using the [AWS emr-dynamodb-connector](https://github.com/awslabs/emr-dynamodb-connector), see [spark-dynamodb-example](https://github.com/leohoc/spark-dynamodb-example).

#### Install requirements

- An AWS account;
- awscli >= 2 with AWS account credentials configured;
- An installed JDK 8;

#### Creating DynamoDB resources

The DynamoDB input data used in this project was generated using the instructions contained in [spark-dynamodb-example](https://github.com/leohoc/spark-dynamodb-example).  

#### Creating an EMR cluster

Using the AWS console, create a EMR cluster with the following configurations:

* Cluster execution mode;
* Software configuration version: emr-5.*;
* Applications: Spark;
* Create a EC2 keypair and choose it in the security and access session;
* Add a policy with read/write access to any DynamoDB table and index to the EC2 instance profile Role;

When cluster initializing is complete, add permission to SSH connection:

* Go to the main security group configuration;
* Edit the inbound rules of the master security group;
* Add a rule with 'SSH' type and 'Anywhere' source;

#### Spark App #1: Counting the number of occurrences of each word in the prophecies of a single day

1. Generate the application jar file:

```bash

./gradlew clean fatJar

```

2. Create a S3 bucket named 'spark-dynamodb-examples';

3. Upload the generated 'build/libs/WordCountAudience-1.0-SNAPSHOT.jar' file to the bucket;

4. Connect to the EMR cluster master node with SSH (click the SSH link in the cluster summary panel and follow the instructions);

5. Download the appliction jar file to the master node:

```bash

aws s3 cp s3://spark-dynamodb-examples/WordCountAudience-1.0-SNAPSHOT.jar .

```

6. Execute the application:
 
```bash

spark-submit --packages com.audienceproject:spark-dynamodb_2.11:1.0.2 ./WordCountAudience-1.0-SNAPSHOT.jar

```