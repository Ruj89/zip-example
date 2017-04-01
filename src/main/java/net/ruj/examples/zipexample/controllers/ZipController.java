package net.ruj.examples.zipexample.controllers;

import net.lingala.zip4j.exception.ZipException;
import net.ruj.examples.zipexample.models.VirtualFile;
import net.ruj.examples.zipexample.services.ZipService;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class ZipController {
    private final ZipService zipService;

    public ZipController(ZipService zipService) {
        this.zipService = zipService;
    }

    public void start(FileOutputStream destination) throws IOException, ZipException {
        String directory = askDirectory();
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream("In memory text file".getBytes())) {

            File parentFolder = Paths.get(directory).toFile();
            File[] childFiles = parentFolder.listFiles();
            assert childFiles != null;

            ArrayList<VirtualFile> virtualFiles = new ArrayList<>();
            addInFolderFiles(childFiles, virtualFiles);
            addInMemoryFile(virtualFiles, inputStream);

            this.zipService.zip(virtualFiles, destination);

            virtualFiles.forEach(vf -> {
                try {
                    vf.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    private String askDirectory() throws IOException {
        System.out.println("Enter directory name: ");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        return bufferRead.readLine();
    }

    private void addInFolderFiles(File[] childFiles, ArrayList<VirtualFile> appendList) throws FileNotFoundException {
        appendList.addAll(
                Arrays.stream(childFiles)
                        .filter(File::isFile)
                        .map(this::fileToVirtualFile)
                        .filter(Objects::nonNull)
                        .collect(Collectors.toList())
        );
    }

    private VirtualFile fileToVirtualFile(File cf) {
        VirtualFile virtualFile = null;
        try {
            virtualFile = new VirtualFile(
                    new FileInputStream(cf),
                    cf.getName()
            );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return virtualFile;
    }

    private void addInMemoryFile(ArrayList<VirtualFile> virtualFiles, ByteArrayInputStream inputStream) {
        VirtualFile inMemoryFile = new VirtualFile(
                inputStream,
                "additionalFile"
        );
        virtualFiles.add(inMemoryFile);
    }
}
