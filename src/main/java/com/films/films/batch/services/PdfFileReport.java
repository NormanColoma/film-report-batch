package com.films.films.batch.services;

import com.films.films.batch.models.Film;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@ConditionalOnProperty(name="type", havingValue="pdf")
@Slf4j
public class PdfFileReport implements FilmReporter {
    private List<Film> existingFilms = new ArrayList<>();
    @Override
    public void generateReport(Path filePath, List<Film> films) {
        try {
            if (Files.exists(filePath)) {
                existingFilms.addAll(films);

                PdfDocument pdfDocument = createPdf(filePath);
                Document document = new Document(pdfDocument);
                document.add(createHeader());

                addFilmsAsRowsToPdf(document);

                closePdf(pdfDocument, document);
            } else {
                existingFilms.addAll(films);

                PdfDocument pdfDocument = createPdf(filePath);
                Document document = new Document(pdfDocument);
                document.add(createHeader());

                closePdf(pdfDocument, document);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addFilmsAsRowsToPdf(Document document) throws IOException {
        for (Film film : existingFilms) {
            log.info("Film to be written: {}", film);
            document.add(createRow(film));
        }
    }

    private PdfDocument createPdf(Path filePath) throws FileNotFoundException {
        return new PdfDocument(new PdfWriter(filePath.toString()));
    }

    private void closePdf(PdfDocument pdfDoc, Document document) {
        document.close();
        pdfDoc.close();
    }

    private Paragraph createRow(Film film) throws IOException {
        return new Paragraph(film.toString())
                                .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                                .setFontSize(14)
                                .setFontColor(Color.BLACK);
    }

    private Paragraph createHeader() throws IOException {
        return new Paragraph("Films")
                            .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                            .setFontSize(16)
                            .setFontColor(Color.BLACK)
                            .setBold();
    }
}
