package com.climpy.modules.discord.events;


import com.climpy.modules.discord.CommandHandler;
import com.climpy.modules.discord.CommandParser;
import net.dv8tion.jda.core.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;


public class MessageEvent extends ListenerAdapter {
  public void onGuildMessageReceived(GuildMessageReceivedEvent event) {

    String[] args = event.getMessage().getContentRaw().split(" ");
    
    if (event.getMessage().getContentRaw().toLowerCase().startsWith("!")) {
      String command = event.getMessage().getContentStripped().replace("!", "").split(" ")[0];
      String[] commandArg = event.getMessage().getContentRaw().replace("!", "").replace(command, "").split(" ");
      CommandParser commandEvent = new CommandParser(event.getMember(), event.getChannel(), event.getMessage(), command, commandArg);
      
      CommandHandler commandHandler = new CommandHandler();
      commandHandler.setupChannelCommand(commandEvent);
    } 
  }
}
