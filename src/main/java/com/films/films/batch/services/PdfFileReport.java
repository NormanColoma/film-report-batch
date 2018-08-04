package com.films.films.batch.services;

import com.films.films.batch.models.Film;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;

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

                Table table = createTable();
                addFilmsAsRowsToTable(table);

                document.add(table);
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

    private void addFilmsAsRowsToTable(Table table) {
        existingFilms.forEach(film -> {
            table.addCell(film.getId());
            table.addCell(film.getName());
            table.addCell(film.getImpressions().toString());
            table.addCell(film.getDownloads().toString());
        });
    }

    private Table createTable() {
        Integer TABLE_COLUMNS = 4;
        Integer ROW_SPAN = 1;
        Integer COL_SPAN = 1;
        String NAME_TABLE_HEADER = "Name";
        String ID_TABLE_HEADER = "Id";
        String IMPRESSIONS_TABLE_HEADER = "Impressions";
        String DOWNLOADS_TABLE_HEADER = "Downloads";

        Table table = new Table(TABLE_COLUMNS);
        Cell idHeader = new Cell(ROW_SPAN,COL_SPAN).add(ID_TABLE_HEADER).setBold();
        Cell nameHeader = new Cell(ROW_SPAN,COL_SPAN).add(NAME_TABLE_HEADER).setBold();
        Cell impressionsHeader = new Cell(ROW_SPAN,COL_SPAN).add(IMPRESSIONS_TABLE_HEADER).setBold();
        Cell downloadsHeader = new Cell(ROW_SPAN,COL_SPAN).add(DOWNLOADS_TABLE_HEADER).setBold();

        addHeaderCells(table, asList(idHeader, nameHeader, impressionsHeader, downloadsHeader));
        return table;
    }

    private void addHeaderCells(Table table, List<Cell> headers) {
        headers.forEach(table::addHeaderCell);
    }

    private PdfDocument createPdf(Path filePath) throws FileNotFoundException {
        return new PdfDocument(new PdfWriter(filePath.toString()));
    }

    private void closePdf(PdfDocument pdfDoc, Document document) {
        document.close();
        pdfDoc.close();
    }

    private Paragraph createHeader() throws IOException {
        return new Paragraph("Films")
                            .setFont(PdfFontFactory.createFont(FontConstants.HELVETICA))
                            .setFontSize(16)
                            .setFontColor(Color.BLACK)
                            .setBold();
    }
}
