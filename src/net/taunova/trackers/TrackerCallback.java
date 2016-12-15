package net.taunova.trackers;

import net.taunova.util.Position;

/**
 *
 * @author maryan
 */
interface TrackerCallback {
    void process (Position begin, Position end);
}
