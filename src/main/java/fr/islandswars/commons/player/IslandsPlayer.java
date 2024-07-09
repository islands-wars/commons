package fr.islandswars.commons.player;

import com.google.gson.annotations.SerializedName;
import fr.islandswars.commons.player.sanction.IslandsSanction;
import fr.islandswars.commons.utils.TimeUtils;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * File <b>IslandsPlayer</b> located on fr.islandswars.commons.player
 * IslandsPlayer is a part of commons.
 * <p>
 * Copyright (c) 2017 - 2024 Islands Wars.
 * <p>
 * commons is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <a href="http://www.gnu.org/licenses/">GNU license</a>.
 * <p>
 *
 * @author Jangliu, {@literal <jangliu@islandswars.fr>}
 * Created the 23/06/2024 at 16:32
 * @since 0.3.1
 */
public abstract class IslandsPlayer {

    protected UUID                  uuid;
    protected List<Rank>            ranks;
    @SerializedName("first_connection")
    protected String                firstConnection;
    @SerializedName("last_connection")
    protected String                lastConnection;
    protected List<IslandsSanction> sanctions;

    public IslandsPlayer() {
        this.ranks = new ArrayList<>();
        this.sanctions = new ArrayList<>();
    }

    //should only be called by proxy!
    public void firstConnection(UUID uuid, String PROXY) {
        setUUID(uuid);
        this.firstConnection = TimeUtils.NOW();
        this.lastConnection = firstConnection;
        addRank(new Rank(IslandsRank.PLAYER, PROXY, TimeUtils.NOW()));
    }

    //should only be called by proxy!
    public void welcomeBack() {
        setLastConnection();
    }

    public IslandsRank getMainRank() {
        return IslandsRank.getHighest(ranks);
    }

    public UUID getUUID() {
        return uuid;
    }

    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    public void setLastConnection() {
        this.lastConnection = TimeUtils.NOW();
    }

    public void addRank(Rank rank) {
        if (ranks.stream().noneMatch(r -> r.getRank().equals(rank.getRank())))
            ranks.add(rank);
        //TODO notify the player and update it !!
    }

    public Optional<IslandsSanction> isKick() {
        for (IslandsSanction sanction : sanctions) {
            var endDate = Instant.parse(sanction.getEnd());
            var now     = Instant.now();
            if (endDate.isAfter(now))
                return Optional.of(sanction);
        }
        return Optional.empty();
    }
}

