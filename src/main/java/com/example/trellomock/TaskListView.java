package com.example.trellomock;

import com.example.trellomock.task.Task;
import com.example.trellomock.task.TaskUpdateService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.shape.SVGPath;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.scene.layout.HBox;
import javafx.event.ActionEvent;

import java.io.IOException;

import java.util.Timer;

public class TaskListView extends ListView<Task> {
    private static Task draggedTask;
    private int taskListState;

    private ScreensConfiguration screens;

    public void setScreens(ScreensConfiguration screens) { this.screens = screens; }

    public TaskListView() {
        this.setCellFactory(param -> new TaskCell());
        this.setEditable(true);
        this.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                this.getSelectionModel().clearSelection();
            }
        });

        setOnDragDetected(event -> {
            Dragboard dragboard = startDragAndDrop(TransferMode.ANY);

            ClipboardContent content = new ClipboardContent();
            content.putString(java.lang.String.valueOf(this.getSelectionModel().getSelectedItem()));

            int selected = this.getSelectionModel().getSelectedIndex();
            Object[] cells = this.lookupAll(".cell").toArray();
            ListCell<Task> cell = (ListCell<Task>) cells[selected];
            cell.setGraphic(null);
            assert (draggedTask != null);
            draggedTask = cell.getItem();
            dragboard.setDragView(cell.snapshot(null, null));
            Task t = cell.getItem();

            dragboard.setContent(content);
            event.consume();
        });
        setOnDragOver(event -> {
            if (event.getGestureSource() != event.getTarget() && event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.MOVE);
            }
            event.consume();
        });

        setOnDragEntered(event -> {
            if (event.getGestureSource() != event.getTarget() && event.getDragboard().hasString()) {
                setOpacity(0.7);
            }
        });

        setOnDragExited(event -> {
            if (event.getGestureSource() != event.getTarget() && event.getDragboard().hasString()) {
                setOpacity(1);

            }
        });
        setOnDragDropped(event -> {
            TaskListView items = (TaskListView) event.getGestureTarget();
            Dragboard db = event.getDragboard();
            boolean success = false;

            if (db.hasString()) {
                draggedTask.SetState(items.getTaskListState());
                items.getItems().add(draggedTask);

                success = true;
            }
            event.setDropCompleted(success);

            event.consume();
        });

        setOnDragDone(event -> {
            if (event.getTransferMode() == TransferMode.MOVE) {
                ListView<Task> list = (ListView<Task>) event.getGestureSource();
                list.getItems().remove(draggedTask);
                list.getSelectionModel().clearSelection();
            }
            event.consume();
        });
    }

    public void remove(Task t) {
        this.getItems().remove(t);
    }

    public void setTaskListState(int tls) {
        this.taskListState = tls;
    }

    public int getTaskListState() {
        return this.taskListState;
    }

    private class TaskCell extends ListCell<Task> {
        private Task editedTask;
        Button editTaskButton = new Button();
        Button completeTaskButton = new Button();
        HBox hbox = new HBox();
        Label label = new Label("");
        Pane pane = new Pane();

        Group editTaskSvg = new Group(
                createPath("M256,192c-35.292,0-64,28.708-64,64s28.708,64,64,64s64-28.708,64-64S291.292,192,256,192z M256,298.667 c-23.521,0-42.667-19.135-42.667-42.667s19.146-42.667,42.667-42.667s42.667,19.135,42.667,42.667S279.521,298.667,256,298.667z", "Gainsboro", "Gainsboro"),
                createPath("M256,384c-35.292,0-64,28.708-64,64c0,35.292,28.708,64,64,64s64-28.708,64-64C320,412.708,291.292,384,256,384z M256,490.667c-23.521,0-42.667-19.135-42.667-42.667s19.146-42.667,42.667-42.667s42.667,19.135,42.667,42.667 S279.521,490.667,256,490.667z", "Gainsboro", "Gainsboro"),
                createPath("M256,128c35.292,0,64-28.708,64-64S291.292,0,256,0s-64,28.708-64,64S220.708,128,256,128z M256,21.333 c23.521,0,42.667,19.135,42.667,42.667S279.521,106.667,256,106.667S213.333,87.531,213.333,64S232.479,21.333,256,21.333z", "Gainsboro", "Gainsboro")
        );

        private SVGPath createPath(String d, String fill, String hoverFill) {
            SVGPath path = new SVGPath();
            path.getStyleClass().add("svg");
            path.setContent(d);
            path.setStyle("-fill:" + fill + ";-hover-fill:" + hoverFill + ';');
            return path;
        }

        public TaskCell() {
            TaskCell thisCell = this;
            setAlignment(Pos.CENTER_LEFT);
            thisCell.setEditable(true);

            hbox.getChildren().addAll(completeTaskButton, label, pane, editTaskButton);
            hbox.setHgrow(pane, Priority.ALWAYS);
            hbox.setAlignment(Pos.CENTER_RIGHT);

            Bounds bounds = editTaskSvg.getBoundsInParent();
            double scale = Math.min(20 / bounds.getWidth(), 20 / bounds.getHeight());
            editTaskButton.setScaleX(scale);
            editTaskButton.setScaleY(scale);

            editTaskButton.setGraphic(editTaskSvg);
            editTaskButton.setMaxSize(20, 30);
            editTaskButton.setMinSize(20, 30);
            editTaskButton.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);

            editTaskButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent t) {
                    try {
                        HandleAddTaskClicked(t);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            completeTaskButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent a) {
                    int state = thisCell.getItem().GetState();
                    thisCell.getItem().SetOverdue(false);
                    thisCell.getItem().setComplete(true);
                    thisCell.getItem().SetState(4);
                    thisCell.getItem().SetColor("LightGreen");
                    updateItem(thisCell.getItem(), false);
                    screens.boardController().moveToDone(thisCell.getItem(), state);
                }
            });
        }

        @Override
        public void updateItem(Task t, boolean empty) {
            super.updateItem(t, empty);

            label.setText("");
            setStyle(null);
            completeTaskButton.setStyle(null);
            setGraphic(null);

            if (!empty || t != null) {
                label.setText("  " + t.GetDescription() + "\n  Story Points: " + t.GetSPoints());

                if (t.GetColor() != null) {
                    String style = String.format("-fx-control-inner-background: %s;", t.GetColor());
                    setStyle(style);
                }
                if (t.GetOverdue()) {
                    if (t.getComplete()) {
                        t.SetColor(t.GetColor());
                        t.SetState(0);
                    } else {
                        t.SetColor("Red"); // TODO Change color only visually in the future
                    }
                }

                String css = this.getClass().getResource("/TaskList.css").toExternalForm();
                completeTaskButton.getStylesheets().add(css);
                completeTaskButton.getStyleClass().add("priority" + String.valueOf(t.GetPriority()));

                completeTaskButton.setPickOnBounds(true);
                editTaskButton.getStylesheets().add(css);
                editTaskButton.getStyleClass().add("svg");
                editTaskButton.setPickOnBounds(true);

                this.setGraphic(hbox);
            }
        }

        public void HandleAddTaskClicked(ActionEvent event) throws IOException {
            ObservableList<Task> taskDialogObservableList = FXCollections.observableArrayList();
            taskDialogObservableList.add(this.getItem());
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/EditTaskDialog.fxml"));
            Parent parent = fxmlLoader.load();
            EditTaskDialog dialogController = fxmlLoader.getController();
            dialogController.setAppMainObservableList(taskDialogObservableList);

            Scene scene = new Scene(parent);
            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit Task");
            stage.setScene(scene);
            stage.showAndWait();

            if (taskDialogObservableList.size() > 0) {
                Task t = taskDialogObservableList.get(taskDialogObservableList.size() - 1);

                this.editedTask = t;
                editedTask.register(new TaskUpdateService() {
                    @Override
                    public void onEvent() {
                        if (editedTask.GetOverdue()) {
                            editedTask.SetColor("Red");
                        } else {
                            editedTask.SetState(0);
                            editedTask.setComplete(false);
                            editedTask.SetColor("");
                            screens.boardController().moveToBacklog(t);
                        }
                        refresh();
                    }
                });
                System.out.println("We have a task!");

                System.out.println(this.getItem().GetDescription());
                taskDialogObservableList.remove(t);
                Timer timer = new Timer();
                if (t.GetRecurrenceType().equals("Every Ten Seconds")) {
                    timer.schedule(editedTask, 10000, 10000);
                    updateItem(t, false);
                } else if (t.GetRecurrenceType().equals("Every Day")) {
                    timer.schedule(editedTask, 86400000, 86400000);
                    updateItem(t, false);
                } else if (t.GetRecurrenceType().equals("Every Other Day")) {
                    timer.schedule(editedTask, 86400000 * 2, 86400000 * 2);
                    updateItem(t, false);
                } else if (t.GetRecurrenceType().equals("Every Week")) {
                    timer.schedule(editedTask, 86400000 * 7, 86400000 * 7);
                    updateItem(t, false);
                }

            }
            this.updateItem(this.getItem(),false);
        }
    }
}




