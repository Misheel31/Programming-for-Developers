import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.concurrent.*;

public class ImageDownloader extends JFrame {

    private JTextField urlTextField;
    private JButton downloadButton;
    private JTextArea statusTextArea;
    private ExecutorService executorService;

    public ImageDownloader() {
        super("Image Downloader");

        urlTextField = new JTextField(30);
        downloadButton = new JButton("Download");
        statusTextArea = new JTextArea(10, 30);
        executorService = Executors.newFixedThreadPool(5);

        setLayout(new FlowLayout());

        add(new JLabel("Enter URL:"));
        add(urlTextField);
        add(downloadButton);
        add(new JScrollPane(statusTextArea));

        downloadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String url = urlTextField.getText();
                if (!url.isEmpty()) {
                    downloadImagesAsync(url);
                }
            }
        });

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void downloadImagesAsync(String url) {
        statusTextArea.setText("");
        SwingWorker<Void, String> worker = new SwingWorker<Void, String>() {
            @Override
            protected Void doInBackground() {
                try {
                    List<String> imageUrls = getImageUrlsFromUrl(url);
                    CountDownLatch latch = new CountDownLatch(imageUrls.size());

                    for (String imageUrl : imageUrls) {
                        // Check if executorService is still running
                        if (!executorService.isShutdown()) {
                            executorService.submit(() -> {
                                try {
                                    downloadImage(imageUrl);
                                } finally {
                                    latch.countDown();
                                }
                            });
                        }
                    }

                    latch.await();

                } catch (Exception e) {
                    publish("Error: " + e.getMessage());
                }
                return null;
            }

            @Override
            protected void process(List<String> chunks) {
                for (String message : chunks) {
                    statusTextArea.append(message + "\n");
                }
            }

            @Override
            protected void done() {
                executorService.submit(() -> {
                    // Check if executorService is still running
                    if (!executorService.isShutdown()) {
                        executorService.shutdown();

                        try {
                            // Attempt to await termination for a reasonable amount of time
                            if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                                // If termination takes too long, force shutdown
                                executorService.shutdownNow();
                            }
                        } catch (InterruptedException e) {
                            executorService.shutdownNow();
                            Thread.currentThread().interrupt();
                        }

                        // Recreate the executorService for future downloads
                        executorService = Executors.newFixedThreadPool(5);
                    }
                });
            }
        };

        worker.execute();
    }

    private List<String> getImageUrlsFromUrl(String url) throws IOException {
        // Implement logic to extract image URLs from the given URL
        // For simplicity, you can use a library like Jsoup for HTML parsing
        // Return a List<String> containing image URLs
        return List.of("https://example.com/image1.jpg", "https://example.com/image2.jpg");
    }

    public void downloadImage(String imageUrl) {
        try {
            @SuppressWarnings("deprecation")
            URL url = new URL(imageUrl);
            BufferedImage image = ImageIO.read(url);

            if (image != null) {
                // Implement logic to save the image or update UI with the downloaded image
                // For simplicity, you can just print the progress and completion percentage
                System.out.println("Downloaded: " + imageUrl);
            } else {
                publish("Error: Unable to read image from URL - " + imageUrl);
            }
        } catch (IOException e) {
            publish("Error downloading image: " + e.getMessage());
        }
    }

    private void publish(String string) {
        SwingUtilities.invokeLater(() -> statusTextArea.append(string + "\n"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ImageDownloader());
    }
}
