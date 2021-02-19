package UpdateFunc;

import java.io.IOException;
import java.util.HashMap;

import static UpdateFunc.FwChecker.ReadCurrentVersion;

/**
 * @author Xiaoyao.L
 * @date 2020/8/12 19:23
 * @project LocalUpdate
 */
public class main {
    private static String baseUrl = "https://iotprocessorstorage.blob.core.windows.net/fileshare/newpb/screen"; // 固件存放位置，起始位置，目录下有两个文件一个版本号，一个是固件压缩包

/*    private final static String staticFileLocation = "/home/xiaoyao/tmp/StaticInformation.json";
    private static String CloudStaticInformation  = "/home/xiaoyao/tmp/CloudStaticInformation.json";
    private final static String fwTempLocation1 = "/home/xiaoyao/tmp/pb_tempfwfile1.zip";
    private final static String fwTempLocation2 = "/home/xiaoyao/tmp/pb_tempfwfile2.zip";
    private final static String fwTempLocation3 = "/home/xiaoyao/tmp/pb_tempfwfile3.zip";
    private final static String fwTempLocation4 = "/home/xiaoyao/tmp/pb_tempfwfile4.zip";
    private final static String fwDestination = "/home/xiaoyao/tmp";
    private final static String ocpp_new = "/home/xiaoyao/tmp/ocpp.jar";
    private final static String UI_new = "/home/xiaoyao/tmp/UI_interface";
    private final static String ocpp_old = "/home/xiaoyao/backup/ocpp.jar";
    private final static String UI_old = "/home/xiaoyao/backup/UI_interface";
    private final static String backup = "/home/xiaoyao/backup/";*/

    private static String staticFileLocation = "/home/root/PC-LCD/StaticInformation.json";
    private static String CloudStaticInformation  = "/home/root/PC-LCD/CloudStaticInformation.json";
    private final static String fwTempLocation1 = "/home/root/PC-LCD/pb_tempfwfile1.zip";
    private final static String fwTempLocation2 = "/home/root/PC-LCD/pb_tempfwfile2.zip";
    private final static String fwTempLocation3 = "/home/root/PC-LCD/pb_tempfwfile3.zip";
    private final static String fwTempLocation4 = "/home/root/PC-LCD/pb_tempfwfile4.zip";
    private final static String fwDestination = "/home/root/PC-LCD";
    private final static String ocpp_new = "/home/root/PC-LCD/ocpp.jar";
    private final static String UI_new = "/home/root/PC-LCD/UI_interface";
    private final static String ocpp_old = "/tmp/ocpp.jar";
    private final static String UI_old = "/tmp/UI_interface";
    private final static String backup = "/tmp/";
    private final static String powerCapsule = "/home/root/PC-LCD/powercapsule.sh";
    private final static String init ="/etc/init.d";

    public static String getInit() {
        return init;
    }

    public static String getPowerCapsule() {
        return powerCapsule;
    }

    private static HashMap<String,Boolean> Downloaded= new HashMap<>();
    public static String getBackup() {
        return backup;
    }

    public static String getOcpp_new() {
        return ocpp_new;
    }

    public static String getUI_new() {
        return UI_new;
    }

    public static String getOcpp_old() {
        return ocpp_old;
    }

    public static String getUI_old() {
        return UI_old;
    }

    public static String getStaticFileLocation() {
        return staticFileLocation;
    }

