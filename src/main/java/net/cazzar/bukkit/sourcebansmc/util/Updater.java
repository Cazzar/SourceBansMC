/*
 * {one line to give the program's name and a brief idea of what it does
 * Copyright (C) 2013 cazzar
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see [http://www.gnu.org/licenses/].
 */

package net.cazzar.bukkit.sourcebansmc.util;

import net.cazzar.bukkit.sourcebansmc.util.logging.LogHelper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;


public class Updater {
    private String plName;
    private String currVer;
    private String latestVer;
    private String priority;

    private static final String VERSION_FILE = "http://plugins.ehaqui.com/bukkit/";

    public Updater(String plName, String currVer) {
        this.plName = plName;
        this.currVer = currVer;
    }


    public void loadLatestVersion() {
        BufferedReader reader = null;

        try {
            String urlUpdate = VERSION_FILE + plName.toLowerCase() + ".txt";

            URL url = new URL(urlUpdate);
            reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String str = reader.readLine();
            String[] split = str.split("\\|");

            latestVer = split[0];
            priority = getPriority(Integer.parseInt(split[1]));

        } catch (IOException e) {
            LogHelper.warning("Could not check for newer version!");
            latestVer = null;
        } finally {
            try {
                if (reader != null)
                    reader.close();
            } catch (IOException ignored) {
            }
        }
    }

    private static String getPriority(int level) {
        switch (level) {
            case 0:
                return "LOW";
            case 1:
                return "NORMAL";
            case 2:
                return "MEDIUM";
            case 3:
                return "HIGH";

        }

        return "NORMAL";
    }

    public boolean isUpdateAvailable() {
        return latestVer != null && compareVer(latestVer, currVer) > 0;

    }

    private int compareVer(String str1, String str2) {
        String[] val1 = str1.split("\\.");
        String[] val2 = str2.split("\\.");
        int i = 0;

        while (i < val1.length && i < val2.length && val1[i].equals(val2[i]))
            i++;

        if (i < val1.length && i < val2.length) {
            int diff = new Integer(val1[i]).compareTo(new Integer(val2[i]));
            return diff < 0 ? -1 : diff == 0 ? 0 : 1;
        }

        return val1.length < val2.length ? -1 : val1.length == val2.length ? 0 : 1;
    }


    public void printMessage() {
        LogHelper.warning("");
        LogHelper.warning("-------- EhAqui Updater --------");
        LogHelper.warning("There is an update to: ", plName);
        LogHelper.warning("Version: ", latestVer);
        LogHelper.warning("");
        LogHelper.warning("Priority: ", priority);
        LogHelper.warning("Download location: http://dev.bukkit.org/server-mods/" + plName.toLowerCase() + "/");
        LogHelper.warning("-------------------------------");
        LogHelper.warning("");
    }


}