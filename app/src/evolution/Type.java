package evolution;

import javafx.scene.paint.Color;

public enum Type {
    BUG,
    DARK,
    DRAGON,
    ELECTRIC,
    FAIRY,
    FIGHTING,
    FIRE,
    FLYING,
    GHOST,
    GRASS,
    GROUND,
    ICE,
    NORMAL,
    POISON,
    PSYCHIC,
    ROCK,
    STEEL,
    WATER;



    public static Type[] allTypes() {
        return new Type[] {BUG, DARK, DRAGON, ELECTRIC, FAIRY, FIGHTING, FIRE, FLYING, GHOST, GRASS, GROUND, ICE, NORMAL, POISON, PSYCHIC, ROCK, STEEL, WATER};
    }

    

    public Color getColor() {
        switch(this) {
            case BUG:
                return Color.CHARTREUSE;
            case DARK:
                return Color.BLACK;
            case DRAGON:
                return Color.DARKSLATEBLUE;
            case ELECTRIC:
                return Color.GOLD;
            case FAIRY:
                return Color.VIOLET;
            case FIGHTING:
                return Color.CRIMSON;
            case FIRE:
                return Color.FIREBRICK;
            case FLYING:
                return Color.PALETURQUOISE;
            case GHOST:
                return Color.LAVENDER;
            case GRASS:
                return Color.LIMEGREEN;
            case GROUND:
                return Color.PERU;
            case ICE:
                return Color.LIGHTCYAN;
            case NORMAL:
                return Color.SILVER;
            case POISON:
                return Color.PURPLE;
            case PSYCHIC:
                return Color.LIGHTCORAL;
            case ROCK:
                return Color.LIGHTSLATEGRAY;
            case STEEL:
                return Color.LIGHTSTEELBLUE;
            case WATER:
                return Color.ROYALBLUE;
            default:
                return Color.WHITE;
        }
    }

    public Double attacks(Type defender, double adv) {
        /* returns 1.6 when attack hits hard and 0.6 when defender is resistant */

        switch(AdvantageSettings.settings.attacks(this, defender)) {
            case STRONG:
                return adv;
            case WEAK:
                return 1/adv;
            default:
                return 1d;
        }
    }
}

