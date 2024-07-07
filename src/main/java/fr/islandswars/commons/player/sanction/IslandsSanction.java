package fr.islandswars.commons.player.sanction;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

/**
 * File <b>IslandsSanction</b> located on fr.islandswars.commons.player.sanction
 * IslandsSanction is a part of commons.
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
 * Created the 24/06/2024 at 22:37
 * @since 0.3.1
 */
public class IslandsSanction {

    private SanctionReason reason;
    private String         start;
    private String         end;
    private UUID           author;
    private String         authorName;

    public IslandsSanction(SanctionReason reason, UUID author, String authorName) {
        this.reason = reason;
        this.author = author;
        this.authorName = authorName;
        var now = Instant.now();
        this.start = DateTimeFormatter.ISO_INSTANT.format(now);
        this.end = DateTimeFormatter.ISO_INSTANT.format(now.plus(reason.getDays(), ChronoUnit.DAYS));
    }

    public String getEnd() {
        return end;
    }

    public SanctionReason getReason() {
        return reason;
    }

    public UUID getAuthor() {
        return author;
    }

    public String getAuthorName() {
        return authorName;
    }

}
