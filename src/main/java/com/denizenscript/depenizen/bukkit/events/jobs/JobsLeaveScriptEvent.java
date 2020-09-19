package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.events.ScriptEvent;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.api.JobsLeaveEvent;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsLeaveScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player leaves job
    // jobs player leaves <job>
    //
    // @Regex ^on jobs player leaves [^\s]+$
    //
    // @Cancellable true
    //
    // @Triggers when a player leaves a Jobs job.
    //
    // @Context
    // <context.job> returns the JobsJobTag that the player left.
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsLeaveScriptEvent() {
        instance = this;
    }

    public static JobsLeaveScriptEvent instance;
    public JobsLeaveEvent event;
    public PlayerTag player;
    public Job job;

    @Override
    public boolean couldMatch(ScriptEvent.ScriptPath path) {
        return (path.eventLower.startsWith("jobs player leaves") && path.eventArgsLower.length > 3);
    }

    @Override
    public boolean matches(ScriptEvent.ScriptPath path) {
        String eJob = path.eventArgLowerAt(3);
        if (!eJob.equals("job") && !runGenericCheck(eJob, new JobsJobTag(job).getJob().getName())) {
            return false;
        }

        if (!runInCheck(path, player.getLocation())) {
            return false;
        }

        return super.matches(path);
    }

    @Override
    public String getName() {
        return "JobsLeaveEvent";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("job")) {
            return new JobsJobTag(job);
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onJobsPlayerLeavesJob(JobsLeaveEvent event) {
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer().getPlayer());
        this.event = event;
        this.job = event.getJob();
        fire(event);
    }
}
