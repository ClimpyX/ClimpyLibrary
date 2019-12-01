package com.climpy.utils;

import org.bukkit.Location;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface UserHelp {
    Map<String, String> globalDataSave();

    Map<String, String> localDataSave();

    boolean isFirstJoin();

    void setFirstJoin(boolean paramBoolean);

    UUID getUniqueUUID();

    String getCurrentServer();

    void setCurrentServer(String paramString);

    String getPlayerName();

    void setPlayerName(String paramString);

    String getIpAddress();

    void setIpAddress(String paramString);


    int getAnnounceSize();

    void setAnnounceSize(int paramInt);

    Location getBackLocation();

    void setBackLocation(Location paramLocation);

    List<UUID> getIgnoredPlayers();

    void setIgnoredPlayer(UUID paramUUID);

    void removeIgnoredPlayer(UUID paramUUID);

    UUID getLastRepliedPlayer();

    void setLastRepliedPlayer(UUID paramUUID);

    long getFirstLoginTime();

    void setFirstLoginTime(long paramLong);

    long getLastLoginTime();

    void setLastLoginTime(long paramLong);

    boolean isStaffMode();

    void setStaffMode(boolean paramBoolean);

    boolean isStaffChat();

    void setStaffChat(boolean paramBoolean);

    boolean isShowTooltipStatus();

    void setShowTooltipStatus(boolean paramBoolean);

    boolean isStaffChatVisible();

    void setStaffChatVisible(boolean paramBoolean);

    boolean isOnlineStatus();

    void setOnlineStatus(boolean paramBoolean);

    boolean isGlobalChatVisible();

    void setGlobalChatVisible(boolean paramBoolean);

    boolean isMessagesVisible();

    void setMessagesVisible(boolean paramBoolean);

    boolean isMessagingSounds();

    void setMessagingSounds(boolean paramBoolean);

    boolean isSocialSpy();

    void setSocialSpy(boolean paramBoolean);

    boolean isFrozenStatus();

    void setFrozenStatus(boolean paramBoolean);

    boolean isVanishStatus();

    void setVanishStatus(boolean paramBoolean);

    boolean isRequestStatus();

    void setRequestStatus(boolean paramBoolean);

    boolean isReportStatus();

    void setReportStatus(boolean paramBoolean);

    boolean isHasStarter();

    void setHasStarter(boolean paramBoolean);

    Long getPlayTime();

    void setPlayTime(Long paramLong);
}
