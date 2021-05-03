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
}