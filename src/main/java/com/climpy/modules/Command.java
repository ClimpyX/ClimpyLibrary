package com.climpy.modules;


import com.climpy.modules.discord.CommandParser;

public abstract class Command
{
  protected String name;
  protected String description;
  protected String[] aliases;
  
  public Command(String name, String description, String[] aliases) {
    this.name = name;
    this.description = description;
    this.aliases = aliases;
  }

  
  public abstract String getUsage();
  
  public abstract void onChannelCommand(CommandParser paramCommandParser);
  
  public boolean equals(Command command) { return (getName() == command.getName()); }


  
  public String getName() { return this.name; }


  
  public String getDescription() { return this.description; }


  
  public String[] getAliases() { return this.aliases; }
}
