package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.api.JobsExpGainEvent;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsExpGainScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player gains experience (from <job>)
    //
    // @Regex ^on jobs player gains experience( from [^\s]+)$
    //
    // @Cancellable true
    //
    // @Switch in:<area> to only process the event if it occurred within a specified area.
    //
    // @Triggers when a player gains experience from a Jobs job.
    //
    // @Context
    // <context.job> returns the JobsJobTag of the job that the player gained experience in.
    // <context.experience> returns the ElementTag of the amount of experience gained.
    //
    // @Determine
    // ElementTag(Decimal) to set the amount of experience gained.
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsExpGainScriptEvent() {
        instance = this;
    }

    public static JobsExpGainScriptEvent instance;
    public JobsExpGainEvent event;
    public PlayerTag player;
    public Job job;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return (path.eventLower.startsWith("jobs player gains experience"));
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (path.eventArgs.length > 4 && path.eventArgLowerAt(4).equals("from")) {
            String eJob = path.eventArgLowerAt(5);
            if (!runGenericCheck(eJob, new JobsJobTag(job).getJob().getName())) {
                return false;
            }
        }

        if (!runInCheck(path, player.getLocation())) {
            return false;
        }

        return super.matches(path);
    }

    @Override
    public String getName() {
        return "JobsExpGainEvent";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (determinationObj instanceof ElementTag) {
            event.setExp(((ElementTag) determinationObj).asDouble());
            return true;
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("job")) {
            return new JobsJobTag(job);
        }
        else if (name.equals("experience")) {
            return new ElementTag(event.getExp());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onJobsPlayerGainsExp(JobsExpGainEvent event) {
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer().getPlayer());
        this.event = event;
        this.job = event.getJob();
        fire(event);
    }
}
