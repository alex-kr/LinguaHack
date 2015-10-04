package com.linguahack.app.desktop;

import com.google.gson.*;
import javaFlacEncoder.FLAC_FileEncoder;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class SimpleSoundCapture extends JPanel implements ActionListener {
    final int bufSize = 16384;

    Capture captureP1 = new Capture();
    Capture captureP2 = new Capture();

    AudioInputStream audioInputStream;

    JButton playB, captB1, captB2;
    JButton sendtB;

    JTextField textField;

    String errStr;

    double duration, seconds;

    File fileP1, fileP2;

    String API_URL = "http://linguahack.herokuapp.com";

    private static final class Lock { }
    private final Object lock = new Lock();

    public SimpleSoundCapture(String fileNameP1, String fileNameP2) {

//        fileP1 = new File(fileNameP1);
//        fileP2 = new File(fileNameP2);


        setLayout(new BorderLayout());
        EmptyBorder eb = new EmptyBorder(5, 5, 5, 5);
        SoftBevelBorder sbb = new SoftBevelBorder(SoftBevelBorder.LOWERED);
        setBorder(new EmptyBorder(5, 5, 5, 5));

        JPanel p1 = new JPanel();
        p1.setLayout(new BoxLayout(p1, BoxLayout.X_AXIS));

        JPanel p2 = new JPanel();
        p2.setBorder(sbb);
        p2.setLayout(new BoxLayout(p2, BoxLayout.Y_AXIS));

        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setBorder(new EmptyBorder(10, 0, 5, 0));
//        playB = addButton("Play", buttonsPanel, false);
        captB1 = addButton("Talk1", buttonsPanel, true, "resources/bb.png");
        captB2 = addButton("Talk2", buttonsPanel, true, "resources/yb.png");
        sendtB = addButton("Analyze", buttonsPanel, true, "red_analyze.png");
        sendtB.addActionListener(new AnalyzeButtonListeer());
        p2.add(buttonsPanel);

        p1.add(p2);
        add(p1);
    }

    public void open() {
    }

    public void close() {
        if (captureP1.thread != null) {
            captB1.doClick(0);
        }
    }

    private JButton addButton(String name, JPanel p, boolean state, String bgImage) {
//        JButton b = new JButton(name);
        BufferedImage buttonIcon = null;
        try {
            buttonIcon = ImageIO.read(new File(bgImage));
        } catch (IOException e) {
            e.printStackTrace();
        }
        JButton b = new JButton(new ImageIcon(buttonIcon));
        b.setName(name);

        b.setBorder(BorderFactory.createEmptyBorder());
        b.setContentAreaFilled(false);

        b.addActionListener(this);
        b.setEnabled(state);
        p.add(b);
        return b;
    }

    private JButton addButtonOld(String name, JPanel p, boolean state) {
        JButton b = new JButton(name);
        b.setName(name);

        b.addActionListener(this);
        b.setEnabled(state);
        p.add(b);
        return b;
    }


    public void actionPerformed(ActionEvent e) {
        Object obj = e.getSource();
        if (obj.equals(captB1)) {
            if (captB1.getName().startsWith("Talk1")) {
                captureP2.stop();
                captureP1.init("Person1", "person1.wav");
                captureP1.start();

//                captB1.getName()"Stop");
                captB2.setFocusable(true);
                captB2.grabFocus();
            } else if (captB1.getName().startsWith("Stop")) {
                captB1.setName("Talk1");
                captB1.setEnabled(false);

//                captB2.setEnabled(true);
                captB2.grabFocus();
//                captureP1.stop();
            }
        } else if (obj.equals(captB2)) {
            if (captB2.getName().startsWith("Talk2")) {
                captureP1.stop();
                captureP2.init("Person2", "person2.wav");
                captureP2.start();

//                captB2.setText("Stop");
                captB1.grabFocus();
            } else if (captB2.getName().startsWith("Stop")) {
                captB2.setText("Talk2");
                captB2.setEnabled(false);

                captB1.setEnabled(true);
//                captureP2.stop();
            }
        }
    }

    static int count = 0;
    static int count2 = 0;
    /**
     * Reads data from the input channel and writes to the output stream
     */
    class Capture implements Runnable {

        TargetDataLine line;

        Thread thread;

        boolean isWakeupNeeded;

        String fileName;

        public void init(String threadName, String fileName) {
            errStr = null;
            thread = new Thread(this);
            thread.setName(threadName);
            thread.getState();
            this.fileName = fileName;
        }

        public void start() {
            thread.start();
        }

        public void stop() {
            thread = null;
        }

        private void shutDown(String message) {
            if ((errStr = message) != null && thread != null) {
                thread = null;
                playB.setEnabled(true);
                captB1.setText("Record");
                System.err.println(errStr);
            }
        }




        public void run() {

            String s = thread.getName();
            duration = 0;
            audioInputStream = null;

            // define the required attributes for our line,
            // and make sure a compatible line is supported.

            AudioFormat.Encoding encoding = AudioFormat.Encoding.PCM_SIGNED;
            float rate = 44100.0f;
            int channels = 2;
            int frameSize = 4;
            int sampleSize = 16;
            boolean bigEndian = true;

            AudioFormat format = new AudioFormat(encoding, rate, sampleSize, channels, (sampleSize / 8) * channels, rate, bigEndian);

            DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

            if (!AudioSystem.isLineSupported(info)) {
                shutDown("Line matching " + info + " not supported.");
                return;
            }

            // get and open the target data line for captureP1.

            try {
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format, line.getBufferSize());
            } catch (LineUnavailableException ex) {
                shutDown("Unable to open the line: " + ex);
                return;
            } catch (SecurityException ex) {
                shutDown(ex.toString());
                //JavaSound.showInfoDialog();
                return;
            } catch (Exception ex) {
                shutDown(ex.toString());
                return;
            }

            // play back the captured audio data
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            int frameSizeInBytes = format.getFrameSize();
            int bufferLengthInFrames = line.getBufferSize() / 8;
            int bufferLengthInBytes = bufferLengthInFrames * frameSizeInBytes;
            byte[] data = new byte[bufferLengthInBytes];
            int numBytesRead;

            line.start();

            while (thread != null) {
                if ((numBytesRead = line.read(data, 0, bufferLengthInBytes)) == -1) {
                    break;
                }
                out.write(data, 0, numBytesRead);
            }

            // we reached the end of the stream.
            // stop and close the line.
            line.stop();
            line.close();
            line = null;

            // stop and close the output stream
            try {
                out.flush();
                out.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // load bytes into the audio input stream for playback

            byte audioBytes[] = out.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(audioBytes);
            audioInputStream = new AudioInputStream(bais, format, audioBytes.length / frameSizeInBytes);

            long milliseconds = (long) ((audioInputStream.getFrameLength() * 1000) / format.getFrameRate());
            duration = milliseconds / 1000.0;



            try {
                if (s.equals("Person1")) {
                    String fileName = "p1" + count;
                    count++;
                    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File("Person1/" + fileName + ".wav"));
                } else if (s.equals("Person2")) {
                    String fileName = "p2" + count2;
                    count2++;
                    AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, new File("Person2/" + fileName + ".wav"));
                }

            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }

        }
    } // End class Capture

    static public void delete(File f) throws IOException {
        if (f.isDirectory()) {
            for (File c : f.listFiles())
                delete(c);
        }
        if (!f.delete())
            throw new FileNotFoundException("Failed to delete file: " + f);
    }




    public static void main(String s[]) {

        try {
            delete(new File("Person1"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            delete(new File("Person2"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        new File("Person1").mkdir();
        new File("Person2").mkdir();

        SimpleSoundCapture ssc = new SimpleSoundCapture("Person1/person1.wav", "Person2/person2.wav");
        ssc.open();
        JFrame f = new JFrame("Talk capture");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add("Center", ssc);
        f.pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int w = 930;
        int h = 350;
        f.setLocation(screenSize.width / 2 - w / 2, screenSize.height / 2 - h / 2);
        f.setSize(w, h);
        f.show();
    }


    private class AnalyzeButtonListeer implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
//            try {
//                System.out.println(getTranslatedTextFromJson(sendFileToGoogleApi("Person1/p10.wav")));
//            } catch (IOException e1) {
//                e1.printStackTrace();
//            }
            try {
                try {
                    delete(new File("PersonTexts1"));
                } catch (IOException e2) {
                    e2.printStackTrace();
                }

                new File("PersonTexts1").mkdir();

                Speech speech1 = getSpeech("Person1", true);
                Speech speech2 = getSpeech("Person2", true);

                RequestData requestData = new RequestData(speech1, speech2);
                System.out.println(requestToJson(requestData));

                String serverResultJson = sendDataToMainServer(requestToJson(requestData));
                System.out.println("response:" + serverResultJson);
                //TODO

                openResultWindow(convertServerResultToClass(serverResultJson));


                System.out.println("-----------vk------------");
                try {
                    String token = getAccessToken();
                    System.out.println("token="+token);
                    System.out.println(getMessages(0, 1, token, 0));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }

            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        private MatchingResult convertServerResultToClass(String serverResultJson) {
            Gson gson = new Gson();
            MatchingResult obj = null;

//                BufferedReader br = new BufferedReader(
//                        new FileReader("c:\\file.json"));

                //convert the json string back to object
                obj = gson.fromJson(serverResultJson, MatchingResult.class);

                System.out.println(obj);

            return obj;
        }

            Map<String, String> textMap = new HashMap<String, String>();
        {
            textMap.put("development", "Развитие и общие интересы");
            textMap.put("emotions", "Отношения и эмоциональная гармония");
            textMap.put("leadership", "Лидерство и распределение обязанностей");
            textMap.put("trusty", "Взаимопонимание и доверие ");
            textMap.put("base", "Основное совпадение");
        }
        private void openResultWindow(MatchingResult matchingResult) {
            JFrame frame = new JFrame("Проблемные области");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            try
            {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setOpaque(true);
            JTextArea textArea = new JTextArea(15, 50);
            textArea.setWrapStyleWord(true);
            textArea.setEditable(false);
            textArea.setFont(Font.getFont(Font.SANS_SERIF));
            JScrollPane scroller = new JScrollPane(textArea);
            scroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
            scroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            JPanel inputpanel = new JPanel();
            inputpanel.setLayout(new BoxLayout(inputpanel, BoxLayout.X_AXIS));

            double basePer = 0;
            for (ResultBlock block : matchingResult.getResultBlock()) {
                if (block.getRelationship_type().equals("base"))
                    basePer = block.getPercent();
            }


            JLabel labelBase = new JLabel("Совпадение: " + basePer);
            labelBase.setFont(new Font(labelBase.getName(), Font.PLAIN, 50));
            panel.add(labelBase);


            JLabel labelEmp1 = new JLabel("Проблемные области:");
            labelEmp1.setFont(new Font(labelEmp1.getName(), Font.PLAIN, 40));
            panel.add(labelEmp1);


            for (ResultBlock block : matchingResult.getResultBlock()) {
                if (block.getPercent() < 50 && !block.getRelationship_type().equals("base")) {
                    JLabel label = new JLabel("---"+textMap.get(block.getRelationship_type()));
                    label.setFont(new Font(label.getName(), Font.PLAIN, 30));
                    panel.add(label);
                }
            }

//            JTextField input = new JTextField(20);
//            JButton button = new JButton("Enter");
            DefaultCaret caret = (DefaultCaret) textArea.getCaret();
            caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
            panel.add(scroller);
//            inputpanel.add(input);
//            inputpanel.add(button);
            panel.add(inputpanel);
            frame.getContentPane().add(BorderLayout.CENTER, panel);
            frame.pack();
            frame.setLocationByPlatform(true);
            frame.setVisible(true);
            frame.setResizable(false);


//            input.requestFocus();
        }

        private String sendDataToMainServer(String speeches) {
            System.out.println("speeches"+speeches);
            String line;
            StringBuffer jsonString = new StringBuffer();
            try {

                URL url = new URL(API_URL);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                connection.setDoInput(true);
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Accept", "application/json");
                connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write(speeches);
                writer.close();
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null) {
                    jsonString.append(line);
                }
                br.close();
                connection.disconnect();
            } catch (Exception exception) {
                throw new RuntimeException(exception.getMessage());
            }

            return jsonString.toString();
        }

        private String requestToJson(RequestData requestData) {
            Gson gson = new Gson();
            String json = gson.toJson(requestData);
            return json;
        }

        private Speech getSpeech(String dir, boolean fromText) throws IOException {
            Speech speech = new Speech();
            if(fromText) {
                if (dir.contains("Person1")) {
                    String text1 = "Who had believe there is a man out there that can sit by a woman he does not know and genuinely be interested in who she is, what she does, without his own agenda?" +
                            "He say My name is Alex and I am a consultant. But she would not be interested in that because she would be counting the seconds until he left";
                    return new Speech(18, text1);
                } else if (dir.contains("Person2")) {
                    String text2 = "I would not even know what that would look like. So what would a guy like that say?" +
                            "Thinking he was like every other guy.";

                    return new Speech(7, text2);
                }
            }
            else {
                String requestForP1 = "";
                StringBuffer sb = new StringBuffer();

                int length = 0;
                for (File file : (new File(dir)).listFiles()) {
                    if (file.getPath().equals(dir + "/.DS_Store") || file.getPath().contains("flac")) continue;

                    length += getWavDuration(file);
                    String translatedText = getTranslatedTextFromJson(sendFileToGoogleApi(file.getPath()));
                    System.out.println(translatedText);
//                requestForP1+=translatedText + ' ';
                    sb.append(translatedText.replaceAll("\"", ""));
                }
                speech.setText(sb.toString());
                speech.setLength(length);
            }
            return speech;
        }
    }

    public int getWavDuration(File file) {
        AudioInputStream audioInputStream = null;
        try {
            audioInputStream = AudioSystem.getAudioInputStream(file);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        AudioFormat format = audioInputStream.getFormat();
        long frames = audioInputStream.getFrameLength();
        return (new Double((frames+0.0) / format.getFrameRate())).intValue();

    }

    public String getTranslatedTextFromJson(String jsonLine) {
        if (jsonLine == null || jsonLine.isEmpty()) return "";
        System.out.println("jsonLine:"+jsonLine);
        JsonElement jelement = new JsonParser().parse(jsonLine);
        JsonObject jobject = jelement.getAsJsonObject();
        jobject = (jobject.getAsJsonArray("result")).get(0).getAsJsonObject();

        JsonArray jarray = jobject.getAsJsonArray("alternative");
        jobject = jarray.get(0).getAsJsonObject();
        String result = jobject.get("transcript").toString();
        return result.toString();
    }

    public void wavToFlac(String fileName) {
        FLAC_FileEncoder flacEncoder = new FLAC_FileEncoder();
        File inputFile = new File(fileName);
        File outputFile = new File(inputFile.getPath()+".flac");

        flacEncoder.encode(inputFile, outputFile);
    }

    public String sendFileToGoogleApi(String fileName) throws IOException {
//        wavToFlac(fileName);
//
//        Path path = Paths.get(fileName + ".flac");
//        byte[] data = Files.readAllBytes(path);
//
//        String request = "http://www.google.com/speech-api/v2/recognize?lang=en-us&key=AIzaSyAfclaFOpuKNc95jDrdISGw_hG0SdrbJxE&output=json";
//        URL url = new URL(request);
//        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//        connection.setDoOutput(true);
//        connection.setDoInput(true);
//        connection.setInstanceFollowRedirects(false);
//        connection.setRequestMethod("POST");
//        connection.setRequestProperty("Content-Type", "audio/x-flac; rate=44100");
//        connection.setRequestProperty("User-Agent", "speech2text");
//        connection.setConnectTimeout(60000);
//        connection.setUseCaches (false);
//
//        DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
//        wr.write(data);
//        wr.flush();
//        wr.close();
//        connection.disconnect();
//
//        System.out.println("Done");
//
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(
//                        connection.getInputStream()));
//        String decodedString;
//        String result = "";
//        while ((decodedString = in.readLine()) != null) {
//            if (decodedString.equals("{\"result\":[]}")) continue;
//            result += decodedString;
//            System.out.println(decodedString);
//        }

        String result = "{\"result\":[{\"alternative\":[{\"transcript\":\"hello time Brian and you my name is Mary Kate but you can call me Kate well let me just put the song you're prettier than my mother thanks\"},{\"transcript\":\"hello time Brian and you my name is Mary Kate but you can call me Kate will let me just put the song you're prettier than my mother thanks\"},{\"transcript\":\"hello time Bryan and you my name is Mary Kate but you can call me Kate well let me just put the song you're prettier than my mother thanks\"},{\"transcript\":\"hello time Brian and you my name is Mary Kate but you can call me Kate well let me just point this out you're prettier than my mother thanks\"},{\"transcript\":\"hello Tom Brian and you my name is Mary Kate but you can call me Kate well let me just put the song you're prettier than my mother thanks\"}],\"final\":true}],\"result_index\":0}";

        return result;
    }

    private String getAccessToken() throws Exception {

//        String url = "https://oauth.vk.com/authorize?client_id=5093231&scope=4096&redirect_uri=https://oauth.vk.com/blank.html&display=page&v=5.37&response_type=token";

        String url = "https://oauth.vk.com/access_token?client_id=5093231&client_secret=mc06tSQYYnjZ0mVuS0lp&v=5.37&grant_type=client_credentials";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        return new JsonParser().parse(response.toString()).getAsJsonObject().get("access_token").toString();
    }

    public String getMessages(int whereToSend, int count, String token, int time_offset) throws Exception {
        String url = "https://api.vk.com/method/messages.get?out="+whereToSend+"&count="+count+"&version=5.37&access_token="+token;//+"&time_offset="+time_offset;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());
        return response.toString();
    }


}