import org.omg.CORBA.CODESET_INCOMPATIBLE;

import java.util.*;

public class JobAccess implements Observer {

    Date today;
    //private ArrayList<JobPosting> jobPostings = new ArrayList<>();
    private HashMap<Company, ArrayList<JobPosting>> jobPostings = new HashMap<>();
    //private ArrayList<JobPosting> closedJobs = new ArrayList<>();
    private HashMap<Company, ArrayList<JobPosting>> closedJobs = new HashMap<>();

//    @Override
//    public void update(Observable o, Object arg) {
//        String expiredPositions = "";
//        for (JobPosting jobPosting : jobPostings) {
//            if (jobPosting.getDateClosed().before(today)) {
//                expiredPositions = expiredPositions + jobPosting.getJob().getPosition() + ",";
//            }
//        }
//        if (!expiredPositions.equals("")) {
//            String strJobs = expiredPositions.substring(0, expiredPositions.length() - 1);
//            String[] strListJobs = strJobs.split(",");
//            for (String str : strListJobs) {
//                this.removeJobPosting(str);
//            }
//        }
//    }

    @Override
    public void update(Observable o, Object arg) {
        String expiredPositions = "";
        //I need to iterate through each company, and then iterate through the list of their jobPostings
        for (Company company : this.jobPostings.keySet()){
            for (JobPosting jP : this.jobPostings.get(company)){
                if (jP.getDateClosed().before(today)){
                    expiredPositions = expiredPositions + jP.getJob().getPosition() + ",";
                }
            }
            if (!expiredPositions.equals("")) {
                String strJobs = expiredPositions.substring(0, expiredPositions.length() - 1);
                String[] strListJobs = strJobs.split(",");
                for (String str : strListJobs){
                    //TODO: update
                    this.removeJobPosting(str, company);
                }
            }
        }
    }

    void retrieveTime(Date date) {
        today = date;
    }

    void addJobPosting(Job job, Date dateClosed, ArrayList<Interviewer> chosenInterviewers, int numHires) {
        JobPosting jobPosting = new JobPosting(job, today, dateClosed, chosenInterviewers, numHires);
        jobPosting.setCompany(jobPosting.getJob().getCompany());
        boolean added = false;
        if (!this.companyCheck(jobPosting.getCompany().getCompanyName(), this.jobPostings)) { //if the company does not exist in map
            if (this.getJobPosting(job.getPosition(), job.getCompany()) == null && validInput(job.getPosition())) {
                ArrayList<JobPosting> newList = new ArrayList<>();
                newList.add(jobPosting);
                this.jobPostings.put(jobPosting.getCompany(), newList);
                added = true;
            }
        } else {
            if (this.getJobPosting(job.getPosition(), job.getCompany()) == null && validInput(job.getPosition())) {
                this.jobPostings.get(jobPosting.getCompany()).add(jobPosting);
            }
        }

        String str = "";
        for (Company company : this.jobPostings.keySet()){
            str = str + company.getCompanyName() + ", ";
        }
        System.out.println(str);
        String test = "";
        for (JobPosting jP : this.jobPostings.get(jobPosting.getCompany())){
            test = test + jP.getJob().getPosition() + ", ";
        }
        System.out.println(test);
        System.out.println("Job created:" + added);
    }

    JobPosting getJobPosting(String jobTitle, Company company) {
        JobPosting result = null;
//        for (int i = 0; i < jobPostings.size(); i++) {
//            if (jobPostings.get(i).getJob().getPosition().equals(jobTitle)) {
//                result = jobPostings.get(i);
//            }
//        }

        for (JobPosting jP : this.jobPostings.get(company)){
            if (jP.getJob().getPosition().equals(jobTitle)){
                result = jP;
            }
        }
        return result;
    }

    JobPosting getClosedJob(String jobTitle, Company company) {
        JobPosting result = null;
//        for (int i = 0; i < closedJobs.size(); i++) {
//            if (closedJobs.get(i).getJob().getPosition().equals(jobTitle)) {
//                result = closedJobs.get(i);
//            }
        for (JobPosting closedJP : this.jobPostings.get(company)){
            if(closedJP.getJob().getPosition().equals(jobTitle)){
                result = closedJP;
            }
        }

        return result;
    }

    private boolean removeJobPosting(String position, Company company) {
        boolean remove = false;
        if (this.getJobPosting(position, company) != null) {
              this.closedJobs.get(company).add(this.getJobPosting(position, company));
              this.jobPostings.get(company).remove(this.getJobPosting(position, company));
              this.getClosedJob(position, company).startInterviewProcess();
//            this.closedJobs.add(this.getJobPosting(position));
//            this.jobPostings.remove(this.getJobPosting(position));
//            this.getClosedJob(position).startInterviewProcess();
            remove = true;
        }
        return remove;
    }

//    ArrayList<String> sort(String s) {
//        ArrayList<String> al = new ArrayList<>();
//        for (JobPosting j : this.jobPostings) {
//            if (s.equals("allJobs")) {
//                al.add(j.getJob().getPosition());
//            } else if (j.getJob().getTag().equals(s)) {
//                al.add(j.getJob().getPosition());
//            }
//        }
//        return al;
//    }

    ArrayList<String>  sort(String s){
        ArrayList<String> al = new ArrayList<>();
        for (Company company : this.jobPostings.keySet()){
            for (JobPosting j : this.jobPostings.get(company)){
                if (s.equals("allJobs")) {
                    al.add(j.getJob().getPosition() + "," + j.getJob().getCompany());
                } else if (j.getJob().getTag().equals(s)){
                    al.add(j.getJob().getPosition() + "," + j.getJob().getCompany());
                }
            }
        }

        return al;
    }

    private boolean validInput(String input) {
        return input.matches(".*[\\S]+.*");
    }

    private boolean companyCheck(String companyName, HashMap map){
        boolean exists = false;
        for (Object object: map.keySet()){
            if (((Company) object).getCompanyName().equals(companyName)){
                exists = true;
            }
        }
        return exists;
    }

    ArrayList<JobPosting> viewClosedJobs(Company company) {
        //return closedJobs;
        return this.closedJobs.get(company);
    }
}