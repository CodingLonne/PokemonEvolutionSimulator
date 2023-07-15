package evolution;

import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import evolution.Visual.BreedingStats;
import evolution.Visual.CreatureStats;
import evolution.Visual.CreaturesDisplay;
import evolution.Visual.ScreenManager;
import evolution.Visual.TypeStatsDisplay;
import evolution.Visual.VisualWorld;
import evolution.Visual.VisualElements.MyColors;
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
        BreedingSettings breedingSettings = new BreedingSettings();
        FightingSettings fightingSettings = new FightingSettings();
        World mainLogic = new World(600, encoder, breedingSettings, fightingSettings);
        //grid
        BorderPane wholeScreen = new BorderPane();
        //different screens
            VisualWorld world = new VisualWorld(600);
            world.setStyle("-fx-background-color: #238028;");
        wholeScreen.setCenter(world);

            VBox infoscreens = new VBox();
            infoscreens.setBackground(new Background(new BackgroundFill(MyColors.champagne, null, null)));
            infoscreens.setPrefSize(550, 600);
                
                HBox switchPane = new HBox();
                switchPane.setAlignment(Pos.CENTER);
                switchPane.setBackground(new Background(new BackgroundFill(MyColors.earthYellow, null, null)));
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
                    typeStats.setBackground(new Background(new BackgroundFill(MyColors.wheat, null, null)));
                    typeStats.setPadding(new Insets(5, 30, 5, 20));
                    typeStats.setPrefWidth(550);

                    CreaturesDisplay creatureOverview = new CreaturesDisplay();
                    creatureOverview.setPrefWidth(550);

                    CreatureStats creatureStats = new CreatureStats(encoder);
                    creatureStats.setPadding(new Insets(5, 30, 5, 20));
                    creatureStats.setPrefWidth(550);

                    BreedingStats breedingSettingsDisplay = new BreedingStats(breedingSettings, nameGranter);
                    breedingSettingsDisplay.setPrefWidth(550);

                    Pane fightingSettingsDisplay = new Pane();
                    fightingSettingsDisplay.setBackground(new Background(new BackgroundFill(MyColors.wheat, null, null)));
                    fightingSettingsDisplay.setPadding(new Insets(5, 30, 5, 20));
                    fightingSettingsDisplay.setPrefWidth(550);

                scroller.setContent(typeStats);
            infoscreens.getChildren().add(scroller);
                

        wholeScreen.setRight(infoscreens);
        
        //setup swappers
        ScreenManager screenSwapper = new ScreenManager(scroller, switchPane, 5, 
                                                        new Pane[]{typeStats, creatureOverview, creatureStats, breedingSettingsDisplay, fightingSettingsDisplay}, 
                                                        new String[]{"types", "creatures", "unnamed", "breeding", "fighting"});
        Scene scene = new Scene(wholeScreen);

        //setup connections
        mainLogic.addCreatureListener(world);
        mainLogic.addCreatureListener(typeStats);
        mainLogic.addCreatureListener(creatureOverview);
        mainLogic.addCreatureListener(breedingSettingsDisplay);
        mainLogic.addCreatureListener(nameGranter);
        mainLogic.addWorldListener(world);
        mainLogic.addWorldListener(breedingSettingsDisplay);
        mainLogic.addCreatureClickListener(creatureStats);
        mainLogic.addCreatureClickListener(screenSwapper);
        creatureOverview.addCreatureBiome(mainLogic);
        encoder.addListener(creatureStats);
        encoder.addListener(typeStats);
        encoder.addListener(creatureOverview);

        //initialize stuff when needed
        mainLogic.initialize();

        //lset all online
        primaryStage.setTitle("pokemon evolution");
        primaryStage.setScene(scene);
        primaryStage.show();


        
        
        int mutations = 3;
        double crossingOver = 0.1;
        for (int x5=0; x5<32; x5++) {
            mainLogic.addCreature(-512+32*x5, -250);
            mainLogic.getCreature(x5).setAlive(false);
        }
        for (int x4=0; x4<16; x4++) { //-512+64*x4, -210
            mainLogic.addCreature(Relationship.breed(mainLogic, -512+64*x4, -210, mainLogic.getCreature(x4*2), mainLogic.getCreature(x4*2+1), mutations, crossingOver, encoder));
        }
        for (int x3=0; x3<8; x3++) { //-512+64*x4, -210
            mainLogic.addCreature(Relationship.breed(mainLogic, -512+128*x3, -170, mainLogic.getCreature(32+x3*2), mainLogic.getCreature(32+x3*2+1), mutations, crossingOver, encoder));
        }
        for (int x2=0; x2<4; x2++) { //-512+64*x4, -210
            mainLogic.addCreature(Relationship.breed(mainLogic, -512+256*x2, -130, mainLogic.getCreature(48+x2*2), mainLogic.getCreature(48+x2*2+1), mutations, crossingOver, encoder));
        }
        for (int x1=0; x1<2; x1++) { //-512+64*x4, -210
            mainLogic.addCreature(Relationship.breed(mainLogic, -512+512*x1, -90, mainLogic.getCreature(56+x1*2), mainLogic.getCreature(56+x1*2+1), mutations, crossingOver, encoder));
        }
        for (int x0=0; x0<1; x0++) { //-512+64*x4, -210
            mainLogic.addCreature(Relationship.breed(mainLogic, -512+1024*x0, -50, mainLogic.getCreature(60+x0*2), mainLogic.getCreature(60+x0*2+1), mutations, crossingOver, encoder));
        }
        
        
        /* 
        for (int x=0; x<45; x++) {
            if (x%3 == 2) {
                mainLogic.addCreature(Relationship.breed(mainLogic, 50+(x/3)*30, 100, mainLogic.getCreature(x-2), mainLogic.getCreature(x-1), 0, encoder));
            } else if (x%3 == 0) {
                mainLogic.addCreature(50+(x/3)*30, 50);
            } else {
                mainLogic.addCreature(50+(x/3)*30, 150);
                mainLogic.getCreature(x).addLove(mainLogic.getCreature(x-1));
                mainLogic.getCreature(x-1).addLove(mainLogic.getCreature(x));
                mainLogic.getCreature(x).setAge(10);
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
        for (int x=2; x<105; x+= 5) {
            mainLogic.getCreature(x).setSleeping(true);
            mainLogic.getCreature(x).setAge(50);
        }*/
        
    }
}
