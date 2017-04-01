package net.ruj.examples.zipexample.services;

import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.io.ZipOutputStream;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.util.Zip4jConstants;
import net.ruj.examples.zipexample.models.VirtualFile;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ZipService {
    public void zip(List<VirtualFile> virtualFiles, OutputStream outputStream) throws ZipException, IOException {
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(outputStream)) {
            for (VirtualFile virtualFile : virtualFiles) {
                ZipParameters dataFileParameters = new ZipParameters();
                dataFileParameters.setCompressionMethod(Zip4jConstants.COMP_DEFLATE);
                dataFileParameters.setCompressionLevel(Zip4jConstants.DEFLATE_LEVEL_NORMAL);
                dataFileParameters.setFileNameInZip(virtualFile.getFilename());
                dataFileParameters.setSourceExternalStream(true);
                zipOutputStream.putNextEntry(null, dataFileParameters);
                IOUtils.copy(virtualFile.getInputStream(), zipOutputStream);
                zipOutputStream.closeEntry();
            }
            zipOutputStream.finish();
        }
    }
}
