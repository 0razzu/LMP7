package service;


import error.ErrorMessage;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


public class IOService {
    private static void checkSize(int size) {
        if (size < 0)
            throw new NegativeArraySizeException(Integer.toString(size));
    }


    /* Филиппов А.В. 20.06.2020 Комментарий не удалять.
     Зачем передавать size? Нужно весь массив записывать же...
    */
    public static void writeIntArrayToBinaryStream(int[] array, OutputStream stream, int size) throws IOException {
        checkSize(size);

        for (int i = 0; i < size; i++) {
            stream.write(array[i] >> 24);
            stream.write(array[i] >> 16);
            stream.write(array[i] >> 8);
            stream.write(array[i]);
        }
    }

    /* Филиппов А.В. 20.06.2020 Комментарий не удалять.
     В задании написано "Предполагается, что до чтения массив уже создан". Почему вы передаете не массив, а размер?
     Для работы с байтами есть ByteBuffer, который умеет LowEndian и BigEndian
    */
    public static int[] readIntArrayFromBinaryStream(InputStream stream, int size) throws IOException {
        int[] array = new int[size];
        byte[] value = new byte[4];

//        ByteBuffer bb = ByteBuffer.wrap(value);
//        bb.getInt()

        for (int i = 0; i < size; i++) {
            if (stream.read(value) < 4)
                throw new IOException();

            array[i] = ((value[0] & 0xff) << 24) +
                    ((value[1] & 0xff) << 16) +
                    ((value[2] & 0xff) << 8) +
                    (value[3] & 0xff);
        }

        return array;
    }


    public static void writeIntArrayToCharStream(int[] array, Writer stream, int size) throws IOException {
        checkSize(size);

        for (int i = 0; i < size; i++) {
            stream.write(Integer.toString(array[i]));
            stream.write(" ");
        }
    }

    /* Филиппов А.В. 20.06.2020 Комментарий не удалять.
     Опять же массив создан и передается в функцию.
    */
    public static int[] readIntArrayFromCharStream(Reader stream, int size) throws IOException {
        char[] chars = new char[12 * size];

        if (stream.read(chars) == -1)
            throw new IOException();

        String[] strings = new String(chars).split(" ");

        int[] array = new int[size];

        for (int i = 0; i < size; i++)
            array[i] = Integer.parseInt(strings[i]);

        return array;
    }


    public static int[] readIntArrayFromRandomAccessFile(RandomAccessFile file, long offset, int size) throws IOException {
        /* Филиппов А.В. 20.06.2020 Комментарий не удалять.
         Бесполезная проверка - seek кинет IOException
        */
        if (offset < 0)
            throw new IllegalArgumentException(ErrorMessage.NEGATIVE_OFFSET);

        checkSize(size);

        file.seek(offset);

        int[] array = new int[size];

        for (int i = 0; i < size; i++)
            array[i] = file.readInt();

        return array;
    }


    public static List<File> getFilesByExtension(File dir, String extension) {
        if (!dir.isDirectory())
            throw new IllegalArgumentException(ErrorMessage.NOT_A_DIRECTORY);

        List<File> files = new ArrayList<>();

        if (extension != null) {
            if (extension.length() == 0) {
                for (File file : dir.listFiles()) {
                    if (file.isFile()) {
                        String fileName = file.getName();

                        if (fileName.lastIndexOf('.') == -1)
                            files.add(file);
                    }
                }
            } else {
                String suffix = "." + extension;

                for (File file : dir.listFiles()) {
                    if (file.isFile()) {
                        String fileName = file.getName();

                        if (fileName.endsWith(suffix))
                            files.add(file);
                    }
                }
            }
        }

        return files;
    }


    private static List<File> getFilesByPattern(File dir, Pattern pattern) {
        List<File> files = new ArrayList<>();

        for (File file : dir.listFiles()) {
            if (pattern.matcher(file.getName()).matches())
                files.add(file);

            if (file.isDirectory())
                files.addAll(getFilesByPattern(file, pattern));
        }

        return files;
    }


    public static List<File> getFilesByRegex(File dir, String regex) {
        if (!dir.isDirectory())
            throw new IllegalArgumentException(ErrorMessage.NOT_A_DIRECTORY);

        return regex == null ?
                new ArrayList<>() :
                getFilesByPattern(dir, Pattern.compile(regex));
    }
}
