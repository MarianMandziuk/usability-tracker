package net.taunova.trackers;

import net.taunova.util.Position;

/**
 *
 * @author maryan
 */
interface TrackerCallback {
    void process (Position begin, Position control1, Position control2, Position end);
}

interface TrackerCallbackStraightLine {
    void process (Position current, Position next);
}
