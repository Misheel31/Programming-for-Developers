import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ImageDownloader extends JFrame {

    private List<JTextField> urlFields;
    private JButton downloadButton;
    private JButton pauseButton;
    private JButton resumeButton;
    private JTextArea logArea;
    private ExecutorService executor;

    // Shared flag for pausing and resuming downloads
    private volatile boolean paused;

    public ImageDownloader() {
        setTitle("Multithreaded Image Downloader");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        urlFields = new ArrayList<>();
        downloadButton = new JButton("Download");
        pauseButton = new JButton("Pause");
        resumeButton = new JButton("Resume");
        logArea = new JTextArea();
        logArea.setEditable(false);

        // Add components to the frame
        JPanel inputPanel = new JPanel(new BorderLayout());
        inputPanel.add(new JLabel("Enter URLs "), BorderLayout.NORTH);

        // Initial text fields
        JPanel urlFieldsPanel = new JPanel(new GridLayout(3, 1));

        for (int i = 0; i < 3; i++) {
            JTextField textField = new JTextField();
            urlFields.add(textField);
            urlFieldsPanel.add(textField);
        }

        inputPanel.add(urlFieldsPanel, BorderLayout.CENTER);
        inputPanel.add(downloadButton, BorderLayout.SOUTH);

        // Add pause and resume buttons
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(pauseButton);
        buttonPanel.add(resumeButton);

        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane scrollPane = new JScrollPane(logArea);

        add(scrollPane, BorderLayout.CENTER);

        // Initialize thread pool with a fixed number of threads
        executor = Executors.newFixedThreadPool(3);

        // Add action listeners to the buttons
        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                downloadImagesAsync();
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paused = true;
                log("Downloads paused");
            }
        });

        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                paused = false;
                log("Downloads resumed");
            }
        });
    }

    private void downloadImagesAsync() {
        for (JTextField urlField : urlFields) {
            String url = urlField.getText().trim();
            if (!url.isEmpty()) {
                downloadImage(url);
            }
        }
    }

    private void downloadImage(String imageUrl) {
        executor.submit(new ImageDownloaderTask(imageUrl));
    }

    private class ImageDownloaderTask implements Runnable {

        private String imageUrl;
        private static final int BUFFER_SIZE = 1024;

        public ImageDownloaderTask(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = connection.getInputStream();
                    int totalSize = connection.getContentLength();
                    int downloadedSize = 0;

                    // Extract filename from URL
                    String fileName = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
                    File outputFile = new File(fileName);
                    RandomAccessFile randomAccessFile = new RandomAccessFile(outputFile, "rw");

                    while (downloadedSize < totalSize) {
                        if (paused) {
                            log("Download paused: " + fileName);
                            return;
                        }
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int bytesRead = inputStream.read(buffer);
                        if (bytesRead == -1) {
                            break;
                        }

                        randomAccessFile.write(buffer, 0, bytesRead);
                        downloadedSize += bytesRead;
                        log("Downloaded: " + fileName + " (" + downloadedSize + "/" + totalSize + ")");
                    }
                } else {
                    log("Error downloading image from " + imageUrl + ": HTTP error code " + responseCode);
                }
            } catch (IOException e) {
                log("Error downloading image from " + imageUrl + ": " + e.getMessage());
            }
        }
    }

    private void log(String message) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(message + "\n");
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new ImageDownloader().setVisible(true);
        });
    }
}
