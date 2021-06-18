package net.za.dawes.wintexprotocol;

/**
 * A data object representing useful state information about a remote panel. In
 * combination with a Panel instance, and appropriate WintexMessage requests and
 * responses, the state data can be updated bi-directionally if desired. Eventually.
 * 
 * @author rogan
 *
 */
public class PanelData {

    String[] zoneNames;
    String[] userNames;

    public PanelData(int zones, int users) {
        zoneNames = new String[zones];
        userNames = new String[users];
    }

    public String getZoneName(int zone) {
        return zoneNames[zone];
    }

    public String getUserName(int user) {
        return userNames[user];
    }
}
