package com.sunshine.genie.timetable.scheduler;

public class Subject {
	int id;
	String name;
	Teacher[] teacher;
	int noteachers;
	Subject(){
		teacher=new Teacher[20];
	}
}
