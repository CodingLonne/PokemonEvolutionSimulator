package evolution;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class Main extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        //logic
        NameGenerator nameGranter = new NameGenerator();
        proteinEncodingManager encoder = new proteinEncodingManager();
        World mainLogic = new World(600, encoder);
        //grid
        BorderPane wholeScreen = new BorderPane();
        //different screens
            VisualWorld world = new VisualWorld(600);
            world.setStyle("-fx-background-color: #238028;");
        wholeScreen.setCenter(world);

            VBox infoscreens = new VBox();
            infoscreens.setStyle("-fx-background-color: #F3E5CE;");
            infoscreens.setPrefSize(550, 600);
                
                HBox switchPane = new HBox();
                switchPane.setAlignment(Pos.CENTER);
                switchPane.setStyle("-fx-background-color: #D39F4A;");
                switchPane.setPadding(new Insets(5, 30, 5, 20));
                switchPane.setPrefSize(550, 70);
            infoscreens.getChildren().add(switchPane);
                ScrollPane scroller = new ScrollPane();
                scroller.setPannable(false);
                scroller.setFitToWidth(true);
                scroller.setFitToHeight(true);
                scroller.setHbarPolicy(ScrollBarPolicy.NEVER);
                scroller.prefViewportHeightProperty().bind(infoscreens.heightProperty().subtract(switchPane.heightProperty()));

                    TypeStatsDisplay typeStats = new TypeStatsDisplay(encoder, mainLogic);
                    typeStats.setStyle("-fx-background-color: #EAD2AC;");
                    typeStats.setPadding(new Insets(5, 30, 5, 20));
                    typeStats.setPrefWidth(550);

                    Pane creatureOverview = new Pane();
                    creatureOverview.setStyle("-fx-background-color: #EAD2AC;");
                    creatureOverview.setPadding(new Insets(5, 30, 5, 20));
                    creatureOverview.setPrefWidth(550);

                    CreatureStats creatureStats = new CreatureStats(encoder);
                    creatureStats.setPadding(new Insets(5, 30, 5, 20));
                    creatureStats.setPrefWidth(550);

                    Pane breedingSettings = new Pane();
                    breedingSettings.setStyle("-fx-background-color: #EAD2AC;");
                    breedingSettings.setPadding(new Insets(5, 30, 5, 20));
                    breedingSettings.setPrefWidth(550);

                    Pane fightingSettings = new Pane();
                    fightingSettings.setStyle("-fx-background-color: #EAD2AC;");
                    fightingSettings.setPadding(new Insets(5, 30, 5, 20));
                    fightingSettings.setPrefWidth(550);

                scroller.setContent(typeStats);
            infoscreens.getChildren().add(scroller);
                

        wholeScreen.setRight(infoscreens);
        
        //setup swappers
        ScreenManager screenSwapper = new ScreenManager(scroller, switchPane, 5, 
                                                        new Pane[]{typeStats, creatureOverview, creatureStats, breedingSettings, fightingSettings}, 
                                                        new String[]{"types", "creatures", "unnamed", "breeding", "fighting"});
        Scene scene = new Scene(wholeScreen);

        //setup connections
        mainLogic.addCreatureListener(world);
        mainLogic.addCreatureListener(typeStats);
        mainLogic.addWorldListener(world);
        mainLogic.addCreatureListener(nameGranter);
        mainLogic.addCreatureClickListener(creatureStats);
        mainLogic.addCreatureClickListener(screenSwapper);

        //lset all online
        primaryStage.setTitle("pokemon evolution");
        primaryStage.setScene(scene);
        primaryStage.show();


        for (int x=0; x<45; x++) {
            if (x%3 == 2) {
                mainLogic.addCreature(Relationship.breed(mainLogic, 50+(x/3)*30, 100, mainLogic.getCreature(x-2), mainLogic.getCreature(x-1), 0, encoder));
            } else if (x%3 == 0) {
                mainLogic.addCreature(50+(x/3)*30, 50);
            } else {
                mainLogic.addCreature(50+(x/3)*30, 150);
                mainLogic.getCreature(x).addLove(mainLogic.getCreature(x-1));
                mainLogic.getCreature(x-1).addLove(mainLogic.getCreature(x));
            }
        }
        for (int x=0; x<45; x++) {
            if (x%3 == 2) {
                mainLogic.addCreature(Relationship.breed(mainLogic, 50+(x/3)*30, 300, mainLogic.getCreature(45+x-2), mainLogic.getCreature(45+x-1), 0, encoder));
            } else if (x%3 == 0) {
                mainLogic.addCreature(50+(x/3)*30, 250);
            } else {
                mainLogic.addCreature(50+(x/3)*30, 350);
                mainLogic.getCreature(45+x).addLove(mainLogic.getCreature(45+x-1));
                mainLogic.getCreature(45+x-1).addLove(mainLogic.getCreature(45+x));
            }
        }
        for (int x=0; x<15; x++) {
            mainLogic.addCreature(Relationship.breed(mainLogic, 50+x*30, 200, mainLogic.getCreature(x*3+2), mainLogic.getCreature(45+x*3+2), 0, encoder));
        }
        mainLogic.getCreature(0).setAlive(false);
    }
}
