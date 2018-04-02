package plutarch.batch.EventEvaluators;

/**
 * Created by joshs on 10/14/2017.
 */
public class EventEvaluatorFactory {
    public static EventEvaluator Resolve(String matchResult) {
        // TODO: bootstrap based on static property from implementers of EventEvaluator?
        // TODO: don't create a new object each time, move to a static list of Scraper
        // TODO: can each of these be turned into config files since the logic is prety simple?
        if (matchResult.equals("Birth_Death"))
            return new BirthDeathEventEvaluator();
        else if (matchResult.equals("Birth"))
            return new BirthEventEvaluator();
        else if (matchResult.equals("Death"))
            return new DeathEventEvaluator();
        else if (matchResult.equals("Battle"))
            return new BattleEventEvaluator();

        return null;
    }
}
