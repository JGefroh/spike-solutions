package com.jgefroh.scheduler;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;


public class JobRunnerTest {
    
    private JobRunner getExposedRunner() {
        JobRunner jobRunner = new JobRunner() {
            public boolean didRun(final Job job) {
                return super.isScheduledToRun(job);
            }
        };
        return jobRunner;
    }
    
    private JobRunner getExposedRunner(final Date date) {
        JobRunner jobRunner = new JobRunner() {
            public boolean didRun(final Job job) {
                return super.isScheduledToRun(job);
            }
            
            @Override
            protected Date getCurrentDate() {
                return date;
            }
        };
        return jobRunner;
    }
    
    
    private Date getDate(final int year, final int zeroBasedMonth, final int date, final int hours, final int minutes) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, zeroBasedMonth, date, hours, minutes);
        return cal.getTime();
    }
    
    @Test
    public void jobRunner_whenNeverRunAndScheduled_expectRun() {
        JobRunner runner = getExposedRunner();
        Job job = new Job();
        job.setFrequency(1);
        job.setSchedulingUnit(SchedulingUnit.DAY);
        
        assertEquals(true, runner.isScheduledToRun(job));
    }
    
    @Test
    public void jobRunner_whenNeverRunAndNotScheduled_expectSkip() {
        JobRunner runner = getExposedRunner();
        Job job = new Job();
        job.setFrequency(1);
        job.setSchedulingUnit(SchedulingUnit.DISABLED);
        
        assertEquals(false, runner.isScheduledToRun(job));
    }
    
    @Test
    public void jobRunner_whenPastScheduledForDay_expectRun() {
        JobRunner runner = getExposedRunner(getDate(2015, 1, 22, 3, 0));
        Job job = new Job();
        job.setFrequency(1);
        job.setSchedulingUnit(SchedulingUnit.DAY);
        job.setLastRun(getDate(2015, 1, 20, 3, 0));
        
        assertEquals(true, runner.isScheduledToRun(job));
    }
    
    @Test
    public void jobRunner_whenBeforeScheduledForDay_expectSkip() {
        JobRunner runner = getExposedRunner(getDate(2015, 1, 18, 3, 0));
        Job job = new Job();
        job.setFrequency(1);
        job.setSchedulingUnit(SchedulingUnit.DAY);
        job.setLastRun(getDate(2015, 1, 18, 3, 0));
        
        assertEquals(false, runner.isScheduledToRun(job));
    }
    
    @Test
    public void jobRunner_whenPastScheduledForMonth_expectRun() {
        JobRunner runner = getExposedRunner(getDate(2015, 2, 23, 3, 0));
        Job job = new Job();
        job.setFrequency(1);
        job.setSchedulingUnit(SchedulingUnit.MONTH);
        job.setLastRun(getDate(2015, 1, 22, 3, 0));
        
        assertEquals(true, runner.isScheduledToRun(job));
    }
    
    @Test
    public void jobRunner_whenBeforeScheduledForMonth_expectSkip() {
        JobRunner runner = getExposedRunner(getDate(2015, 1, 19, 3, 0));
        Job job = new Job();
        job.setFrequency(1);
        job.setSchedulingUnit(SchedulingUnit.MONTH);
        job.setLastRun(getDate(2015, 0, 30, 3, 0));
        
        assertEquals(false, runner.isScheduledToRun(job));
    }
    
    @Test
    public void jobRunner_whenPastScheduledForWeek_expectRun() {
        JobRunner runner = getExposedRunner(getDate(2015, 2, 23, 3, 0));
        Job job = new Job();
        job.setFrequency(1);
        job.setSchedulingUnit(SchedulingUnit.WEEK);
        job.setLastRun(getDate(2015, 2, 15, 3, 0));
        
        assertEquals(true, runner.isScheduledToRun(job));
    }
    
    @Test
    public void jobRunner_whenBeforeScheduledForWeek_expectSkip() {
        JobRunner runner = getExposedRunner(getDate(2015, 1, 20, 3, 0));
        Job job = new Job();
        job.setFrequency(1);
        job.setSchedulingUnit(SchedulingUnit.WEEK);
        job.setLastRun(getDate(2015, 1, 24, 3, 0));
        
        assertEquals(false, runner.isScheduledToRun(job));
    }
}
