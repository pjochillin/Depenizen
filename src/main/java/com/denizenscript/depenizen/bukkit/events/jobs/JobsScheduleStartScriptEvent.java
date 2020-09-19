package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.ListTag;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.api.JobsScheduleStartEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

public class JobsScheduleStartScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs schedule starts
    //
    // @Regex ^on jobs schedule starts$
    //
    // @Cancellable true
    //
    // @Triggers when a Jobs schedule starts.
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
    // @Group Depenizen
    //
    // -->

    public JobsScheduleStartScriptEvent() {
        instance = this;
    }

    public static JobsScheduleStartScriptEvent instance;
    public JobsScheduleStartEvent event;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return (path.eventLower.startsWith("jobs schedule starts"));
    }

    @Override
    public String getName() {
        return "JobsScheduleStartEvent";
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (Argument.valueOf(determinationObj.toString().toLowerCase()).matchesArgumentList(JobsJobTag.class)) {
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
    public void onJobsScheduleStarts(JobsScheduleStartEvent event) {
        this.event = event;
        fire(event);
    }
}
