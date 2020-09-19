package com.denizenscript.depenizen.bukkit.events.jobs;

import com.denizenscript.denizen.events.BukkitScriptEvent;
import com.denizenscript.denizen.objects.PlayerTag;
import com.denizenscript.denizen.utilities.implementation.BukkitScriptEntryData;
import com.denizenscript.denizencore.objects.ObjectTag;
import com.denizenscript.denizencore.objects.core.ElementTag;
import com.denizenscript.denizencore.objects.core.MapTag;
import com.denizenscript.denizencore.scripts.ScriptEntryData;
import com.denizenscript.depenizen.bukkit.objects.jobs.JobsJobTag;
import com.gamingmesh.jobs.Jobs;
import com.gamingmesh.jobs.api.JobsJoinEvent;
import com.gamingmesh.jobs.api.JobsLevelUpEvent;
import com.gamingmesh.jobs.api.JobsPaymentEvent;
import com.gamingmesh.jobs.container.CurrencyType;
import com.gamingmesh.jobs.container.Job;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class JobsPaymentScriptEvent extends BukkitScriptEvent implements Listener {

    // <--[event]
    // @Events
    // jobs player receives payment
    //
    // @Regex ^on jobs player receives payment$
    //
    // @Cancellable true
    //
    // @Switch in:<area> to only process the event if it occurred within a specified area.
    //
    // @Triggers when a player receives payment from a Jobs job.
    //
    // @Context
    // <context.money> returns an ElementTag of the amount of money received.
    // <context.experience> returns an ElementTag of the amount of experience received.
    // <context.points> returns an ElementTag of the amount of points received.
    //
    // @Plugin Depenizen, Jobs
    //
    // @Player Always.
    //
    // @Group Depenizen
    //
    // -->

    public JobsPaymentScriptEvent() {
        instance = this;
    }

    public static JobsPaymentScriptEvent instance;
    public JobsPaymentEvent event;
    public PlayerTag player;

    @Override
    public boolean couldMatch(ScriptPath path) {
        return (path.eventLower.startsWith("jobs player receives payment"));
    }

    @Override
    public boolean matches(ScriptPath path) {
        if (!runInCheck(path, player.getLocation())) {
            return false;
        }

        return super.matches(path);
    }

    @Override
    public String getName() {
        return "JobsPaymentEvent";
    }

    @Override
    public ScriptEntryData getScriptEntryData() {
        return new BukkitScriptEntryData(player, null);
    }

    @Override
    public ObjectTag getContext(String name) {
        if (name.equals("money")) {
            return new ElementTag(event.get(CurrencyType.MONEY));
        }
        else if (name.equals("experience")) {
            return new ElementTag(event.get(CurrencyType.EXP));
        }
        else if (name.equals("points")) {
            return new ElementTag(event.get(CurrencyType.POINTS));
        }
        return super.getContext(name);
    }

    @EventHandler
    public void onJobsPlayerReceivesPayment(JobsPaymentEvent event) {
        player = PlayerTag.mirrorBukkitPlayer(event.getPlayer().getPlayer());
        this.event = event;
        fire(event);
    }

}
