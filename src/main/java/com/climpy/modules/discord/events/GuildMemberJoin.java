package com.climpy.modules.discord.events;

import net.dv8tion.jda.core.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

public class GuildMemberJoin extends ListenerAdapter {

    public void onGuildMemberJoin(GuildMemberJoinEvent event) {

        // Rol ekler
        //event.getGuild().getController().addRolesToMember(event.getMember(), event.getGuild().getRolesByName("\uD83D\uDC51 - Sunucu Sahibi\n", true)).complete();
    }
}
