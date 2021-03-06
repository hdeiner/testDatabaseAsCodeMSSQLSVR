package com.deinersoft;

import java.io.IOException;

public class ProcessTsvFileTitlePrincipals extends ProcessTsvFile {
    public ProcessTsvFileTitlePrincipals(String tsvFileNameIn, String[] tsvFileNameOuts) {
        super(tsvFileNameIn, tsvFileNameOuts);
    }

    @Override
    public void processRow(String[] row) {
        String dataRow;
        if (tsvProcessedCount == 0) {
            dataRow = row[0] +"\t" + row[1] + "\t" + row[2] + "\t" + row[3] + "\t" + row[4] + "\t" + row[5] + "\n";
            try {
                tsvOutputStreamWriter[0].write(dataRow);
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[0]);
                System.err.println(e.getMessage());
            }
        } else {
            dataRow = row[0]; // tconst
            dataRow += "\t" + processInteger(row[1]); // ordering
            dataRow += "\t" + row[2]; // nconst
            dataRow += "\t" + processEscapedQuotes(row[3]); // category
            dataRow += "\t" + processEscapedQuotes(row[4]); // job
            dataRow += "\t" + processEscapedQuotes(row[5]) + "\n"; // character
            try {
                tsvOutputStreamWriter[0].write(dataRow);
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[0]);
                System.err.println(e.getMessage());
            }
        }
    }
}