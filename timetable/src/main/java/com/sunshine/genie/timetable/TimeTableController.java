package com.sunshine.genie.timetable;

import com.sunshine.genie.timetable.scheduler.Chromosome;
import com.sunshine.genie.timetable.spreadsheet.SpreadSheetParser;
import com.sunshine.genie.timetable.scheduler.SchedulerMain;
import com.sunshine.genie.timetable.scheduler.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api")
public class TimeTableController {

    @RequestMapping(value = "/testTimeTable", method = RequestMethod.GET)
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        new Model().takeinput();

        //invokes algorithm
        new SchedulerMain();

        //grabs final chromosome i.e. the output
        Chromosome finalson = SchedulerMain.finalson;

        return "Hello";
    }
    private String fileLocation;


    @RequestMapping(value = "/testTimeTable", method = RequestMethod.POST)
    public String uploadFile(org.springframework.ui.Model model, MultipartFile file) throws IOException {

        InputStream in = file.getInputStream();
        File currDir = new File(".");
        String path = currDir.getAbsolutePath();
        fileLocation = path.substring(0, path.length() - 1) + file.getOriginalFilename();
        FileOutputStream f = new FileOutputStream(fileLocation);
        int ch = 0;
        while ((ch = in.read()) != -1) {
            f.write(ch);
        }
        f.flush();
        f.close();
        model.addAttribute("message", "File: " + file.getOriginalFilename()
                + " has been uploaded successfully!");


        new SpreadSheetParser(fileLocation).populateGenome();
        //invokes algorithm
        new SchedulerMain();

        //grabs final chromosome i.e. the output
        Chromosome finalson = SchedulerMain.finalson;

        return finalson.getTimeTable();
    }
}
