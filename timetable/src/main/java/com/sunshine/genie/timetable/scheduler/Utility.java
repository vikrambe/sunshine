package com.sunshine.genie.timetable.scheduler;

public class Utility {
	
	public static void printInputData(){
		System.out.println("Nostgrp="+ Model.nostudentgroup+" Noteachers="+ Model.noteacher+" daysperweek="+ Model.daysperweek+" hoursperday="+ Model.hoursperday);
		for(int i = 0; i< Model.nostudentgroup; i++){
			
			System.out.println(Model.studentgroup[i].id+" "+ Model.studentgroup[i].name);
			
			for(int j = 0; j< Model.studentgroup[i].nosubject; j++){
				System.out.println(Model.studentgroup[i].subject[j]+" "+ Model.studentgroup[i].hours[j]+" hrs "+ Model.studentgroup[i].teacherid[j]);
			}
			System.out.println("");
		}
		
		for(int i = 0; i< Model.noteacher; i++){
			System.out.println(Model.teacher[i].id+" "+ Model.teacher[i].name+" "+ Model.teacher[i].subject+" "+ Model.teacher[i].assigned);
		}
	}
	
	
	public static void printSlots(){
		
		int days= Model.daysperweek;
		int hours= Model.hoursperday;
		int nostgrp= Model.nostudentgroup;
		System.out.println("----Slots----");
		for(int i=0;i<days*hours*nostgrp;i++){
			if(TimeTable.slot[i]!=null)
				System.out.println(i+"- "+TimeTable.slot[i].studentgroup.name+" "+TimeTable.slot[i].subject+" "+TimeTable.slot[i].teacherid);
			else
				System.out.println("Free Period");
			if((i+1)%(hours*days)==0) System.out.println("******************************");
		}
		
	}
	
	
	
}
