package fr.islandswars.commons.player;

import fr.islandswars.commons.utils.TimeUtils;

import java.time.Instant;

/**
 * File <b>Ranks</b> located on fr.islandswars.commons.player
 * Ranks is a part of commons.
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
 * Created the 25/06/2024 at 22:19
 * @since 0.3.1
 */
public class Rank {

    private String rank;
    private String author;
    private String date;

    public Rank(IslandsRank rank, String givenBy, String date) {
        this.rank = rank.name();
        this.author = givenBy;
        this.date = date;
    }

    public Instant getDate() {
        return TimeUtils.FROM_ISO_STRING(date);
    }

    public String getAuthor() {
        return author;
    }

    public IslandsRank getRank() {
        return IslandsRank.valueOf(rank);
    }
}
