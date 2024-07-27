public class Specification {
    private static AbstractScheduleManager abstractScheduleManager;

    public static AbstractScheduleManager getAbstractScheduleManager() {
        return abstractScheduleManager;
    }
    public static void initialize(AbstractScheduleManager spec) {
        abstractScheduleManager = spec;
    }

}
