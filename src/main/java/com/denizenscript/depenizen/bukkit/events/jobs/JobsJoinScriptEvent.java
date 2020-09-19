package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.api.JobsJoinEvent;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsJoinScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player joins job
    // jobs player joins <job>
    //
    // @Regex ^on jobs player joins [^\s]+$
    //
    // @Cancellable true
    //
    // @Switch in:<area> to only process the event if it occurred within a specified area.
    //
    // @Triggers when a player joins a Jobs job.
    //
    // @Context
    // <context.job> returns the JobsJobTag that the player joined.
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsJoinScriptEvent() {
        instance = this;
    }

    public static JobsJoinScriptEvent instance;
    public JobsJoinEvent event;
    public PlayerTag player;
    public Job job;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return (path.eventLower.startsWith("jobs player joins") && path.eventArgsLower.length > 3);
    }

    @Override
    public boolean matches(ScriptPath path) {
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
        return "JobsJoinEvent";
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
    public void onJobsPlayerJoinsJob(JobsJoinEvent event) {
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer().getPlayer());
        this.event = event;
        this.job = event.getJob();
        fire(event);
    }
}
