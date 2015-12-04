package com.daluga.sftp;

import com.jcraft.jsch.Logger;

import java.util.HashMap;
import java.util.Map;

public class SecureFTPLogger implements Logger {

    static Map<Integer, String> name = new HashMap<Integer, String>();

    static {
        name.put(DEBUG, "DEBUG: ");
        name.put(INFO, "INFO: ");
        name.put(WARN, "WARN: ");
        name.put(ERROR, "ERROR: ");
        name.put(FATAL, "FATAL: ");
    }

    public boolean isEnabled(int level) {
        return true;
    }

    public void log(int level, String message) {
        System.out.print(name.get(level));
        System.out.println(message);
    }
}
