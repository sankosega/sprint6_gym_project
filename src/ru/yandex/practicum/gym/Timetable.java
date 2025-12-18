package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {

    private final Map<DayOfWeek, HashMap<TimeOfDay, List<TrainingSession>>> timetable;
    private final Map<DayOfWeek, List<TrainingSession>> allSessionsByDay;

    public Timetable() {
        timetable = new HashMap<>();
        allSessionsByDay = new HashMap<>();
        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new HashMap<>());
            allSessionsByDay.put(day, new ArrayList<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek day = trainingSession.getDayOfWeek();
        TimeOfDay time = trainingSession.getTimeOfDay();

        HashMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(day);

        if (!daySchedule.containsKey(time)) {
            daySchedule.put(time, new ArrayList<>());
        }

        daySchedule.get(time).add(trainingSession);

        List<TrainingSession> dayList = allSessionsByDay.get(day);
        int insertPosition = 0;
        for (int i = 0; i < dayList.size(); i++) {
            if (dayList.get(i).getTimeOfDay().compareTo(time) > 0) {
                break;
            }
            insertPosition = i + 1;
        }
        dayList.add(insertPosition, trainingSession);
    }

    public List<TrainingSession> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return new ArrayList<>(allSessionsByDay.get(dayOfWeek));
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        HashMap<TimeOfDay, List<TrainingSession>> daySchedule = timetable.get(dayOfWeek);

        if (daySchedule.containsKey(timeOfDay)) {
            return new ArrayList<>(daySchedule.get(timeOfDay));
        }

        return new ArrayList<>();
    }

    public List<CounterOfTrainings> getCountByCoaches() {
        Map<Coach, Integer> coachCountMap = new HashMap<>();

        for (HashMap<TimeOfDay, List<TrainingSession>> daySchedule : timetable.values()) {
            for (List<TrainingSession> sessions : daySchedule.values()) {
                for (TrainingSession session : sessions) {
                    Coach coach = session.getCoach();
                    coachCountMap.put(coach, coachCountMap.getOrDefault(coach, 0) + 1);
                }
            }
        }

        List<CounterOfTrainings> result = new ArrayList<>();
        for (Map.Entry<Coach, Integer> entry : coachCountMap.entrySet()) {
            result.add(new CounterOfTrainings(entry.getKey(), entry.getValue()));
        }

        result.sort(Collections.reverseOrder());

        return result;
    }
}
