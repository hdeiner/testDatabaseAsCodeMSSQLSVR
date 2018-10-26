package com.deinersoft;

import java.io.IOException;

public class ProcessTsvFileTitleEpisode extends ProcessTsvFile {
    public ProcessTsvFileTitleEpisode(String tsvFileNameIn, String[] tsvFileNameOuts) {
        super(tsvFileNameIn, tsvFileNameOuts );
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
        } else {
            dataRow = row[0]; // tconst
            dataRow += "\t" + row[1]; // tconstParent
            dataRow += "\t" + processInteger(row[2]); // seasonNumber
            dataRow += "\t" + processInteger(row[3]) + "\n"; // episodeNumber
            try {
                tsvOutputStreamWriter[0].write(dataRow);
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[0]);
                System.err.println(e.getMessage());
            }
        }
    }
}
