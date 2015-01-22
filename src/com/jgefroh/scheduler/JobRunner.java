package com.jgefroh.scheduler;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Scheduler that looks up jobs and runs them if it is time to run them.
 * 
 * Pros: Avoid duplicate schedules, transaction can ensure jobs only run once per scheduled unit of time
 * Cons: Indirection to actually run jobs, possible to make an omniscient class that knows about all jobs in system.
 *       Doesn't support run at specific time.
 * @author Joseph Gefroh
 *
 */
public class JobRunner {

   //@Schedule (run every X units)
    public void checkForJobsToRun() {
        List<Job> jobs = getJobs();
        
        for (Job job : jobs) {
            try {
                //Transaction boundary BEGIN
                if (shouldRun(job)) {
                    run(job);
                    logSuccess(job);
                }
                //Transaction boundary END
            }
            catch (Exception e) {
                //log job failure
                logFailure(job);
            }
        }
    }
    
    private List<Job> getJobs() {
        //DAO call to get jobs...
        return Collections.emptyList();
    }
    
    private boolean shouldRun(final Job job) {
        return isScheduledToRun(job) && isAllowedToRun(job);
    }
    
    protected boolean isScheduledToRun(final Job job) {
        //Exposed so it can be tested.
        if (SchedulingUnit.DISABLED.equals(job.getSchedulingUnit())) {
            return false;
        }

        Date lastRun = job.getLastRun();
        if (lastRun == null) {
            return true;
        }
        
        Date earliestDateToRun = getEarliestDateToRun(job);
        if (earliestDateToRun == null) {
            return false;
        }
        
        return earliestDateToRun.before(getCurrentDate());
    }

    protected Date getCurrentDate() {
        //[JG]: Exposed so it can be tested.
        return new Date();
    }
    
    private Date getEarliestDateToRun(final Job job) {
        SchedulingUnit schedulingUnit = job.getSchedulingUnit();
        Calendar cal = Calendar.getInstance();
        cal.setTime(job.getLastRun());
        int frequency = job.getFrequency();
        switch (schedulingUnit) {
            case DAY:
                cal.add(Calendar.DATE, frequency);
                break;
            case DISABLED:
                return null;
            case MONTH:
                cal.add(Calendar.MONTH, frequency);
                break;
            case WEEK:
                cal.add(Calendar.WEEK_OF_YEAR, frequency);
                break;
            default:
                return null;
        }
        return cal.getTime();
    }
    
    private boolean isAllowedToRun(final Job job) {
        //add some sort of time range whitelist/blacklist for the job?
            //Possibly move this to the DAO - only get jobs that can be run at the current time
        return true;
    }
    
    private void run(final Job job) {
        //Find job logic
        //Run job logic
        System.out.println("Running job " + job.getName());
    }
    
    private void logSuccess(final Job job) {
        job.setLastRun(new Date());
    }
    
    private void logFailure(final Job job) {
        System.out.println("Job " + job.getId() + " failed.");
    }
}
