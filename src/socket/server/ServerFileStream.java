package socket.server;

import java.io.*;

public class ServerFileStream {
    private BufferedReader reader;
    private BufferedReader nowFileReader;
    private String fileData;

    public ServerFileStream() throws FileNotFoundException {
        reader = new BufferedReader(new FileReader("data\\memoList.data"));

        StringBuilder result = new StringBuilder();
        String str;

        try {
            while ((str = reader.readLine()) != null) {
                result.append(str);
                result.append("\n");
            }

        } catch (IOException ioe) {
            System.out.println("입출력 에러...");
        }

        try {
            fileData = result.toString();
        } catch (NullPointerException npe) {
            System.out.println("빈 파일입니다...");
        }
    }

    public String getList() {
        return fileData;
    }

    public String getFileData(String fname) {
        try {
            nowFileReader = new BufferedReader(new FileReader("data\\" + fname + ".data"));
        } catch (FileNotFoundException fnfe) {
            System.out.println("파일이 존재하지 않습니다...");
            System.out.println("파일 : " + fname);
            return "Error";
        }

        StringBuilder result = new StringBuilder();
        String str;

        try {
            while ((str = nowFileReader.readLine()) != null) {
                result.append(str);
                result.append("\n");
            }
            nowFileReader.close();

        } catch (IOException ioe) {
            System.out.println("입출력 에러...");
        }

        try {
            return result.toString();
        } catch (NullPointerException npe) {
            System.out.println("빈 파일입니다...");
            return "None";
        }

    }

    public void makeNewFile(String fname) {
        String txt = "Nothing";
        String fileName = "data\\" + fname.split(",")[0] + ".data";
        try{
            BufferedWriter fw = new BufferedWriter(new FileWriter(fileName));
            fw.write(txt);
            fw.flush();
            fw.close();

            fileData = (Integer.parseInt(String.valueOf(fileData.charAt(0))) + 1) +
                    fileData.substring(1) + fname + "\n";
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter("data\\memoList.data"));
            writer.write(fileData);
            writer.flush();
            writer.close();
            reader = new BufferedReader(new FileReader("data\\memoList.data"));

        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public void deleteFile(String fname) {
        try{
            fileData = (Integer.parseInt(String.valueOf(fileData.charAt(0))) - 1) +
                    fileData.substring(1).replace(fname + "\n", "");
            reader.close();
            BufferedWriter writer = new BufferedWriter(new FileWriter("data\\memoList.data"));
            writer.write(fileData);
            writer.flush();
            writer.close();
            reader = new BufferedReader(new FileReader("data\\memoList.data"));

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void updateFile(String fname, String data) {
        try {
            BufferedWriter fw = new BufferedWriter(new FileWriter("data\\" + fname + ".data"));
            fw.write(data);
            fw.flush();
            fw.close();
        } catch (IOException ioe) {
            System.out.println("입출력 에러...");
        }
    }

}
