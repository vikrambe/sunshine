package com.sunshine.genie.timetable.spreadsheet;

import com.sunshine.genie.timetable.scheduler.StudentGroup;
import com.sunshine.genie.timetable.scheduler.Teacher;
import com.sunshine.genie.timetable.scheduler.Model;
import org.apache.poi.ss.usermodel.*;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;

public class SpreadSheetParser extends Model {

    private Workbook workbook;

    private enum SHEET {
        BasicDetails,
        StudentGroups,
        Teachers;
    }

    public SpreadSheetParser(String fileLocation) throws IOException {
        workbook = WorkbookFactory.create(new File(fileLocation));
    }

    public void populateGenome() {
        Iterator<Sheet> sheetIterator = workbook.sheetIterator();
        DataFormatter dataFormatter = new DataFormatter();

        while (sheetIterator.hasNext()) {
            Sheet sheet = sheetIterator.next();
            if (SHEET.BasicDetails.name().equalsIgnoreCase(sheet.getSheetName())) processBasicDetails(dataFormatter, sheet);
            if (SHEET.StudentGroups.name().equalsIgnoreCase(sheet.getSheetName())) processStudentGroups(dataFormatter, sheet);
            if (SHEET.Teachers.name().equalsIgnoreCase(sheet.getSheetName())) processTeachers(dataFormatter, sheet);
        }
        assignTeacher();
    }

    private void processTeachers(DataFormatter dataFormatter, Sheet sheet) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        int i = 0;
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Now let's iterate over the columns of the current row
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = dataFormatter.formatCellValue(cell);
                if (cellValue.equalsIgnoreCase("Name")) break;
                teacher[i] = new Teacher();
                teacher[i].setId(i);
                teacher[i].setName(cellValue);
                teacher[i].setSubject(dataFormatter.formatCellValue(cellIterator.next()));
                i++;
                noteacher = i;
            }
        }
        System.out.println("=> " + sheet.getSheetName());
    }

    private void processStudentGroups(DataFormatter dataFormatter, Sheet sheet){
        int i = 0; int j=0;
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Now let's iterate over the columns of the current row
            Iterator<Cell> cellIterator = row.cellIterator();
            if(row.getLastCellNum() < 3) break;

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = dataFormatter.formatCellValue(cell);
                if (cellValue.equalsIgnoreCase("ClassName")) break;
                boolean isFirstCellNonEmpty = !StringUtils.isEmpty(dataFormatter.formatCellValue(row.getCell(0)));
                if (isFirstCellNonEmpty) {
                    if(Objects.nonNull(studentgroup[i])) i++;
                    studentgroup[i] = new StudentGroup();
                    studentgroup[i].id = i;
                    studentgroup[i].name = cellValue;
                    studentgroup[i].nosubject = 0;
                    j = 0;
                }
                studentgroup[i].subject[j] = isFirstCellNonEmpty ? dataFormatter.formatCellValue(cellIterator.next()) : cellValue;
                studentgroup[i].hours[j++] = Integer.parseInt(dataFormatter.formatCellValue(cellIterator.next()));
                studentgroup[i].nosubject++;
            }
        }
        nostudentgroup = i;
    }

    private void processBasicDetails(DataFormatter dataFormatter, Sheet sheet) {
        Iterator<Row> rowIterator = sheet.rowIterator();
        while (rowIterator.hasNext()) {
            Row row = rowIterator.next();

            // Now let's iterate over the columns of the current row
            Iterator<Cell> cellIterator = row.cellIterator();

            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                String cellValue = dataFormatter.formatCellValue(cell);
                if (cellValue.equalsIgnoreCase("Hoursperday")) {
                    hoursperday = Integer.parseInt(dataFormatter.formatCellValue(cellIterator.next()));
                }
                if (cellValue.equalsIgnoreCase("Daysperweek")) {
                    daysperweek = Integer.parseInt(dataFormatter.formatCellValue(cellIterator.next()));
                }

                System.out.print(cellValue + "\t");
            }
        }
        System.out.println("=> " + sheet.getSheetName());
    }

    // assigning a teacher for each subject for every studentgroup
    public void assignTeacher() {

        // looping through every studentgroup
        for (int i = 0; i < nostudentgroup; i++) {

            // looping through every subject of a student group
            for (int j = 0; j < studentgroup[i].nosubject; j++) {

                int teacherid = -1;
                int assignedmin = -1;

                String subject = studentgroup[i].subject[j];

                // looping through every teacher to find which teacher teaches the current subject
                for (int k = 0; k < noteacher; k++) {

                    // if such teacher found,checking if he should be assigned the subject or some other teacher based on prior assignments
                    if (teacher[k].getSubject().equalsIgnoreCase(subject)) {

                        // if first teacher found for this subject
                        if (assignedmin == -1) {
                            assignedmin = teacher[k].assigned;
                            teacherid = k;
                        }

                        // if teacher found has less no of pre assignments than the teacher assigned for this subject
                        else if (assignedmin > teacher[k].assigned) {
                            assignedmin = teacher[k].assigned;
                            teacherid = k;
                        }
                    }
                }

                // 'assigned' variable for selected teacher incremented
                teacher[teacherid].assigned++;

                studentgroup[i].teacherid[j]= teacherid;
            }
        }
    }
}
