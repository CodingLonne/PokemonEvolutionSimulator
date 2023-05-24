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

    

    Color getColor() {
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

    public Double defense(Type attacker, double adv) {
        /* returns 1.6 when attack hits hard and 0.6 when defender is resistant */

        switch(this) {
            case BUG:
                switch (attacker) {
                    case FIGHTING, GROUND, GRASS:
                        return 1/adv;
                    case FLYING, ROCK, FIRE:
                        return adv;
                    default:
                        return 1.0;
                }
            case DARK:
                switch (attacker) {
                    case GHOST, PSYCHIC, DARK:
                        return 1/adv;
                    case FIGHTING, BUG, FAIRY:
                        return adv;
                    default:
                        return 1.0;
                }
            case DRAGON:
                switch (attacker) {
                    case FIRE, WATER, GRASS, ELECTRIC:
                        return 1/adv;
                    case ICE, DRAGON, FAIRY:
                        return adv;
                    default:
                        return 1.0;
                }
            case ELECTRIC:
                switch (attacker) {
                    case FLYING, STEEL, ELECTRIC:
                        return 1/adv;
                    case GROUND:
                        return adv;
                    default:
                        return 1.0;
                }
            case FAIRY:
                switch (attacker) {
                    case FIGHTING, BUG, DRAGON, DARK:
                        return 1/adv;
                    case POISON, STEEL:
                        return adv;
                    default:
                        return 1.0;
                }
            case FIGHTING:
                switch (attacker) {
                    case ROCK, BUG, DARK:
                        return 1/adv;
                    case FLYING, PSYCHIC, FAIRY:
                        return adv;
                    default:
                        return 1.0;
                }
            case FIRE:
                switch (attacker) {
                    case BUG, STEEL, FIRE, GRASS, ICE:
                        return 1/adv;
                    case GROUND, ROCK, WATER:
                        return adv;
                    default:
                        return 1.0;
                }
            case FLYING:
                switch (attacker) {
                    case FIGHTING, GROUND, BUG, GRASS:
                        return 1/adv;
                    case ROCK, ELECTRIC, ICE:
                        return adv;
                    default:
                        return 1.0;
                }
            case GHOST:
                switch (attacker) {
                    case NORMAL, FIGHTING, POISON, BUG:
                        return 1/adv;
                    case GHOST, DARK:
                        return adv;
                    default:
                        return 1.0;
                }
            case GRASS:
                switch (attacker) {
                    case GROUND, WATER, GRASS, ELECTRIC:
                        return 1/adv;
                    case FLYING, POISON, BUG, FIRE, ICE:
                        return adv;
                    default:
                        return 1.0;
                }
            case GROUND:
                switch (attacker) {
                    case POISON, ROCK, ELECTRIC:
                        return 1/adv;
                    case WATER, GRASS, ICE:
                        return adv;
                    default:
                        return 1.0;
                }
            case ICE:
                switch (attacker) {
                    case ICE:
                        return 1/adv;
                    case FIGHTING, ROCK, STEEL, FIRE:
                        return adv;
                    default:
                        return 1.0;
                }
            case NORMAL:
                switch (attacker) {
                    case GHOST:
                        return 1/adv;
                    case FIGHTING:
                        return adv;
                    default:
                        return 1.0;
                }
            case POISON:
                switch (attacker) {
                    case FIGHTING, POISON, GRASS, FAIRY:
                        return 1/adv;
                    case GROUND, PSYCHIC:
                        return adv;
                    default:
                        return 1.0;
                }
            case PSYCHIC:
                switch (attacker) {
                    case FIGHTING, PSYCHIC:
                        return 1/adv;
                    case BUG, GHOST, DARK:
                        return adv;
                    default:
                        return 1.0;
                }
            case ROCK:
                switch (attacker) {
                    case NORMAL, FLYING, POISON, FIRE:
                        return 1/adv;
                    case FIGHTING, GROUND, STEEL, WATER, GRASS:
                        return adv;
                    default:
                        return 1.0;
                }
            case STEEL:
                switch (attacker) {
                    case NORMAL, FLYING, POISON, ROCK, BUG, STEEL, GRASS, PSYCHIC, ICE, DRAGON, FAIRY:
                        return 1/adv;
                    case FIGHTING, GROUND, FIRE:
                        return adv;
                    default:
                        return 1.0;
                }
            case WATER:
                switch (attacker) {
                    case STEEL, WATER, FIRE, ICE:
                        return 1/adv;
                    case GRASS, ELECTRIC:
                        return adv;
                    default:
                        return 1.0;
                }
            default:
                return 1.0;
        }
    }
    public Double attack(Type defender, double adv) {
        /* returns 1.6 when attack hits hard and 0.6 when defender is resistant */
        switch(this) {
            case BUG:
                switch (defender) {
                    case GRASS, PSYCHIC, DARK:
                        return adv;
                    case FIGHTING, FLYING, POISON, GHOST, STEEL, FIRE, FAIRY:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case DARK:
                switch (defender) {
                    case GHOST, PSYCHIC:
                        return adv;
                    case FIGHTING, DARK, FAIRY:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case DRAGON:
                switch (defender) {
                    case DRAGON:
                        return adv;
                    case STEEL, FAIRY:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case ELECTRIC:
                switch (defender) {
                    case FLYING, WATER:
                        return adv;
                    case GROUND, GRASS, ELECTRIC, DRAGON:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case FAIRY:
                switch (defender) {
                    case FIGHTING, DRAGON, DARK:
                        return adv;
                    case POISON, STEEL, FIRE:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case FIGHTING:
                switch (defender) {
                    case NORMAL, ROCK, STEEL, ICE, DARK:
                        return adv;
                    case FLYING, POISON, PSYCHIC, BUG, GHOST, FAIRY:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case FIRE:
                switch (defender) {
                    case BUG, STEEL, GRASS, ICE:
                        return adv;
                    case ROCK, FIRE, WATER, DRAGON:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case FLYING:
                switch (defender) {
                    case FIGHTING, BUG, GRASS:
                        return adv;
                    case ROCK, STEEL, ELECTRIC:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case GHOST:
                switch (defender) {
                    case GHOST, PSYCHIC:
                        return adv;
                    case NORMAL, DARK:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case GRASS:
                switch (defender) {
                    case GROUND, ROCK, WATER:
                        return adv;
                    case FLYING, POISON, BUG, STEEL, FIRE, GRASS, DRAGON:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case GROUND:
                switch (defender) {
                    case POISON, ROCK, STEEL, FIRE, ELECTRIC:
                        return adv;
                    case FLYING, BUG, GRASS:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case ICE:
                switch (defender) {
                    case FLYING, GROUND, GRASS, DRAGON:
                        return adv;
                    case STEEL, FIRE, WATER, ICE:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case NORMAL:
                switch (defender) {
                    case ROCK, GHOST, STEEL:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case POISON:
                switch (defender) {
                    case GRASS, FAIRY:
                        return adv;
                    case POISON, GROUND, ROCK, GHOST, STEEL:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case PSYCHIC:
                switch (defender) {
                    case FIGHTING, POISON:
                        return adv;
                    case STEEL, PSYCHIC, DARK:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case ROCK:
                switch (defender) {
                    case FLYING, BUG, FIRE, ICE:
                        return adv;
                    case FIGHTING, GROUND, STEEL:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case STEEL:
                switch (defender) {
                    case ROCK, ICE, FAIRY:
                        return adv;
                    case STEEL, FIRE, WATER, ELECTRIC:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            case WATER:
                switch (defender) {
                    case GROUND, ROCK, FIRE:
                        return adv;
                    case WATER, GRASS, DRAGON:
                        return 1/adv;
                    default:
                        return 1.0;
                }
            default:
                return 1.0;
        }
    }
}

