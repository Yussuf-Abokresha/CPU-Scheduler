package gui;

import java.util.LinkedList;

import core.ProcessCpu;
import core.IntervalLists.IntervalList;
import core.IntevalCpus.IntervalCpu;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class DisplayService extends Application {

        private static IntervalList intervals;
        private static LinkedList<ProcessCpu> process;
        private static String[] colors;
        private static Scene scene;
        private GanttChart<Number, String> ganttChart;

        public static void CreateDisplay(IntervalList intervals2, LinkedList<ProcessCpu> process2) {
                intervals = intervals2;
                process = process2;
                colors = new String[] {
                                "red",
                                "blue",
                                "green",
                                "yellow",
                                "purple"
                };
                launch();
        }

        @Override
        public void start(Stage primaryStage) throws Exception {

                AnchorPane root = new AnchorPane();
                root.setPrefSize(1360, 889);

                HBox mainHBox = new HBox(10);
                mainHBox.setPrefSize(1360, 889);

                // Adjust left panel
                AnchorPane leftPanel = new AnchorPane();
                VBox leftVBox = new VBox(10);
                leftVBox.setPadding(new Insets(10));
                leftVBox.setAlignment(Pos.TOP_CENTER);

                NumberAxis xAxis = new NumberAxis();
                xAxis.setLabel("Time");
                CategoryAxis yAxis = new CategoryAxis();
                yAxis.setLabel("Tasks");

                ganttChart = new GanttChart<>(xAxis, yAxis);
                ganttChart.setBlockHeight(60);

                GanttChart.Series<Number, String> series = new GanttChart.Series<>();
                for (IntervalCpu interval : intervals) {
                        series.getData().add(new GanttChart.Data<>(
                                        interval.startTime, "P" + interval.Pnum,
                                        new GanttChart.ExtraData(interval.executedTime(),
                                                        "status-" + colors[interval.Pnum % colors.length])));
                }
                ganttChart.getData().add(series);

                ganttChart.setPrefSize(1007, 849);

                HBox statsHBox = new HBox(12);
                statsHBox.setAlignment(Pos.CENTER_LEFT);
                Label statisticsLabel = new Label("Statistics");
                statisticsLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: red;");
                Label aataLabel = new Label(String.format("ATA : %.2f", intervals.averageTurnAroundTime));
                Label awtLabel = new Label(String.format("AWT : %.2f", intervals.averageWaitingTime));
                statsHBox.getChildren().addAll(statisticsLabel, aataLabel, awtLabel);

                leftVBox.getChildren().addAll(ganttChart, statsHBox);

                AnchorPane.setTopAnchor(leftVBox, 0.0);
                AnchorPane.setBottomAnchor(leftVBox, 0.0);
                AnchorPane.setLeftAnchor(leftVBox, 0.0);
                AnchorPane.setRightAnchor(leftVBox, 0.0);

                leftPanel.getChildren().add(leftVBox);

                // Adjust right panel
                AnchorPane rightPanel = new AnchorPane();

                // Create a TableView
                TableView<ProcessTableViewer> processTable = new TableView<>();
                processTable.setPrefSize(300, 858);

                TableColumn<ProcessTableViewer, Integer> pNumColumn = new TableColumn<>("Process Number");
                pNumColumn.setCellValueFactory(new PropertyValueFactory<>("PNum"));

                TableColumn<ProcessTableViewer, String> colorColumn = new TableColumn<>("Color");
                colorColumn.setCellValueFactory(new PropertyValueFactory<>("Color"));

                TableColumn<ProcessTableViewer, Integer> priorityColumn = new TableColumn<>("Priority");
                priorityColumn.setCellValueFactory(new PropertyValueFactory<>("Priority"));

                processTable.getColumns().addAll(
                                pNumColumn,
                                colorColumn,
                                priorityColumn);

                LinkedList<ProcessTableViewer> ProcessViewer = new LinkedList<ProcessTableViewer>();
                for (ProcessCpu proc : process) {
                        ProcessViewer.add(new ProcessTableViewer(proc, colors[proc.PNum % colors.length]));
                }
                ObservableList<ProcessTableViewer> observableList = FXCollections.observableArrayList(ProcessViewer);
                processTable.setItems(observableList);

                AnchorPane.setTopAnchor(processTable, 0.0);
                AnchorPane.setBottomAnchor(processTable, 5.0); // 5px padding from the bottom
                AnchorPane.setLeftAnchor(processTable, 0.0);
                AnchorPane.setRightAnchor(processTable, 5.0);

                rightPanel.getChildren().add(processTable);

                // Add panels to main layout
                mainHBox.getChildren().addAll(leftPanel, rightPanel);
                HBox.setHgrow(leftPanel, javafx.scene.layout.Priority.ALWAYS);
                HBox.setHgrow(rightPanel, javafx.scene.layout.Priority.ALWAYS);

                AnchorPane.setTopAnchor(mainHBox, 0.0);
                AnchorPane.setBottomAnchor(mainHBox, 0.0);
                AnchorPane.setLeftAnchor(mainHBox, 0.0);
                AnchorPane.setRightAnchor(mainHBox, 0.0);

                root.getChildren().add(mainHBox);

                scene = new Scene(root);
                scene.getStylesheets()
                                .add(getClass()
                                                .getResource("/gantt-chart-styles.css")
                                                .toExternalForm());

                primaryStage.setScene(scene);
                primaryStage.setTitle("JavaFX Window");
                primaryStage.show();
        }

}
