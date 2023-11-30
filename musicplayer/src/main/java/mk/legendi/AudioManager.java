package mk.legendi;

import com.sun.javafx.application.PlatformImpl;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.nio.file.Path;
import java.util.List;
import java.util.function.BiConsumer;

public class AudioManager {
    public enum Status {
        NONE, PLAYING, PAUSED, ERROR
    }

    public static final List<String> SUPPORTED_FORMATS = List.of("mp3", "wav", "aiff");

    private Status status = Status.NONE;
    private MediaPlayer mediaPlayer = null;

    public AudioManager() {
        // Start up JavaFX
        PlatformImpl.startup(() -> {
        });
    }

    public Status getStatus() {
        return status;
    }

    public void startPlayback(Path path, BiConsumer<Duration, Duration> timeHandler, Runnable endHandler) {
        if (mediaPlayer != null) {
            // Stop, remove old media player.
            mediaPlayer.dispose();
            mediaPlayer = null;
        }

        try {
            Media media = new Media(path.toUri().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.currentTimeProperty().addListener(((observableValue, duration, t1) ->
                    timeHandler.accept(duration, mediaPlayer.getStopTime())
            ));
            mediaPlayer.setOnEndOfMedia(() -> {
                status = Status.PAUSED;
                endHandler.run();
            });
            mediaPlayer.play();
            status = Status.PLAYING;
        } catch (Exception exception) {
            System.err.println("Error starting playback of: " + path);
            exception.printStackTrace();

            mediaPlayer = null;
            status = Status.ERROR;
        }
    }

    public void playOrPause() {
        if (mediaPlayer == null) {
            return;
        }

        switch (mediaPlayer.getStatus()) {
            case READY:
            case PAUSED:
            case STOPPED:
                mediaPlayer.play();
                status = Status.PLAYING;
                break;
            case PLAYING:
                mediaPlayer.pause();
                status = Status.PAUSED;
                break;
            case HALTED:
                status = Status.ERROR;
                break;
        }
    }

    public void seek(double durationFraction) {
        if (mediaPlayer == null) return;

        Duration duration = new Duration(durationFraction * mediaPlayer.getStopTime().toMillis());
        mediaPlayer.seek(duration);
        mediaPlayer.play();
        status = Status.PLAYING;
    }
}
