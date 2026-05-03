package com.clougence.clouddm.ds.mongodb.execute.jdbc;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.bson.Document;

import com.clougence.utils.io.IOUtils;

class MongoResultBuffer implements Closeable, Iterable<Document> {

    private static File          CACHE_FILE     = new File("/tmp");
    private static long          thresholdBytes = 4 * 1024 * 1024;

    private final List<Document> memoryBuffer;
    private File                 tempFile;
    private ObjectOutputStream   fileOutput;
    private long                 currentSize;
    private boolean              switchedToDisk;

    public static void setCacheFile(String fileDir) { MongoResultBuffer.CACHE_FILE = new File(fileDir); }

    public static void setThresholdBytes(long thresholdBytes) { MongoResultBuffer.thresholdBytes = thresholdBytes; }

    protected MongoResultBuffer(){
        this.memoryBuffer = new ArrayList<>();
        this.currentSize = 0;
        this.switchedToDisk = false;
    }

    public boolean isSpilled() { return switchedToDisk; }

    public void add(Document doc) throws SQLException, IOException {
        // Simple size estimation
        long docSize = doc.toJson().length() * 2L; // Rough estimate
        currentSize += docSize;

        if (switchedToDisk) {
            fileOutput.writeObject(doc);
        } else {
            memoryBuffer.add(doc);
            if (currentSize > thresholdBytes) {
                switchToDisk();
            }
        }
    }

    private void switchToDisk() throws IOException {
        if (!CACHE_FILE.exists()) {
            CACHE_FILE.mkdirs();
        }
        tempFile = File.createTempFile("mongo_result_", ".dat", CACHE_FILE);
        tempFile.deleteOnExit();
        fileOutput = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile)));

        for (Document doc : memoryBuffer) {
            fileOutput.writeObject(doc);
        }
        memoryBuffer.clear();
        switchedToDisk = true;
    }

    public void finish() throws IOException {
        if (fileOutput != null) {
            fileOutput.flush();
            fileOutput.close();
        }
    }

    @Override
    public Iterator<Document> iterator() {
        if (!switchedToDisk) {
            return memoryBuffer.iterator();
        }

        return new Iterator<Document>() {

            private ObjectInputStream input;
            private Document          nextDoc;

            {
                try {
                    input = new ObjectInputStream(new BufferedInputStream(new FileInputStream(tempFile)));
                    advance();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            private void advance() {
                try {
                    Object obj = input.readObject();
                    if (obj instanceof Document) {
                        nextDoc = (Document) obj;
                    } else {
                        nextDoc = null;
                        close();
                    }
                } catch (EOFException e) {
                    nextDoc = null;
                    close();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }

            private void close() {
                if (input != null) {
                    IOUtils.closeQuietly(input);
                    input = null;
                }
            }

            @Override
            public boolean hasNext() {
                return nextDoc != null;
            }

            @Override
            public Document next() {
                if (nextDoc == null) {
                    throw new NoSuchElementException();
                }
                Document current = nextDoc;
                advance();
                return current;
            }
        };
    }

    @Override
    public void close() throws IOException {
        if (fileOutput != null) {
            IOUtils.closeQuietly(fileOutput);
        }
        if (tempFile != null && tempFile.exists()) {
            tempFile.delete();
        }
    }

}
