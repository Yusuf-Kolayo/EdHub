package com.ajaxict.edhub;

import java.io.File;

public class Utility {

    public String userHomeDir = System.getProperty("user.home");
    public String userDpDirectory = userHomeDir+"/edhub/user_dp";

    public static void checkUserDpFolder() {
        // create the target directory for the uploaded user pictures if it does not already exist
        String userHomeDir = System.getProperty("user.home");
        String projectName = "edhub";
        File projectDir = new File(userHomeDir, projectName);
        if (!projectDir.exists()) {
            projectDir.mkdir();
        }
        File userDpDir = new File(projectDir, "user_dp");
        if (!userDpDir.exists()) {
            userDpDir.mkdir();
        }
    }

}
