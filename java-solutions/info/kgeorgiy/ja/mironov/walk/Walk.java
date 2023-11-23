package info.kgeorgiy.ja.mironov.walk;

import java.io.*;
import java.math.BigInteger;
import java.nio.file.InvalidPathException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class
Walk {

    public static void main(String[] args) {
        walker(args[0], args[1]);
    }

    private static String sha_256Sum(String path) throws IOException, NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] buf = new byte[1024];
        int len;
        InputStream stream = new BufferedInputStream(new FileInputStream(path));
        while ((len = stream.read(buf)) > 0) {
            digest.update(buf, 0, len);
        }
        stream.close();
        BigInteger num = new BigInteger(1, digest.digest());
        StringBuilder hex = new StringBuilder(num.toString(16));
        while (hex.length() < 64) {
            hex.insert(0, '0');
        }
        return hex.toString();
    }

    private static void walker(String inputPath, String outputPath) {
        //Path path;

        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath)))) {
                String path, sha_256;
                while ((path = reader.readLine()) != null) {
                    try {
                        sha_256 = sha_256Sum(path);
                        writer.write(String.format("%s %s", sha_256, path));
                        writer.newLine();
                    } catch (InvalidPathException e) {
                        writer.write(String.format("%64x %s", 0, path));
                        writer.newLine();
                    } catch (NoSuchAlgorithmException e) {
                        //e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                System.out.println("ERROR: IOException: " + e.getMessage());
            }
        } catch (IOException e) {
            System.out.println("ERROR: IOException: " + e.getMessage());
        }
    }
}

