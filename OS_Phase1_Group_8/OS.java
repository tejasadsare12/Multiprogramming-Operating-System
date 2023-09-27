import java.io.*;

public class OS {

    private char[][] memory = new char[100][4];
    private char[] buffer = new char[40];
    private char[] R = new char[4];
    private char[] IR = new char[4];
    private int IC;
    private int C;
    private int SI;

    private String input_file;
    private String output_file;
    private FileReader input;
    private BufferedReader fread;
    private FileWriter outp````````````````````ut;

    public OS(String file, String output) {
        this.input_file = file;
        this.SI = 0;
        try {
            this.input = new FileReader(file);
            this.fread = new BufferedReader(input);
            this.output = new FileWriter(output);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void INIT() {
        memory = new char[100][4];
        C = 0;
        IC = 0;
    }

    private void MASTER_MODE() {
        int si = this.SI;
        switch (si) {
            case 1:
                READ();
                break;
            case 2:
                WRITE();
                break;
            case 3:
                TERMINATE();
        }
    }

    private void READ() {
        IR[3] = '0';
        String readline = new String(IR);

        int num = Integer.parseInt(readline.substring(2));

        try {
            readline = fread.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = readline.toCharArray();
        for (int i = 0; i < readline.length();) {
            memory[num][(i % 4)] = buffer[i];
            i++;
            if (i % 4 == 0)
                num++;
        }
        EXECUTEUSERPROGRAM();
    }

    private void WRITE() {
        IR[3] = '0';
        String line = new String(IR);
        int number = Integer.parseInt(line.substring(2));
        String t, total = "";
        for (int i = 0; i <= 9; i++) {
            t = new String(memory[number + i]);
            total = total.concat(t);
        }
        System.out.println(total + "In write");
        try {
            output.write(total);
            output.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        EXECUTEUSERPROGRAM();
    }

    private void TERMINATE() {
        try {
            output.write("\n\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOAD();
    }

    public void LOAD() {
        String line;
        try {

            while ((line = fread.readLine()) != null) {
                buffer = line.toCharArray();
                if (buffer[0] == '$' && buffer[1] == 'A' && buffer[2] == 'M' && buffer[3] == 'J') {
                    System.out.println("Control card AMJ is detected");
                    INIT();
                    continue;
                } else if (buffer[0] == '$' && buffer[1] == 'D' && buffer[2] == 'T' && buffer[3] == 'A') {
                    System.out.println("Data card DTA is detected");
                    STARTEXECUTION();
                    continue;
                } else if (buffer[0] == '$' && buffer[1] == 'E' && buffer[2] == 'N' && buffer[3] == 'D') {
                    System.out.println("END card detected");
                    printthememory();
                    continue;
                }
                if (IC == 100) {
                    System.out.println("Abort due to exceed memory usage");
                }

                for (int i = 0; i < line.length();) {
                    memory[IC][i % 4] = buffer[i];
                    i++;
                    if (i % 4 == 0)
                        IC++;
                }

            }
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void STARTEXECUTION() {
        IC = 0;
        EXECUTEUSERPROGRAM();
    }

    private void EXECUTEUSERPROGRAM() {
        while (1 < 2) {
            if (IC == 100)
                break;
            IR[0] = memory[IC][0];
            IR[1] = memory[IC][1];
            IR[2] = memory[IC][2];
            IR[3] = memory[IC][3];

            IC = IC + 1;
            if (IR[0] == 'L' && IR[1] == 'R') {
                String line = new String(IR);

                int num = Integer.parseInt(line.substring(2));
                R[0] = memory[num][0];
                R[1] = memory[num][1];
                R[2] = memory[num][2];
                R[3] = memory[num][3];
            } else if (IR[0] == 'S' && IR[1] == 'R') {
                String line = new String(IR);
                int num = Integer.parseInt(line.substring(2));
                memory[num][0] = R[0];
                memory[num][1] = R[1];
                memory[num][2] = R[2];
                memory[num][3] = R[3];
            } else if (IR[0] == 'C' && IR[1] == 'R') {
                String line = new String(IR);
                int num = Integer.parseInt(line.substring(2));
                if (memory[num][0] == R[0] && memory[num][1] == R[1] && memory[num][2] == R[2]
                        && memory[num][3] == R[3]) {
                    C = 1;
                } else {
                    C = 0;

                }
            } else if (IR[0] == 'B' && IR[1] == 'T') {
                if (C == 1) {
                    String line = new String(IR);
                    int num = Integer.parseInt(line.substring(2));
                    IC = num;
                    C = 0;
                }
            } else if (IR[0] == 'G' && IR[1] == 'D') {
                SI = 1;
                MASTER_MODE();
            } else if (IR[0] == 'P' && IR[1] == 'D') {
                SI = 2;
                MASTER_MODE();
            } else if (IR[0] == 'H') {
                SI = 3;
                MASTER_MODE();
            }
        }
    }

    public void printthememory() {
        for (int i = 0; i < 100; i++) {
            System.out.println("M[" + i + "] " + new String(memory[i]));
        }
    }
}