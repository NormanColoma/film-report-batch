package com.films.films.batch.services;

import com.films.films.batch.models.Film;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

@Component
@ConditionalOnProperty(name="type", havingValue="pdf")
@Slf4j
public class PdfFileReport implements FilmReporter {
    @Override
    public void generateReport(Path filePath, List<Film> films) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream(filePath.toString()));
            document.open();
            Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);


            for(Film film : films) {
                log.info("Film to be written: {}", film);
                Paragraph paragraph = new Paragraph(film.toString(), font);
                document.add(paragraph);
                /*byte[] strToBytes = (film.toString() + System.lineSeparator()).getBytes();
                log.info("Film to be written: {}", film);
                try {
                    Files.write(filePath, strToBytes, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
                } catch (IOException e) {
                    e.printStackTrace();
                }*/
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        document.close();
    }
}
