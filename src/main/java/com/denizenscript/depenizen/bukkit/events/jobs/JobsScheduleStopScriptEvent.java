package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.api.JobsScheduleStopEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class JobsScheduleStopScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs schedule stops
    //
    // @Regex ^on jobs schedule stops$
    //
    // @Cancellable true
    //
    // @Triggers when a Jobs schedule stops.
    //
    // @Context
    // <context.jobs> returns a ListTag of the job(s) that the schedule affects.
    // <context.name> returns an ElementTag of the schedule's name.
    // <context.experience> returns an ElementTag of the schedule's experience multiplier.
    // <context.money> returns an ElementTag of the schedule's money multiplier.
    //
    // @Determine
    // ListTag(JobsJobTag) to set the job(s) that the schedule affects.
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsScheduleStopScriptEvent() {
        instance = this;
    }

    public static JobsScheduleStopScriptEvent instance;
    public JobsScheduleStopEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return (path.eventLower.startsWith("jobs schedule stops"));
    }

    @Override
    public String getName() {
        return "JobsScheduleStopEvent";
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (determinationObj instanceof ListTag) {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < ((ListTag) determinationObj).size(); i++) {
                list.add(((JobsJobTag) ((ListTag) determinationObj).getObject(i)).getJob().getName());
            }
            event.getSchedule().setJobs(list);
            return true;
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("jobs")) {
            ListTag list = new ListTag();
            for (Job j : event.getSchedule().getJobs()) {
                list.addObject(new JobsJobTag(j));
            }
            return list;
        }
        else if (name.equals("name")) {
            return new ElementTag(event.getSchedule().getName());
        }
        else if (name.equals("experience")) {
            return new ElementTag(event.getSchedule().getBoost(CurrencyType.EXP));
        }
        else if (name.equals("money")) {
            return new ElementTag(event.getSchedule().getBoost(CurrencyType.MONEY));
        }

        return super.getContext(name);
    }

    @EventHandler
    public void onJobsScheduleStops(JobsScheduleStopEvent event) {
        this.event = event;
        fire(event);
    }
}
