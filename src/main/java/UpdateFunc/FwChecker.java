package UpdateFunc;

/**
 * @author Xiaoyao.L
 * @date 2020/8/12 18:44
 * @project LocalUpdate
 */

import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.net.URL;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
//import java.util.zip.ZipFile;


public class FwChecker {

    private static FileWriter file;

    public static int ReadCurrentVersion() throws Exception {
        int version = 0;
        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader(main.getStaticFileLocation()));
            JSONObject jsonObject = (JSONObject) obj;
            String softwareVersion = (String) jsonObject.get("firmwareVersion");
            if (!softwareVersion.isEmpty()) {
                //softwareVersion = softwareVersion.replace(".", "");
                version = Integer.parseInt(softwareVersion);


            }

        } catch (IOException e) {
            return 0;
        }

        return version;

    }

    public static int Check(int currentVersion, String versionUrl) throws IOException {

        try {
            URL url = new URL(versionUrl);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String versionLine;
            versionLine = in.readLine();
            int latestVersion = Integer.parseInt((versionLine.replaceFirst("^0+(?!$)", "")));

            if (latestVersion > currentVersion) {
                return latestVersion;
            }
            return currentVersion;
        } catch (Exception e) {

            return 0;
        }

    }

    public static boolean Download(String fwFileLocation, String tempLocation) throws IOException {
        try {
            long startTime = System.currentTimeMillis();
            System.out.println("downloading...");
            File tempFile = new File(tempLocation);
            boolean exists = tempFile.exists();
            //FileOutputStream fileOutputStream;
            if (!exists) {
                tempFile.getParentFile().mkdirs();
                tempFile.createNewFile();
            }


            URL url = new URL(fwFileLocation);
            FileUtils.copyURLToFile(url, tempFile, 3000,3000);
            /*
            HttpURLConnection conn = null;
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(2000);
            System.out.println(conn.getContentLength());
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            System.out.println(readableByteChannel.isOpen());
            fileOutputStream = new FileOutputStream(tempFile);
            FileChannel fileChannel = fileOutputStream.getChannel();
            long index=0;
            long Memory=20971520;
            while(fileChannel.size()<conn.getContentLength()){
                System.out.println("downloading...");
                fileChannel.transferFrom(readableByteChannel, index , Memory);
                System.out.println(readableByteChannel.isOpen());
                index=fileChannel.size();
                System.out.println(fileChannel.size());
            }

            //fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();*/
            long endTime = System.currentTimeMillis();
            System.out.println("Downloading costs "+ (endTime-startTime)/1000 + "s");
            return true;
        }catch (IOException ioException)
        {
            System.out.println("Problem Occured While Downloading the File = " + ioException.getMessage());
            return false;
        }
        catch (Exception e) {
            return false;
        }

    }

    public static void unpack(String tempfile, String outputdir) throws IOException {
        File zipfile = new File(tempfile);
        if (zipfile.exists()) {
            outputdir = outputdir + File.separator;
            FileUtils.forceMkdir(new File(outputdir));

            ZipFile zf = new ZipFile(zipfile);
            Enumeration zipArchiveEntrys = zf.getEntries();
            while (zipArchiveEntrys.hasMoreElements()) {
                ZipArchiveEntry zipArchiveEntry = (ZipArchiveEntry) zipArchiveEntrys.nextElement();
                if (zipArchiveEntry.isDirectory()) {
                    FileUtils.forceMkdir(new File(outputdir + zipArchiveEntry.getName() + File.separator));
                } else {
                    IOUtils.copy(zf.getInputStream(zipArchiveEntry), FileUtils.openOutputStream(new File(outputdir + zipArchiveEntry.getName())));
                }
            }
        } else {
            throw new IOException("指定的解压文件不存在：\t" + tempfile);
        }
    }

    public static boolean Update(String tempFile1, String tempFile2, String tempFile3, String tempFile4,
                                 String targetLocation, int latestVersion, String staticFileLocation, String cloudinformation)
            throws IOException {

        try {
            System.out.println("unpacking...");
            Runtime runtime = Runtime.getRuntime();
//            Process process1 =runtime.exec(new String[]{"rm" ,"/home/root/PC-LCD/UI_interface"});
//            Process process2 =runtime.exec(new String[]{"rm" ,"/home/root/PC-LCD/ocpp.jar"});

            runtime.exec(new String[]{"cp" ,main.getOcpp_new(),main.getBackup()}).waitFor();
            //process1.destroy();
            runtime.exec(new String[]{"cp" ,main.getUI_new(),main.getBackup()}).waitFor();
            //process2.destroy();

            runtime.exec(new String[]{"rm" ,main.getOcpp_new()}).waitFor();
            //process10.destroy();
            runtime.exec(new String[]{"rm" ,main.getUI_new()}).waitFor();
            //process11.destroy();

/*            byte[] buffer = new byte[4096];
            File destinationFolder = new File(targetLocation);
            if (!destinationFolder.exists()) {
                destinationFolder.mkdir();
            }
            ZipInputStream zis = new ZipInputStream(new FileInputStream(tempFile1));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = CheckFile(destinationFolder, zipEntry);

                if(!newFile.canWrite())
                {
                	newFile.setReadable(true);
                }


                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();*/
            unpack(tempFile1,targetLocation);
            unpack(tempFile2,targetLocation);
            unpack(tempFile3,targetLocation);
            unpack(tempFile4,targetLocation);



            File ocpp = new File(main.getOcpp_new());
            File UI = new File(main.getUI_new());
            if(ocpp.exists()&&UI.exists())
            {
                runtime.exec(new String[]{"rm" ,main.getOcpp_old()}).waitFor();
                //process3.destroy();
                runtime.exec(new String[]{"rm" ,main.getUI_old()}).waitFor();
                //process4.destroy();
                WriteUpdatedVersion(latestVersion, staticFileLocation,cloudinformation);
                update();
                return true;
            }
            else
            {
                if(UI.exists()) {
                    runtime.exec(new String[]{"rm", main.getUI_new()}).waitFor();
                    //process5.destroy();
                }
                if(ocpp.exists()) {
                    runtime.exec(new String[]{"rm", main.getOcpp_new()}).waitFor();
                    //process6.destroy();
                }
                runtime.exec(new String[]{"cp" ,main.getUI_old(),main.getUI_new()}).waitFor();
                //process7.destroy();
                runtime.exec(new String[]{"cp" ,main.getOcpp_old(),main.getOcpp_new()}).waitFor();
                //process8.destroy();
                runtime.exec(new String[]{"rm", main.getUI_old()}).waitFor();
                //process12.destroy();
                runtime.exec(new String[]{"rm", main.getOcpp_old()}).waitFor();
                //process13.destroy();
                return false;

            }


            //System.out.println("update config...");


            //System.out.println("update successfully, will take effect from next reboot");

        } catch (Exception e) {
            System.out.println(e);
            return false;
        }

    }

    private static File CheckFile(File destinationDir, ZipEntry zipEntry) throws IOException {
        File destFile = new File(destinationDir, zipEntry.getName());
        boolean exists = destFile.exists();
        if(!exists){
            destFile.getParentFile().mkdirs();
            destFile.createNewFile();
        }

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new IOException("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }

    private static void WriteUpdatedVersion(int version, String staticFileLocation) throws Exception, IOException {

        JSONParser parser = new JSONParser();
        try {

            Object obj = parser.parse(new FileReader(staticFileLocation));
            JSONObject jsonObject = (JSONObject) obj;
            jsonObject.put("firmwareVersion", Integer.toString(version));
            file = new FileWriter(staticFileLocation);
            file.write(jsonObject.toJSONString());

        } catch (IOException e) {
            //System.out.println("failed to update version in config file...");
        }

        finally {
            file.flush();
            file.close();
        }

    }
    private static void WriteUpdatedVersion(int version, String staticFileLocation, String cloudStaticInformation) throws Exception, IOException {

        JSONParser parser = new JSONParser();
        try {

            Object obj_1 = parser.parse(new FileReader(staticFileLocation));
            JSONObject jsonObject_1 = (JSONObject) obj_1;
            jsonObject_1.put("firmwareVersion", Integer.toString(version));
            file = new FileWriter(staticFileLocation);
            file.write(jsonObject_1.toJSONString());

            Object obj_2 = parser.parse(new FileReader(cloudStaticInformation));
            JSONObject jsonObject_2 = (JSONObject) obj_2;
            jsonObject_2.put("SoftwareVersion", Integer.toString(version));
            FileWriter file_ = new FileWriter(cloudStaticInformation);
            file_.write(jsonObject_2.toJSONString());
            file_.flush();
            file_.close();

        } catch (IOException e) {
            //System.out.println("failed to update version in config file...");
        }

        finally {
            file.flush();
            file.close();
        }

    }
    private static void update() throws IOException, InterruptedException {
        Runtime runtime = Runtime.getRuntime();
        runtime.exec(new String[]{"cp" ,main.getPowerCapsule(),main.getInit()}).waitFor();
        runtime.exec(new String[]{"chmod +x","powercapsule.sh"}).waitFor();
        runtime.exec(new String[]{"In -s",main.getPowerCapsule(),"/etc/rc5.d/S98powercapsule"}).waitFor();

    }
}

