package com.deinersoft;

import java.io.IOException;

public class ProcessTsvFileTitleBasics extends ProcessTsvFile {
    public ProcessTsvFileTitleBasics(String tsvFileNameIn, String[] tsvFileNameOuts) {
        super(tsvFileNameIn, tsvFileNameOuts);
    }

    @Override
    public void processRow(String[] row) {
        String dataRow;
        if (tsvProcessedCount == 0) {
            dataRow = row[0] +"\t" + row[1] + "\t" + row[2] + "\t" + row[3] + "\t" + row[4] + "\t" + row[5] + "\t" + row[6] + "\t" + row[7] + "\n";
            try {
                tsvOutputStreamWriter[0].write(dataRow);
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[0]);
                System.err.println(e.getMessage());
            }
            dataRow = row[0] +"\t" + "genre" + "\n";
            try {
                tsvOutputStreamWriter[1].write(dataRow);
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[1]);
                System.err.println(e.getMessage());
            }
        } else {
            dataRow = row[0]; // tconst
            dataRow += "\t" + processEscapedQuotes(row[1]); // titleType
            dataRow += "\t" + processEscapedQuotes(row[2]); // primaryTitle
            dataRow += "\t" + processEscapedQuotes(row[3]); // originalTitle
            dataRow += "\t" + processBoolean(row[4]); // isAdult
            dataRow += "\t" + processYear(row[5]); // startYear
            dataRow += "\t" + processYear(row[6]); // endYear
            dataRow += "\t" + processInteger(row[7]) + "\n"; // runTimeMinutes
            try {
                tsvOutputStreamWriter[0].write(dataRow);
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[0]);
                System.err.println(e.getMessage());
            }

            if (row[8] != null) {
                String[] genres = row[8].split(",[ ]*");
                for (String genre : genres) {
                    dataRow = row[0]; // tconst
                    dataRow += "\t" + genre + "\n"; // genre
                    try {
                        tsvOutputStreamWriter[1].write(dataRow);
                    } catch (IOException e) {
                        System.err.println("Got an IOException on " + tsvFileNameOut[1]);
                        System.err.println(e.getMessage());
                    }
                }
            }
        }
    }
}
