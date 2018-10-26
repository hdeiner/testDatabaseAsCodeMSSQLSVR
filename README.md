The goal of this project is to demonstrate how meaningful size databases can be built on command for test/QA type work using Microsoft SQL Server.  This project is quite related to https://github.com/hdeiner/testDatabaseAsCode, with the biggest differences being that this targets MSSQLSVR insead of Oracle, uses Bulk Data Loading instead of a custom JDBC application, and does not depend upon AWS for any heavy lifting. 

We will build the IMDB database from scratch.  The input files are roughly 1.5GB.  We'll go through the following process:

* We will create an initial MSSQLSVR database container
* Locally, we put the schema into the database using Liquibase (note: we could easily target other database engines by making appropriate changes in liquibase.properties)
* That container is then committed and pushed to a DockerHub repository.
* We then process the IMDB data scripts into bco compatible tsv files by running GeneratePopulateDatabaseScripts.  NOTE!  ONE THING THAT WE DEMONSTRATE HERE IS HOW TO AVOID A NON-DETERMINISM BASED ON DATES.  This is a large point of discussion within the CoP right now.  Here, we offset all dates (YEARS) by 5 years, making everything 5 years older than it really is now. 
* Next, we commit and push the Docker container again.  This time, it will be "fat" with all the data baked into it.
* Now, we can pull the completed IMBD from the DockerHub repository any time we need a fresh copy of the database and it's data.

Nuts.  GitHub has a policy where they will not allow files greater than 100MB.  So, you'll have to put your own files from IMDB in the data directory.  See https://datasets.imdbws.com.

Note that this project runs several magnitudes of order faster than the https://github.com/hdeiner/testDatabaseAsCode project.  The difference is probably because of use of the bulk data load facility more than anything else.

Here's a look at the results produced.  

