package uoc.ds.pr;


import uoc.ds.pr.exceptions.DSException;
import uoc.ds.pr.pr2.SystemIssues;
import uoc.ds.pr.pr2.SystemIssuesPR2Impl;
import uoc.ds.pr.pr3.SystemIssuesPR3;
import uoc.ds.pr.pr3.SystemIssuesPR3Impl;

public class FactorySystemIssues {

    public static SystemIssuesPR3 getComputerProjects() throws DSException {
        SystemIssuesPR3 systemIssues;
        systemIssues = new SystemIssuesPR3Impl();


        return systemIssues;
    }



}