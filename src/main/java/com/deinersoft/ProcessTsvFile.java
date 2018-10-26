package com.deinersoft;

import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;

import java.io.*;
import java.time.Duration;
import java.time.Instant;

public class ProcessTsvFile {

    final int YEARS_OFFSET = 5;

    String tsvFileNameIn;
    String[] tsvFileNameOut;
    OutputStreamWriter[] tsvOutputStreamWriter;

    int tsvTotalLines;
    int tsvProcessedCount;
    int percentCompletePrevious;
    Instant startTime;

    public ProcessTsvFile(String tsvFileNameIn, String[] tsvFileNameOuts) {
        this.tsvFileNameIn = tsvFileNameIn;

        this.tsvFileNameOut = new String[tsvFileNameOuts.length];
        for (int i=0; i<tsvFileNameOuts.length; i++) {
            this.tsvFileNameOut[i] = tsvFileNameOuts[i];

        }
        this.tsvOutputStreamWriter = new OutputStreamWriter[tsvFileNameOuts.length];

        for (int i=0; i<tsvFileNameOuts.length; i++) {
            try {
                tsvOutputStreamWriter[i] = new OutputStreamWriter(new FileOutputStream(tsvFileNameOut[i]), "UTF-8");
            } catch (FileNotFoundException e) {
                System.err.println("Got an FileNotFoundException on " + tsvFileNameOut[i]);
                System.err.println(e.getMessage());
            } catch (UnsupportedEncodingException e) {
                System.err.println("Got an UnsupportedEncodingException on " + tsvFileNameOut[i]);
                System.err.println(e.getMessage());
            }
        }
    }

    public void execute() {
        tsvTotalLines = countLines(tsvFileNameIn);
        percentCompletePrevious = 0;
        startTime = Instant.now();

        TsvParserSettings settings = new TsvParserSettings();
        settings.getFormat().setLineSeparator("\n");
        settings.setMaxCharsPerColumn(32768);

        TsvParser parser = new TsvParser(settings);

        String[] row;
        tsvProcessedCount = 0;
        parser.beginParsing(getReader(tsvFileNameIn));

        while ((row = parser.parseNext()) != null) {
            processRow(row);
            tsvProcessedCount++;
            periodicReport();
        }

        periodicReport();

        for (int i=0; i<tsvFileNameOut.length; i++) {
            try {
                tsvOutputStreamWriter[i].flush();
                tsvOutputStreamWriter[i].close();
            } catch (IOException e) {
                System.err.println("Got an IOException on " + tsvFileNameOut[i]);
                System.err.println(e.getMessage());
            }
        }
    }

    public void processRow(String[] row) {
    }

    public void periodicReport() {
        int percentCompleteCurrent = tsvProcessedCount * 100 / tsvTotalLines;
        if (percentCompleteCurrent >= (percentCompletePrevious+10)) {
            percentCompletePrevious = percentCompleteCurrent;
            Instant nowTime = Instant.now();
            long milliSecondsElapsedTime = Duration.between(startTime, nowTime).toMillis();
            long milliSecondsProjectedCompletionTime = Math.round(Double.valueOf(milliSecondsElapsedTime) / (Double.valueOf(tsvProcessedCount) / Double.valueOf(tsvTotalLines)));
            long secondsRemainingCompletionTime = (milliSecondsProjectedCompletionTime - milliSecondsElapsedTime) / 1000;
            int minutesElapsed = (int) ((milliSecondsElapsedTime/1000) / 60);
            int secondsElapsed = (int) ((milliSecondsElapsedTime/1000) % 60);
            int minutesRemaining = (int) (secondsRemainingCompletionTime / 60);
            int secondsRemaining = (int) (secondsRemainingCompletionTime % 60);
            System.out.println(tsvFileNameIn +
                    " - " + percentCompleteCurrent + "% complete" +
                    " - elapsed time = " + String.format("%d:%02d", minutesElapsed, secondsElapsed) +
                    " - remaining time = " + String.format("%d:%02d", minutesRemaining, secondsRemaining));
        }
    }

    public final int countLines(String fileName)  {
        File file = new File(fileName);
        ProcessBuilder builder = new ProcessBuilder("wc", "-l", file.getAbsolutePath());
        Process process = null;
        try {
            process = builder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream in = process.getInputStream();
        LineNumberReader reader = new LineNumberReader(new InputStreamReader(in));
        String line = null;
        try {
            line = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (line != null) {
            return Integer.parseInt(line.trim().split(" ")[0]);
        } else {
            return -1;
        }
    }

    public Reader getReader(String relativePath) {
        try {
            return new InputStreamReader(new FileInputStream(relativePath), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Unable to read input", e);
        } catch (FileNotFoundException e) {
            throw new IllegalStateException("Unable to read input", e);
        }
    }

    public String processEscapedQuotes(String field) {
        if (field.equals("\\N")) {
            return "null";
        }
        return field.replaceAll("'", "''");
    }

    public String processYear(String year) {
        String result = "";
        if (year.matches("^\\d+$")) {
            result = Integer.toString(Integer.parseInt(year) - YEARS_OFFSET);
        }
        return result;
    }

    public String processBoolean(String trueOrFalse) {
        String result = "0";
        if (trueOrFalse.matches("^1$")) {
            result = "1";
        }
        return result;
    }

    public String processInteger(String number) {
        String result = "";
        if (number.matches("^\\d+$")) {
            result = number;
        }
        return result;
    }

}