It took about 15 minutes to run (including all network traffic to DockerHub), and produced the following containers:
```log
howarddeiner@ubuntu:~/IdeaProjects/testDatabaseAsCodeMSSQLSVR$ sudo docker images
REPOSITORY                       TAG                 IMAGE ID            CREATED             SIZE
howarddeiner/imdbmssqlsvr        dataloaded          b129f9b1de50        2 minutes ago       8.44GB
howarddeiner/imdbmssqlsvr        schema              f67793f0c644        12 minutes ago      1.49GB
```
Here's the blow by blow on the log produced:
```log
Fri Oct 26 10:23:25 EDT 2018
Stop current IMDBMSSQLSVR Docker container
IMDBMSSQLSVR
Remove current IMDBMSSQLSVR Docker container
IMDBMSSQLSVR
Create a fresh Docker IMDBMSSQLSVR container
Starting microsoft/mssql-server-linux:latest in Docker container
a0bc28641eabf6c3fe657cc56df15e1bddeb673cfb90310bd85e9888cdd2264b
Pause 30 seconds to allow MSSQLSVR to start up
Install Schema
Starting Liquibase at Fri, 26 Oct 2018 10:23:59 EDT (version 3.6.1 built at 2018-04-11 08:41:04)
Liquibase: Update has been successful.
Commit and push the Docker MSSQLSVR container with just schema as a Docker image
sha256:f67793f0c644c693f20775e8216783fed454ca330fdabb3a6bbd38c424be6435
The push refers to repository [docker.io/howarddeiner/imdbmssqlsvr]
982e1daff2aa: Preparing
e173741a25f0: Preparing
b35af3dc736e: Preparing
45feb6b3c7be: Preparing
912a24c355e6: Preparing
bb83128af95f: Preparing
49907af65b0a: Preparing
4589f96366e6: Preparing
b97229212d30: Preparing
cd181336f142: Preparing
0f5ff0cf6a1c: Preparing
49907af65b0a: Waiting
4589f96366e6: Waiting
b97229212d30: Waiting
cd181336f142: Waiting
0f5ff0cf6a1c: Waiting
bb83128af95f: Waiting
e173741a25f0: Layer already exists
45feb6b3c7be: Layer already exists
912a24c355e6: Layer already exists
b35af3dc736e: Layer already exists
49907af65b0a: Layer already exists
bb83128af95f: Layer already exists
b97229212d30: Layer already exists
4589f96366e6: Layer already exists
cd181336f142: Layer already exists
0f5ff0cf6a1c: Layer already exists
982e1daff2aa: Pushed
schema: digest: sha256:fdc5a84fe6c6669122390862c9ef48b0ce3f48c3020b5a105ac932b0aa49d352 size: 2624
Create and the database data massager
[INFO] Scanning for projects...
[INFO]   
[INFO] ------------------------------------------------------------------------
[INFO] Building testDatabaseAsCodeMSSQLSVR 1.0
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- maven-clean-plugin:2.5:clean (default-clean) @ testDatabaseAsCodeMSSQLSVR ---
[INFO] Deleting /home/howarddeiner/IdeaProjects/testDatabaseAsCodeMSSQLSVR/target
[INFO]
[INFO] --- maven-resources-plugin:3.1.0:resources (default-resources) @ testDatabaseAsCodeMSSQLSVR ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] Copying 0 resource
[INFO]
[INFO] --- maven-compiler-plugin:3.5.1:compile (default-compile) @ testDatabaseAsCodeMSSQLSVR ---
[INFO] Changes detected - recompiling the module!
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[INFO] Compiling 9 source files to /home/howarddeiner/IdeaProjects/testDatabaseAsCodeMSSQLSVR/target/classes
[INFO]
[INFO] --- maven-resources-plugin:3.1.0:resources (default-resources) @ testDatabaseAsCodeMSSQLSVR ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] Copying 0 resource
[INFO]
[INFO] --- maven-compiler-plugin:3.5.1:compile (default-compile) @ testDatabaseAsCodeMSSQLSVR ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-resources-plugin:3.1.0:testResources (default-testResources) @ testDatabaseAsCodeMSSQLSVR ---
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] skip non existing resourceDirectory /home/howarddeiner/IdeaProjects/testDatabaseAsCodeMSSQLSVR/src/test/resources
[INFO]
[INFO] --- maven-compiler-plugin:3.5.1:testCompile (default-testCompile) @ testDatabaseAsCodeMSSQLSVR ---
[INFO] Nothing to compile - all classes are up to date
[INFO]
[INFO] --- maven-surefire-plugin:2.17:test (default-test) @ testDatabaseAsCodeMSSQLSVR ---
[INFO] No tests to run.
[INFO]
[INFO] --- maven-jar-plugin:2.4:jar (default-jar) @ testDatabaseAsCodeMSSQLSVR ---
[INFO] Building jar: /home/howarddeiner/IdeaProjects/testDatabaseAsCodeMSSQLSVR/target/testDatabaseAsCodeMSSQLSVR-1.0.jar
[INFO]
[INFO] --- maven-dependency-plugin:3.1.1:copy-dependencies (copy-dependencies) @ testDatabaseAsCodeMSSQLSVR ---
[INFO] Copying univocity-parsers-2.7.6.jar to /home/howarddeiner/IdeaProjects/testDatabaseAsCodeMSSQLSVR/target/lib/univocity-parsers-2.7.6.jar
[INFO] Copying ojdbc-12.2.0.1.jar to /home/howarddeiner/IdeaProjects/testDatabaseAsCodeMSSQLSVR/target/lib/ojdbc-12.2.0.1.jar
[INFO]
[INFO] --- maven-resources-plugin:3.1.0:copy-resources (copy-resources) @ testDatabaseAsCodeMSSQLSVR ---
[WARNING] File encoding has not been set, using platform encoding UTF-8, i.e. build is platform dependent!
[WARNING] Please take a look into the FAQ: https://maven.apache.org/general.html#encoding-warning
[WARNING] Using platform encoding (UTF-8 actually) to copy filtered resources, i.e. build is platform dependent!
[INFO] Copying 25 resources
[INFO]
[INFO] --- maven-shade-plugin:3.2.0:shade (default) @ testDatabaseAsCodeMSSQLSVR ---
[INFO] Including com.univocity:univocity-parsers:jar:2.7.6 in the shaded jar.
[INFO] Including com.oracle:ojdbc:jar:12.2.0.1 in the shaded jar.
[INFO] Replacing original artifact with shaded artifact.
[INFO] Replacing /home/howarddeiner/IdeaProjects/testDatabaseAsCodeMSSQLSVR/target/testDatabaseAsCodeMSSQLSVR-1.0.jar with /home/howarddeiner/IdeaProjects/testDatabaseAsCodeMSSQLSVR/target/testDatabaseAsCodeMSSQLSVR-1.0-shaded.jar
[INFO] Dependency-reduced POM written at: /home/howarddeiner/IdeaProjects/testDatabaseAsCodeMSSQLSVR/dependency-reduced-pom.xml
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time: 5.050 s
[INFO] Finished at: 2018-10-26T10:24:13-04:00
[INFO] Final Memory: 35M/357M
[INFO] ------------------------------------------------------------------------
Generate the database creation scripts
data/name.basics.tsv - 10% complete - elapsed time = 0:02 - remaining time = 0:21
data/name.basics.tsv - 20% complete - elapsed time = 0:04 - remaining time = 0:16
data/name.basics.tsv - 30% complete - elapsed time = 0:05 - remaining time = 0:13
data/name.basics.tsv - 40% complete - elapsed time = 0:07 - remaining time = 0:10
data/name.basics.tsv - 50% complete - elapsed time = 0:08 - remaining time = 0:08
data/name.basics.tsv - 60% complete - elapsed time = 0:10 - remaining time = 0:06
data/name.basics.tsv - 70% complete - elapsed time = 0:11 - remaining time = 0:04
data/name.basics.tsv - 80% complete - elapsed time = 0:13 - remaining time = 0:03
data/name.basics.tsv - 90% complete - elapsed time = 0:14 - remaining time = 0:01
data/name.basics.tsv - 100% complete - elapsed time = 0:15 - remaining time = 0:00
data/title.akas.tsv - 10% complete - elapsed time = 0:00 - remaining time = 0:06
data/title.akas.tsv - 20% complete - elapsed time = 0:01 - remaining time = 0:05
data/title.akas.tsv - 30% complete - elapsed time = 0:02 - remaining time = 0:04
data/title.akas.tsv - 40% complete - elapsed time = 0:02 - remaining time = 0:04
data/title.akas.tsv - 50% complete - elapsed time = 0:03 - remaining time = 0:03
data/title.akas.tsv - 60% complete - elapsed time = 0:04 - remaining time = 0:02
data/title.akas.tsv - 70% complete - elapsed time = 0:04 - remaining time = 0:02
data/title.akas.tsv - 80% complete - elapsed time = 0:05 - remaining time = 0:01
data/title.akas.tsv - 90% complete - elapsed time = 0:05 - remaining time = 0:00
data/title.akas.tsv - 100% complete - elapsed time = 0:06 - remaining time = 0:00
data/title.basics.tsv - 10% complete - elapsed time = 0:01 - remaining time = 0:14
data/title.basics.tsv - 20% complete - elapsed time = 0:03 - remaining time = 0:12
data/title.basics.tsv - 30% complete - elapsed time = 0:04 - remaining time = 0:10
data/title.basics.tsv - 40% complete - elapsed time = 0:06 - remaining time = 0:09
data/title.basics.tsv - 50% complete - elapsed time = 0:07 - remaining time = 0:07
data/title.basics.tsv - 60% complete - elapsed time = 0:09 - remaining time = 0:06
data/title.basics.tsv - 70% complete - elapsed time = 0:10 - remaining time = 0:04
data/title.basics.tsv - 80% complete - elapsed time = 0:12 - remaining time = 0:03
data/title.basics.tsv - 90% complete - elapsed time = 0:13 - remaining time = 0:01
data/title.basics.tsv - 100% complete - elapsed time = 0:15 - remaining time = 0:00
data/title.crew.tsv - 10% complete - elapsed time = 0:00 - remaining time = 0:04
data/title.crew.tsv - 20% complete - elapsed time = 0:00 - remaining time = 0:03
data/title.crew.tsv - 30% complete - elapsed time = 0:01 - remaining time = 0:02
data/title.crew.tsv - 40% complete - elapsed time = 0:01 - remaining time = 0:02
data/title.crew.tsv - 50% complete - elapsed time = 0:01 - remaining time = 0:01
data/title.crew.tsv - 60% complete - elapsed time = 0:02 - remaining time = 0:01
data/title.crew.tsv - 70% complete - elapsed time = 0:02 - remaining time = 0:01
data/title.crew.tsv - 80% complete - elapsed time = 0:02 - remaining time = 0:00
data/title.crew.tsv - 90% complete - elapsed time = 0:03 - remaining time = 0:00
data/title.crew.tsv - 100% complete - elapsed time = 0:03 - remaining time = 0:00
data/title.episode.tsv - 10% complete - elapsed time = 0:00 - remaining time = 0:02
data/title.episode.tsv - 20% complete - elapsed time = 0:00 - remaining time = 0:02
data/title.episode.tsv - 30% complete - elapsed time = 0:00 - remaining time = 0:01
data/title.episode.tsv - 40% complete - elapsed time = 0:01 - remaining time = 0:01
data/title.episode.tsv - 50% complete - elapsed time = 0:01 - remaining time = 0:01
data/title.episode.tsv - 60% complete - elapsed time = 0:01 - remaining time = 0:01
data/title.episode.tsv - 70% complete - elapsed time = 0:01 - remaining time = 0:00
data/title.episode.tsv - 80% complete - elapsed time = 0:02 - remaining time = 0:00
data/title.episode.tsv - 90% complete - elapsed time = 0:02 - remaining time = 0:00
data/title.episode.tsv - 100% complete - elapsed time = 0:02 - remaining time = 0:00
data/title.principals.tsv - 10% complete - elapsed time = 0:03 - remaining time = 0:28
data/title.principals.tsv - 20% complete - elapsed time = 0:06 - remaining time = 0:25
data/title.principals.tsv - 30% complete - elapsed time = 0:09 - remaining time = 0:21
data/title.principals.tsv - 40% complete - elapsed time = 0:12 - remaining time = 0:19
data/title.principals.tsv - 50% complete - elapsed time = 0:15 - remaining time = 0:15
data/title.principals.tsv - 60% complete - elapsed time = 0:19 - remaining time = 0:12
data/title.principals.tsv - 70% complete - elapsed time = 0:22 - remaining time = 0:09
data/title.ratings.tsv - 10% complete - elapsed time = 0:00 - remaining time = 0:00
data/title.ratings.tsv - 20% complete - elapsed time = 0:00 - remaining time = 0:00
data/title.ratings.tsv - 30% complete - elapsed time = 0:00 - remaining time = 0:00
data/title.ratings.tsv - 40% complete - elapsed time = 0:00 - remaining time = 0:00
data/title.ratings.tsv - 50% complete - elapsed time = 0:00 - remaining time = 0:00
data/title.ratings.tsv - 60% complete - elapsed time = 0:00 - remaining time = 0:00
data/title.ratings.tsv - 70% complete - elapsed time = 0:00 - remaining time = 0:00
data/title.ratings.tsv - 80% complete - elapsed time = 0:00 - remaining time = 0:00
data/title.ratings.tsv - 90% complete - elapsed time = 0:00 - remaining time = 0:00
data/title.ratings.tsv - 100% complete - elapsed time = 0:00 - remaining time = 0:00
Bulk Data import to name

Starting copy...

8907267 rows copied.
Network packet size (bytes): 4096
Clock Time (ms.) Total     : 74498  Average : (119563.8 rows per sec.)
Bulk Data import to nameProfession

Starting copy...

10028630 rows copied.
Network packet size (bytes): 4096
Clock Time (ms.) Total     : 47714  Average : (210182.1 rows per sec.)
Bulk Data import to nameTitle

Starting copy...

15728012 rows copied.
Network packet size (bytes): 4096
Clock Time (ms.) Total     : 34688  Average : (453413.6 rows per sec.)
Bulk Data import to title

Starting copy...

5337123 rows copied.
Network packet size (bytes): 4096
Clock Time (ms.) Total     : 34755  Average : (153564.2 rows per sec.)
Bulk Data import to titleAka

Starting copy...

3676160 rows copied.
Network packet size (bytes): 4096
Clock Time (ms.) Total     : 19482  Average : (188695.2 rows per sec.)
Bulk Data import to titleDirector

Starting copy...

3950389 rows copied.
Network packet size (bytes): 4096
Clock Time (ms.) Total     : 8788   Average : (449520.8 rows per sec.)
Bulk Data import to titleEpisode

Starting copy...

3630675 rows copied.
Network packet size (bytes): 4096
Clock Time (ms.) Total     : 9627   Average : (377134.6 rows per sec.)
Bulk Data import to titleGenre

Starting copy...

8569312 rows copied.
Network packet size (bytes): 4096
Clock Time (ms.) Total     : 17658  Average : (485293.5 rows per sec.)
Bulk Data import to titlePrincipals

Starting copy...

30365273 rows copied.
Network packet size (bytes): 4096
Clock Time (ms.) Total     : 131328 Average : (231217.0 rows per sec.)
Bulk Data import to titleRaings

Starting copy...

875370 rows copied.
Network packet size (bytes): 4096
Clock Time (ms.) Total     : 1860   Average : (470629.0 rows per sec.)
Bulk Data import to titleWriter

Starting copy...

6054248 rows copied.
Network packet size (bytes): 4096
Clock Time (ms.) Total     : 14075  Average : (430142.0 rows per sec.)
Commit and push the Docker MSSQLSVR container fully loaded with data as a Docker image
sha256:b129f9b1de5081ed0defeced46680e822c40ada923fd39c47c0f489b370f48b8
The push refers to repository [docker.io/howarddeiner/imdbmssqlsvr]
b579411d89f2: Preparing
e173741a25f0: Preparing
b35af3dc736e: Preparing
45feb6b3c7be: Preparing
912a24c355e6: Preparing
bb83128af95f: Preparing
49907af65b0a: Preparing
4589f96366e6: Preparing
b97229212d30: Preparing
cd181336f142: Preparing
0f5ff0cf6a1c: Preparing
49907af65b0a: Waiting
4589f96366e6: Waiting
b97229212d30: Waiting
cd181336f142: Waiting
0f5ff0cf6a1c: Waiting
bb83128af95f: Waiting
e173741a25f0: Layer already exists
912a24c355e6: Layer already exists
b35af3dc736e: Layer already exists
45feb6b3c7be: Layer already exists
bb83128af95f: Layer already exists
49907af65b0a: Layer already exists
b97229212d30: Layer already exists
cd181336f142: Layer already exists
0f5ff0cf6a1c: Layer already exists
4589f96366e6: Layer already exists
b579411d89f2: Pushed
dataloaded: digest: sha256:3e456f2e09240d240f879ff2761beb5ee214431f23701e6ff1e71b0519da4f1c size: 2627
Fri Oct 26 10:38:40 EDT 2018
```