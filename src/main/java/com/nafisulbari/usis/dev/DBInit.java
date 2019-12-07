package com.nafisulbari.usis.dev;

import com.nafisulbari.usis.entity.Course;
import com.nafisulbari.usis.service.CourseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class DBInit implements CommandLineRunner {

    private CourseService courseService;

    public DBInit(CourseService courseService) {
        this.courseService = courseService;
    }

    private static void labOnlyCourseSave(String courseCode, String section, String labFaculty, String labRoom,
                                          String labTimeDay, CourseService courseService) {

        Course labOnlyCourse = new Course();
        labOnlyCourse.setCourseCode(courseCode);
        labOnlyCourse.setSection((int) Double.parseDouble(section));
        labOnlyCourse.setFaculty(labFaculty);
        labOnlyCourse.setCourseTitle(labRoom);
        labOnlyCourse.setSeat(0);
        labOnlyCourse.setExamTime("");
        labOnlyCourse.setLab(0);

        labOnlyCourse.setSaturday("");
        labOnlyCourse.setSunday("");
        labOnlyCourse.setMonday("");
        labOnlyCourse.setTuesday("");
        labOnlyCourse.setWednesday("");
        labOnlyCourse.setThursday("");

        if (labTimeDay.substring(12, 15).equals("SAT")) {
            labOnlyCourse.setSaturday(labTimeDay.substring(0, 11).replace('-', ','));
        }

        if (labTimeDay.substring(12, 15).equals("SUN")) {
            labOnlyCourse.setSunday(labTimeDay.substring(0, 11).replace('-', ','));
        }

        if (labTimeDay.substring(12, 15).equals("MON")) {
            labOnlyCourse.setMonday(labTimeDay.substring(0, 11).replace('-', ','));
        }

        if (labTimeDay.substring(12, 15).equals("TUE")) {
            labOnlyCourse.setTuesday(labTimeDay.substring(0, 11).replace('-', ','));
        }

        if (labTimeDay.substring(12, 15).equals("WED")) {
            labOnlyCourse.setWednesday(labTimeDay.substring(0, 11).replace('-', ','));
        }

        if (labTimeDay.substring(12, 15).equals("THU")) {
            labOnlyCourse.setThursday(labTimeDay.substring(0, 11).replace('-', ','));
        }
        //=====Save labonly course in db======================================================================================
        System.out.println("lab only course:" + labOnlyCourse);
        courseService.saveOrUpdateCourse(labOnlyCourse);


    }

    private static void theoryAndLabCourseSave(String courseCode, String section, String theoryFaculty,
                                               String theoryRoom, String theoryDay, String theoryTime,
                                               String labFaculty, String labTimeDay, String labRoom,
                                               CourseService courseService) {
        Course mainCourse = new Course();
        mainCourse.setCourseCode(courseCode);
        mainCourse.setSection((int) Double.parseDouble(section));
        mainCourse.setFaculty(theoryFaculty);
        mainCourse.setCourseTitle(theoryRoom);

        mainCourse.setSeat(0);
        mainCourse.setExamTime("");
        mainCourse.setLab(0);

        mainCourse.setSaturday("");
        mainCourse.setSunday("");
        mainCourse.setMonday("");
        mainCourse.setTuesday("");
        mainCourse.setWednesday("");
        mainCourse.setThursday("");


        if (theoryDay.substring(0, 3).equals("SAT")) {
            mainCourse.setSaturday(theoryTime.substring(0, 5));
        }
        if (theoryDay.substring(4).equals("SAT")) {
            mainCourse.setSaturday(theoryTime.substring(0, 5));
        }
        if (theoryDay.substring(0, 3).equals("SUN")) {
            mainCourse.setSunday(theoryTime.substring(0, 5));
        }
        if (theoryDay.substring(4).equals("SUN")) {
            mainCourse.setSunday(theoryTime.substring(0, 5));
        }
        if (theoryDay.substring(0, 3).equals("MON")) {
            mainCourse.setMonday(theoryTime.substring(0, 5));
        }
        if (theoryDay.substring(4).equals("MON")) {
            mainCourse.setMonday(theoryTime.substring(0, 5));
        }
        if (theoryDay.substring(0, 3).equals("TUE")) {
            mainCourse.setTuesday(theoryTime.substring(0, 5));
        }
        if (theoryDay.substring(4).equals("TUE")) {
            mainCourse.setTuesday(theoryTime.substring(0, 5));
        }
        if (theoryDay.substring(0, 3).equals("WED")) {
            mainCourse.setWednesday(theoryTime.substring(0, 5));
        }
        if (theoryDay.substring(4).equals("WED")) {
            mainCourse.setWednesday(theoryTime.substring(0, 5));
        }
        if (theoryDay.substring(0, 3).equals("THU")) {
            mainCourse.setThursday(theoryTime.substring(0, 5));
        }
        if (theoryDay.substring(4).equals("THU")) {
            mainCourse.setThursday(theoryTime.substring(0, 5));
        }


        //----------theory's lab------------------------
        Course labCourse = new Course();
        labCourse.setCourseCode(courseCode);
        labCourse.setCourseTitle(labRoom);
        labCourse.setFaculty(labFaculty);
        labCourse.setSection((int) Double.parseDouble(section));
        labCourse.setLab(1);
        labCourse.setExamTime("");
        labCourse.setSeat(0);

        labCourse.setSaturday("");
        labCourse.setSunday("");
        labCourse.setMonday("");
        labCourse.setTuesday("");
        labCourse.setWednesday("");
        labCourse.setThursday("");
        if (!labFaculty.equals("")) {


            if (labTimeDay.substring(12, 15).equals("SAT")) {
                labCourse.setSaturday(labTimeDay.substring(0, 11).replace('-', ','));
            }

            if (labTimeDay.substring(12, 15).equals("SUN")) {
                labCourse.setSunday(labTimeDay.substring(0, 11).replace('-', ','));
            }

            if (labTimeDay.substring(12, 15).equals("MON")) {
                labCourse.setMonday(labTimeDay.substring(0, 11).replace('-', ','));
            }

            if (labTimeDay.substring(12, 15).equals("TUE")) {
                labCourse.setTuesday(labTimeDay.substring(0, 11).replace('-', ','));
            }

            if (labTimeDay.substring(12, 15).equals("WED")) {
                labCourse.setWednesday(labTimeDay.substring(0, 11).replace('-', ','));
            }

            if (labTimeDay.substring(12, 15).equals("THU")) {
                labCourse.setThursday(labTimeDay.substring(0, 11).replace('-', ','));
            }


        }
//===============Save theory or theoryOnly course in db ================================================================
        System.out.println("theory: " + mainCourse);
        courseService.saveOrUpdateCourse(mainCourse);

        if (!labFaculty.equals("")) {
//===============Save Lab course in db==================================================================================
            System.out.println("lab of theory: " + labCourse);
            courseService.saveOrUpdateCourse(labCourse);
        }

    }

    @Override
    public void run(String... args) throws Exception {
/*


//COMMENT--------------COMMENT--------------COMMENT--------------COMMENT--------------COMMENT--------------COMMENT--------------COMMENT--------------
//------------RUNS EVERYTIME SERVER RUNS--------------------------------------------

        //  System.out.println(System.getProperty("user.dir"));
        File file = new File("routine.xlsx");   //creating a new file instance
        FileInputStream fis = new FileInputStream(file);   //obtaining bytes from the file
//creating Workbook instance that refers to .xlsx file
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);     //creating a Sheet object to retrieve object
        Iterator<Row> itr = sheet.iterator();    //iterating over excel file
//ignoring first 3 rows
        itr.next();
        itr.next();
        itr.next();
        String courseCode = null;
        String section = null;
        String theoryFaculty = null;
        String theoryTime = null;
        String theoryDay = null;
        String theoryRoom = null;
        String labFaculty = null;
        String labTimeDay = null;
        String labRoom = null;

        while (itr.hasNext()) {
            Row row = itr.next();
            Iterator<Cell> cellIterator = row.cellIterator();   //iterating over each column
            int count = 0;

            while (cellIterator.hasNext()) {

                Cell excelCell = cellIterator.next();
                String cell = excelCell.toString();

                // System.out.print("column: " + excelCell.getColumnIndex());
                // System.out.print("::::" + cell);
                switch (excelCell.getColumnIndex()) {
                    case 3:
                        courseCode = cell;
                    case 4:
                        section = cell;
                    case 5:
                        theoryFaculty = cell;
                    case 6:
                        theoryTime = cell;
                    case 7:
                        theoryDay = cell;
                    case 8:
                        theoryRoom = cell;
                    case 9:
                        labFaculty = cell;
                    case 10:
                        labTimeDay = cell;
                    case 11:
                        labRoom = cell;
                    default:
                }
            }
            if (!labTimeDay.equals("")) {
                int temp = Integer.parseInt(labTimeDay.substring(0, 2)) + 1;
                String lbTime = "" + temp;
                if (temp < 10) {
                    lbTime = "0" + lbTime;
                }
                labTimeDay = labTimeDay.substring(0, 5) + "-" + lbTime + ":30/" + labTimeDay.substring(12, 15);
            }

//-----------------------WORK--------------------------------------
            //lab only course-----------------------------------------------------------------------
            if (theoryDay.equals("")) {

                System.out.println("labOnlyCourse--------------------------------");
                labOnlyCourseSave(courseCode, section, labFaculty, labRoom, labTimeDay, courseService);


            } else {

                //-THEORY DAY length 7-- theory course, can have lab----------------------------------------------------
                if (theoryDay.length() == 7) {
                    System.out.println("theory and lab or just theory--------------------------");
                    theoryAndLabCourseSave(courseCode, section, theoryFaculty, theoryRoom, theoryDay, theoryTime,
                            labFaculty, labTimeDay, labRoom, courseService);

                }
                //-THEORY DAY LENGTH 3---could be just theory on different day, can have lab-------------------
                if (theoryDay.length() == 3) {

                    String courseCode2 = null;
                    String section2 = null;
                    String theoryFaculty2 = null;
                    String theoryTime2 = null;
                    String theoryDay2 = null;
                    String theoryRoom2 = null;
                    String labFaculty2 = null;
                    String labTimeDay2 = null;
                    String labRoom2 = null;

                    Row row2 = itr.next();
                    Iterator<Cell> cellIterator2 = row2.cellIterator();   //iterating over each column

                    while (cellIterator2.hasNext()) {

                        Cell excelCell = cellIterator2.next();
                        String cell = excelCell.toString();

                        switch (excelCell.getColumnIndex()) {
                            case 3:
                                courseCode2 = cell;
                            case 4:
                                section2 = cell;
                            case 5:
                                theoryFaculty2 = cell;
                            case 6:
                                theoryTime2 = cell;
                            case 7:
                                theoryDay2 = cell;

                            case 8:
                                theoryRoom2 = cell;
                            case 9:
                                labFaculty2 = cell;
                            case 10:
                                labTimeDay2 = cell;
                            case 11:
                                labRoom2 = cell;
                            default:
                        }

                    }
                    //-
                    if (!labTimeDay2.equals("")) {
                        int temp = Integer.parseInt(labTimeDay2.substring(0, 2)) + 1;
                        String lbTime = "" + temp;
                        if (temp < 10) {
                            lbTime = "0" + lbTime;
                        }
                        labTimeDay2 = labTimeDay2.substring(0, 5) + "-" + lbTime + ":30/" + labTimeDay2.substring(12, 15);
                    }
                    //----------
                    if (section.equals(section2) && courseCode.equals(courseCode2) && theoryFaculty.equals(theoryFaculty2)) {
                        Course mainCourse = new Course();
                        mainCourse.setCourseCode(courseCode);
                        mainCourse.setSection((int) Double.parseDouble(section));
                        mainCourse.setFaculty(theoryFaculty);

                        if (theoryRoom.equals(theoryRoom2)) {
                            mainCourse.setCourseTitle(theoryRoom);
                        } else {
                            mainCourse.setCourseTitle(theoryRoom + "," + theoryRoom2);
                        }


                        mainCourse.setSeat(0);
                        mainCourse.setExamTime("");
                        mainCourse.setLab(0);

                        mainCourse.setSaturday("");
                        mainCourse.setSunday("");
                        mainCourse.setMonday("");
                        mainCourse.setTuesday("");
                        mainCourse.setWednesday("");
                        mainCourse.setThursday("");


                        if (theoryDay.equals("SAT")) {
                            mainCourse.setSaturday(theoryTime.substring(0, 5));
                        }
                        if (theoryDay.equals("SUN")) {
                            mainCourse.setSunday(theoryTime.substring(0, 5));
                        }
                        if (theoryDay.equals("MON")) {
                            mainCourse.setMonday(theoryTime.substring(0, 5));
                        }
                        if (theoryDay.equals("TUE")) {
                            mainCourse.setTuesday(theoryTime.substring(0, 5));
                        }
                        if (theoryDay.equals("WED")) {
                            mainCourse.setWednesday(theoryTime.substring(0, 5));
                        }
                        if (theoryDay.equals("THU")) {
                            mainCourse.setThursday(theoryTime.substring(0, 5));
                        }
                        if (theoryDay2.equals("SAT")) {
                            if (mainCourse.getSaturday().equals("")) {
                                mainCourse.setSaturday(theoryTime2.substring(0, 5));
                            } else {
                                mainCourse.setSaturday(mainCourse.getSaturday() + "," + theoryTime2.substring(0, 5));
                            }
                        }
                        if (theoryDay2.equals("SUN")) {

                            if (mainCourse.getSunday().equals("")) {
                                mainCourse.setSunday(theoryTime2.substring(0, 5));
                            } else {
                                mainCourse.setSunday(mainCourse.getSunday() + "," + theoryTime2.substring(0, 5));
                            }
                        }
                        if (theoryDay2.equals("MON")) {
                            if (mainCourse.getMonday().equals("")) {
                                mainCourse.setMonday(theoryTime2.substring(0, 5));
                            } else {
                                mainCourse.setMonday(mainCourse.getMonday() + "," + theoryTime2.substring(0, 5));
                            }

                        }
                        if (theoryDay2.equals("TUE")) {
                            if (mainCourse.getTuesday().equals("")) {
                                mainCourse.setTuesday(theoryTime2.substring(0, 5));
                            } else {
                                mainCourse.setTuesday(mainCourse.getTuesday() + "," + theoryTime2.substring(0, 5));
                            }
                        }
                        if (theoryDay2.equals("WED")) {
                            if (mainCourse.getWednesday().equals("")) {
                                mainCourse.setWednesday(theoryTime2.substring(0, 5));
                            } else {
                                mainCourse.setWednesday(mainCourse.getWednesday() + "," + theoryTime2.substring(0, 5));
                            }
                        }
                        if (theoryDay2.equals("THU")) {
                            if (mainCourse.getThursday().equals("")) {
                                mainCourse.setThursday(theoryTime2.substring(0, 5));
                            } else {
                                mainCourse.setThursday(mainCourse.getThursday() + "," + theoryTime2.substring(0, 5));
                            }
                        }
                        //-different day different time course has lab
                        Course labCourse2 = new Course();
                        labCourse2.setCourseCode(courseCode);
                        labCourse2.setSection((int) Double.parseDouble(section));
                        labCourse2.setFaculty(labFaculty2);
                        labCourse2.setCourseTitle(labRoom2);

                        labCourse2.setSeat(0);
                        labCourse2.setExamTime("");
                        labCourse2.setLab(1);

                        labCourse2.setSaturday("");
                        labCourse2.setSunday("");
                        labCourse2.setMonday("");
                        labCourse2.setTuesday("");
                        labCourse2.setWednesday("");
                        labCourse2.setThursday("");
                        if (!labFaculty2.equals("")) {


                            if (labTimeDay2.substring(12, 15).equals("SAT")) {
                                labCourse2.setSaturday(labTimeDay2.substring(0, 11).replace('-', ','));
                            }

                            if (labTimeDay2.substring(12, 15).equals("SUN")) {
                                labCourse2.setSunday(labTimeDay2.substring(0, 11).replace('-', ','));
                            }

                            if (labTimeDay2.substring(12, 15).equals("MON")) {
                                labCourse2.setMonday(labTimeDay2.substring(0, 11).replace('-', ','));
                            }

                            if (labTimeDay2.substring(12, 15).equals("TUE")) {
                                labCourse2.setTuesday(labTimeDay2.substring(0, 11).replace('-', ','));
                            }

                            if (labTimeDay2.substring(12, 15).equals("WED")) {
                                labCourse2.setWednesday(labTimeDay2.substring(0, 11).replace('-', ','));
                            }

                            if (labTimeDay2.substring(12, 15).equals("THU")) {
                                labCourse2.setThursday(labTimeDay2.substring(0, 11).replace('-', ','));
                            }


                        }
//===========Save theory in db=============================================================================
                        System.out.println("=================================================================================================");
                        System.out.println(mainCourse);
                        courseService.saveOrUpdateCourse(mainCourse);

//===========Save lab in db============================================================================
                        if (!labFaculty2.equals("")) {
                            System.out.println("=================================================================================================");
                            System.out.println(labCourse2);
                            courseService.saveOrUpdateCourse(labCourse2);
                        }
                    }
                }

            }//----next ROW------

            System.out.println("");
        }
//COMMENT--------------COMMENT--------------COMMENT--------------COMMENT--------------COMMENT--------------COMMENT--------------COMMENT--------------
*/

    }
}
