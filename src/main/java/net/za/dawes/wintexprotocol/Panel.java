package net.za.dawes.wintexprotocol;

public enum Panel {
    // FIXME: The number of users is obviously incorrect for Premier panels!
    UNKNOWN(0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0),
    PREMIER_412(Globals.prPremierInt, Globals.pt412, 500, 16, 2, 32, 2, 0, 1, 0, 0, 0),
    PREMIER_412V16_12_01(Globals.prPremierInt, Globals.pt412, 500, 16, 2, 0, 2, 0, 1, 0, 0, 0),
    PREMIER_816(Globals.prPremierInt, Globals.pt816, 500, 16, 4, 32, 2, 0, 1, 0, 0, 0),
    PREMIER_832(Globals.prPremierInt, Globals.pt832, 750, 32, 4, 32, 2, 0, 1, 64, 0, 0),
    PREMIER_24(Globals.prPremierInt, Globals.pt24, 500, 24, 1, 32, 1, 0, 2, 0, 0, 0),
    PREMIER_48(Globals.prPremierInt, Globals.pt48, 500, 48, 4, 32, 4, 8, 2, 0, 0, 0),
    PREMIER_88(Globals.prPremierInt, Globals.pt88, 1000, 88, 8, 32, 8, 8, 2, 0, 0, 0),
    PREMIER_168(Globals.prPremierInt, Globals.pt168, 1000, 168, 16, 32, 8, 8, 2, 0, 0, 0),
    PREMIER_640(Globals.prPremierInt, Globals.pt640, 1000, 640, 64, 32, 8, 8, 9, 0, 0, 0),
    PREM_412(Globals.prPremInt, Globals.pt412, 500, 16, 2, 32, 2, 0, 1, 0, 0, 0),
    PREM_412V16_12_01(Globals.prPremInt, Globals.pt412, 500, 16, 2, 0, 2, 0, 1, 0, 0, 0),
    PREM_816(Globals.prPremInt, Globals.pt816, 500, 16, 4, 32, 2, 0, 1, 0, 0, 0),
    PREM_832(Globals.prPremInt, Globals.pt832, 750, 32, 4, 32, 2, 0, 1, 0, 0, 0),
    ELITE_12(Globals.prElite, Globals.ptE12, 250, 16, 2, 32, 4, 8, 2, 8, 8, 2),
    ELITE_24(Globals.prElite, Globals.ptE24, 500, 24, 2, 32, 4, 8, 2, 25, 16, 4),
    ELITE_48(Globals.prElite, Globals.ptE48, 500, 48, 4, 32, 4, 8, 2, 50, 32, 4),
    ELITE_88(Globals.prElite, Globals.ptE88, 1000, 88, 8, 32, 8, 8, 2, 100, 64, 8),
    ELITE_168(Globals.prElite, Globals.ptE168, 2000, 168, 16, 32, 8, 8, 2, 200, 128, 16),
    ELITE_640(Globals.prElite, Globals.ptE640, 5000, 640, 64, 32, 8, 8, 9, 1000, 512, 64),
    ELITE_64(Globals.prElite, Globals.ptE64, 500, 80, 4, 32, 4, 8, 2, 50, 64, 4),;

    public final int range;
    public final int type;
    public final int eventLogSize;
    public final int zones;
    public final int areas;
    public final int systemOutputs;
    public final int pcOutputs;
    public final int x10Outputs;
    public final int byteLength;
    public final int users;
    public final int ricochetDevices;
    public final int keypads;

    // Memory address ranges for Premier832
    // These will very likely have to be initialised per panel eventually.
    // Sizes are likely to be constant, though.
    public final int ZONES_ADDRESS = 0x0;
    public final int ZONE_SIZE = 0x10;
    public final int USERS_ADDRESS = 0x002000;
    public final int USER_SIZE = 0x08;
    
    /* c1 = 13.57V. c2 = 13.64V */
    public final int DIAGNOSTIC_VOLTAGE_ADDRESS = 0xdc7;
    public final int DIAGNOSTIC_VOLTAGE_SIZE = 4;

    public final int DIAGNOSTIC_ONBOARD_COMMS_ADDRESS = 0xcda;
    public final int DIAGNOSTIC_ONBOARD_COMMS_SIZE = 0x1;
    
    public final int DIAGNOSTIC_SYSTEM_ADDRESS = 0xcd4;
    public final int DIAGNOSTIC_SYSTEM_SIZE = 0x5;
    
    public final int DIAGNOSTIC_PARTITIONS_ADDRESS = 0xd85;
    public final int DIAGNOSTIC_PARTITIONS_SIZE = 0x13;
    
    public final int DIAGNOSTIC_PCOUTPUTS_ADDRESS = 0xcd7;
    public final int DIAGNOSTIC_PCOUTPUTS_SIZE = 0x1;
    
    public final int DIAGNOSTIC_TIMERS_SYSTEM_ADDRESS = 0xd8d;
    public final int DIAGNOSTIC_TIMERS_SYSTEM_SIZE = 0x7;
    
