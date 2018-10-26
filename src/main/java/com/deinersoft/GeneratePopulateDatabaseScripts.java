package com.deinersoft;

import java.io.*;
import java.util.*;

public class GeneratePopulateDatabaseScripts {

    public static void main(String[] args) {

        new ProcessTsvFileNameBasics("data/name.basics.tsv", new String[] {"target/name.tsv", "target/nameProfession.tsv", "target/nameTitle.tsv"}).execute();
        new ProcessTsvFileTitleAkas("data/title.akas.tsv", new String[] {"target/titleAka.tsv"}).execute();
        new ProcessTsvFileTitleBasics("data/title.basics.tsv", new String[]{"target/title.tsv","target/titleGenre.tsv"}).execute();
        new ProcessTsvFileTitleCrew("data/title.crew.tsv", new String[]{"target/titleDirector.tsv","target/titleWriter.tsv"}).execute();
        new ProcessTsvFileTitleEpisode("data/title.episode.tsv", new String[]{"target/titleEpisode.tsv"}).execute();
        new ProcessTsvFileTitlePrincipals("data/title.principals.tsv", new String[] {"target/titlePrincipals.tsv"}).execute();
        new ProcessTsvFileTitleRatings("data/title.ratings.tsv", new String[] {"target/titleRatings.tsv"}).execute();
    }

}