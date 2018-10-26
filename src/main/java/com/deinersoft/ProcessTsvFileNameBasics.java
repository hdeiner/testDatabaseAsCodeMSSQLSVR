package com.deinersoft;

import java.io.IOException;

public class ProcessTsvFileNameBasics extends ProcessTsvFile {
    public ProcessTsvFileNameBasics(String tsvFileNameIn, String[] tsvFileNameNameOuts) {
        super(tsvFileNameIn, tsvFileNameNameOuts);
    }

    @Override
    public void processRow(String[] row) {
        String dataRow;
        if (tsvProcessedCount == 0) {
            dataRow = row[0] +"\t" + row[1] + "\t" + row[2] + "\t" + row[3] + "\n";
            try {
                tsvOutputStreamWriter[0].write(dataRow);
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[0]);
                System.err.println(e.getMessage());
            }

            dataRow = row[0] +"\t" + "profession" + "\n";
            try {
                tsvOutputStreamWriter[1].write(dataRow);
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[1]);
                System.err.println(e.getMessage());
            }

            dataRow = row[0] +"\t" + row[5] + "\n";
            try {
                tsvOutputStreamWriter[2].write(dataRow);
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[2]);
                System.err.println(e.getMessage());
            }
        }
        else {
            dataRow = row[0]; // nconst
            dataRow += "\t" + processEscapedQuotes(row[1]); // primaryName
            dataRow += "\t" + processYear(row[2]); // birthYear
            dataRow += "\t" + processYear(row[3]) + "\n"; // deathYear
            try {
                tsvOutputStreamWriter[0].write(dataRow);
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[0]);
                System.err.println(e.getMessage());
            }

            if (row[4] != null) {
                String[] professions = row[4].split(",[ ]*");
                for (String profession : professions) {
                    dataRow = row[0]; // nconst
                    dataRow += "\t" + profession + "\n"; // profession
                    try {
                        tsvOutputStreamWriter[1].write(dataRow);
                    } catch (IOException e) {
                        System.err.println("Got an IOException on " + tsvFileNameOut[1]);
                        System.err.println(e.getMessage());
                    }
                }
            }

            if (row[5] != null) {
                String[] titles = row[5].split(",[ ]*");
                for (String title : titles) {
                    dataRow = row[0]; // nconst
                    dataRow += "\t" + title + "\n"; // tconst
                    try {
                        tsvOutputStreamWriter[2].write(dataRow);
                    } catch (IOException e) {
                        System.err.println("Got an IOException on " + tsvFileNameOut[2]);
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
    }
}
