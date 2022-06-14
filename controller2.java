package com.example.demo;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * Компонент - класс HelloControllerNew, отвечающий за работу кнопок
 * @implNote Initializable позволяет совместить controller с interface
 * (кнопки размешенные в интерфейсе совпадали с прописанными им функциями в контроллере)
 */
public class HelloControllerNew implements Initializable {

    @FXML
    private Pane pane;
    @FXML
    /**
     * Компонет - переменная, отвечающая за название песни
     */
    public Label songLabel;
    @FXML
    /**
     * Компонет - переменная, отвечающая за кнопки воспроизведения, паузы, перематывания, воспроизведения предыдущего и следующего mp3 файла, повтора mp3 файла
     */
    public Button playButton, pauseButton, resetButton, previousButton, nextButton, repeatButton;
    @FXML
    /**
     * Компонет - переменная, отвечающая за убавление и прибавление громкости
     */
    public Slider volumeSlider;
    @FXML
    private Slider songProgressBar;
    @FXML
    private ListView<String> songList;
    private Media media;
    private MediaPlayer mediaPlayer;

    private ArrayList<File> songs;

    private int songNumber;
    @FXML
    private MediaView mediaView;
    private Timer timer;
    private boolean running;

    @FXML
    private void chooseFileMethod(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File file = fileChooser.showOpenDialog(null);
        String path = file.toURI().toString();
        if (path != null) {
            Media media = new Media(path);
            mediaPlayer = new MediaPlayer(media);
            mediaView.setMediaPlayer(mediaPlayer);

            DoubleProperty widthProp = mediaView.fitWidthProperty();
            DoubleProperty heightProp = mediaView.fitHeightProperty();
            widthProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "width"));
            heightProp.bind(Bindings.selectDouble(mediaView.sceneProperty(), "height"));

            mediaPlayer.play();
        }
    }

    /**
     * Метод play отвечает за воспроизведение mp3 файла
     * @param actionEvent - это действие, которое происходит после нажатия на кнопку
     */
    public void play(ActionEvent actionEvent) {
        mediaPlayer.play();
    }

    /**
     * Метод pause отвечает за приостановку mp3 файла
     * @param actionEvent - это действие, которое происходит после нажатия на кнопку
     */
    public void pause(ActionEvent actionEvent) {
        mediaPlayer.pause();
    }

    /**
     * Метод skip10 отвечает за перематывание mp3 файла на 10 секунд вперед
     * @param actionEvent - это действие, которое происходит после нажатия на кнопку
     */
    public void skip10(ActionEvent actionEvent) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(10)));
    }

    /**
     * Метод back10 отвечает за перематывание mp3 файла на 10 секунд назад
     * @param actionEvent - это действие, которое происходит после нажатия на кнопку
     */
    public void back10(ActionEvent actionEvent) {
        mediaPlayer.seek(mediaPlayer.getCurrentTime().add(Duration.seconds(-10)));
    }


    public void playMedia(ActionEvent actionEvent) {
        beginTimer();
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();
    }

    public void pauseMedia(ActionEvent actionEvent) {
        cancelTimer();
        mediaPlayer.pause();
    }

    public void resetMedia(ActionEvent actionEvent) {
        songProgressBar.setValue(0);
        mediaPlayer.seek(Duration.seconds(0));
    }

    /**
     * Метод previousMedia отвечает за воспроизведение предыдущего mp3 файла
     * @param actionEvent - это действие, которое происходит после нажатия на кнопку
     */
    public void previousMedia(ActionEvent actionEvent) {
        if (songNumber > 0) {
            songNumber = songNumber - 1;
        } else {
            songNumber = songs.size() - 1;
        }
        mediaPlayer.stop();
        if (running) {
            cancelTimer();
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());
        playMedia(actionEvent);
    }

    /**
     * Метод nextMedia отвечает за воспроизведение следующего mp3 файла
     * @param actionEvent - это действие, которое происходит после нажатия на кнопку
     */
    public void nextMedia(ActionEvent actionEvent) {
        if (songNumber < songs.size() - 1) {
            songNumber = songNumber + 1;
            mediaPlayer.stop();
            if (running) {
                cancelTimer();
            }
        } else {
            songNumber = 0;
            mediaPlayer.stop();
        }
        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songLabel.setText(songs.get(songNumber).getName());
        playMedia();
    }

    private void playMedia() {
        beginTimer();
        mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
        mediaPlayer.play();
    }

    /**
     * Метод beginTimer отвечает за отсчет времени с начала воспроизведения mp3 файла
     */
    public void beginTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();
                songProgressBar.setValue(current / end);
                if (current / end == 1) {
                    cancelTimer();
                }
            }
        };
        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    /**
     * Метод cancelTimer останавливает отсчет времени(beginTimer)
     */
    public void cancelTimer() {
        running = false;
        timer.cancel();
    }

    /**
     * Метод initialize отвечает за связь с application
     * @param arg0
     * @param arg1
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        songs = new ArrayList<File>();

        File directory = new File("C:\\Users\\Лера\\Лера\\Личное\\BTS");
        File[] files = directory.listFiles();
        if (files != null) {
            songs.addAll(Arrays.asList(files));
        }

        ArrayList<String> list = new ArrayList<>();
        for (File file : Objects.requireNonNull(directory.listFiles())) {
            if (file.isFile())
                list.add(String.valueOf(file.getName()));
        }
        songList.getItems().addAll(list);
        songList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.play();
            }
        });

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);


        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {

                mediaPlayer.setVolume(volumeSlider.getValue() * 0.01);
            }
        });


        mediaPlayer.currentTimeProperty().addListener(new ChangeListener<javafx.util.Duration>() {
            @Override
            public void changed(ObservableValue<? extends Duration> observable, Duration oldValue, Duration newValue) {
                songProgressBar.setValue(newValue.toSeconds());
            }
                                                      }
        );
        songProgressBar.setStyle("-fx-accent: #00FF00;");
        songProgressBar.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.seek(Duration.seconds(songProgressBar.getValue()));
            }
        });
    }

    /**
     * Метод repeatMedia отвчает за повтор mp3 файла
     * @param actionEvent - это действие, которое происходит после нажатия на кнопку
     */
    public void repeatMedia(ActionEvent actionEvent) {
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.seek(Duration.ZERO);
                mediaPlayer.play();
            }
        });
    }

    /**
     * Метод shuffle отвечает за перемешку mp3 файлов в плейлисте
     * @param actionEvent
     */
    public void shuffle(ActionEvent actionEvent) {

    }
}

