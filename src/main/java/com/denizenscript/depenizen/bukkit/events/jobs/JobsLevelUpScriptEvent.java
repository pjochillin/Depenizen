package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.Argument;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.api.JobsLevelUpEvent;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsLevelUpScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player levels up job
    // jobs player levels up <job>
    //
    // @Regex ^on jobs player levels up [^\s]+$
    //
    // @Cancellable true
    //
    // @Switch in:<area> to only process the event if it occurred within a specified area.
    //
    // @Triggers when a player levels up a Jobs job.
    //
    // @Context
    // <context.job> returns the JobsJobTag that the player leveled up.
    // <context.level> returns an ElementTag of the job's new level.
    // <context.sound> returns an ElementTag of the sound's name.
    // Refer to <@link url https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Sound.html> for sounds.
    // <context.volume> returns an ElementTag of the sound's volume.
    // <context.pitch> returns an ElementTag of the sound's pitch.
    //
    // @Determine
    // "SOUND:" + ElementTag to set the sound that plays.
    // "VOLUME:" + ElementTag(Number) to set the sound's volume.
    // "PITCH:" + ElementTag(Number) to set the sound's pitch.
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsLevelUpScriptEvent() {
        instance = this;
    }

    public static JobsLevelUpScriptEvent instance;
    public JobsLevelUpEvent event;
    public PlayerTag player;
    public Job job;
    public ElementTag level;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return (path.eventLower.startsWith("jobs player levels up") && path.eventArgsLower.length > 4);
    }

    @Override
    public boolean matches(ScriptPath path) {
        String eJob = path.eventArgLowerAt(4);
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
        return "JobsLevelUpEvent";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public boolean applyDetermination(ScriptPath path, ObjectTag determinationObj) {
        if (determinationObj instanceof ElementTag) {
            String lower = ((ElementTag) determinationObj).asString().toLowerCase();
            if (lower.startsWith("sound:")) {
                Sound sound = Sound.valueOf(lower.substring("sound:".length()).toUpperCase());
                event.setSound(sound);
                return true;
            }
            else if (lower.startsWith("volume:")) {
                int volume = Argument.valueOf(lower.substring("volume:".length())).asElement().asInt();
                event.setSoundVolume(volume);
                return true;
            }
            else if (lower.startsWith("pitch:")) {
                int pitch = Argument.valueOf(lower.substring("pitch:".length())).asElement().asInt();
                event.setSoundPitch(pitch);
                return true;
            }
        }
        return super.applyDetermination(path, determinationObj);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("job")) {
            return new JobsJobTag(job);
        }
        else if (name.equals("level")) {
            return level;
        }
        else if (name.equals("sound")) {
            return new ElementTag(event.getSound().toString());
        }
        else if (name.equals("volume")) {
            return new ElementTag(event.getSoundVolume());
        }
        else if (name.equals("pitch")) {
            return new ElementTag(event.getSoundPitch());
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onJobsPlayerLevelsUpJob(JobsLevelUpEvent event) {
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer().getPlayer());
        this.event = event;
        this.job = Jobs.getJob(event.getJobName());
        this.level = new ElementTag(event.getLevel());
        fire(event);
    }
}