    public static Thread Download(String fwTempLocation) {
        Runnable runnable = () -> {
            if (fwTempLocation.equals(fwTempLocation1)) {
                String fwurl = baseUrl + "/pb_firmware1.zip";
                for (int i = 0; i < 5; i++) {
                    try {
                        boolean isDownloaded = FwChecker.Download(fwurl, fwTempLocation1);

                        if (isDownloaded)
                        {
                            System.out.println("1st zip file's download try: "+(i+1)+" success");
                            Downloaded.put(fwTempLocation1, true);
                            break;
                        }
                        else
                        {
                            System.out.println("1st zip file's download try: "+(i+1)+" fail");
                            Downloaded.put(fwTempLocation1,false);
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
            if (fwTempLocation.equals(fwTempLocation2)) {
                String fwurl = baseUrl + "/pb_firmware2.zip";
                for (int i = 0; i < 5; i++) {
                    try {
                        boolean isDownloaded = FwChecker.Download(fwurl, fwTempLocation2);

                        if (isDownloaded)
                        {
                            System.out.println("2nd zip file's download try: "+(i+1)+" success");
                            Downloaded.put(fwTempLocation2, true);
                            break;
                        }
                        else
                        {
                            System.out.println("2nd zip file's download try: "+(i+1)+" fail");
                            Downloaded.put(fwTempLocation2,false);
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
            if (fwTempLocation.equals(fwTempLocation3)) {
                String fwurl = baseUrl + "/pb_firmware3.zip";
                for (int i = 0; i < 5; i++) {
                    try {
                        boolean isDownloaded = FwChecker.Download(fwurl, fwTempLocation3);

                        if (isDownloaded)
                        {
                            System.out.println("3rd zip file's download try: "+(i+1)+" success");
                            Downloaded.put(fwTempLocation3, true);
                            break;
                        }
                        else
                        {
                            System.out.println("3rd zip file's download try: "+(i+1)+" fail");
                            Downloaded.put(fwTempLocation3,false);
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }
            if (fwTempLocation.equals(fwTempLocation4)) {
                String fwurl = baseUrl + "/pb_firmware4.zip";
                for (int i = 0; i < 5; i++) {
                    try {
                        boolean isDownloaded = FwChecker.Download(fwurl, fwTempLocation4);

                        if (isDownloaded)
                        {
                            System.out.println("4th zip file's download try: "+(i+1)+" success");
                            Downloaded.put(fwTempLocation4, true);
                            break;
                        }
                        else
                        {
                            System.out.println("4th zip file's download try: "+(i+1)+" fail");
                            Downloaded.put(fwTempLocation4,false);
                        }
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
            }

        };
        return new Thread(runnable);
    }

    public static void main(String[] args) throws Exception {

        System.out.println("checking version...");
        int currentVersion = ReadCurrentVersion();

        System.out.println("Current version on machine is " + currentVersion);

        String versionFileLocation = baseUrl + "/pb_fwversion";
        int latestVersion = FwChecker.Check(currentVersion, versionFileLocation);
        if(latestVersion == 0)
        {
            System.out.println("Internet connection lost");
        }
        System.out.println("Latest version on Cloud Server is " + latestVersion);
        boolean foundNew = latestVersion > currentVersion;



        if (foundNew) {
/*            String fwurl = baseUrl + "/pc_firmware1.zip";
            boolean isDownloaded = FwChecker.Download(fwurl, fwTempLocation1);*/

            Thread t1 = Download(fwTempLocation1);
            Thread t2 = Download(fwTempLocation2);
            Thread t3 = Download(fwTempLocation3);
            Thread t4 = Download(fwTempLocation4);

            t1.start();
            t2.start();
            t3.start();
            t4.start();

            t1.join();
            t2.join();
            t3.join();
            t4.join();
            //System.out.println("downloading ...");
            if (Downloaded.get(fwTempLocation1)&&Downloaded.get(fwTempLocation2)&&Downloaded.get(fwTempLocation3)&&Downloaded.get(fwTempLocation4)) {
                System.out.println("downloaded");
                boolean isUpdated = FwChecker.Update(fwTempLocation1, fwTempLocation2, fwTempLocation3, fwTempLocation4,
                        fwDestination, latestVersion, staticFileLocation, CloudStaticInformation);
                if (isUpdated) {
                    System.out.println("updated");
                } else {
                    System.out.println("update fail");
                }
            } else {
                System.out.println("download fail");
            }
            //System.out.println("updating ...");

/*            boolean isUpdated = FwChecker.Update(fwTempLocation, fwDestination, latestVersion, staticFileLocation, CloudStaticInformation);
            if (isUpdated) {
                System.out.println("updated");
            }
            else {
                System.out.println("update fail");
            } */
        }
            else {
            System.out.println("no new version found");
        }
    }
}


