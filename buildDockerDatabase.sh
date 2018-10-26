#!/usr/bin/env bash

date

echo Stop current IMDBMSSQLSVR Docker container
sudo -S <<< "password" docker stop IMDBMSSQLSVR

echo Remove current IMDBMSSQLSVR Docker container
sudo -S <<< "password" docker rm IMDBMSSQLSVR

echo Create a fresh Docker IMDBMSSQLSVR container
echo Starting microsoft/mssql-server-linux:latest in Docker container
sudo -S <<< "password" docker run -d -p 1433:1433 -e 'ACCEPT_EULA=Y' -e 'SA_PASSWORD=Strong!Passw0rd' --name IMDBMSSQLSVR microsoft/mssql-server-linux:latest

echo Pause 30 seconds to allow MSSQLSVR to start up
sleep 30

echo Install Schema
liquibase --changeLogFile=src/main/db/IMDB-schema.xml update

echo Commit and push the Docker MSSQLSVR container with just schema as a Docker image
sudo -S <<< "password"  docker commit -a howarddeiner -m "IMDB Schema" IMDBMSSQLSVR howarddeiner/imdbmssqlsvr:schema
sudo -S <<< "password"  docker push howarddeiner/imdbmssqlsvr:schema

echo Create and the database data massager
mvn clean compile package

echo Generate the database creation scripts
java -cp target/testDatabaseAsCodeMSSQLSVR-1.0.jar com.deinersoft.GeneratePopulateDatabaseScripts

echo Bulk Data import to name
bcp name in target/name.tsv -U 'sa' -P 'Strong!Passw0rd' -S localhost -c -b 100000 -F 2 -m 0 -e target/name.err

echo Bulk Data import to nameProfession
bcp nameProfession in target/nameProfession.tsv -U 'sa' -P 'Strong!Passw0rd' -S localhost -c -b 100000 -F 2 -m 0 -e target/nameProfession.err

echo Bulk Data import to nameTitle
bcp nameTitle in target/nameTitle.tsv -U 'sa' -P 'Strong!Passw0rd' -S localhost -c -b 100000 -F 2 -m 0 -e target/nameTitle.err

echo Bulk Data import to title
bcp title in target/title.tsv -U 'sa' -P 'Strong!Passw0rd' -S localhost -c -b 100000 -F 2 -m 0 -e target/title.err

echo Bulk Data import to titleAka
bcp titleAka in target/titleAka.tsv -U 'sa' -P 'Strong!Passw0rd' -S localhost -c -b 100000 -F 2 -m 0 -e target/titleAka.err

echo Bulk Data import to titleDirector
bcp titleDirector in target/titleDirector.tsv -U 'sa' -P 'Strong!Passw0rd' -S localhost -c -b 100000 -F 2 -m 0 -e target/titleDirector.err

echo Bulk Data import to titleEpisode
bcp titleEpisode in target/titleEpisode.tsv -U 'sa' -P 'Strong!Passw0rd' -S localhost -c -b 100000 -F 2 -m 0 -e target/titleEpisode.err

echo Bulk Data import to titleGenre
bcp titleGenre in target/titleGenre.tsv -U 'sa' -P 'Strong!Passw0rd' -S localhost -c -b 100000 -F 2 -m 0 -e target/titleGenre.err

echo Bulk Data import to titlePrincipals
bcp titlePrincipals in target/titlePrincipals.tsv -U 'sa' -P 'Strong!Passw0rd' -S localhost -c -b 100000 -F 2 -m 0 -e target/titlePrincipals.err

echo Bulk Data import to titleRaings
bcp titleRatings in target/titleRatings.tsv -U 'sa' -P 'Strong!Passw0rd' -S localhost -c -b 100000 -F 2 -m 0 -e target/titleRatings.err

echo Bulk Data import to titleWriter
bcp titleWriter in target/titleWriter.tsv -U 'sa' -P 'Strong!Passw0rd' -S localhost -c -b 100000 -F 2 -m 0 -e target/titleWriter.err

echo Commit and push the Docker MSSQLSVR container fully loaded with data as a Docker image
sudo -S <<< "password"  docker commit -a howarddeiner -m "IMDB Schema and Data" IMDBMSSQLSVR howarddeiner/imdbmssqlsvr:dataloaded
sudo -S <<< "password"  docker push howarddeiner/imdbmssqlsvr:dataloaded

date
