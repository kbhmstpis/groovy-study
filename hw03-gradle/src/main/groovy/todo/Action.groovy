package todo

import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

class Action {

    LocalDateTime beginTime
    LocalDateTime endTime

    int duration
    TimeUnit durationUnit

    String actionName


    Action(String actionName, LocalDateTime beginTime, int duration, TimeUnit durationUnit) {
        this.actionName = actionName
        this.beginTime = beginTime
        this.duration = duration
        this.durationUnit = durationUnit
        this.endTime = beginTime.plusSeconds(durationUnit.toSeconds(duration))
    }

}
