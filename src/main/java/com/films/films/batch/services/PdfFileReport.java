package com.films.films.batch.services;

import com.films.films.batch.models.Film;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
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
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        try {
            if (!Files.exists(filePath)) {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath.toString()));
                document.open();
                for(Film film : films) {
                    log.info("Film to be written: {}", film);
                    Paragraph paragraph = new Paragraph(film.toString(), font);
                    document.add(paragraph);
                }
                document.close();
            } else {
                PdfReader reader = new PdfReader(filePath.toString());
                PdfStamper pdfStamper = new PdfStamper(reader, new FileOutputStream(filePath.toString()));

                PdfContentByte overContent = pdfStamper.getOverContent(1);
                ColumnText columnText = new ColumnText(overContent);

                for (Film film : films) {
                    log.info("Film to be written: {}", film);
                    Paragraph paragraph = new Paragraph(film.toString(), font);
                    columnText.addText(paragraph);
                }

                pdfStamper.close();
                reader.close();
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        }
    }
}
