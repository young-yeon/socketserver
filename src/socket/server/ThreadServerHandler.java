package socket.server;

import java.io.*;
import java.net.Socket;


// 데이터 전송용 thread 클래스
public class ThreadServerHandler extends Thread {
    // 전달받은 socket 저장
    private Socket connectedClientSocket;
    private ServerFileStream fileStream;
    private BufferedWriter writer;
    private BufferedReader reader;
    private String[] dataList;

    public ThreadServerHandler(Socket connectedClientSocket) {
        this.connectedClientSocket = connectedClientSocket;
        try { this.fileStream = new ServerFileStream(); }
        catch (FileNotFoundException fnfo) {
            System.out.println("memoList가 없습니다...");
        }
    }

    // start 시 실행
    public void run() {
        try {
            // buffer io
            writer = new BufferedWriter(
                    new OutputStreamWriter(connectedClientSocket.getOutputStream()));
            reader = new BufferedReader(
                    new InputStreamReader(connectedClientSocket.getInputStream()));

            // 버퍼에 문자열을 기록
            writer.write("연결 확인");
            writer.newLine();

            // client 로 전송
            writer.flush();

            // 리스트 반환
            returnList();

            // 입력 확인
            String str;
            while (true) {
                while ((str = reader.readLine()) == null);

                try {
                    int userChoose = Integer.parseInt(str);
                    switch (userChoose) {
                        case 1:
                            userChooseEdit();
                            break;
                        case 2:
                            userChooseNew();
                            break;
                        case 3:
                            userChooseDelete();
                            break;
                        case 4:
                            userChooseSave();
                        case 9:
                            userCheckPasswd();
                            break;
                        default:
                            throw new Exception("이상한 값이 전달됨...");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } catch(IOException ignored) {
        } finally {
            try {
                connectedClientSocket.close(); // 클라이언트 접속 종료
            } catch(IOException ignored) {}
        }
    }

    private void returnList() {
        dataList = fileStream.getList().split("\n");
        try {
            for (String dataName : dataList) {
                writer.write(dataName.split(",")[0]);
                writer.newLine();
            }
            writer.flush();

        } catch (IOException ioe) {
            System.out.println("입출력 에러...");
        }
    }

    private void userChooseEdit() {
        String fname;
        try {
            while ((fname = reader.readLine()) == null);
            String data = fileStream.getFileData(fname);
            writer.write(data);
            writer.newLine();
            writer.write("=*= FinFile =*=");
            writer.newLine();
            writer.flush();
        }
        catch (IOException ioe) {
            System.out.println("서버 입출력 에러...");
        }
    }

    private void userChooseNew() {
        String fname;
        try {
            while ((fname = reader.readLine()) == null);
            fileStream.makeNewFile(fname);

            returnList();
        } catch (IOException ioe) {
            System.out.println("입출력 에러...");
        }

    }

    private void userChooseDelete() {
        String memoName = null, passwd = null;
        try {
            memoName = reader.readLine();
            passwd = reader.readLine();

        } catch (IOException ioe) {
            System.out.println("서버 수신 오류");
        }
        for (String name : dataList) {
            if (name.split(",")[0].equals(memoName)) {
                if (name.split(",")[1].equals(passwd)) {
                    fileStream.deleteFile(memoName + "," + passwd);
                }

            }
        }
        returnList();
    }

    private void userChooseSave() {
        String memoName, data;
        try {
            memoName = reader.readLine();

            String str;
            StringBuilder result = new StringBuilder();
            while (!"=*= FinFile =*=".equals(str = reader.readLine())) {
                result.append(str);
                result.append("\n");
            }

            data = result.toString();
            fileStream.updateFile(memoName, data);
            reader.readLine();
        } catch (IOException ioe) {
            System.out.println("입출력 오류...");
        }
    }

    private void userCheckPasswd() {
        String memoName = null, passwd = null;
        try {
            memoName = reader.readLine();
            passwd = reader.readLine();

        } catch (IOException ioe) {
            System.out.println("서버 수신 오류");
        }

        boolean state = true;
        for (String name : dataList) {
            if (name.split(",")[0].equals(memoName)) {
                if (name.split(",")[1].equals(passwd)) {
                    try {
                        writer.write("OK");
                        writer.newLine();
                        writer.flush();
                        state = false;
                    } catch (IOException ioe) {
                        System.out.println("서버 출력 에러...");
                    }
                }

            }
        }
        if (state) {
            try {
                writer.write("NO");
                writer.newLine();
                writer.flush();
            } catch (IOException ioe) {
                System.out.println("서버 출력 에러...");
            }
        }
    }

}