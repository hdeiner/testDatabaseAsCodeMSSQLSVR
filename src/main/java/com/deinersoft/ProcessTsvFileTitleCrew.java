package com.deinersoft;

import java.io.IOException;

public class ProcessTsvFileTitleCrew extends ProcessTsvFile {

    public ProcessTsvFileTitleCrew(String tsvFileNameIn, String[] tsvFileNameOuts) {
        super(tsvFileNameIn, tsvFileNameOuts);
    }

    @Override
    public void processRow(String[] row) {
        String dataRow;
        if (tsvProcessedCount == 0) {
            dataRow = "tconst" +"\t" + "nconst" + "\n";
            try {
                tsvOutputStreamWriter[0].write(dataRow);
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[0]);
                System.err.println(e.getMessage());
            }
            dataRow = "tconst" +"\t" + "nconst" + "\n";
            try {
                tsvOutputStreamWriter[1].write(dataRow);
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[1]);
                System.err.println(e.getMessage());
            }
        } else {
            if (row[1].equals("\\N")) row[1] = null;
            if (row[1] != null) {
                String[] directors = row[1].split(",[ ]*");
                for (String director : directors) {
                    dataRow = row[0] + "', "; // tconst
                    dataRow += "\t" + director + "\n"; // nconst
                    try {
                        tsvOutputStreamWriter[0].write(dataRow);
                    } catch (IOException e) {
                        System.err.println("Got an IOException on " + tsvFileNameOut[0]);
                        System.err.println(e.getMessage());
                    }
                }
            }

            if (row[2].equals("\\N")) row[2] = null;
            if (row[2] != null) {
                String[] writers = row[2].split(",[ ]*");
                for (String writer : writers) {
                    dataRow = row[0] + "', "; // tconst
                    dataRow += "\t" + writer + "\n"; // nconst
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
