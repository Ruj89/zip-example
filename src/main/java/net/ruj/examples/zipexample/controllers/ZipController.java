package net.ruj.examples.zipexample.controllers;

import net.lingala.zip4j.exception.ZipException;
import net.ruj.examples.zipexample.models.VirtualFile;
import net.ruj.examples.zipexample.services.ZipService;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ZipController {
    private final ZipService zipService;

    public ZipController(ZipService zipService) {
        this.zipService = zipService;
    }

    public void start(FileOutputStream destination) throws IOException, ZipException {
        String directory = askDirectory();

        File parentFolder = Paths.get(directory).toFile();
        File[] childFiles = parentFolder.listFiles();
        assert childFiles != null;

        ArrayList<VirtualFile> virtualFiles = new ArrayList<>();
        addInFolderFiles(childFiles, virtualFiles);
        addInMemoryFile(virtualFiles);

        this.zipService.zip(virtualFiles, destination);
    }

    private String askDirectory() throws IOException {
        System.out.println("Enter directory name: ");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        return bufferRead.readLine();
    }

    private void addInFolderFiles(File[] childFiles, ArrayList<VirtualFile> virtualFiles) throws FileNotFoundException {
        for (File childFile : childFiles)
            virtualFiles.add(
                    new VirtualFile(
                            new FileInputStream(childFile),
                            childFile.getName()
                    )
            );
    }

    private void addInMemoryFile(ArrayList<VirtualFile> virtualFiles) {
        VirtualFile inMemoryFile = new VirtualFile(
                new ByteArrayInputStream("In memory text file".getBytes()),
                "additionalFile"
        );
        virtualFiles.add(inMemoryFile);
    }
}