    public final int DIAGNOSTIC_RFDEVICES_ADDRESS_1_16_1 = 0x6c6;
    public final int DIAGNOSTIC_RFDEVICES_SIZE_1_16_1 = 0x10;
    
    public final int DIAGNOSTIC_RFDEVICES_ADDRESS_17_32_1 = 0x6d6;
    public final int DIAGNOSTIC_RFDEVICES_SIZE_17_32_1 = 0x10;
    
    public final int DIAGNOSTIC_RFDEVICES_ADDRESS_1_16_2 = 0x6e6;
    public final int DIAGNOSTIC_RFDEVICES_SIZE_1_16_2 = 0x10;
    
    public final int DIAGNOSTIC_RFDEVICES_ADDRESS_17_32_2 = 0x6f6;
    public final int DIAGNOSTIC_RFDEVICES_SIZE_17_32_2 = 0x10;
    
    public final int DIAGNOSTIC_RFDEVICES_ADDRESS_1_16_3 = 0x706;
    public final int DIAGNOSTIC_RFDEVICES_SIZE_1_16_3 = 0x10;
    
    public final int DIAGNOSTIC_RFDEVICES_ADDRESS_17_32_3 = 0x716;
    public final int DIAGNOSTIC_RFDEVICES_SIZE_17_32_3 = 0x10;
    
    public final int DIAGNOSTIC_RFDEVICES_ADDRESS_1_16_4 = 0x726;
    public final int DIAGNOSTIC_RFDEVICES_SIZE_1_16_4 = 0x10;
    
    public final int DIAGNOSTIC_RFDEVICES_ADDRESS_17_32_4 = 0x736;
    public final int DIAGNOSTIC_RFDEVICES_SIZE_17_32_4 = 0x10;
    
    public final int DIAGNOSTIC_EXPANDER_1_ADDRESS_1 = 0x500;
    public final int DIAGNOSTIC_EXPANDER_1_SIZE_1 = 0x08;
    
    public final int DIAGNOSTIC_EXPANDER_1_ADDRESS_2 = 0x520;
    public final int DIAGNOSTIC_EXPANDER_1_SIZE_2 = 0x08;
    
    public final int DIAGNOSTIC_EXPANDER_2_ADDRESS_1 = 0x508;
    public final int DIAGNOSTIC_EXPANDER_2_SIZE_1 = 0x08;
    
    public final int DIAGNOSTIC_EXPANDER_2_ADDRESS_2 = 0x528;
    public final int DIAGNOSTIC_EXPANDER_2_SIZE_2 = 0x08;
    
    public final int DIAGNOSTIC_EXPANDER_3_ADDRESS_1 = 0x510;
    public final int DIAGNOSTIC_EXPANDER_3_SIZE_1 = 0x08;
    
    public final int DIAGNOSTIC_EXPANDER_3_ADDRESS_2 = 0x530;
    public final int DIAGNOSTIC_EXPANDER_3_SIZE_2 = 0x08;
    
    public final int DIAGNOSTIC_SYSTEM_ZONE_STATUS_ADDRESS = 0xdb7; // 0x4f8 and 0x518 DEFINITELY appear to be zone activity related!
    public final int DIAGNOSTIC_SYSTEM_ZONE_STATUS_SIZE = 0x10;
    
    Panel(int range, int type, int eventLogSize, int zones, int areas, int systemOutputs, int pcOutputs, int x10Outputs,
            int byteLength, int users, int ricochetDevices, int keypads) {
        this.range = range;
        this.type = type;
        this.eventLogSize = eventLogSize;
        this.zones = zones;
        this.areas = areas;
        this.systemOutputs = systemOutputs;
        this.pcOutputs = pcOutputs;
        this.x10Outputs = x10Outputs;
        this.byteLength = byteLength;
        this.users = users;
        this.ricochetDevices = ricochetDevices;
        this.keypads = keypads;
    }

    public static Panel fromString(String resp) {
        if (resp.contains("Prem816V46212")) {
            return PREMIER_412V16_12_01;
        } else if (resp.contains("Premier")) {
            if (resp.contains("412")) {
                return PREMIER_412;
            } else if (resp.contains("816")) {
                return PREMIER_816;
            } else if (resp.contains("832")) {
                return PREMIER_832;
            } else if (resp.contains("Premier 24")) {
                return PREMIER_24;
            } else if (resp.contains("Premier 48")) {
                return PREMIER_48;
            } else if (resp.contains("Premier 88")) {
                return PREMIER_88;
            } else if (resp.contains("Premier 168")) {
                return PREMIER_168;
            } else if (resp.contains("Premier 640")) {
                return PREMIER_640;
            } else {
                return PREMIER_816;
            }
        } else if (resp.contains("Prem")) {
            if (resp.contains("412")) {
                return PREM_412;
            } else if (resp.contains("816")) {
                return PREM_816;
            } else if (resp.contains("832")) {
                return PREM_832;
            } else {
                return PREM_816;
            }
        } else if (resp.contains("Elite")) {
            if (resp.contains("Elite 12")) {
                return ELITE_12;
            } else if (resp.contains("Elite 24")) {
                return ELITE_24;
            } else if (resp.contains("Elite 48")) {
                return ELITE_48;
            } else if (resp.contains("Elite 88")) {
                return ELITE_88;
            } else if (resp.contains("Elite 168")) {
                return ELITE_168;
            } else if (resp.contains("Elite 640")) {
                return ELITE_640;
            } else if (resp.contains("Elite 64")) {
                return ELITE_64;
            } else {
                return ELITE_48;
            }
        }
        return UNKNOWN;
    }

