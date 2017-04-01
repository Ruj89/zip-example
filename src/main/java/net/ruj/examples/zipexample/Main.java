package net.ruj.examples.zipexample;

import net.lingala.zip4j.exception.ZipException;
import net.ruj.examples.zipexample.controllers.ZipController;
import net.ruj.examples.zipexample.services.ZipService;

import java.io.FileOutputStream;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, ZipException {
        ZipService zipService = new ZipService();
        ZipController zipController = new ZipController(zipService);

        FileOutputStream destination = new FileOutputStream("output.zip");
        zipController.start(destination);
    }
}
