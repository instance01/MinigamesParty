package com.comze_instancelabs.minigamesparty.nms;

public class VersionManager {

	public static NMSAbstraction getNMSHandler(int version) {
		if (version == 172) {
			return new NMSHandler172();
		} else if (version == 164) {
			return new NMSHandler164();
		} else if (version == 175) {
			return new NMSHandler175();
		} else if (version == 178) {
			return new NMSHandler178();
		} else if (version == 1710) {
			return new NMSHandler1710();
		}
		return null;
	}

}