    private static int getStrings(String[] strings, int off, byte[] data, int len) {
        int i = 0;
        for (i = 0; i < strings.length - off && i * len < data.length; i++) {
            strings[off + i] = new String(data, i * len, len).trim();
        }
        return i;
    }

    /**
     * Create a suitable WintexMessage to retrieve up to the next 4 zone names
     * starting from the startZone, or less if there are fewer zones remaining to be
     * read.
     * 
     * @param startZone is a zero-based index.
     * @return a WintexMessage for the particular panel.
     * @throws IndexOutOfBoundsException if startZone is greater than the number of
     *                                   zones supported by the panel - 1
     */

    public WintexMessage newGetZoneNamesMessage(int startZone) {
        if (startZone < 0 || startZone >= zones)
            throw new IndexOutOfBoundsException(startZone);
        return WintexMessage.readConfigurationMemory(ZONES_ADDRESS + (ZONE_SIZE * startZone),
                Math.min(4, zones - startZone) * ZONE_SIZE);
    }

    /**
     * Updates the provided PanelData with the names of the zones contained in the
     * provided response message Automatically calculates which zones the message
     * contains.
     * 
     * @param msg a response message from the panel, to a newGetZoneNamesMessage().
     * @param pd  the PanelData instance to update
     * @return the number of the last zone name included in the message
     */
    public int updateZoneNames(WintexMessage msg, PanelData pd) {
        byte[] data = msg.getMemory();
        int start = (msg.addr() - ZONES_ADDRESS) / ZONE_SIZE;
        if (start < 0 || start >= zones)
            throw new RuntimeException("Out of range zone name query, startZone = " + start);
        return getStrings(pd.zoneNames, start, data, ZONE_SIZE);
    }

    /**
     * Create a suitable WintexMessage to retrieve up to the next 8 users starting
     * from the startUser, or less if there are fewer users remaining to be read.
     * 
     * @param startUser is a 0-based index.
     * @return a WintexMessage for the particular panel.
     * @throws IndexOutOfBoundsException if startUser is greater than the number of
     *                                   users supported by the panel - 1
     */

    public WintexMessage newGetUsersMessage(int startUser) {
        if (startUser < 0 || startUser >= users)
            throw new IndexOutOfBoundsException(startUser);
        return WintexMessage.readConfigurationMemory(USERS_ADDRESS + (USER_SIZE * startUser),
                Math.min(8, users - startUser) * USER_SIZE);
    }

    /**
     * Updates the provided PanelData with the names of the users contained in the
     * provided response message Automatically calculates which users the message
     * contains.
     * 
     * @param msg a response message from the panel, to a newGetUserNamesMessage().
     * @param pd  the PanelData instance to update
     * @return the number of the last user name included in the message
     */
    public int updateUserNames(WintexMessage msg, PanelData pd) {
        byte[] data = msg.getMemory();
        int start = (msg.addr() - USERS_ADDRESS) / USER_SIZE;
        if (start < 0 || start >= users)
            throw new RuntimeException("Out of range user name query, start = " + start);
        return getStrings(pd.zoneNames, start, data, ZONE_SIZE);
    }

    public String toString(WintexMessage msg) {
        byte type = msg.getType();
        if (type == 'W' || type == 'I' || type == 'O' || type == 'R') {
            int addr = msg.addr();
            int len = ((int) (msg.getData()[3] & 0xFF));
            String region = "";
            String data = "";
            switch (type) {
            case 'O':
                region = "config";
                break;
            case 'I':
                region = "config data";
                data = "\n" + Xxd.dump(msg.getMemory());
                break;
            case 'R':
                region = "transient";
                break;
            case 'W':
                region = "transient data";
                data = "\n" + Xxd.dump(msg.getMemory());
                break;
            }
            String descr = "";
            if (region.equals("config") && addr >= ZONES_ADDRESS && addr < ZONES_ADDRESS + (zones * ZONE_SIZE))
                descr = " Zones";
            if (region.equals("config") && addr >= USERS_ADDRESS && addr < USERS_ADDRESS + (users * USER_SIZE))
                descr = " Users";
            return "Read " + region + " (0x" + Integer.toHexString(addr) + ":" + Integer.toHexString(len) + ")" + descr + data;
        } else if (type == 'Z') {
            if (msg.getData().length == 0) {
                return "Heartbeat";
            } else if (msg.getData().length == 4) {
                return "Login";
            } else if (WintexMessage.isHeartbeatResponse(msg)) {
                return "Heartbeat Response";
            }
        }
        return msg.toString();
    }
}